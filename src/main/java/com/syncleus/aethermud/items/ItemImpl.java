/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;

import java.util.List;
import java.util.Set;

public class ItemImpl implements Item {

    private String itemName;
    private String itemDescription;
    private String internalItemName;
    private List<String> itemTriggers;
    private String restingName;
    private Loot loot;
    private int itemHalfLifeTicks;
    private Equipment equipment;
    private Rarity rarity;
    private int valueInGold;
    private Set<Effect> effects;
    private int maxUses;
    private boolean isDisposable;
    private List<TimeTracker.TimeOfDay> validTimeOfDays;
    private Stats itemApplyStats;
    private Set<SpawnRule> spawnRules;
    private Set<Forage> forages;

    public ItemImpl() {
    }

    public ItemImpl(String itemName, String itemDescription, String internalItemName, List<String> itemTriggers, String restingName, Loot loot, int itemHalfLifeTicks, Equipment equipment, Rarity rarity, int valueInGold, Set<Effect> effects, int maxUses, boolean isDisposable, Set<TimeTracker.TimeOfDay> validTimeOfDays, Stats itemApplyStats, Set<SpawnRule> spawnRules, Set<Forage> forages) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.internalItemName = internalItemName;
        this.itemTriggers = (itemTriggers == null ? Lists.newArrayList() : Lists.newArrayList(itemTriggers));
        this.restingName = restingName;
        this.loot = loot;
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        this.equipment = equipment;
        this.rarity = rarity;
        this.valueInGold = valueInGold;
        this.effects = (effects == null ? Sets.newHashSet() : Sets.newHashSet(effects));
        this.maxUses = maxUses;
        this.isDisposable = isDisposable;
        this.validTimeOfDays = (validTimeOfDays == null ? Lists.newArrayList() : Lists.newArrayList(validTimeOfDays));
        this.itemApplyStats = itemApplyStats;
        this.spawnRules = (spawnRules == null ? Sets.newHashSet() : Sets.newHashSet(spawnRules));
        this.forages = (forages == null ? Sets.newHashSet() : Sets.newHashSet(forages));
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
    public String getInternalItemName() {
        return internalItemName;
    }

    @Override
    public String getItemName() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(itemName);
    }

    @Override
    public void setItemName(String itemName) {
        this.itemName = ColorizedTextTemplate.renderToTemplateLanguage(itemName);
    }

    @Override
    public String getItemDescription() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(itemDescription);
    }

    @Override
    public List<String> getItemTriggers() {
        return itemTriggers;
    }

    @Override
    public String getRestingName() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(restingName);

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
    public void setItemDescription(String itemDescription) {
        this.itemDescription = ColorizedTextTemplate.renderToTemplateLanguage(itemDescription);
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
        this.restingName = ColorizedTextTemplate.renderToTemplateLanguage(restingName);
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
    public Set<SpawnRule> getSpawnRules() {
        return spawnRules;
    }

    @Override
    public void setSpawnRules(Set<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
    }

    @Override
    public Set<Forage> getForages() {
        return forages;
    }

    @Override
    public void setForages(Set<Forage> forages) {
        this.forages = forages;
    }
}
