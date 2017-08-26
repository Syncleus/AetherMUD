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
package com.syncleus.aethermud.player.combat_simuation;

import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.npc.Npc;

import java.util.Set;

public class CombatSimulationDetails {

    private final int level;
    private final int totalIterations;
    private final Set<Item> equipmentSet;
    private final Npc npc;

    public CombatSimulationDetails(int level, Set<Item> equipmentSet, Npc npc) {
        this.level = level;
        this.totalIterations = 1000;
        this.equipmentSet = equipmentSet;
        this.npc = npc;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalIterations() {
        return totalIterations;
    }

    public Npc getNpc() {
        return npc;
    }

    public Set<Item> getEquipmentSet() {
        return equipmentSet;
    }
}
