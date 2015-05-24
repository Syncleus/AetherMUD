package com.comandante.creeper.Items;


import com.comandante.creeper.player.Equipment;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {

    private final String itemName;
    private final String itemDescription;
    private final List<String> itemTriggers;
    private final String restingName;
    private final String itemId;
    private final Integer itemTypeId;
    private int numberOfUses;
    private boolean isWithPlayer;
    private final Loot loot;
    private final int itemHalfLifeTicks;
    private Equipment equipment;
    private final Rarity rarity;

    public static final int CORPSE_ID_RESERVED = 100;
    public static final int EQUIPMENT_ID_RESERVED = 101;

    public Item(String itemName, String itemDescription, List<String> itemTriggers, String restingName, String itemId, Integer itemTypeId, int numberOfUses, boolean isWithPlayer, int itemHalfLifeTicks, Rarity rarity) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemTriggers = itemTriggers;
        this.restingName = restingName;
        this.itemId = itemId;
        this.itemTypeId = itemTypeId;
        this.numberOfUses = numberOfUses;
        this.loot = null;
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        this.isWithPlayer = isWithPlayer;
        this.rarity = rarity;
    }

    public Item(String itemName, String itemDescription, List<String> itemTriggers, String restingName, String itemId, Integer itemTypeId, int numberOfUses, boolean isWithPlayer,int itemHalfLifeTicks, Loot loot, Rarity rarity) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemTriggers = itemTriggers;
        this.restingName = restingName;
        this.itemId = itemId;
        this.itemTypeId = itemTypeId;
        this.numberOfUses = numberOfUses;
        this.isWithPlayer = isWithPlayer;
        this.loot = loot;
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        this.rarity = rarity;

    }

    public Item(Item origItem) {
        this.itemName = origItem.getItemName();
        this.itemDescription = origItem.itemDescription;
        this.itemTriggers = origItem.itemTriggers;
        this.restingName = origItem.restingName;
        this.itemId = origItem.itemId;
        this.itemTypeId = origItem.itemTypeId;
        this.numberOfUses = new Integer(origItem.numberOfUses);
        this.loot = origItem.loot;
        this.itemHalfLifeTicks = origItem.itemHalfLifeTicks;
        this.isWithPlayer = new Boolean(origItem.isWithPlayer);
        if (origItem.equipment != null) {
            this.equipment = new Equipment(origItem.equipment);
        }
        this.rarity = origItem.rarity;
    }

    public boolean isWithPlayer() {
        return isWithPlayer;
    }

    public void setWithPlayer(boolean isWithPlayer) {
        this.isWithPlayer = isWithPlayer;
    }

    public int getNumberOfUses() {
        return numberOfUses;
    }

    public void setNumberOfUses(int numberOfUses) {
        this.numberOfUses = numberOfUses;
    }

    public String getItemId() {
        return itemId;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public List<String> getItemTriggers() {
        return itemTriggers;
    }

    public String getRestingName() {
        return restingName;
    }

    public int getItemHalfLifeTicks() {
        return itemHalfLifeTicks;
    }

    public Loot getLoot() {
        return loot;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemTriggers=" + itemTriggers +
                ", restingName='" + restingName + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemTypeId=" + itemTypeId +
                ", numberOfUses=" + numberOfUses +
                ", isWithPlayer=" + isWithPlayer +
                ", loot=" + loot +
                ", itemHalfLifeTicks=" + itemHalfLifeTicks +
                ", equipment=" + equipment +
                ", rarity=" + rarity +
                '}';
    }
}
