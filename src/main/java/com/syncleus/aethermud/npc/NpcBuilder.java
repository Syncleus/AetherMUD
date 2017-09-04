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
package com.syncleus.aethermud.npc;

import com.google.common.collect.Sets;
import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.stats.StatsPojo;
import com.syncleus.aethermud.storage.graphdb.StatsData;
import com.syncleus.aethermud.storage.graphdb.NpcData;
import com.syncleus.aethermud.world.model.Area;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class NpcBuilder {
    private GameManager gameManager;
    private String name;
    private String colorName;
    private long lastPhraseTimestamp;
    private Stats stats;
    private String dieMessage;
    private Set<Area> roamAreas;
    private Set<String> validTriggers;
    private Loot loot;
    private Set<SpawnRule> spawnRules;
    private Temperament temperament;
    // The messages used when dealing damage
    private Set<AetherMudMessage> attackMessages;
    // The messages used when landing critical attacks
    private Set<AetherMudMessage> criticalAttackMessages;
    // Things the NPC randomly says during battle
    private Set<AetherMudMessage> battleMessages;
    // Things that npcs say randomly when idle
    private Set<AetherMudMessage> idleMessages;

    public NpcBuilder() {
    }

    public NpcBuilder(NpcSpawn npcSpawn) {
        this.name = npcSpawn.getName();
        this.colorName = npcSpawn.getColorName();
        this.lastPhraseTimestamp = npcSpawn.getLastPhraseTimestamp();
        this.stats = new StatsPojo(npcSpawn.getStats());
        this.dieMessage = npcSpawn.getDieMessage();
        this.roamAreas = npcSpawn.getRoamAreas();
        this.validTriggers = npcSpawn.getValidTriggers();
        this.loot = npcSpawn.getLoot();
        this.spawnRules = npcSpawn.getSpawnRules();
        this.gameManager = npcSpawn.getGameManager();
        this.temperament = npcSpawn.getTemperament();
        this.attackMessages = npcSpawn.getAttackMessages();
        this.criticalAttackMessages = npcSpawn.getCriticalAttackMessages();
        this.battleMessages = npcSpawn.getBattleMessages();
        this.idleMessages = npcSpawn.getIdleMessages();
    }

    public NpcBuilder(NpcData npcData) {
        this.name = npcData.getName();
        this.colorName = npcData.getColorName();
        this.stats = new StatsPojo(npcData.getStats());
        this.dieMessage = npcData.getDieMessage();
        this.roamAreas = new HashSet<>(npcData.getRoamAreas());
        this.validTriggers = Sets.newHashSet(npcData.getValidTriggers());
        this.loot = npcData.getLoot();
        this.spawnRules = Sets.newHashSet(npcData.getSpawnRules());
        this.temperament = npcData.getTemperament();
        this.attackMessages = Sets.newHashSet(npcData.getAttackMessages());
        this.criticalAttackMessages = Sets.newHashSet(npcData.getCriticalAttackMessages());
        this.battleMessages = Sets.newHashSet(npcData.getBattleMessages());
        this.idleMessages = Sets.newHashSet(npcData.getIdleMessages());
    }

    public NpcBuilder setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        return this;
    }

    public NpcBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NpcBuilder setColorName(String colorName) {
        this.colorName = colorName;
        return this;
    }

    public NpcBuilder setLastPhraseTimestamp(long lastPhraseTimestamp) {
        this.lastPhraseTimestamp = lastPhraseTimestamp;
        return this;
    }

    public NpcBuilder setStats(StatsData stats) {
        this.stats = stats;
        return this;
    }

    public NpcBuilder setDieMessage(String dieMessage) {
        this.dieMessage = dieMessage;
        return this;
    }

    public NpcBuilder setTemperament(Temperament temperament) {
        this.temperament = temperament;
        return this;
    }

    public NpcBuilder setRoamAreas(Set<Area> roamAreas) {
        this.roamAreas = roamAreas;
        return this;
    }

    public NpcBuilder setValidTriggers(Set<String> validTriggers) {
        this.validTriggers = validTriggers;
        return this;
    }

    public NpcBuilder setLoot(Loot loot) {
        this.loot = loot;
        return this;
    }

    public NpcBuilder setSpawnRules(Set<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
        return this;
    }

    public NpcBuilder setAttackMessages(Set<AetherMudMessage> attackMessages) {
        this.attackMessages = attackMessages;
        return this;
    }

    public NpcBuilder setCriticalAttackMessages(Set<AetherMudMessage> criticalAttackMessages) {
        this.criticalAttackMessages = criticalAttackMessages;
        return this;
    }

    public NpcBuilder setBattleMessages(Set<AetherMudMessage> battleMessages) {
        this.battleMessages = battleMessages;
        return this;
    }

    public NpcBuilder setIdleMessages(Set<AetherMudMessage> idleMessages) {
        this.idleMessages = idleMessages;
        return this;
    }

    public NpcSpawn createNpc() {
        checkNotNull(gameManager);
        if (loot != null ) {
            if (loot.getLootGoldMin() > loot.getLootGoldMax()) {
                throw new RuntimeException("Invalid loot configuration.");
            }
        }
        return new NpcSpawn(gameManager, name, colorName, lastPhraseTimestamp, stats, dieMessage, temperament, roamAreas, validTriggers, loot, spawnRules, attackMessages, criticalAttackMessages, battleMessages, idleMessages);
    }
}
