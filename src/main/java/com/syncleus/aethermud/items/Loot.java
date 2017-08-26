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
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.Set;

public class Loot implements Serializable {

    private Set<String> internalItemNames;
    private long lootGoldMax;
    private long lootGoldMin;

    @JsonCreator
    public Loot(@JsonProperty("lootGoldMin") long lootGoldMin, @JsonProperty("lootGoldMax") long lootGoldMax, @JsonProperty("items") Set<String> internalItemNames) {
        this.internalItemNames = internalItemNames;
        this.lootGoldMax = lootGoldMax;
        this.lootGoldMin = lootGoldMin;
    }

    public Loot() {
    }

    public Set<String> getInternalItemNames() {
        if (internalItemNames == null) {
            internalItemNames = Sets.newHashSet();
        }
        return internalItemNames;
    }

    public long getLootGoldMax() {
        return lootGoldMax;
    }

    public long getLootGoldMin() {
        return lootGoldMin;
    }

    public void setItems(Set<String> items) {
        this.internalItemNames = items;
    }

    public void setLootGoldMax(long lootGoldMax) {
        this.lootGoldMax = lootGoldMax;
    }

    public void setLootGoldMin(long lootGoldMin) {
        this.lootGoldMin = lootGoldMin;
    }
}
