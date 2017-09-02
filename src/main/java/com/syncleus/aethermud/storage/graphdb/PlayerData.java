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
package com.syncleus.aethermud.storage.graphdb;


import com.google.api.client.util.Lists;
import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.player.*;
import com.google.common.collect.Sets;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.ClassInitializer;
import com.syncleus.ferma.DefaultClassInitializer;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.Property;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.util.*;

public abstract class PlayerData extends AbstractVertexFrame {

    static final ClassInitializer<PlayerData> DEFAULT_INITIALIZER = new DefaultClassInitializer(PlayerData.class);

    @Property("name")
    public abstract String getPlayerName();

    @Property("name")
    public abstract void setPlayerName(String name);

    @Property("password")
    public abstract String getPassword();

    @Property("password")
    public abstract void setPassword(String password);

    @Property("playerId")
    public abstract String getPlayerId();

    @Property("playerId")
    public abstract void setPlayerId(String playerId);

    @Property("inventory")
    public abstract List<String> getInventory();

    @Property("inventory")
    public abstract void setInventory(List<String> inventory);

    @Property("lockerInventory")
    public abstract List<String> getLockerInventory();

    @Property("lockerInventory")
    public abstract void setLockerInventory(List<String> lockerInventory);

    @Property("gold")
    public abstract int getGold();

    @Property("gold")
    public abstract void setGold(int gold);

    @Property("goldInBank")
    public abstract int getGoldInBank();

    @Property("goldInBank")
    public abstract void setGoldInBank(int goldInBank);

    @Property("roleSet")
    public abstract Collection<PlayerRole> getPlayerRoleTextCollection();

    public Set<PlayerRole> getPlayerRoleSet() {
        HashSet<PlayerRole> roles = new HashSet<>();
        Collection<PlayerRole> rolesText = getPlayerRoleTextCollection();
        for(final PlayerRole roleText : rolesText)
            roles.add(roleText);
        return Collections.unmodifiableSet(roles);
    }

    @Property("roleSet")
    public abstract void setPlayerRoleSet(Collection<PlayerRole> playerRoleSet);

    @Property("equipment")
    public abstract List<String> getPlayerEquipment();

    @Property("equipment")
    public abstract void setPlayerEquipment(List<String> playerEquipment);

    @Property("effects")
    public abstract List<Effect> getEffects();

    @Property("effects")
    public abstract void setEffects(List<Effect> effects);

    @Property("markedForDelete")
    public abstract boolean isMarkedForDelete();

    @Property("markedForDelete")
    public abstract void setIsMarkedForDelete(boolean markedForDelete);

    @Property("settings")
    public abstract Map<String, String> getPlayerSettings();

    @Property("settings")
    public abstract void setPlayerSettings(Map<String, String> playerSettings);

    @Property("learnedSpells")
    public abstract List<String> getLearnedSpells();

    @Property("learnedSpells")
    public abstract void setLearnedSpells(List<String> learnedSpells);

    @Property("npcKillLog")
    public abstract Map<String, Long> getNpcKillLog();

    @Property("npcKillLog")
    public abstract void setNpcKillLog(Map<String, Long> killLog);

    @Property("playerClass")
    public abstract PlayerClass getPlayerClass();

    @Property("playerClass")
    public abstract void setPlayerClass(PlayerClass playerClass);

    @Property("currentRoomId")
    public abstract Integer getCurrentRoomId();

    @Property("currentRoomId")
    public abstract void setCurrentRoomId(Integer currentRoomId);

    @Adjacency(label = "coolDowns", direction = Direction.OUT)
    public abstract <N extends CoolDownData> Iterator<? extends N> getCoolDowns(Class<? extends N> type);

    @Adjacency(label = "coolDowns", direction = Direction.OUT)
    public abstract CoolDownData addCoolDowns(CoolDownData coolDowns);

    @Adjacency(label = "coolDowns", direction = Direction.OUT)
    public abstract void removeCoolDown(CoolDownData stats);

    @Adjacency(label = "coolDowns", direction = Direction.OUT)
    public abstract void addCoolDown(CoolDownData coolDown);

    public Map<CoolDownType, CoolDownData> getCoolDownMap() {
        Iterator<? extends CoolDownData> coolDowns = getCoolDowns(CoolDownData.class);
        final HashMap<CoolDownType, CoolDownData> coolDownsMap = new HashMap<>();
        while(coolDowns.hasNext()) {
            CoolDownData coolDown = coolDowns.next();
            coolDownsMap.put(coolDown.getCoolDownType(), coolDown);
        }

        return Collections.unmodifiableMap(coolDownsMap);
    }

    public void setCoolDowns(Map<CoolDownType, CoolDownData> coolDowns) {
        Iterator<? extends CoolDownData> existingCoolDowns = getCoolDowns(CoolDownData.class);
        while(existingCoolDowns.hasNext()) {
            CoolDownData existingCoolDown = existingCoolDowns.next();
            this.removeCoolDown(existingCoolDown);
        }

        for(CoolDownData coolDown : coolDowns.values()) {
            this.addCoolDown(coolDown);
        }
    }

    public Set<CoolDownData> getCoolDowns() {
        Map<CoolDownType, CoolDownData> coolDowns = this.getCoolDownMap();
        return Collections.unmodifiableSet(Sets.newHashSet(coolDowns.values()));
    }

    public CoolDownData createCoolDown() {
        final CoolDownData coolDown = this.getGraph().addFramedVertex(CoolDownData.class);
        this.addCoolDown(coolDown);
        return coolDown;
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract <N extends StatsData> Iterator<? extends N> getAllStats(Class<? extends N> type);

    public StatsData getStats() {
        Iterator<? extends StatsData> allStats = this.getAllStats(StatsData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract StatsData addStats(StatsData stats);

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract void removeStats(StatsData stats);

    public void setStats(StatsData stats) {
        Iterator<? extends StatsData> existingAll = this.getAllStats(StatsData.class);
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                StatsData existing = existingAll.next();
                this.removeStats(existing);
                existing.remove();
            }

        }
        if( stats != null )
            this.addStats(stats);
    }

    public StatsData createStats() {
        if( this.getStats() != null )
            throw new IllegalStateException("Already has stats, can't create another");
        final StatsData stats = this.getGraph().addFramedVertex(StatsData.class);
        stats.setAgile(0);
        stats.setAim(0);
        stats.setArmorRating(0);
        stats.setCurrentHealth(0);
        stats.setCurrentMana(0);
        stats.setExperience(0);
        stats.setForaging(0);
        stats.setIntelligence(0);
        stats.setInventorySize(0);
        stats.setMaxEffects(0);
        stats.setMaxHealth(0);
        stats.setMaxMana(0);
        stats.setMeleSkill(0);
        stats.setNumberOfWeaponRolls(0);
        stats.setStrength(0);
        stats.setWeaponRatingMax(0);
        stats.setWeaponRatingMin(0);
        stats.setWillpower(0);
        this.setStats(stats);
        return stats;
    }

    public void addInventoryEntityId(String newEntityId) {
        List<String> inventory = this.getInventory();
        if (inventory == null) {
            inventory = new ArrayList<>();
        }
        inventory.add(newEntityId);
        this.setInventory(inventory);
    }

    public void addLockerEntityId(String newEntityId) {
        List<String> lockerInventory = this.getLockerInventory();
        if (lockerInventory == null) {
            lockerInventory = new ArrayList<>();
        }
        lockerInventory.add(newEntityId);
        this.setLockerInventory(lockerInventory);
    }

    public void addNpcKill(String npcName) {
        Map<String, Long> npcKillLog = this.getNpcKillLog();
        if (npcKillLog == null) {
            npcKillLog = new HashMap<>();
        }

        if (npcKillLog.containsKey(npcName)) {
            Long aLong = npcKillLog.get(npcName);
            Long newLong = aLong + 1;
            npcKillLog.put(npcName, newLong);
        } else {
            npcKillLog.put(npcName, 1L);
        }

        this.setNpcKillLog(npcKillLog);
    }

    public void removeLockerEntityId(String newEntityId) {
        List<String> lockerInventory = this.getLockerInventory();
        lockerInventory.remove(newEntityId);
        this.setLockerInventory(lockerInventory);
    }

    public void removeInventoryEntityId(String itemId) {
        List<String> inventory = this.getInventory();
        inventory.remove(itemId);
        this.setInventory(inventory);
    }

    public void addLearnedSpellByName(String spellName) {
        List<String> learnedSpells = this.getLearnedSpells();
        if (learnedSpells == null) {
            learnedSpells = new ArrayList<>();
        }
        learnedSpells.add(spellName);
        this.setLearnedSpells(learnedSpells);
    }

    public void removeLearnedSpellByName(String spellName) {
        List<String> learnedSpellsKeep = this.getLearnedSpells();
        learnedSpellsKeep.remove(spellName);
        this.setLearnedSpells(learnedSpellsKeep);
    }

    public void addEquipmentEntityId(String equipmentItemId) {
        List<String> playerEquipment = this.getPlayerEquipment();
        if (playerEquipment == null) {
            playerEquipment = new ArrayList<>();
        }
        playerEquipment.add(equipmentItemId);
        this.setPlayerEquipment(playerEquipment);
    }

    public void removeEquipmentEntityId(String equipmentItemId) {
        List<String> playerEquipment = this.getPlayerEquipment();
        playerEquipment.remove(equipmentItemId);
        this.setPlayerEquipment(playerEquipment);
    }

    public void addEffect(Effect effect) {
        List<Effect> effects = this.getEffects();
        if (effects == null) {
            effects = new ArrayList<>();
        }
        effects.add(effect);
        this.setEffects(effects);
    }

    public void removeEffect(Effect effect) {
        List<Effect> effects = this.getEffects();
        effects.remove(effect);
        this.setEffects(effects);
    }

    public void incrementGold(int amt) {
        int gold = this.getGold();
        gold = gold + amt;
        this.setGold(gold);
    }

    public void transferGoldToBank(int amt) {
        int gold = this.getGold();
        int goldInBank = this.getGoldInBank();
        gold = gold - amt;
        goldInBank = goldInBank + amt;
        this.setGold(gold);
        this.setGoldInBank(goldInBank);
    }

    public void transferBankGoldToPlayer(int amt) {
        int gold = this.getGold();
        int goldInBank = this.getGoldInBank();
        goldInBank = goldInBank - amt;
        gold = gold + amt;
        this.setGold(gold);
        this.setGoldInBank(goldInBank);
    }

    public void addPlayerRole(PlayerRole playerRole) {
        Set<PlayerRole> playerRoleSet = Sets.newHashSet(this.getPlayerRoleSet());
        if (playerRoleSet == null) {
            playerRoleSet = Sets.newHashSet();
        }
        playerRoleSet.add(playerRole);
        this.setPlayerRoleSet(playerRoleSet);
    }

    public void resetPlayerRoles() {
        this.setPlayerRoleSet(new HashSet());
    }

    public void resetCoolDowns() {
        this.setCoolDowns(new HashMap<>());
    }

    public void resetEffects(){
        this.setEffects(new ArrayList<>());
    }

    public boolean setSetting(String key, String value) {
        Map<String, String> playerSettings = this.getPlayerSettings();
        if (playerSettings == null) {
            playerSettings = new HashMap<>();
        }
        if (playerSettings.size() >= 100) {
            return false;
        }
        PlayerSettings byKey = PlayerSettings.getByKey(key);
        if (byKey == null) {
            return false;
        }
        if (byKey.getType().equals(Integer.TYPE)) {
            try {
                int i = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        playerSettings.put(key, value);
        this.setPlayerSettings(playerSettings);
        return true;
    }

    public String getSetting(String key) {
        Map<String, String> playerSettings = this.getPlayerSettings();
        if (playerSettings == null) {
            return null;
        }
        return playerSettings.get(key);
    }

    public void deleteSetting(String key) {
        Map<String, String> playerSettings = this.getPlayerSettings();
        if (playerSettings == null) {
            return;
        }
        playerSettings.remove(key);
        this.setPlayerSettings(playerSettings);
    }
}

