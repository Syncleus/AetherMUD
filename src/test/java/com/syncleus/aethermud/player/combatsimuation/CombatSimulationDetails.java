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
package com.syncleus.aethermud.player.combatsimuation;

import com.syncleus.aethermud.items.ItemPojo;
import com.syncleus.aethermud.npc.NpcSpawn;

import java.util.Set;

public class CombatSimulationDetails {

    private final int level;
    private final int totalIterations;
    private final Set<ItemPojo> equipmentSet;
    private final NpcSpawn npcSpawn;

    public CombatSimulationDetails(int level, Set<ItemPojo> equipmentSet, NpcSpawn npcSpawn) {
        this.level = level;
        this.totalIterations = 1000;
        this.equipmentSet = equipmentSet;
        this.npcSpawn = npcSpawn;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalIterations() {
        return totalIterations;
    }

    public NpcSpawn getNpcSpawn() {
        return npcSpawn;
    }

    public Set<ItemPojo> getEquipmentSet() {
        return equipmentSet;
    }
}
