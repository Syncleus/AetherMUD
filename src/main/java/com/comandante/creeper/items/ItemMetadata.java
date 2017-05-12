package com.comandante.creeper.items;


import com.comandante.creeper.npc.Npc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Set;

public class ItemMetadata {

    private String basicItemName;

    private String itemName;
    private String itemDescription;
    private String restingName;
    private int numberOfUses;
    private int valueInGold;
    private int itemHalfLifeTicks;
    private Rarity rarity;
    private Equipment equipment;
    private Set<Effect> effects;
    private List<String> itemTriggers;
    private boolean hasBeenWithPlayer;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getRestingName() {
        return restingName;
    }

    public void setRestingName(String restingName) {
        this.restingName = restingName;
    }

    public int getNumberOfUses() {
        return numberOfUses;
    }

    public void setNumberOfUses(int numberOfUses) {
        this.numberOfUses = numberOfUses;
    }

    public int getValueInGold() {
        return valueInGold;
    }

    public void setValueInGold(int valueInGold) {
        this.valueInGold = valueInGold;
    }

    public int getItemHalfLifeTicks() {
        return itemHalfLifeTicks;
    }

    public void setItemHalfLifeTicks(int itemHalfLifeTicks) {
        this.itemHalfLifeTicks = itemHalfLifeTicks;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Set<Effect> getEffects() {
        return effects;
    }

    public void setEffects(Set<Effect> effects) {
        this.effects = effects;
    }

    public String getBasicItemName() {
        return basicItemName;
    }

    public List<String> getItemTriggers() {
        return itemTriggers;
    }

    public void setItemTriggers(List<String> itemTriggers) {
        this.itemTriggers = itemTriggers;
    }

    public void setBasicItemName(String basicItemName) {
        this.basicItemName = basicItemName;
    }
}
