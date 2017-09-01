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

import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.storage.graphdb.StatsData;
import com.syncleus.aethermud.world.model.Area;

import java.util.Set;

public interface Npc {
    Set<AetherMudMessage> getCriticalAttackMessages();

    void setCriticalAttackMessages(Set<AetherMudMessage> criticalAttackMessages);

    Set<AetherMudMessage> getBattleMessages();

    void setBattleMessages(Set<AetherMudMessage> battleMessages);

    Set<AetherMudMessage> getIdleMessages();

    void setIdleMessages(Set<AetherMudMessage> idleMessages);

    Set<AetherMudMessage> getAttackMessages();

    void setAttackMessages(Set<AetherMudMessage> attackMessages);

    String getName();

    void setName(String name);

    Temperament getTemperament();

    void setTemperament(Temperament temperament);

    Set<Area> getRoamAreas();

    void setRoamAreas(Set<Area> roamAreas);

    Set<String> getValidTriggers();

    void setValidTriggers(Set<String> validTriggers);

    Set<SpawnRule> getSpawnRules();

    void setSpawnRules(Set<SpawnRule> spawnRules);

    Loot getLoot();

    void setLoot(Loot loot);

    String getColorName();

    void setColorName(String colorName);

    String getDieMessage();

    void setDieMessage(String dieMessage);

    StatsData getStats();

    void setStats(StatsData stats);
}

