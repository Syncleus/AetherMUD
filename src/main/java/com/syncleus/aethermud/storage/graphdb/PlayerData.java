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
import com.syncleus.aethermud.player.CoolDown;
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.aethermud.player.PlayerClass;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.ClassInitializer;
import com.syncleus.ferma.DefaultClassInitializer;
import com.syncleus.ferma.annotations.Property;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class PlayerData extends AbstractVertexFrame {
    static final ClassInitializer<PlayerData> DEFAULT_INITIALIZER = new DefaultClassInitializer(PlayerData.class);

    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

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
    public abstract String[] getPlayerEquipment();

    @Property("equipment")
    public abstract void setPlayerEquipment(String[] playerEquipment);

    @Property("effects")
    public abstract List<Effect> getEffects();

    @Property("effects")
    public abstract void setEffects(List<Effect> effects);

    @Property("markedForDelete")
    public abstract boolean isMarkedForDelete();

    @Property("markedForDelete")
    public abstract void setMarkedForDelete(boolean markedForDelete);

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
    public abstract Map<CoolDownType, CoolDown> getCoolDowns();

    @Property("coolDowns")
    public abstract void setCoolDowns(Map<CoolDownType, CoolDown> coolDowns);

    @Property("currentRoomId")
    public abstract Integer getCurrentRoomId();

    @Property("currentRoomId")
    public abstract void setCurrentRoomId(Integer currentRoomId);
}
