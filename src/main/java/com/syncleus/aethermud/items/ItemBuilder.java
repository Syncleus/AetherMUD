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


import com.google.common.collect.Sets;
import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;

import java.util.HashSet;
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
    private Set<SpawnRule> spawnRules;
    private Set<Forage> forages;

    public ItemBuilder from(Item item) {
        this.internalItemName = item.getInternalItemName();
        this.itemName = item.getItemName();
        this.itemDescription = item.getItemDescription();
        this.itemTriggers = item.getItemTriggers();
        this.restingName = item.getRestingName();
        this.itemId = UUID.randomUUID().toString();
        // zero uses, its new.
        this.numberOfUses = 0;
        this.isWithPlayer = false;
        this.itemHalfLifeTicks = item.getItemHalfLifeTicks();
        this.rarity = item.getRarity();
        this.valueInGold = item.getValueInGold();
        this.maxUses = item.getMaxUses();
        this.loot = null;
        this.isDisposable = item.isDisposable();
        this.equipment = item.getEquipment();
        this.validTimeOfDays = new HashSet<>(item.getValidTimeOfDays());
        Set<Effect> effects = item.getEffects();
        this.effects = (effects != null ? Sets.newHashSet(item.getEffects()) : null );
        this.itemApplyStats = item.getItemApplyStats();
        this.spawnRules = (spawnRules != null ? Sets.newHashSet(item.getSpawnRules()) : null);
        this.forages = (forages != null ? Sets.newHashSet(item.getForages()) : null);
        return this;
    }

    public ItemBuilder from(ItemInstance origItem) {
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
        this.validTimeOfDays = Sets.newHashSet(origItem.getValidTimeOfDays());
        this.itemApplyStats = origItem.getItemApplyStats();
        this.spawnRules = (spawnRules != null ? Sets.newHashSet(origItem.getSpawnRules()) : null);
        this.forages = (forages != null ? Sets.newHashSet(origItem.getForages()) : null);
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

    public ItemInstance create() {
        Item item = new ItemImpl(itemName, itemDescription, internalItemName, itemTriggers, restingName, loot, itemHalfLifeTicks, equipment, rarity, valueInGold, effects, maxUses, isDisposable, validTimeOfDays, itemApplyStats, spawnRules, forages);
        return new ItemInstanceImpl(item, itemId, numberOfUses, isWithPlayer, hasBeenWithPlayer);
    }


}
