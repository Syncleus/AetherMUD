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

public class ItemPojo implements Serializable, Item {

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

    protected ItemPojo(String itemName, String itemDescription, String internalItemName, List<String> itemTriggers, String restingName, String itemId, int numberOfUses, boolean isWithPlayer, Loot loot, int itemHalfLifeTicks, Equipment equipment, Rarity rarity, int valueInGold, Set<Effect> effects, boolean hasBeenWithPlayer, int maxUses, boolean isDisposable, Set<TimeTracker.TimeOfDay> validTimeOfDays, Stats itemApplyStats) {
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
        this.validTimeOfDays = Lists.newArrayList(validTimeOfDays);
        this.itemApplyStats = itemApplyStats;
    }

    @Override
    public Stats getItemApplyStats() {
        return itemApplyStats;
    }

    @Override
    public List<TimeTracker.TimeOfDay> getValidTimeOfDays() {
        return validTimeOfDays;
    }

    @Override
    public void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.validTimeOfDays = Lists.newArrayList(validTimeOfDays);
    }

    @Override
    public void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.validTimeOfDays = Lists.newArrayList(validTimeOfDays);
    }

    @Override
    public boolean isDisposable() {
        return isDisposable;
    }

    @Override
    public int getMaxUses() {
        return maxUses;
    }

    @Override
    public boolean isWithPlayer() {
        return isWithPlayer;
    }

    @Override
    public void setWithPlayer(boolean isWithPlayer) {
        if (isWithPlayer) {
            setHasBeenWithPlayer(true);
        }
        this.isWithPlayer = isWithPlayer;
    }

    @Override
    public int getNumberOfUses() {
        return numberOfUses;
    }

    @Override
    public void setNumberOfUses(int numberOfUses) {
        this.numberOfUses = numberOfUses;
    }

    @Override
    public String getItemId() {
        return itemId;
    }


    @Override
    public String getInternalItemName() {
        return internalItemName;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public String getItemDescription() {
        return itemDescription;
    }

    @Override
    public List<String> getItemTriggers() {
        return itemTriggers;
    }

    @Override
    public String getRestingName() {
        return restingName;
    }

    @Override
    public int getItemHalfLifeTicks() {
        return itemHalfLifeTicks;
    }

    @Override
    public Loot getLoot() {
        return loot;
    }

    @Override
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    public void setHasBeenWithPlayer(boolean hasBeenWithPlayer) {
        this.hasBeenWithPlayer = hasBeenWithPlayer;
    }

    @Override
    public Equipment getEquipment() {
        return equipment;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public int getValueInGold() {
        return valueInGold;
    }

    @Override
    public void setEffects(Set<Effect> effects) {
        this.effects = effects;
    }

    @Override
    public Set<Effect> getEffects() {
        return effects;
    }

    @Override
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Override
    public void setInternalItemName(String internalItemName) {
        this.internalItemName = internalItemName;
    }

    @Override
    public void setItemTriggers(List<String> itemTriggers) {
        this.itemTriggers = itemTriggers;
    }

    @Override
    public void setRestingName(String restingName) {
        this.restingName = restingName;
    }

    @Override
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public void setLoot(Loot loot) {
        this.loot = loot;
    }

    @Override
    public void setItemHalfLifeTicks(int itemHalfLifeTicks) {
        this.itemHalfLifeTicks = itemHalfLifeTicks;
    }

    @Override
    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public void setValueInGold(int valueInGold) {
        this.valueInGold = valueInGold;
    }

    @Override
    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public void setDisposable(boolean disposable) {
        isDisposable = disposable;
    }

    @Override
    public void setItemApplyStats(Stats itemApplyStats) {
        this.itemApplyStats = itemApplyStats;
    }

    @Override
    public boolean isHasBeenWithPlayer() {
        return hasBeenWithPlayer;
    }

    public static ItemPojo createCorpseItem(String name, Loot loot) {

        ItemPojo item = new ItemBuilder()
                .internalItemName(ItemPojo.CORPSE_INTENAL_NAME)
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
