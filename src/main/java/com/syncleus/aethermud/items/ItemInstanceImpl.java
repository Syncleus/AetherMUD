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
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;

import java.util.List;
import java.util.Set;

public class ItemInstanceImpl implements ItemInstance {

    private Item item;
    private String itemId;
    private int numberOfUses;
    private boolean isWithPlayer;
    private boolean hasBeenWithPlayer;

    public ItemInstanceImpl() {
    }

    public ItemInstanceImpl(Item item, String itemId, int numberOfUses, boolean isWithPlayer, boolean hasBeenWithPlayer) {
        this.item = item;
        this.itemId = itemId;
        this.numberOfUses = numberOfUses;
        this.isWithPlayer = isWithPlayer;
        this.hasBeenWithPlayer = hasBeenWithPlayer;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public Set<SpawnRule> getSpawnRules() {
        return item.getSpawnRules();
    }

    @Override
    public void setSpawnRules(Set<SpawnRule> spawnRules) {
        item.setSpawnRules(spawnRules);
    }

    @Override
    public Set<Forage> getForages() {
        return item.getForages();
    }

    @Override
    public void setForages(Set<Forage> forages) {
        item.setForages(forages);
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
    public void setHasBeenWithPlayer(boolean hasBeenWithPlayer) {
        this.hasBeenWithPlayer = hasBeenWithPlayer;
    }

    @Override
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean isHasBeenWithPlayer() {
        return hasBeenWithPlayer;
    }

    @Override
    public Stats getItemApplyStats() {
        return item.getItemApplyStats();
    }

    @Override
    public List<TimeTracker.TimeOfDay> getValidTimeOfDays() {
        return item.getValidTimeOfDays();
    }

    @Override
    public void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        item.setValidTimeOfDays(validTimeOfDays);
    }

    @Override
    public void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays) {
        item.setValidTimeOfDays(validTimeOfDays);
    }

    @Override
    public boolean isDisposable() {
        return item.isDisposable();
    }

    @Override
    public int getMaxUses() {
        return item.getMaxUses();
    }

    @Override
    public String getInternalItemName() {
        return item.getInternalItemName();
    }

    @Override
    public String getItemName() {
        return item.getItemName();
    }

    @Override
    public String getItemDescription() {
        return item.getItemDescription();
    }

    @Override
    public List<String> getItemTriggers() {
        return item.getItemTriggers();
    }

    @Override
    public String getRestingName() {
        return item.getRestingName();
    }

    @Override
    public int getItemHalfLifeTicks() {
        return item.getItemHalfLifeTicks();
    }

    @Override
    public Loot getLoot() {
        return item.getLoot();
    }

    @Override
    public void setEquipment(Equipment equipment) {
        item.setEquipment(equipment);
    }

    @Override
    public Equipment getEquipment() {
        return item.getEquipment();
    }

    @Override
    public Rarity getRarity() {
        return item.getRarity();
    }

    @Override
    public int getValueInGold() {
        return item.getValueInGold();
    }

    @Override
    public void setEffects(Set<Effect> effects) {
        item.setEffects(effects);
    }

    @Override
    public Set<Effect> getEffects() {
        return item.getEffects();
    }

    @Override
    public void setItemName(String itemName) {
        item.setItemName(itemName);
    }

    @Override
    public void setItemDescription(String itemDescription) {
        item.setItemDescription(itemDescription);
    }

    @Override
    public void setInternalItemName(String internalItemName) {
        item.setInternalItemName(internalItemName);
    }

    @Override
    public void setItemTriggers(List<String> itemTriggers) {
        item.setItemTriggers(itemTriggers);
    }

    @Override
    public void setRestingName(String restingName) {
        item.setRestingName(restingName);
    }

    @Override
    public void setLoot(Loot loot) {
        item.setLoot(loot);
    }

    @Override
    public void setItemHalfLifeTicks(int itemHalfLifeTicks) {
        item.setItemHalfLifeTicks(itemHalfLifeTicks);
    }

    @Override
    public void setRarity(Rarity rarity) {
        item.setRarity(rarity);
    }

    @Override
    public void setValueInGold(int valueInGold) {
        item.setValueInGold(valueInGold);
    }

    @Override
    public void setMaxUses(int maxUses) {
        item.setMaxUses(maxUses);
    }

    @Override
    public void setDisposable(boolean disposable) {
        item.setDisposable(disposable);
    }

    @Override
    public void setItemApplyStats(Stats itemApplyStats) {
        item.setItemApplyStats(itemApplyStats);
    }
}
