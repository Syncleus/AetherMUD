package com.comandante.creeper.Items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

public class Loot implements Serializable {

    private final Set<ItemType> items;
    private final long lootGoldMax;
    private final long lootGoldMin;

    @JsonCreator
    public Loot(@JsonProperty("lootGoldMin") long lootGoldMin, @JsonProperty("lootGoldMax") long lootGoldMax, @JsonProperty("items") Set<ItemType> items) {
        this.items = items;
        this.lootGoldMax = lootGoldMax;
        this.lootGoldMin = lootGoldMin;
    }

    public Set<ItemType> getItems() {
        return items;
    }

    public long getLootGoldMax() {
        return lootGoldMax;
    }

    public long getLootGoldMin() {
        return lootGoldMin;
    }

}
