package com.comandante.creeper.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

public class Loot implements Serializable {

    private Set<ItemType> items;
    private long lootGoldMax;
    private long lootGoldMin;

    @JsonCreator
    public Loot(@JsonProperty("lootGoldMin") long lootGoldMin, @JsonProperty("lootGoldMax") long lootGoldMax, @JsonProperty("items") Set<ItemType> items) {
        this.items = items;
        this.lootGoldMax = lootGoldMax;
        this.lootGoldMin = lootGoldMin;
    }

    public Loot() {
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

    public void setItems(Set<ItemType> items) {
        this.items = items;
    }

    public void setLootGoldMax(long lootGoldMax) {
        this.lootGoldMax = lootGoldMax;
    }

    public void setLootGoldMin(long lootGoldMin) {
        this.lootGoldMin = lootGoldMin;
    }
}
