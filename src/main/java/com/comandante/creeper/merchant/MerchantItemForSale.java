package com.comandante.creeper.merchant;

import com.comandante.creeper.items.ItemType;


public class MerchantItemForSale {
    private final ItemType itemType;
    private final int cost;

    public MerchantItemForSale(ItemType itemType, int cost) {
        this.itemType = itemType;
        this.cost = cost;
    }

    public ItemType getItem() {
        return itemType;
    }

    public int getCost() {
        return cost;
    }
}
