/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.items;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.aethermud.world.model.Room;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class ForageManager {

    public final GameManager gameManager;
    private static final Random random = new Random();
    private static final Logger log = Logger.getLogger(ForageManager.class);

    public ForageManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void addForage(String internalItemName, Forage forage) {
        for (Area area: forage.getForageAreas()) {
            Set<Room> roomsByArea = gameManager.getRoomManager().getRoomsByArea(area);
            for (Room room : roomsByArea) {
                ForageBuilder forageBuiler = new ForageBuilder().from(forage);
                forageBuiler.setInternalItemName(internalItemName);
                Forage newForage = forageBuiler.createForage();
                room.addForage(newForage);
                gameManager.getEntityManager().addEntity(newForage);
            }
        }
    }

    public void getForageForRoom(Room room, Player player) {
       if (player.isActiveForageCoolDown()) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "Your forage cooldown is still active!\r\n");
            return;
        }
        gameManager.getChannelUtils().write(player.getPlayerId(), "You scan the ground for plants, herbs and fungi...\r\n");
        long countOfForagesFound = 0;
        long totalForageXp = 0;
        Stats playerStatsWithEquipmentAndLevel = player.getPlayerStatsWithEquipmentAndLevel();
        long foragingLevel = getLevel(playerStatsWithEquipmentAndLevel.getForaging());
        try {
            for (Forage forage : room.getForages().values()) {
                if (forage.getMinLevel() > foragingLevel) {
                    // System.out.prlongln("Foraging level not high enough.");
                    return;
                }
                if (forage.getCoolDownTicksLeft() > 0) {
                    //System.out.prlongln("Forage is still cooling down. Ticks left: " + forage.getCoolDownTicksLeft());
                    return;
                }
                forage.setCoolDownTicksLeft(forage.getCoolDownTicks());
                double foragePctOfSuccess = forage.getPctOfSuccess();
                long modifiedLevelForForage = getLevel(player.getPlayerStatsWithEquipmentAndLevel().getForaging());
                double pctSuccessBoostForLevel = getPctSuccessBoostForLevel(modifiedLevelForForage);
                //System.out.prlongln("you get a boost of " + pctSuccessBoostForLevel);
                foragePctOfSuccess = (foragePctOfSuccess * pctSuccessBoostForLevel) + foragePctOfSuccess;
                //System.out.prlongln("final pct of success for forage: " + foragePctOfSuccess);
                Item item;
                try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
                    Optional<ItemData> itemOptional = tx.getStorage().getItem(forage.getInternalItemName());
                    if (!itemOptional.isPresent()) {
                        continue;
                    }
                    item = ItemData.copyItem(itemOptional.get());
                }
                if (getRandPercent(foragePctOfSuccess)) {
                    player.updatePlayerForageExperience(forage.getForageExperience());
                    long numberToHarvest = randInt(forage.getMinAmt(), forage.getMaxAmt());
                    totalForageXp += forage.getForageExperience();
                    for (long i = 0; i < numberToHarvest; i++) {
                        countOfForagesFound++;
                        ItemInstance itemInstance = new ItemBuilder().from(item).create();
                        gameManager.getEntityManager().saveItem(itemInstance);
                        gameManager.acquireItem(player, itemInstance.getItemId());
                    }
                    gameManager.writeToRoom(room.getRoomId(), player.getPlayerName() + " foraged (" + numberToHarvest + ") " + item.getItemName() + "\r\n");
                } else {
                    gameManager.getChannelUtils().write(player.getPlayerId(), "Attempt to forage " + item.getItemName() + " failed.\r\n");
                    //System.out.prlongln("failed to obtain forage, random pctsuccess failed.");
                }
            }
        } finally {
            if (totalForageXp > 0) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "You gained " + Color.GREEN + "+" + totalForageXp + Color.RESET + " forage experience points." + "\r\n", true);
            }
            if (countOfForagesFound == 0) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Nothing foraged." + "\r\n");
            }
            if (foragingLevel <= 10) {
                player.addCoolDown(CoolDownType.FORAGE_LONG);
            } else if (foragingLevel > 10 && foragingLevel <= 30) {
                player.addCoolDown(CoolDownType.FORAGE_MEDIUM);
            } else if (foragingLevel > 30 && foragingLevel <= 40) {
                player.addCoolDown(CoolDownType.FORAGE_SHORT);
            } else if (foragingLevel > 40) {
                player.addCoolDown(CoolDownType.FORAGE_SUPERSHORT);
            }
        }
    }

    private static long randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static boolean getRandPercent(double percent) {
        double rangeMin = 0;
        double rangeMax = 100;
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        return randomValue <= percent;
    }

    private static double FORAGE_EXP_CONSTANT_MODIFIER = 0.05;
    private static double FORAGE_LEVEL_PCT_BOOST_MODIFIER = .5;
    private static double FORAGE_DELAY_TIME_CALCULATION_MODIFIER = 1.91;


    public static double getPctSuccessBoostForLevel(long level) {
        double v = FORAGE_LEVEL_PCT_BOOST_MODIFIER * sqrt(level);
        return v;
    }

    public static long getLevel(long experience) {
        double v = FORAGE_EXP_CONSTANT_MODIFIER * sqrt(experience);
        return (long) Math.floor(v);
    }

    public static long getXp(long level) {
        double v = pow(level, 2) / pow(FORAGE_EXP_CONSTANT_MODIFIER, 2);
        return (long) Math.ceil(v);
    }

    public static long getForageDelayTime(long level) {
        double v = pow(level, 2) / pow(FORAGE_DELAY_TIME_CALCULATION_MODIFIER, 2);
        return (long) Math.ceil(v);
    }

    public static void main1(String[] args) throws InterruptedException {
        long i = 0;
        while (i < 1000000) {
            long level = getLevel(i);
            System.out.println("xp is: " + i + " level is: " + level + " double checking math: " + getXp(level));
            i = i + 1000;
        }

        long level = 0;
        while (level < 60) {
            level++;
            long xp = getXp(level);
            System.out.println("level: " + level + " is " + xp + "exp.");
        }

        level = 0;
        while (level < 60) {
            level++;
            double xp = getPctSuccessBoostForLevel(level);
            System.out.println("level: " + level + " is bosted by " + xp + "pct.");
        }

        level = 0;
        while (level < 60) {
            level++;
            long xp = getForageDelayTime(level);
            System.out.println("level: " + level + " will delay by: " + xp + "ms");
        }

        Thread.sleep(-1000);
    }

}
