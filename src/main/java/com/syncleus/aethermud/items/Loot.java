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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Loot {

    private List<String> internalItemNames;
    private int lootGoldMax;
    private int lootGoldMin;

    @JsonCreator
    public Loot(@JsonProperty("lootGoldMin") int lootGoldMin, @JsonProperty("lootGoldMax") int lootGoldMax, @JsonProperty("items") List<String> internalItemNames) {
        this.internalItemNames = internalItemNames;
        this.lootGoldMax = lootGoldMax;
        this.lootGoldMin = lootGoldMin;
    }

    public Loot() {
    }

    public List<String> getInternalItemNames() {
        if (internalItemNames == null) {
            internalItemNames = Lists.newArrayList();
        }
        return internalItemNames;
    }

    public int getLootGoldMax() {
        return lootGoldMax;
    }

    public int getLootGoldMin() {
        return lootGoldMin;
    }

    public void setLootGoldMax(int lootGoldMax) {
        this.lootGoldMax = lootGoldMax;
    }

    public void setLootGoldMin(int lootGoldMin) {
        this.lootGoldMin = lootGoldMin;
    }

    public void setInternalItemNames(List<String> internalItemNames) {
        this.internalItemNames = internalItemNames;
    }
}
