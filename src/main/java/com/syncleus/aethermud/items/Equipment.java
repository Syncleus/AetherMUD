/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.syncleus.aethermud.items;


import com.syncleus.aethermud.stats.Stats;

public class Equipment {

    private final EquipmentSlotType equipmentSlotType;
    private final Stats statsIncreaseWhileEquipped;

    public Equipment(EquipmentSlotType equipmentSlotType, Stats statsIncreaseWhileEquipped) {
        this.equipmentSlotType = equipmentSlotType;
        this.statsIncreaseWhileEquipped = statsIncreaseWhileEquipped;
    }

    public Equipment(Equipment equipment) {
        this.equipmentSlotType = equipment.equipmentSlotType;
        this.statsIncreaseWhileEquipped = equipment.statsIncreaseWhileEquipped;
    }

    public EquipmentSlotType getEquipmentSlotType() {
        return equipmentSlotType;
    }

    public Stats getStats() {
        return statsIncreaseWhileEquipped;
    }
}
