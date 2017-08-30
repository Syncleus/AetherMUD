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


import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.stats.Stats;
import com.google.common.collect.Sets;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.ClassInitializer;
import com.syncleus.ferma.DefaultClassInitializer;
import com.syncleus.ferma.annotations.Property;

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

    @Property("stats")
    public abstract Stats getStats();

    @Property("stats")
    public abstract void setStats(Stats stats);

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
    public abstract Set<PlayerRole> getPlayerRoleSet();

    @Property("roleSet")
    public abstract void setPlayerRoleSet(Set<PlayerRole> playerRoleSet);

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

    @Property("coolDowns")
    public abstract Map<CoolDownType, CoolDown> getCoolDownMap();

    @Property("coolDowns")
    public abstract void setCoolDowns(Map<CoolDownType, CoolDown> coolDowns);

    @Property("currentRoomId")
    public abstract Integer getCurrentRoomId();

    @Property("currentRoomId")
    public abstract void setCurrentRoomId(Integer currentRoomId);

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

    public void addCoolDown(CoolDown coolDown) {
        Map<CoolDownType, CoolDown> coolDowns = this.getCoolDownMap();

        if (coolDowns == null) {
            coolDowns = new HashMap<>();
        }
        coolDowns.put(coolDown.getCoolDownType(), coolDown);
        this.setCoolDowns(coolDowns);
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
        Set<PlayerRole> playerRoleSet = this.getPlayerRoleSet();
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

    public Set<CoolDown> getCoolDowns() {
        Map<CoolDownType, CoolDown> coolDowns = this.getCoolDownMap();
        if (coolDowns == null) {
            coolDowns = new HashMap<>();
        }
        return Sets.newHashSet(coolDowns.values());
    }


}

