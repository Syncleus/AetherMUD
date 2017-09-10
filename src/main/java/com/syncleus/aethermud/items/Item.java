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


import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.stats.Stats;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Item implements Serializable {

    private String itemName;
    private String itemDescription;
    private String internalItemName;
    private List<String> itemTriggers;
    private String restingName;
    private String itemId;
    private int numberOfUses;
    private boolean isWithPlayer;
    private Loot loot;
    private int itemHalfLifeTicks;
    private Equipment equipment;
    private Rarity rarity;
    private int valueInGold;
    private Set<Effect> effects;
    private boolean hasBeenWithPlayer;
    private int maxUses;
    private boolean isDisposable;
    private List<TimeTracker.TimeOfDay> validTimeOfDays;
    private Stats itemApplyStats;

    public static final String CORPSE_INTENAL_NAME = "corpse";

    public Item() {
    }

    protected Item(String itemName, String itemDescription, String internalItemName, List<String> itemTriggers, String restingName, String itemId, int numberOfUses, boolean isWithPlayer, Loot loot, int itemHalfLifeTicks, Equipment equipment, Rarity rarity, int valueInGold, Set<Effect> effects, boolean hasBeenWithPlayer, int maxUses, boolean isDisposable, Set<TimeTracker.TimeOfDay> validTimeOfDays, Stats itemApplyStats) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.internalItemName = internalItemName;
        this.itemTriggers = itemTriggers;
        this.restingName = restingName;
        this.itemId = itemId;
        this.numberOfUses = numberOfUses;
        this.isWithPlayer = isWithPlayer;
        this.loot = loot;
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        this.equipment = equipment;
        this.rarity = rarity;
        this.valueInGold = valueInGold;
        this.effects = effects;
        this.hasBeenWithPlayer = hasBeenWithPlayer;
        this.maxUses = maxUses;
        this.isDisposable = isDisposable;
        this.validTimeOfDays = ( validTimeOfDays == null ? Lists.newArrayList() : Lists.newArrayList(validTimeOfDays));
        this.itemApplyStats = itemApplyStats;
    }

    public Stats getItemApplyStats() {
        return itemApplyStats;
    }

    public List<TimeTracker.TimeOfDay> getValidTimeOfDays() {
        return validTimeOfDays;
    }

    public void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.validTimeOfDays = Lists.newArrayList(validTimeOfDays);
    }

    public void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.validTimeOfDays = Lists.newArrayList(validTimeOfDays);
    }

    public boolean isDisposable() {
        return isDisposable;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public boolean isWithPlayer() {
        return isWithPlayer;
    }

    public void setWithPlayer(boolean isWithPlayer) {
        if (isWithPlayer) {
            setHasBeenWithPlayer(true);
        }
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


    public String getInternalItemName() {
        return internalItemName;
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

    public void setHasBeenWithPlayer(boolean hasBeenWithPlayer) {
        this.hasBeenWithPlayer = hasBeenWithPlayer;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getValueInGold() {
        return valueInGold;
    }

    public void setEffects(Set<Effect> effects) {
        this.effects = effects;
    }

    public Set<Effect> getEffects() {
        return effects;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setInternalItemName(String internalItemName) {
        this.internalItemName = internalItemName;
    }

    public void setItemTriggers(List<String> itemTriggers) {
        this.itemTriggers = itemTriggers;
    }

    public void setRestingName(String restingName) {
        this.restingName = restingName;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setLoot(Loot loot) {
        this.loot = loot;
    }

    public void setItemHalfLifeTicks(int itemHalfLifeTicks) {
        this.itemHalfLifeTicks = itemHalfLifeTicks;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setValueInGold(int valueInGold) {
        this.valueInGold = valueInGold;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public void setDisposable(boolean disposable) {
        isDisposable = disposable;
    }

    public void setItemApplyStats(Stats itemApplyStats) {
        this.itemApplyStats = itemApplyStats;
    }

    public boolean isHasBeenWithPlayer() {
        return hasBeenWithPlayer;
    }

    public static Item createCorpseItem(String name, Loot loot) {

        Item item = new ItemBuilder()
                .internalItemName(Item.CORPSE_INTENAL_NAME)
                .itemName(name + " corpse")
                .itemDescription("a bloody corpse")
                .itemTriggers(Lists.newArrayList("corpse", "c", name, name + " corpse"))
                .itemId(UUID.randomUUID().toString())
                .itemHalfLifeTicks(120)
                .rarity(Rarity.BASIC)
                .valueInGold(5)
                .isDisposable(false)
                .restingName("a corpse lies on the ground.")
                .loot(loot)
                .create();

        return item;

    }
}
