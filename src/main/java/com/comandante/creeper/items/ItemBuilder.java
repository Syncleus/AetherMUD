/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.items;


import com.comandante.creeper.core_game.service.TimeTracker;
import com.comandante.creeper.stats.Stats;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ItemBuilder {

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
    private Set<TimeTracker.TimeOfDay> validTimeOfDays;
    private Stats itemApplyStats;

    public ItemBuilder from(ItemMetadata itemMetadata) {
        this.internalItemName = itemMetadata.getInternalItemName();
        this.itemName = itemMetadata.getItemName();
        this.itemDescription = itemMetadata.getItemDescription();
        this.itemTriggers = itemMetadata.getItemTriggers();
        this.restingName = itemMetadata.getRestingName();
        this.itemId = UUID.randomUUID().toString();
        // zero uses, its new.
        this.numberOfUses = 0;
        this.isWithPlayer = false;
        this.itemHalfLifeTicks = itemMetadata.getItemHalfLifeTicks();
        this.rarity = itemMetadata.getRarity();
        this.valueInGold = itemMetadata.getValueInGold();
        this.maxUses = itemMetadata.getMaxUses();
        this.loot = null;
        this.isDisposable = itemMetadata.isDisposable();
        this.equipment = itemMetadata.getEquipment();
        this.validTimeOfDays = itemMetadata.getValidTimeOfDays();
        this.effects = itemMetadata.getEffects();
        this.itemApplyStats = itemMetadata.getItemApplyStats();
        return this;
    }

    public ItemBuilder from(Item origItem) {
        this.internalItemName = origItem.getInternalItemName();
        this.itemName = origItem.getItemName();
        this.itemDescription = origItem.getItemDescription();
        this.itemTriggers = origItem.getItemTriggers();
        this.restingName = origItem.getRestingName();
        this.itemId = origItem.getItemId();
        this.numberOfUses = new Integer(origItem.getNumberOfUses());
        this.loot = origItem.getLoot();
        this.itemHalfLifeTicks = origItem.getItemHalfLifeTicks();
        this.isWithPlayer = new Boolean(origItem.isWithPlayer());
        if (origItem.getEquipment() != null) {
            this.equipment = new Equipment(origItem.getEquipment());
        }
        this.rarity = origItem.getRarity();
        this.valueInGold = origItem.getValueInGold();
        this.effects = origItem.getEffects();
        this.hasBeenWithPlayer = new Boolean(origItem.isHasBeenWithPlayer());
        this.maxUses = origItem.getMaxUses();
        this.isDisposable = origItem.isDisposable();
        this.validTimeOfDays = origItem.getValidTimeOfDays();
        this.itemApplyStats = origItem.getItemApplyStats();
        return this;
    }

    public ItemBuilder itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public ItemBuilder itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public ItemBuilder internalItemName(String internalItemName) {
        this.internalItemName = internalItemName;
        return this;
    }

    public ItemBuilder itemTriggers(List<String> itemTriggers) {
        this.itemTriggers = itemTriggers;
        return this;
    }

    public ItemBuilder restingName(String restingName) {
        this.restingName = restingName;
        return this;
    }

    public ItemBuilder itemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public ItemBuilder numberOfUses(int numberOfUses) {
        this.numberOfUses = numberOfUses;
        return this;
    }

    public ItemBuilder isWithPlayer(boolean isWithPlayer) {
        this.isWithPlayer = isWithPlayer;
        return this;
    }

    public ItemBuilder loot(Loot loot) {
        this.loot = loot;
        return this;
    }

    public ItemBuilder itemHalfLifeTicks(int itemHalfLifeTicks) {
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        return this;
    }

    public ItemBuilder equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public ItemBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemBuilder valueInGold(int valueInGold) {
        this.valueInGold = valueInGold;
        return this;
    }

    public ItemBuilder effects(Set<Effect> effects) {
        this.effects = effects;
        return this;
    }

    public ItemBuilder hasBeenWithPlayer(boolean hasBeenWithPlayer) {
        this.hasBeenWithPlayer = hasBeenWithPlayer;
        return this;
    }

    public ItemBuilder maxUses(int maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    public ItemBuilder isDisposable(boolean isDisposable) {
        this.isDisposable = isDisposable;
        return this;
    }

    public ItemBuilder validTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.validTimeOfDays = validTimeOfDays;
        return this;
    }

    public ItemBuilder itemApplyStats(Stats itemApplyStats) {
        this.itemApplyStats = itemApplyStats;
        return this;
    }

    public Item create() {
            return new Item(itemName, itemDescription, internalItemName, itemTriggers, restingName, itemId, numberOfUses, isWithPlayer, loot, itemHalfLifeTicks, equipment, rarity, valueInGold, effects, hasBeenWithPlayer, maxUses, isDisposable, validTimeOfDays, itemApplyStats);
    }


}
