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
package com.syncleus.aethermud.storage;

import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.npc.Temperament;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.world.model.Area;

import java.util.Set;

public class NpcMetadata {

    private String name;
    private String colorName;
    private Stats stats;
    private String dieMessage;
    private Temperament temperament;
    private Set<Area> roamAreas;
    private Set<String> validTriggers;
    private Set<SpawnRule> spawnRules;
    private Loot loot;
    // The messages used when dealing damage
    private Set<AetherMudMessage> attackMessages;
    // The messages used when landing critical attacks
    private Set<AetherMudMessage> criticalAttackMessages;
    // Things the NPC randomly says during battle
    private Set<AetherMudMessage> battleMessages;
    // Things that npcs say randomly when idle
    private Set<AetherMudMessage> idleMessages;

    public NpcMetadata() {
    }

    public Set<AetherMudMessage> getCriticalAttackMessages() {
        return criticalAttackMessages;
    }

    public void setCriticalAttackMessages(Set<AetherMudMessage> criticalAttackMessages) {
        this.criticalAttackMessages = criticalAttackMessages;
    }

    public Set<AetherMudMessage> getBattleMessages() {
        return battleMessages;
    }

    public void setBattleMessages(Set<AetherMudMessage> battleMessages) {
        this.battleMessages = battleMessages;
    }

    public Set<AetherMudMessage> getIdleMessages() {
        return idleMessages;
    }

    public void setIdleMessages(Set<AetherMudMessage> idleMessages) {
        this.idleMessages = idleMessages;
    }

    public Set<AetherMudMessage> getAttackMessages() {
        return attackMessages;
    }

    public void setAttackMessages(Set<AetherMudMessage> attackMessages) {
        this.attackMessages = attackMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String getDieMessage() {
        return dieMessage;
    }

    public void setDieMessage(String dieMessage) {
        this.dieMessage = dieMessage;
    }

    public Temperament getTemperament() {
        return temperament;
    }

    public void setTemperament(Temperament temperament) {
        this.temperament = temperament;
    }

    public Set<Area> getRoamAreas() {
        return roamAreas;
    }

    public void setRoamAreas(Set<Area> roamAreas) {
        this.roamAreas = roamAreas;
    }

    public Set<String> getValidTriggers() {
        return validTriggers;
    }

    public void setValidTriggers(Set<String> validTriggers) {
        this.validTriggers = validTriggers;
    }

    public Set<SpawnRule> getSpawnRules() {
        return spawnRules;
    }

    public void setSpawnRules(Set<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
    }

    public Loot getLoot() {
        return loot;
    }

    public void setLoot(Loot loot) {
        this.loot = loot;
    }
}


