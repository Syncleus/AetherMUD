/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.npc;

import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.world.model.Area;

import java.util.List;

public class Npc {

    private String name;
    private String colorName;
    private Stats stats;
    private String dieMessage;
    private Temperament temperament;
    private List<Area> roamAreas;
    private List<String> validTriggers;
    private List<SpawnRule> spawnRules;
    private Loot loot;
    // The messages used when dealing damage
    private List<AetherMudMessage> attackMessages;
    // The messages used when landing critical attacks
    private List<AetherMudMessage> criticalAttackMessages;
    // Things the NPC randomly says during battle
    private List<AetherMudMessage> battleMessages;
    // Things that npcs say randomly when idle
    private List<AetherMudMessage> idleMessages;

    public Npc() {
    }

    public List<AetherMudMessage> getCriticalAttackMessages() {
        return criticalAttackMessages;
    }

    public void setCriticalAttackMessages(List<AetherMudMessage> criticalAttackMessages) {
        this.criticalAttackMessages = criticalAttackMessages;
    }

    public List<AetherMudMessage> getBattleMessages() {
        return battleMessages;
    }

    public void setBattleMessages(List<AetherMudMessage> battleMessages) {
        this.battleMessages = battleMessages;
    }

    public List<AetherMudMessage> getIdleMessages() {
        return idleMessages;
    }

    public void setIdleMessages(List<AetherMudMessage> idleMessages) {
        this.idleMessages = idleMessages;
    }

    public List<AetherMudMessage> getAttackMessages() {
        return attackMessages;
    }

    public void setAttackMessages(List<AetherMudMessage> attackMessages) {
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
        if( ! (stats instanceof Stats) )
            throw new IllegalStateException("not a pojo");
        this.stats = (Stats) stats;
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

    public List<String> getValidTriggers() {
        return validTriggers;
    }

    public void setValidTriggers(List<String> validTriggers) {
        this.validTriggers = validTriggers;
    }

    public List<SpawnRule> getSpawnRules() {
        return spawnRules;
    }

    public void setSpawnRules(List<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
    }

    public Loot getLoot() {
        return loot;
    }

    public void setLoot(Loot loot) {
        this.loot = loot;
    }

    public List<Area> getRoamAreas() {
        return roamAreas;
    }

    public void setRoamAreas(List<Area> roamAreas) {
        this.roamAreas = roamAreas;
    }
}

