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


import com.google.common.collect.Lists;

import java.util.List;

public enum EquipmentSlotType {

    HAND("hand"),
    HEAD("head"),
    FEET("feet"),
    LEGS("legs"),
    WRISTS("wrists"),
    CHEST("chest"),
    BAG("bag");

    private final String name;

    EquipmentSlotType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EquipmentSlotType getByName(String name) {
        EquipmentSlotType[] values = EquipmentSlotType.values();
        for (EquipmentSlotType e: values) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static List<EquipmentSlotType> getAll() {
        return Lists.newArrayList(EquipmentSlotType.values());
    }
}
