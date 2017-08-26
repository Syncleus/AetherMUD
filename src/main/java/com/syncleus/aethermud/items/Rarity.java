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
package com.syncleus.aethermud.items;


public enum Rarity {

    OFTEN("often", 40),
    BASIC("basic", 15.0),
    UNCOMMON("uncommon", 7),
    RARE("rare", 3),
    LEGENDARY("legendary", 1),
    EXOTIC("exotic", .5);

    private final String rarityTypeName;
    private final double percentToLoot;

    Rarity(String rarityTypeName, double percentToLoot) {
        this.rarityTypeName = rarityTypeName;
        this.percentToLoot = percentToLoot;
    }

    public String getRarityTypeName() {
        return rarityTypeName;
    }

    public double getPercentToLoot() {
        return percentToLoot;
    }
}
