package com.comandante.creeper.Items;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.Area;
import com.comandante.creeper.world.Room;

import java.util.Random;
import java.util.Set;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class ForageManager {

    public final GameManager gameManager;
    private static final Random random = new Random();

    public ForageManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void addForageToArea(Area area, ForageBuilder forageBuilder) {
        Set<Room> roomsByArea = gameManager.getRoomManager().getRoomsByArea(area);
        for (Room room: roomsByArea) {
            Forage newForage = forageBuilder.createForage();
            room.addForage(newForage);
            gameManager.getEntityManager().addEntity(newForage);
        }
    }

    public void getForageForRoom(Room room, Player player) {
        int countOfForagesFound = 0;
        int totalForageXp = 0;
        try {
            for (Forage forage : room.getForages().values()) {
                int foragingLevel = getLevel(gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getStats().getForaging());
                if (forage.getMinLevel() > foragingLevel) {
                    System.out.println("Foraging level not high enough.");
                    return;
                }
                if (forage.getCoolDownTicksLeft() > 0) {
                    System.out.println("Forage is still cooling down. Ticks left: " + forage.getCoolDownTicksLeft());
                    return;
                }
                forage.setCoolDownTicksLeft(forage.getCoolDownTicks());
                double foragePctOfSuccess = forage.getPctOfSuccess();
                int pctSuccessBoostForLevel = getPctSuccessBoostForLevel(foragingLevel);
                System.out.println("you get a boost of " + pctSuccessBoostForLevel);
                foragePctOfSuccess = foragePctOfSuccess + pctSuccessBoostForLevel;
                System.out.println("final pct of success for forage: " + foragePctOfSuccess);
                if (getRandPercent(foragePctOfSuccess)) {
                    gameManager.getPlayerManager().updatePlayerForageExperience(player, forage.getForageExperience());
                    int numberToHarvest = randInt(forage.getMinAmt(), forage.getMaxAmt());
                    totalForageXp += forage.getForageExperience();
                    for (int i = 0; i < numberToHarvest; i++) {
                        countOfForagesFound++;
                        Item item = forage.getItemType().create();
                        gameManager.getEntityManager().saveItem(item);
                        gameManager.acquireItem(player, item.getItemId());
                    }
                    gameManager.writeToRoom(room.getRoomId(), player.getPlayerName() + " foraged (" + numberToHarvest + ") " + forage.getItemType().getItemName() + "\r\n");
                } else {
                    gameManager.getChannelUtils().write(player.getPlayerId(), "Attempt to forage " + forage.getItemType().getItemName() + " failed.\r\n");
                    System.out.println("failed to obtain forage, random pctsuccess failed.");
                }
            }
        } finally {
            if (totalForageXp > 0) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "You gained " + Color.GREEN + "+" + totalForageXp + Color.RESET + " forage experience points." + "\r\n", true);
            }
            if (countOfForagesFound == 0) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Nothing foraged." + "\r\n");
                return;
            }
        }
    }

    private static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static boolean getRandPercent(double percent) {
        double rangeMin = 0;
        double rangeMax = 100;
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        return randomValue <= percent;
    }

    private static double FORAGE_EXP_CONSTANT_MODIFIER = 0.05;
    private static double FORAGE_LEVEL_PCT_BOOST_MODIFIER = 0.05;

    public static int getPctSuccessBoostForLevel(int level) {
        double v = FORAGE_LEVEL_PCT_BOOST_MODIFIER * sqrt(level);
        return (int) Math.ceil(v);
    }

    public static int getLevel(int experience) {
        double v = FORAGE_EXP_CONSTANT_MODIFIER * sqrt(experience);
        return (int) Math.floor(v);
    }

    public static int getXp(int level) {
        double v = pow(level, 2) / pow(FORAGE_EXP_CONSTANT_MODIFIER, 2);
        return (int) Math.ceil(v);
    }


    public static void main(String[] args) {
        int i = 0;
        while (i < 1000000) {
            int level = getLevel(i);
            System.out.println("xp is: " + i + " level is: " + level + " double checking math: " + getXp(level));
            i = i + 1000;
        }

        int level = 0;
        while (level < 60) {
            level++;
            int xp = getXp(level);
            System.out.println("level: " + level + " is " + xp + "exp.");
        }
    }



}
