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
package com.syncleus.aethermud.items;


import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class ItemMetadata {

    // Used for persisting to disk (file-name)
    // Spaces become underscores.
    // Needs to be unique across all itemmetadata's.
    // is essentially serving as the item "type".
    private String internalItemName;
    // This is the unique identifier to represent this itemmetadata (which drives itemtype)
    private String itemName;
    private String itemDescription;
    private String restingName;
    private int valueInGold;
    private int itemHalfLifeTicks;
    private Rarity rarity;
    private Equipment equipment;
    private Set<Effect> effects;
    private List<String> itemTriggers;
    private Set<TimeTracker.TimeOfDay> validTimeOfDays;
    private boolean isDisposable;
    private int maxUses;
    private Set<SpawnRule> spawnRules;
    private Stats itemApplyStats;
    private Set<Forage> forages;

    public Set<Forage> getForages() {
        if (forages == null) {
            return Sets.newHashSet();
        }
        return forages;
    }

    public void setForages(Set<Forage> forages) {
        this.forages = forages;
    }

    public Stats getItemApplyStats() {
        return itemApplyStats;
    }

    public void setItemApplyStats(Stats itemApplyStats) {
        this.itemApplyStats = itemApplyStats;
    }

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

    public String getInternalItemName() {
        return internalItemName;
    }

    public List<String> getItemTriggers() {
        return itemTriggers;
    }

    public void setItemTriggers(List<String> itemTriggers) {
        this.itemTriggers = itemTriggers;
    }

    public void setInternalItemName(String internalItemName) {
        this.internalItemName = internalItemName;
    }

    public Set<TimeTracker.TimeOfDay> getValidTimeOfDays() {
        return validTimeOfDays;
    }

    public void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.validTimeOfDays = validTimeOfDays;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public boolean isDisposable() {
        return isDisposable;
    }

    public void setDisposable(boolean disposable) {
        isDisposable = disposable;
    }

    public Set<SpawnRule> getSpawnRules() {
        if (spawnRules == null) {
            return Sets.newHashSet();
        }
        return spawnRules;
    }

    public void setSpawnRules(Set<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }
}
