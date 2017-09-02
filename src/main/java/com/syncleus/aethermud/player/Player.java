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
package com.syncleus.aethermud.player;


import com.codahale.metrics.Meter;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.common.AetherMudUtils;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcStatsChangeBuilder;
import com.syncleus.aethermud.npc.Temperament;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.stats.Levels;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.graphdb.CoolDownData;
import com.syncleus.aethermud.storage.graphdb.StatsData;
import com.syncleus.aethermud.stats.StatsBuilder;
import com.syncleus.aethermud.stats.StatsHelper;
import com.syncleus.aethermud.storage.graphdb.PlayerData;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.collect.*;
import com.syncleus.aethermud.items.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Player extends AetherMudEntity {

    private static final Logger log = Logger.getLogger(Player.class);
    private final GameManager gameManager;
    private final String playerId;
    private final Interner<String> interner = Interners.newWeakInterner();
    private final Random random = new Random();
    private String playerName;
    private Channel channel;
    private Optional<String> returnDirection = Optional.empty();
    private Room currentRoom;
    private SortedMap<Long, ActiveFight> activeFights = Collections.synchronizedSortedMap(new TreeMap<Long, ActiveFight>());
    private int tickBucket = 0;
    private int fightTickBucket = 0;
    private final Set<NpcSpawn> alertedNpcSpawns = Sets.newHashSet();
    private Optional<Room> previousRoom = Optional.empty();
    private final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1000);
    private AtomicBoolean isChatMode = new AtomicBoolean(false);

    public static final int FIGHT_TICK_BUCKET_SIZE = 4;


    public Player(String playerName, GameManager gameManager) {
        this.playerName = playerName;
        this.playerId = new String(Base64.encodeBase64(playerName.getBytes()));
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        synchronized (interner.intern(playerId)) {
            try {
                if (processTickBucket(10)) {
                    processRegens();
                    processEffects();
                    if (activeFights.size() > 0) {
                        writePrompt();
                    }
                }
                tickAllActiveCoolDowns();
                activateNextPrimaryActiveFight();
                if (processFightTickBucket(FIGHT_TICK_BUCKET_SIZE)) {
                    processFightRounds();
                }
            } catch (Exception e) {
                log.error("Player ticker failed! + " + playerName, e);
                SentryManager.logSentry(this.getClass(), e, "Player ticker problem!");
            }
        }
    }

    private boolean processTickBucket(int numberOfTicksToFillBucket) {
        if (tickBucket == numberOfTicksToFillBucket) {
            tickBucket = 0;
            return true;
        } else {
            tickBucket = tickBucket + 1;
            return false;
        }
    }

    private boolean processFightTickBucket(int numberOfTicksToFillBucket) {
        if (fightTickBucket == numberOfTicksToFillBucket) {
            fightTickBucket = 0;
            return true;
        } else {
            fightTickBucket = fightTickBucket + 1;
            return false;
        }
    }

    private void processFightRounds() {

        DamageProcessor playerDamageProcesor = getPlayerClass().getDamageProcessor();
        Set<Map.Entry<Long, ActiveFight>> entries = activeFights.entrySet();
        for (Map.Entry<Long, ActiveFight> next : entries) {
            Optional<String> npcIdOptional = next.getValue().getNpcId();
            if (npcIdOptional.isPresent()) {
                // If the NPC has died- bail out.
                String npcId = npcIdOptional.get();
                addCoolDown(new CoolDownPojo(CoolDownType.NPC_FIGHT));
                NpcSpawn npcSpawn = gameManager.getEntityManager().getNpcEntity(npcId);
                if (npcSpawn == null) {
                    continue;
                }
                doFightRound(playerDamageProcesor, npcSpawn.getDamageProcessor(), next.getValue());
            }
        }
    }

    private void processRegens() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            Stats stats = getPlayerStatsWithEquipmentAndLevel();
            if (isActive(CoolDownType.NPC_FIGHT) || isActive(CoolDownType.DEATH)) {
                return;
            }
            if (playerData.getStats().getCurrentHealth() < stats.getMaxHealth()) {
                updatePlayerHealth((int) (stats.getMaxHealth() * .05), null);
            }
            if (playerData.getStats().getCurrentMana() < stats.getMaxMana()) {
                addMana((int) (stats.getMaxMana() * .03));
            }
        }
    }

    private void processEffects() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            List<Effect> effectsToRemove = Lists.newArrayList();
            for (Effect effect : playerData.getEffects()) {
                if (effect.getEffectApplications() >= effect.getMaxEffectApplications()) {
                    gameManager.getChannelUtils().write(playerId, Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + effect.getEffectName() + " has worn off.\r\n", true);
                    effectsToRemove.add(effect);
                    continue;
                } else {
                    effect.setEffectApplications(effect.getEffectApplications() + 1);
                    gameManager.getEffectsManager().application(effect, this);
                }

            }
            for (Effect effect : effectsToRemove) {
                playerData.removeEffect(effect);
            }
            savePlayerMetadata();
        }
    }

    public void killPlayer(NpcSpawn npcSpawn) {
        resetEffects();
        synchronized (interner.intern(playerId)) {
            if (npcSpawn != null && doesActiveFightExist(npcSpawn)) {
                removeAllActiveFights();
            }
            if (!isActive(CoolDownType.DEATH)) {
                Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
                if (!playerMetadataOptional.isPresent()) {
                    return;
                }
                PlayerData playerData = playerMetadataOptional.get();
                int newGold = playerData.getGold() / 2;
                playerData.setGold(newGold);
                if (newGold > 0) {
                    gameManager.getChannelUtils().write(getPlayerId(), "You just " + Color.BOLD_ON + Color.RED + "lost " + Color.RESET + newGold + Color.YELLOW + " gold" + Color.RESET + "!\r\n");
                }
                removeActiveAlertStatus();
                savePlayerMetadata();
                CoolDownPojo death = new CoolDownPojo(CoolDownType.DEATH);
                addCoolDown(death);
                gameManager.writeToPlayerCurrentRoom(getPlayerId(), getPlayerName() + " is now dead." + "\r\n");
                PlayerMovement playerMovement = new PlayerMovement(this, gameManager.getRoomManager().getPlayerCurrentRoom(this).get().getRoomId(), GameManager.LOBBY_ID, "vanished into the ether.", "");
                movePlayer(playerMovement);
                String prompt = gameManager.buildPrompt(playerId);
                gameManager.getChannelUtils().write(getPlayerId(), prompt, true);
            }
        }
    }


    public boolean updatePlayerHealth(int amount, NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return false;
            }
            PlayerData playerData = playerMetadataOptional.get();
            if (amount > 0) {
                addHealth(amount, playerData);
                savePlayerMetadata();
                return false;
            } else {
                StatsData stats = playerData.getStats();
                if ((stats.getCurrentHealth() + amount) < 0) {
                    stats.setCurrentHealth(0);
                } else {
                    stats.setCurrentHealth(stats.getCurrentHealth() + amount);
                }
                savePlayerMetadata();
                if (playerData.getStats().getCurrentHealth() == 0) {
                    killPlayer(npcSpawn);
                    return true;
                }
            }
        }
        return false;
    }

    public Optional<Room> getPreviousRoom() {
        synchronized (interner.intern(playerId)) {
            return previousRoom;
        }
    }

    public void setPreviousRoom(Room previousRoom) {
        synchronized (interner.intern(playerId)) {
            this.previousRoom = Optional.ofNullable(previousRoom);
        }
    }

    public void writeMessage(String msg) {
        gameManager.getChannelUtils().write(getPlayerId(), msg);
    }

    public long getAvailableMana() {
        return getPlayerStatsWithEquipmentAndLevel().getCurrentMana();
    }


    private void addHealth(int addAmt, PlayerData playerData) {
        int currentHealth = playerData.getStats().getCurrentHealth();
        Stats statsModifier = getPlayerStatsWithEquipmentAndLevel();
        int maxHealth = statsModifier.getMaxHealth();
        int proposedNewAmt = currentHealth + addAmt;
        if (proposedNewAmt > maxHealth) {
            if (currentHealth < maxHealth) {
                int adjust = proposedNewAmt - maxHealth;
                proposedNewAmt = proposedNewAmt - adjust;
            } else {
                proposedNewAmt = proposedNewAmt - addAmt;
            }
        }
        playerData.getStats().setCurrentHealth(proposedNewAmt);
    }

    public void addMana(int addAmt) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            PlayerData playerData = playerMetadataOptional.get();
            int currentMana = playerData.getStats().getCurrentMana();
            Stats statsModifier = getPlayerStatsWithEquipmentAndLevel();
            int maxMana = statsModifier.getMaxMana();
            int proposedNewAmt = currentMana + addAmt;
            if (proposedNewAmt > maxMana) {
                if (currentMana < maxMana) {
                    int adjust = proposedNewAmt - maxMana;
                    proposedNewAmt = proposedNewAmt - adjust;
                } else {
                    proposedNewAmt = proposedNewAmt - addAmt;
                }
            }
            playerData.getStats().setCurrentMana(proposedNewAmt);
            savePlayerMetadata();
        }
    }

    public void addExperience(int exp) {
        synchronized (interner.intern(playerId)) {
            final Meter requests = Main.metrics.meter("experience-" + playerName);
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            int currentExperience = playerData.getStats().getExperience();
            int currentLevel = Levels.getLevel(currentExperience);
            playerData.getStats().setExperience(currentExperience + exp);
            requests.mark(exp);
            int newLevel = Levels.getLevel(playerData.getStats().getExperience());
            if (newLevel > currentLevel) {
                gameManager.announceLevelUp(playerName, currentLevel, newLevel);
            }
            savePlayerMetadata();
        }
    }

    public int getLevel() {
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        return playerMetadataOptional.map(playerMetadata -> Levels.getLevel(playerMetadata.getStats().getExperience())).orElse(0);
    }

    private Optional<PlayerData> getPlayerMetadata() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId);
    }

    private void savePlayerMetadata() {
        gameManager.getPlayerManager().persist();
    }

    public long getCurrentHealth() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            return playerMetadataOptional.map(playerMetadata -> playerMetadata.getStats().getCurrentHealth()).orElse(0);
        }
    }

    public void transferGoldToBank(int amt) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.transferGoldToBank(amt);
            savePlayerMetadata();
        }
    }

    public void transferBankGoldToPlayer(int amt) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.transferBankGoldToPlayer(amt);
            savePlayerMetadata();
        }
    }

    public void incrementGold(int amt) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.incrementGold(amt);
            savePlayerMetadata();
        }
    }

    public boolean addEffect(Effect effect) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return false;
            }
            PlayerData playerData = playerMetadataOptional.get();
            if (playerData.getEffects() != null && (playerData.getEffects().size() >= playerData.getStats().getMaxEffects())) {
                return false;
            }
            playerData.addEffect(effect);
            savePlayerMetadata();
            return true;
        }
    }

    public void resetEffects() {
        synchronized (interner) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.resetEffects();
            savePlayerMetadata();
        }
    }

    public void addLearnedSpellByName(String spellName) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.addLearnedSpellByName(spellName);
            savePlayerMetadata();
        }
    }

    public boolean doesHaveSpellLearned(String spellName) {
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (!playerMetadataOptional.isPresent()) {
            return false;
        }
        PlayerData playerData = playerMetadataOptional.get();
        List<String> learnedSpells = playerData.getLearnedSpells();
        if (learnedSpells == null || learnedSpells.isEmpty()) {
            return false;
        }
        return learnedSpells.contains(spellName);
    }

    public void removeLearnedSpellByName(String spellName) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.removeLearnedSpellByName(spellName);
            savePlayerMetadata();
        }
    }

    public List<String> getLearnedSpells() {
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (!playerMetadataOptional.isPresent()) {
            return Lists.newArrayList();
        }
        PlayerData playerData = playerMetadataOptional.get();
        return Lists.newArrayList(playerData.getLearnedSpells());
    }

    public boolean isActiveAlertNpcStatus(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            return alertedNpcSpawns.contains(npcSpawn);
        }
    }

    public boolean isActiveAlertNpcStatus() {
        synchronized (interner.intern(playerId)) {
            return alertedNpcSpawns.size() > 0;
        }
    }

    public boolean areAnyAlertedNpcsInCurrentRoom() {
        return currentRoom.getPresentNpcs().stream().filter(this::isActiveAlertNpcStatus).count() > 0;
    }

    public boolean areInTheSameRoom(NpcSpawn npcSpawn) {
        return currentRoom.getPresentNpcs().contains(npcSpawn);
    }

    public void setIsActiveAlertNpcStatus(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            alertedNpcSpawns.add(npcSpawn);
        }
    }

    public void removeActiveAlertStatus() {
        synchronized (interner.intern(playerId)) {
            alertedNpcSpawns.clear();
        }
    }

    public void removeActiveAlertStatus(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            alertedNpcSpawns.clear();
        }
    }

    public Set<NpcSpawn> getAlertedNpcSpawns() {
        return alertedNpcSpawns;
    }

    public void addInventoryId(String inventoryId) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.addInventoryEntityId(inventoryId);
            savePlayerMetadata();
        }
    }

    public void transferItemToLocker(String inventoryId) {
        synchronized (interner.intern(playerId)) {
            removeInventoryId(inventoryId);
            addLockerInventoryId(inventoryId);
        }
    }

    public void removeInventoryId(String inventoryId) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.removeInventoryEntityId(inventoryId);
            savePlayerMetadata();
        }
    }

    public void addLockerInventoryId(String entityId) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.addLockerEntityId(entityId);
            savePlayerMetadata();
        }
    }

    public void addNpcKillLog(String npcName) {
        gameManager.getEventProcessor().addEvent(() -> {
            synchronized (interner.intern(playerId)) {
                Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
                if (!playerMetadataOptional.isPresent()) {
                    return;
                }
                PlayerData playerData = playerMetadataOptional.get();
                playerData.addNpcKill(npcName);
                savePlayerMetadata();
            }
        });
    }

    public void transferItemFromLocker(String entityId) {
        synchronized (interner.intern(playerId)) {
            if (gameManager.acquireItem(this, entityId)) {
                removeLockerInventoryId(entityId);
            }
        }
    }

    public void removeLockerInventoryId(String lockerInventoryId) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.removeLockerEntityId(lockerInventoryId);
            savePlayerMetadata();
        }
    }

    public void updatePlayerMana(int amount) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            StatsData stats = playerData.getStats();
            stats.setCurrentMana(stats.getCurrentMana() + amount);
            savePlayerMetadata();
        }
    }

    public void updatePlayerForageExperience(int amount) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            StatsData stats = playerData.getStats();
            stats.setForaging(stats.getForaging() + amount);
            savePlayerMetadata();
        }
    }

    public void addCoolDown(CoolDownPojo coolDown) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            CoolDown newCoolDown = playerData.createCoolDown();
            newCoolDown.setNumberOfTicks(coolDown.getNumberOfTicks());
            newCoolDown.setCoolDownType(coolDown.getCoolDownType());
            newCoolDown.setName(coolDown.getName());
            newCoolDown.setOriginalNumberOfTicks(coolDown.getOriginalNumberOfTicks());
            savePlayerMetadata();
        }
    }

    public Set<? extends CoolDown> getCoolDowns() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return Sets.newHashSet();
            }
            PlayerData playerData = playerMetadataOptional.get();
            return playerData.getCoolDowns();
        }
    }

    public boolean isActiveCoolDown() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return false;
            }
            PlayerData playerData = playerMetadataOptional.get();
            return playerData.getCoolDowns().size() > 0;
        }
    }

    public boolean isActiveForageCoolDown() {
        if (isActive(CoolDownType.FORAGE_LONG) ||
                isActive(CoolDownType.FORAGE_MEDIUM) ||
                isActive(CoolDownType.FORAGE_SHORT) ||
                isActive(CoolDownType.FORAGE_SUPERSHORT)) {
            return true;
        }
        return false;
    }

    public boolean isActive(CoolDownType coolDownType) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return false;
            }
            PlayerData playerData = playerMetadataOptional.get();
            Set<CoolDownData> coolDowns = playerData.getCoolDowns();
            for (CoolDownData c : coolDowns) {
                if (c.getCoolDownType() != null && c.getCoolDownType().equals(coolDownType)) {
                    if (c.isActive()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isActiveSpellCoolDown(String spellName) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return false;
            }
            PlayerData playerData = playerMetadataOptional.get();
            Set<CoolDownData> coolDowns = playerData.getCoolDowns();
            for (CoolDownData coolDown : coolDowns) {
                if (coolDown.getName().equalsIgnoreCase(spellName)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void tickAllActiveCoolDowns() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            Set<CoolDownData> coolDowns = playerData.getCoolDowns();
            for(final CoolDownData coolDown : coolDowns) {
                if (coolDown.isActive()) {
                    coolDown.decrementTick();
                } else {
                    if (coolDown.equals(CoolDownType.DEATH)) {
                        gameManager.getChannelUtils().write(playerId, "You have risen from the dead.\r\n");
                    }
                    coolDown.remove();
                }
            }
            savePlayerMetadata();
        }
    }

    public void writePrompt() {
        String prompt = gameManager.buildPrompt(playerId);
        gameManager.getChannelUtils().write(playerId, prompt, true);
    }

    public String getPlayerId() {
        return playerId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Optional<String> getReturnDirection() {
        return returnDirection;
    }

    public void setReturnDirection(Optional<String> returnDirection) {
        this.returnDirection = returnDirection;
    }

    public Room getCurrentRoom() {
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (currentRoom == null && playerMetadataOptional.isPresent()) {
            PlayerData playerData = playerMetadataOptional.get();
            Integer currentRoomId = playerData.getCurrentRoomId();
            if (currentRoomId != null) {
                this.currentRoom = gameManager.getRoomManager().getRoom(currentRoomId);
            }
        }
        return currentRoom;
    }

    public void setCurrentRoomAndPersist(Room currentRoom) {
        // Persisting lazily so that performance doesn't suffer.
        setCurrentRoom(currentRoom);
        gameManager.getEventProcessor().addEvent(() -> {
            synchronized (interner.intern(playerId)) {
                Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
                if (!playerMetadataOptional.isPresent()) {
                    return;
                }
                PlayerData playerData = playerMetadataOptional.get();
                playerData.setCurrentRoomId(currentRoom.getRoomId());
                savePlayerMetadata();
            }
        });
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Map<String, Long> getNpcKillLog() {
        ImmutableMap.Builder<String, Long> builder = ImmutableMap.builder();
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (!playerMetadataOptional.isPresent()) {
            return Maps.newHashMap();
        }
        PlayerData playerData = playerMetadataOptional.get();
        playerData.getNpcKillLog().forEach(builder::put);
        return builder.build();
    }

    public void removePlayerFromRoom(Room room) {
        synchronized (interner.intern(playerId)) {
            room.removePresentPlayer(getPlayerId());
        }
    }

    public void movePlayer(PlayerMovement playerMovement) {
        synchronized (interner.intern(playerId)) {
            Optional<Room> sourceRoom = Optional.empty();
            if (playerMovement.getSourceRoomId() != null) {
                sourceRoom = Optional.ofNullable(gameManager.getRoomManager().getRoom(playerMovement.getSourceRoomId()));
            }

            Room destinationRoom = gameManager.getRoomManager().getRoom(playerMovement.getDestinationRoomId());

            if (sourceRoom.isPresent()) {
                removePlayerFromRoom(sourceRoom.get());
                for (Player next : sourceRoom.get().getPresentPlayers()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(playerMovement.getPlayer().getPlayerName());
                    sb.append(" ").append(playerMovement.getRoomExitMessage());
                    gameManager.getChannelUtils().write(next.getPlayerId(), sb.toString(), true);
                }
                setPreviousRoom(currentRoom);
            }

            destinationRoom.addPresentPlayer(playerMovement.getPlayer().getPlayerId());
            setCurrentRoomAndPersist(destinationRoom);
            for (Player next : destinationRoom.getPresentPlayers()) {
                if (next.getPlayerId().equals(playerMovement.getPlayer().getPlayerId())) {
                    continue;
                }
                gameManager.getChannelUtils().write(next.getPlayerId(), playerMovement.getPlayer().getPlayerName() + " arrived.", true);
            }
            setReturnDirection(java.util.Optional.ofNullable(playerMovement.getReturnDirection()));
            gameManager.currentRoomLogic(playerId, gameManager.getRoomManager().getRoom(playerMovement.getDestinationRoomId()));
            gameManager.getRoomManager().getRoom(playerMovement.getDestinationRoomId());
            processNpcAggro();
        }
    }

    public void processNpcAggro() {
        synchronized (interner.intern(playerId)) {
            if (isActive(CoolDownType.DEATH)) {
                return;
            }
            List<NpcSpawn> aggresiveRoomNpcSpawns = currentRoom.getNpcIds().stream()
                    .map(npcId -> gameManager.getEntityManager().getNpcEntity(npcId))
                    .filter(npc -> npc.getTemperament().equals(Temperament.AGGRESSIVE))
                    .filter(npc -> {
                        NpcSpawn.NpcLevelColor levelColor = npc.getLevelColor((int) Levels.getLevel(getPlayerStatsWithEquipmentAndLevel().getExperience()));
                        return !levelColor.equals(NpcSpawn.NpcLevelColor.WHITE);
                    })
                    .collect(Collectors.toList());

            aggresiveRoomNpcSpawns.forEach(npc -> {
                gameManager.writeToPlayerCurrentRoom(getPlayerId(), getPlayerName() + " has alerted a " + npc.getColorName() + "\r\n");
                gameManager.getChannelUtils().write(playerId, "You can return to your previous location by typing \"back\"" + "\r\n");
                setIsActiveAlertNpcStatus(npc);
                scheduledExecutor.schedule(() -> {
                    removeActiveAlertStatus(npc);
                    if (!areInTheSameRoom(npc)) {
                        return;
                    }
                    if (!npc.getIsAlive().get()) {
                        return;
                    }
                    if (isActive(CoolDownType.DEATH)) {
                        return;
                    }
                    gameManager.writeToPlayerCurrentRoom(getPlayerId(), getPlayerName() + " has " + Color.BOLD_ON + Color.RED + "ANGERED" + Color.RESET + " a " + npc.getColorName() + "\r\n");
                    addActiveFight(npc);
                }, 5, TimeUnit.SECONDS);
            });
        }
    }

    public Optional<Item> getInventoryItem(String itemKeyword) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return Optional.empty();
            }
            PlayerData playerData = playerMetadataOptional.get();
            for (String itemId : playerData.getInventory()) {
                Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
                if (!itemOptional.isPresent()) {
                    log.info("Orphaned inventoryId:" + itemId + " player: " + getPlayerName());
                    continue;
                }
                Item itemEntity = itemOptional.get();
                if (itemEntity.getItemTriggers().contains(itemKeyword)) {
                    return Optional.of(itemEntity);
                }
            }
            return Optional.empty();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<String> getRolledUpLockerInventory() {
        synchronized (interner.intern(playerId)) {
            List<String> rolledUp = Lists.newArrayList();
            List<Item> inventory = getLockerInventory();
            Map<String, Integer> itemAndCounts = Maps.newHashMap();
            if (inventory != null) {
                for (Item item : inventory) {
                    StringBuilder invItem = new StringBuilder();
                    invItem.append(item.getItemName());
                    int maxUses = item.getMaxUses();
                    if (item.getMaxUses() > 0) {
                        int remainingUses = maxUses - item.getNumberOfUses();
                        invItem.append(" - ").append(remainingUses);
                        if (remainingUses == 1) {
                            invItem.append(" use left.");
                        } else {
                            invItem.append(" uses left.");
                        }
                    }
                    if (itemAndCounts.containsKey(invItem.toString())) {
                        Integer integer = itemAndCounts.get(invItem.toString());
                        integer = integer + 1;
                        itemAndCounts.put(invItem.toString(), integer);
                    } else {
                        itemAndCounts.put(invItem.toString(), 1);
                    }
                }
                StringBuilder inventoryLine = new StringBuilder();
                for (Map.Entry<String, Integer> next : itemAndCounts.entrySet()) {
                    if (next.getValue() > 1) {
                        inventoryLine.append(next.getKey()).append(" (").append(next.getValue()).append(")").append("\r\n");
                    } else {
                        inventoryLine.append(next.getKey()).append("\r\n");
                    }
                }
                rolledUp.add(inventoryLine.toString());
            }
            return rolledUp;
        }
    }

    public List<Item> getLockerInventory() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptonal = getPlayerMetadata();
            if (!playerMetadataOptonal.isPresent()) {
                return Lists.newArrayList();
            }
            PlayerData playerData = playerMetadataOptonal.get();
            List<Item> inventoryItems = Lists.newArrayList();
            List<String> inventory = playerData.getLockerInventory();
            if (inventory != null) {
                for (String itemId : inventory) {
                    Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
                    if (!itemOptional.isPresent()) {
                        log.info("Orphaned inventoryId:" + itemId + " player: " + getPlayerName());
                        continue;
                    }
                    inventoryItems.add(itemOptional.get());
                }
            }
            inventoryItems.sort(Comparator.comparing(Item::getItemName));
            return inventoryItems;
        }
    }

    public List<String> getRolledUpIntentory() {
        synchronized (interner.intern(playerId)) {
            List<String> rolledUp = Lists.newArrayList();
            List<Item> inventory = getInventory();
            Map<String, Integer> itemAndCounts = Maps.newHashMap();
            if (inventory != null) {
                for (Item item : inventory) {
                    StringBuilder invItem = new StringBuilder();
                    invItem.append(item.getItemName());
                    int maxUses = item.getMaxUses();
                    if (maxUses > 0) {
                        int remainingUses = maxUses - item.getNumberOfUses();
                        invItem.append(" - ").append(remainingUses);
                        if (remainingUses == 1) {
                            invItem.append(" use left.");
                        } else {
                            invItem.append(" uses left.");
                        }
                    }
                    if (itemAndCounts.containsKey(invItem.toString())) {
                        Integer integer = itemAndCounts.get(invItem.toString());
                        integer = integer + 1;
                        itemAndCounts.put(invItem.toString(), integer);
                    } else {
                        itemAndCounts.put(invItem.toString(), 1);
                    }
                }
                StringBuilder inventoryLine = new StringBuilder();
                for (Map.Entry<String, Integer> next : itemAndCounts.entrySet()) {
                    if (next.getValue() > 1) {
                        inventoryLine.append(next.getKey()).append(" (").append(next.getValue()).append(")").append("\r\n");
                    } else {
                        inventoryLine.append(next.getKey()).append("\r\n");
                    }
                }
                rolledUp.add(inventoryLine.toString());
            }
            return rolledUp;
        }
    }

    public List<Item> getInventory() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return Lists.newArrayList();
            }
            PlayerData playerData = playerMetadataOptional.get();
            List<Item> inventoryItems = Lists.newArrayList();
            List<String> inventory = playerData.getInventory();
            if (inventory != null) {
                for (String itemId : inventory) {
                    Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
                    if (!itemOptional.isPresent()) {
                        log.info("Orphaned inventoryId:" + itemId + " player: " + getPlayerName());
                        continue;
                    }
                    inventoryItems.add(itemOptional.get());
                }
            }
            inventoryItems.sort(Comparator.comparing(Item::getItemName));
            return inventoryItems;
        }
    }

    public Set<Item> getEquipment() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return Sets.newHashSet();
            }
            PlayerData playerData = playerMetadataOptional.get();
            Set<Item> equipmentItems = Sets.newHashSet();
            List<String> equipment = playerData.getPlayerEquipment();
            if (equipment != null) {
                for (String itemId : equipment) {
                    Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
                    if (!itemOptional.isPresent()) {
                        log.info("Orphaned equipmentId:" + itemId + " player: " + getPlayerName());
                        continue;
                    }
                    equipmentItems.add(itemOptional.get());
                }
            }
            return equipmentItems;
        }
    }

    public void equip(Item item) {
        synchronized (interner.intern(playerId)) {
            if (item.getEquipment() == null) {
                return;
            }
            Equipment equipment = item.getEquipment();
            EquipmentSlotType equipmentSlotType = equipment.getEquipmentSlotType();
            Optional<Item> slotItemOptional = getSlotItem(equipmentSlotType);
            if (slotItemOptional.isPresent()) {
                if (!unEquip(slotItemOptional.get())) {
                    return;
                }
            }
            gameManager.getChannelUtils().write(playerId, "Equipping " + item.getItemName() + "\r\n");
            addEquipmentId(item.getItemId());
            removeInventoryId(item.getItemId());
        }
    }

    public Optional<Item> getSlotItem(EquipmentSlotType slot) {
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (!playerMetadataOptional.isPresent()) {
            return Optional.empty();
        }
        PlayerData playerData = playerMetadataOptional.get();
        if (playerData.getPlayerEquipment() == null) {
            return Optional.empty();
        }
        for (String item : playerData.getPlayerEquipment()) {
            Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(item);
            if (!itemOptional.isPresent()) {
                continue;
            }
            Item itemEntity = itemOptional.get();
            EquipmentSlotType equipmentSlotType = itemEntity.getEquipment().getEquipmentSlotType();
            if (equipmentSlotType.equals(slot)) {
                return Optional.of(itemEntity);
            }
        }
        return Optional.empty();
    }

    public boolean unEquip(Item item) {
        synchronized (interner.intern(playerId)) {
            gameManager.getChannelUtils().write(playerId, "Un-equipping " + item.getItemName() + "\r\n");
            if (gameManager.acquireItem(this, item.getItemId())) {
                removeEquipmentId(item.getItemId());
                return true;
            }
            return false;
        }
    }

    public void addEquipmentId(String equipmentId) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.addEquipmentEntityId(equipmentId);
            savePlayerMetadata();
        }
    }

    public void removeEquipmentId(String equipmentId) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.removeEquipmentEntityId(equipmentId);
            savePlayerMetadata();
        }
    }

    public String getLookString() {
        StringBuilder sb = new StringBuilder();
        Stats origStats = gameManager.getStatsModifierFactory().getStatsModifier(this);
        Stats modifiedStats = getPlayerStatsWithEquipmentAndLevel();
        Stats diffStats = StatsHelper.getDifference(modifiedStats, origStats);
        sb.append(Color.MAGENTA)
                .append("-+=[ ").append(Color.RESET).append(playerName).append(Color.MAGENTA + " ]=+- " + Color.RESET)
                .append("\r\n");
        sb.append("Level ").append(Levels.getLevel(origStats.getExperience())).append(" ")
                .append(Color.YELLOW).append("[").append(Color.RESET).append(AetherMudUtils.capitalize(getPlayerClass().getIdentifier())).append(Color.YELLOW).append("]").append(Color.RESET)
                .append("\r\n");
        sb.append("Foraging Level ").append(ForageManager.getLevel(modifiedStats.getForaging())).append("\r\n");
        sb.append(Color.MAGENTA + "Equip--------------------------------" + Color.RESET).append("\r\n");
        sb.append(buildEquipmentString()).append("\r\n");
        sb.append(Color.MAGENTA + "Stats--------------------------------" + Color.RESET).append("\r\n");
        sb.append(gameManager.buildLookString(playerName, modifiedStats, diffStats)).append("\r\n");
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (playerMetadataOptional.isPresent()) {
            PlayerData playerData = playerMetadataOptional.get();
            if (playerData.getEffects() != null && playerData.getEffects().size() > 0) {
                sb.append(Color.MAGENTA + "Effects--------------------------------" + Color.RESET).append("\r\n");
                sb.append(buldEffectsString()).append("\r\n");
            }
        }
        StringBuilder finalString = new StringBuilder();
        Lists.newArrayList(sb.toString().split("[\\r\\n]+")).forEach(s -> finalString.append(AetherMudUtils.trimTrailingBlanks(s)).append("\r\n"));
        return finalString.toString();
    }

    public Stats getPlayerStatsWithEquipmentAndLevel() {
        synchronized (interner.intern(playerId)) {
            StatsBuilder statsBuilder = new StatsBuilder();
            Stats newStats = statsBuilder.createStats();

            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return newStats;
            }
            PlayerData playerData = playerMetadataOptional.get();
            Stats playerStats = gameManager.getStatsModifierFactory().getStatsModifier(this);
            StatsHelper.combineStats(newStats, playerStats);
            List<String> playerEquipment = playerData.getPlayerEquipment();
            if (playerEquipment == null) {
                return playerStats;
            }
            for (String equipId : playerEquipment) {
                Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(equipId);
                if (!itemOptional.isPresent()) {
                    continue;
                }
                Item itemEntity = itemOptional.get();
                Equipment equipment = itemEntity.getEquipment();
                Stats stats = equipment.getStats();
                StatsHelper.combineStats(newStats, stats);
            }
            if (playerData.getEffects() != null) {
                for (Effect effect : playerData.getEffects()) {
                    StatsHelper.combineStats(newStats, effect.getDurationStats());
                }
            }
            return newStats;
        }
    }

    public PlayerClass getPlayerClass() {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return PlayerClass.BASIC;
            }
            PlayerData playerData = playerMetadataOptional.get();
            PlayerClass playerClass = playerData.getPlayerClass();
            if (playerClass == null) {
                return PlayerClass.BASIC;
            }
            return playerClass;
        }
    }

    public void setPlayerClass(PlayerClass playerClass) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.setPlayerClass(playerClass);
            savePlayerMetadata();
        }
    }

    public String buildEquipmentString() {
        Table t = new Table(2, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.NONE);
        t.setColumnWidth(0, 16, 20);

        List<EquipmentSlotType> all = EquipmentSlotType.getAll();
        for (EquipmentSlotType slot : all) {
            t.addCell(capitalize(slot.getName()));
            Optional<Item> slotItemOptional = getSlotItem(slot);
            if (slotItemOptional.isPresent()) {
                t.addCell(slotItemOptional.get().getItemName());
            } else {
                t.addCell("");
            }
        }
        return t.render();
    }

    /* FIGHT FIGHT FIGHT FIGHT */

    public String buldEffectsString() {
        Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
        if (!playerMetadataOptional.isPresent()) {
            return "";
        }
        PlayerData playerData = playerMetadataOptional.get();
        List<Effect> effects = playerData.getEffects();
        return gameManager.renderEffectsString(effects);
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public void removeAllActiveFights() {
        synchronized (interner.intern(playerId)) {
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                iterator.remove();
            }
        }
    }

    public boolean setPlayerSetting(String key, String value) {
        boolean success;
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return false;
            }
            PlayerData playerData = playerMetadataOptional.get();
            success = playerData.setSetting(key, value);
            savePlayerMetadata();
        }
        return success;
    }

    public Optional<String> getPlayerSetting(String key) {
        return getPlayerMetadata().flatMap(playerMetadata -> Optional.ofNullable(playerMetadata.getSetting(key)));
    }

    public void removePlayerSetting(String key) {
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = getPlayerMetadata();
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.deleteSetting(key);
            savePlayerMetadata();
        }
    }

    public Map<String, String> getPlayerSettings() {
        return getPlayerMetadata().map(PlayerData::getPlayerSettings).orElseGet(Maps::newHashMap);
    }

    public boolean addActiveFight(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            if (gameManager.getEntityManager().getNpcEntity(npcSpawn.getEntityId()) != null) {
                if (!doesActiveFightExist(npcSpawn)) {
                    addCoolDown(new CoolDownPojo(CoolDownType.NPC_FIGHT));
                    ActiveFight activeFight = ActiveFight.builder()
                            .npcId(npcSpawn.getEntityId())
                            .isPrimary(false)
                            .create();
                    activeFights.put(System.nanoTime(), activeFight);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doesActiveFightExist(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            if (gameManager.getEntityManager().getNpcEntity(npcSpawn.getEntityId()) == null) {
                removeActiveFight(npcSpawn);
            }
            for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
                ActiveFight fight = entry.getValue();
                Optional<String> npcIdOptional = fight.getNpcId();
                if (npcIdOptional.isPresent() && npcIdOptional.get().equals(npcSpawn.getEntityId())) {
                    return true;
                }
            }
            return false;
        }
    }

    public void removeActiveFight(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                if (next.getValue().getNpcId().equals(npcSpawn.getEntityId())) {
                    if (next.getValue().isPrimary()) {
                    }
                    iterator.remove();
                }
            }
        }
    }

    public boolean isActiveFights() {
        synchronized (interner.intern(playerId)) {
            if (activeFights.size() > 0) {
                // Remove any fights with dead NPCs that no longer exist in Entity Manager.
                activeFights.entrySet().removeIf(next -> next.getValue().getNpcId().isPresent() && gameManager.getEntityManager().getNpcEntity(next.getValue().getNpcId().get()) == null);
            }
        }
        return activeFights.size() > 0;
    }

    public boolean isValidPrimaryActiveFight(NpcSpawn npcSpawn) {
        synchronized (interner.intern(playerId)) {
            for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
                ActiveFight fight = entry.getValue();
                Optional<String> npcIdOptional = fight.getNpcId();
                if (npcIdOptional.isPresent() && fight.getNpcId().get().equals(npcSpawn.getEntityId()) && fight.isPrimary()) {
                    return true;
                }
            }
            return false;
        }
    }

    public Optional<ActiveFight> getPrimaryActiveFight() {
        synchronized (interner.intern(playerId)) {
            for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
                ActiveFight fight = entry.getValue();
                if (fight.isPrimary()) {
                    return Optional.of(fight);
                }
            }
            return Optional.empty();
        }
    }

    public void activateNextPrimaryActiveFight() {
        synchronized (interner.intern(playerId)) {
            if (!getPrimaryActiveFight().isPresent()) {
                if (activeFights.size() > 0) {
                    activeFights.get(activeFights.firstKey()).setIsPrimary(true);
                }
            }
        }
    }

    private void doFightRound(DamageProcessor playerDamageProcessor, DamageProcessor npcDamageProcessor, ActiveFight activeFight) {
        removeActiveAlertStatus();

        // IF FIGHTING NPC
        Optional<String> npcIdOptional = activeFight.getNpcId();
        if (npcIdOptional.isPresent()) {
            String npcId = npcIdOptional.get();
            NpcSpawn npcSpawn = gameManager.getEntityManager().getNpcEntity(npcId);
            if (npcSpawn == null) {
                return;
            }

            NpcStatsChangeBuilder npcStatsChangeBuilder = new NpcStatsChangeBuilder().setPlayer(this);
            if (this.isValidPrimaryActiveFight(npcSpawn)) {
                calculatePlayerDamageToNpc(playerDamageProcessor, npcSpawn, npcStatsChangeBuilder);
            }

            if (this.doesActiveFightExist(npcSpawn)) {
                calculateNpcDamageToPlayer(npcDamageProcessor, npcSpawn, npcStatsChangeBuilder);
            }
        }

        // IF FIGHTING PLAYER?
    }

    private void calculatePlayerDamageToNpc(DamageProcessor playerDamageProcessor, NpcSpawn npcSpawn, NpcStatsChangeBuilder npcStatsChangeBuilder) {
        int damageToVictim = 0;
        int chanceToHit = playerDamageProcessor.getChanceToHit(this, npcSpawn);
        if (randInt(0, 100) < chanceToHit) {
            damageToVictim = playerDamageProcessor.getAttackAmount(this, npcSpawn);
        }
        if (damageToVictim > 0) {
            if (randInt(0, 100) > (100 - playerDamageProcessor.getCriticalChance(this, npcSpawn))) {
                int criticalDamage = damageToVictim * 3;
                final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + Color.YELLOW + "The " + npcSpawn.getColorName() + " was caught off guard by the attack! " + "+" + NumberFormat.getNumberInstance(Locale.US).format(criticalDamage) + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npcSpawn.getColorName();
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-(criticalDamage)).createStats());
                npcStatsChangeBuilder.setDamageStrings(Collections.singletonList(fightMsg));
            } else {
                final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + Color.YELLOW + "+" + NumberFormat.getNumberInstance(Locale.US).format(damageToVictim) + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npcSpawn.getColorName();
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Collections.singletonList(fightMsg));
            }
        } else {
            final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + "You MISS " + npcSpawn.getName() + "!";
            npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
            npcStatsChangeBuilder.setDamageStrings(Collections.singletonList(fightMsg));
        }
    }

    private void calculateNpcDamageToPlayer(DamageProcessor npcDamageProcessor, NpcSpawn npcSpawn, NpcStatsChangeBuilder npcStatsChangeBuilder) {
        int chanceToHitBack = npcDamageProcessor.getChanceToHit(this, npcSpawn);
        int damageBack = npcDamageProcessor.getAttackAmount(this, npcSpawn);
        if (randInt(0, 100) < chanceToHitBack) {
            final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + npcSpawn.buildAttackMessage(this.getPlayerName()) + " -" + NumberFormat.getNumberInstance(Locale.US).format(damageBack) + Color.RESET;
            npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(-damageBack).createStats());
            npcStatsChangeBuilder.setPlayerDamageStrings(Collections.singletonList(fightMsg));

        } else {
            final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + npcSpawn.getColorName() + Color.BOLD_ON + Color.CYAN + " MISSES" + Color.RESET + " you!";
            npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(0).createStats());
            npcStatsChangeBuilder.setPlayerDamageStrings(Collections.singletonList(fightMsg));
        }
        npcSpawn.addNpcDamage(npcStatsChangeBuilder.createNpcStatsChange());
    }


    public SortedMap<Long, ActiveFight> getActiveFights() {
        return activeFights;
    }

    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public Interner<String> getInterner() {
        return interner;
    }


    public boolean toggleChat() {
        synchronized (interner.intern(playerId)) {
            if (isChatModeOn()) {
                setNotIsChatMode();
                return false;
            } else {
                setIsChatMode();
                return true;
            }
        }
    }

    public void setIsChatMode() {
        this.isChatMode.compareAndSet(false, true);
    }

    public void setNotIsChatMode() {
        this.isChatMode.compareAndSet(true, false);
    }

    public boolean isChatModeOn() {
        return isChatMode.get();
    }
}
