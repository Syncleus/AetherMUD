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
package com.syncleus.aethermud.storage.graphdb.model;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.*;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GraphElement
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
    public abstract Collection<String> getPlayerRoleTextCollection();

    public Set<PlayerRole> getPlayerRoles() {
        HashSet<PlayerRole> roles = new HashSet<>();
        Collection<String> rolesText = getPlayerRoleTextCollection();
        for (final String roleText : rolesText)
            roles.add(PlayerRole.valueOf(roleText));
        return Collections.unmodifiableSet(roles);
    }

    public void setPlayerRoles(Collection<PlayerRole> playerRoleSet) {
        ArrayList<String> newProperty = new ArrayList<String>();
        for (PlayerRole role : playerRoleSet) {
            newProperty.add(role.toString());
        }
        this.setProperty("roleSet", newProperty);
    }

    @Property("equipment")
    public abstract List<String> getPlayerEquipment();

    @Property("equipment")
    public abstract void setPlayerEquipment(List<String> playerEquipment);

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

    public Map<String, Long> getNpcKillLog() {
        final List<? extends TEdge> killEdges = this.traverse((v) -> v.outE().hasLabel("npcKillLog")).toList(TEdge.class);
        if( killEdges == null || killEdges.isEmpty())
            return Collections.emptyMap();

        final Map<String, Long> killLog = new HashMap<>(killEdges.size());
        for(TEdge killEdge : killEdges) {
            final Long count = killEdge.getProperty("count");
            final String name = killEdge.traverse((e) -> this.getGraph().getTypeResolver().hasNotType(e.inV(), NpcData.class)).next(NpcData.class).getProperty("name");
            killLog.put(name, count);
        }
        return Collections.unmodifiableMap(killLog);
    }

    public void setNpcKillLog(Map<String, Long> killLog) {
        final Map<String, Long> existingLog = this.getNpcKillLog();

        final Set<String> toDeleteNames = Sets.newHashSet(existingLog.keySet());
        toDeleteNames.removeAll(killLog.keySet());
        final Map<String, Long> toAdd = Maps.newHashMap(killLog);
        toAdd.keySet().removeAll(existingLog.keySet());

        final List<? extends TEdge> killEdges = this.traverse((v) -> v.outE().hasLabel("npcKillLog")).toList(TEdge.class);
        for(TEdge killEdge : killEdges) {
            final String name = killEdge.traverse((e) -> this.getGraph().getTypeResolver().hasNotType(e.inV(), NpcData.class)).next(NpcData.class).getProperty("name");
            if(toDeleteNames.contains(name))
                killEdge.remove();
            else
                killEdge.setProperty("count", killLog.get(name));
        }

        for(Map.Entry<String, Long> addEntry : toAdd.entrySet()) {
            final String name = addEntry.getKey();
            final Long count = addEntry.getValue();
            final NpcData dest = this.getGraph().traverse((g) -> this.getGraph().getTypeResolver().hasNotType(g.V().has("name", name), NpcData.class)).next(NpcData.class);
            final TEdge addedEdge = this.getGraph().addFramedEdge(this, dest, "npcKillLog");
            addedEdge.setProperty("npcKillLog", count);
        }
    }

    @Property("playerClass")
    public abstract PlayerClass getPlayerClass();

    @Property("playerClass")
    public abstract void setPlayerClass(PlayerClass playerClass);

    @Property("currentRoomId")
    public abstract Integer getCurrentRoomId();

    @Property("currentRoomId")
    public abstract void setCurrentRoomId(Integer currentRoomId);

    @Adjacency(label = "effect", direction = Direction.OUT)
    public abstract EffectData addEffect(EffectData effects);

    @Adjacency(label = "effect", direction = Direction.OUT)
    public abstract void removeEffect(EffectData stats);

    @Adjacency(label = "effect", direction = Direction.OUT)
    public abstract <N extends EffectData> Iterator<? extends N> getEffects(Class<? extends N> type);

    public Set<EffectData> getEffects() {
        return Sets.newHashSet(this.getEffects(EffectData.class));
    }

    public void setEffects(Set<EffectData> effects) {
        this.resetEffects();

        if (effects == null || effects.size() == 0) {
            return;
        }

        for (EffectData effect : effects) {
            this.addEffect(effect);
        }
    }

    public EffectData createEffect() {
        final EffectData effect = this.getGraph().addFramedVertex(EffectData.class);
        this.addEffect(effect);
        return effect;
    }

    public void resetEffects() {
        Iterator<? extends EffectData> existingAll = this.getEffects(EffectData.class);
        if (existingAll != null) {
            while (existingAll.hasNext()) {
                EffectData existing = existingAll.next();
                this.removeEffect(existing);
                existing.remove();
            }
        }
    }

    @Adjacency(label = "coolDown", direction = Direction.OUT)
    public abstract <N extends CoolDownData> Iterator<? extends N> getCoolDowns(Class<? extends N> type);

    @Adjacency(label = "coolDown", direction = Direction.OUT)
    public abstract void removeCoolDown(CoolDownData stats);

    @Adjacency(label = "coolDown", direction = Direction.OUT)
    public abstract void addCoolDown(CoolDownData coolDown);

    public void setCoolDowns(Map<CoolDownType, CoolDownData> coolDowns) {
        Iterator<? extends CoolDownData> existingCoolDowns = getCoolDowns(CoolDownData.class);
        while (existingCoolDowns.hasNext()) {
            CoolDownData existingCoolDown = existingCoolDowns.next();
            this.removeCoolDown(existingCoolDown);
        }

        for (CoolDownData coolDown : coolDowns.values()) {
            this.addCoolDown(coolDown);
        }
    }

    public Set<CoolDownData> getCoolDowns() {
        return Collections.unmodifiableSet(Sets.newHashSet(this.getCoolDowns(CoolDownData.class)));
    }

    public CoolDownData createCoolDown(CoolDownType type) {
        Iterator<? extends CoolDownData> coolDowns = getCoolDowns(CoolDownData.class);
        while (coolDowns.hasNext()) {
            CoolDownData coolDown = coolDowns.next();
            if (coolDown.getCoolDownType().equals(type)) {
                coolDown.remove();
            }
        }

        final CoolDownData coolDown = this.getGraph().addFramedVertex(CoolDownData.class);
        coolDown.setNumberOfTicks(type.getTicks());
        coolDown.setOriginalNumberOfTicks(type.getTicks());
        coolDown.setName(type.getName());
        coolDown.setCoolDownType(type);
        this.addCoolDown(coolDown);
        return coolDown;
    }

    public CoolDownData createCoolDown(CoolDown coolDownSource) {
        Iterator<? extends CoolDownData> coolDowns = getCoolDowns(CoolDownData.class);
        while (coolDowns.hasNext()) {
            CoolDownData coolDown = coolDowns.next();
            if (coolDown.getCoolDownType().equals(coolDownSource.getCoolDownType())) {
                coolDown.remove();
            }
        }

        final CoolDownData coolDown = this.getGraph().addFramedVertex(CoolDownData.class);
        try {
            PropertyUtils.copyProperties(coolDown, coolDownSource);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Can't copy properties", e);
        }
        this.addCoolDown(coolDown);
        return coolDown;
    }

    public void resetCoolDowns() {
        this.setCoolDowns(new HashMap<>());
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract <N extends StatData> Iterator<? extends N> getStatDataIterator(Class<? extends N> type);

    public StatData getStatData() {
        Iterator<? extends StatData> allStats = this.getStatDataIterator(StatData.class);
        if (allStats.hasNext())
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract StatData addStatData(StatData stats);

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract void removeStatData(StatData stats);

    public void setStats(StatData stats) {
        DataUtils.setAllElements(Collections.singletonList(stats), () -> this.getStatDataIterator(StatData.class), statsData -> this.addStatData(statsData), () -> createStatData());
    }

    public StatData createStatData() {
        if (this.getStatData() != null)
            throw new IllegalStateException("Already has stats, can't create another");
        final StatData stats = this.getGraph().addFramedVertex(StatData.class);
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
        stats.setMeleeSkill(0);
        stats.setNumberOfWeaponRolls(0);
        stats.setStrength(0);
        stats.setWeaponRatingMax(0);
        stats.setWeaponRatingMin(0);
        stats.setWillpower(0);
        this.addStatData(stats);
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
        Set<PlayerRole> playerRoleSet = Sets.newHashSet(this.getPlayerRoles());
        if (playerRoleSet == null) {
            playerRoleSet = Sets.newHashSet();
        }
        playerRoleSet.add(playerRole);
        this.setPlayerRoles(playerRoleSet);
    }

    public void resetPlayerRoles() {
        this.setPlayerRoles(new HashSet());
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

