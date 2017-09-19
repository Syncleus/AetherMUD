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
package com.syncleus.aethermud.storage.graphdb.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.items.*;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GraphElement
public abstract class ItemData extends AbstractInterceptingVertexFrame {
    @Property("validTimeOfDays")
    public abstract List<TimeTracker.TimeOfDay> getValidTimeOfDays();

    @Property("validTimeOfDays")
    public abstract void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays);

    @Property("validTimeOfDays")
    public abstract void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays);

    @Property("disposable")
    public abstract boolean isDisposable();

    @Property("maxUses")
    public abstract int getMaxUses();

    @Property("internalItemName")
    public abstract String getInternalItemName();

    public String getItemName() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(this.getProperty("itemName"));
    }

    public void setItemName(String itemName) {
        this.setProperty("itemName", ColorizedTextTemplate.renderToTemplateLanguage(itemName));
    }

    public String getItemDescription() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(this.getProperty("itemDescription"));
    }

    @Property("itemTriggers")
    public abstract List<String> getItemTriggers();

    @Property("itemTriggers")
    public abstract void setItemTriggers(List<String> itemTriggers);

    public String getRestingName() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(this.getProperty("restingName"));
    }

    @Property("itemHalfLifeTicks")
    public abstract int getItemHalfLifeTicks();

    @Property("rarity")
    public abstract Rarity getRarity();

    @Property("rarity")
    public abstract void setRarity(Rarity rarity);

    @Property("valueInGold")
    public abstract int getValueInGold();

    public void setItemDescription(String itemDescription) {
        this.setProperty("itemDescription", ColorizedTextTemplate.renderToTemplateLanguage(itemDescription));
    }

    @Property("internalItemName")
    public abstract void setInternalItemName(String internalItemName);

    public void setRestingName(String restingName) {
        this.setProperty("restingName", ColorizedTextTemplate.renderToTemplateLanguage(restingName));
    }

    @Property("itemHalfLifeTicks")
    public abstract void setItemHalfLifeTicks(int itemHalfLifeTicks);

    @Property("valueInGold")
    public abstract void setValueInGold(int valueInGold);

    @Property("maxUses")
    public abstract void setMaxUses(int maxUses);

    @Property("disposable")
    public abstract void setDisposable(boolean disposable);

    @Property("forage")
    public abstract Set<Forage> getForages();

    @Property("forage")
    public abstract void setForages(Set<Forage> forages);

    @Adjacency(label = "spawnRule", direction = Direction.OUT)
    public abstract <N extends SpawnRuleData> Iterator<? extends N> getSpawnRulesDataIterator(Class<? extends N> type);

    public List<SpawnRuleData> getSpawnRuleDatas() {
        return Collections.unmodifiableList(Lists.newArrayList(this.getSpawnRulesDataIterator(SpawnRuleData.class)));
    }

    @Adjacency(label = "spawnRule", direction = Direction.OUT)
    public abstract void addSpawnRuleData(SpawnRuleData spawnRule);

    @Adjacency(label = "spawnRule", direction = Direction.OUT)
    public abstract void removeSpawnRuleData(SpawnRuleData spawnRule);

    public void setSpawnRulesDatas(List<SpawnRuleData> spawnRules) {
        DataUtils.setAllElements(spawnRules, () -> this.getSpawnRulesDataIterator(SpawnRuleData.class), ruleData -> this.addSpawnRuleData(ruleData), () -> {} );
    }

    public SpawnRuleData createSpawnRuleData() {
        final SpawnRuleData rule = this.getGraph().addFramedVertex(SpawnRuleData.class);
        this.addSpawnRuleData(rule);
        return rule;
    }

    @Adjacency(label = "equipment", direction = Direction.OUT)
    public abstract <N extends EquipmentData> Iterator<? extends N> getEquipmentDataIterator(Class<? extends N> type);

    public EquipmentData getEquipmentData() {
        Iterator<? extends EquipmentData> allEquipment = this.getEquipmentDataIterator(EquipmentData.class);
        if( allEquipment.hasNext() )
            return allEquipment.next();
        else
            return null;
    }

    @Adjacency(label = "equipment", direction = Direction.OUT)
    public abstract EquipmentData addEquipmentData(EquipmentData equipment);

    @Adjacency(label = "equipment", direction = Direction.OUT)
    public abstract void removeEquipmentData(EquipmentData equipment);

    public void setEquipmentData(EquipmentData equipment) {
        DataUtils.setAllElements(Collections.singletonList(equipment), () -> this.getEquipmentDataIterator(EquipmentData.class), equipmentData -> this.addEquipmentData(equipmentData), () -> createEquipmentData() );
    }

    public EquipmentData createEquipmentData() {
        if( this.getEquipmentData() != null )
            throw new IllegalStateException("Already has stats, can't create another");
        final EquipmentData equipment = this.getGraph().addFramedVertex(EquipmentData.class);
        this.addEquipmentData(equipment);
        return equipment;
    }

    @Adjacency(label = "effect", direction = Direction.OUT)
    public abstract EffectData addEffectData(EffectData effects);

    @Adjacency(label = "effect", direction = Direction.OUT)
    public abstract void removeEffectData(EffectData stats);

    @Adjacency(label = "effect", direction = Direction.OUT)
    public abstract <N extends EffectData> Iterator<? extends N> getEffectDatasIterator(Class<? extends N> type);

    public Set<EffectData> getEffectDatas() {
        return Collections.unmodifiableSet(Sets.newHashSet(this.getEffectDatasIterator(EffectData.class)));
    }

    public void setEffectDatas(Set<EffectData> effects) {
        DataUtils.setAllElements(effects, () -> this.getEffectDatasIterator(EffectData.class), effectData -> this.addEffectData(effectData), () -> {} );
    }

    public EffectData createEffectData() {
        final EffectData effect = this.getGraph().addFramedVertex(EffectData.class);
        this.addEffectData(effect);
        return effect;
    }

    @Adjacency(label = "itemApplyStats", direction = Direction.OUT)
    public abstract <N extends StatData> Iterator<? extends N> getItemApplyStatDatasIterator(Class<? extends N> type);

    public StatData getItemApplyStatData() {
        Iterator<? extends StatData> allStats = this.getItemApplyStatDatasIterator(StatData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "itemApplyStats", direction = Direction.OUT)
    public abstract StatData addStatData(StatData stats);

    @Adjacency(label = "itemApplyStats", direction = Direction.OUT)
    public abstract void removeStatData(StatData stats);

    public void setItemApplyStatData(StatData stats) {
        DataUtils.setAllElements(Collections.singletonList(stats), () -> this.getItemApplyStatDatasIterator(StatData.class), statsData -> this.addStatData(statsData), () -> createItemApplyStatData() );
    }

    public StatData createItemApplyStatData() {
        if( this.getItemApplyStatData() != null )
            throw new IllegalStateException("Already has stats, can't create another");
        final StatData stats = this.getGraph().addFramedVertex(StatData.class);
        stats.setAgile(0);
        stats.setAim(0);
        stats.setArmorRating(0);
        stats.setCurrentHealth(0);
        stats.setCurrentMana(0);
        stats.setExperience(0);
        stats.setForaging(0);
        stats.setIntelligence(0);
        stats.setInventorySize(0);
        stats.setMaxEffects(0);
        stats.setMaxHealth(0);
        stats.setMaxMana(0);
        stats.setMeleeSkill(0);
        stats.setNumberOfWeaponRolls(0);
        stats.setStrength(0);
        stats.setWeaponRatingMax(0);
        stats.setWeaponRatingMin(0);
        stats.setWillpower(0);
        this.addStatData(stats);
        return stats;
    }

    @Adjacency(label = "loot", direction = Direction.OUT)
    public abstract <N extends LootData> Iterator<? extends N> getLootDatasIterator(Class<? extends N> type);

    public LootData getLootData() {
        Iterator<? extends LootData> allStats = this.getLootDatasIterator(LootData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "loot", direction = Direction.OUT)
    public abstract LootData addLootData(LootData loot);

    @Adjacency(label = "loot", direction = Direction.OUT)
    public abstract void removeLootData(LootData loot);

    public void setLootData(LootData loot) {
        DataUtils.setAllElements(Collections.singletonList(loot), () -> this.getLootDatasIterator(LootData.class), lootData -> this.addLootData(lootData), () -> createLootData() );
    }

    public LootData createLootData() {
        if( this.getLootData() != null )
            throw new IllegalStateException("Already has loot, can't create another");
        final LootData loot = this.getGraph().addFramedVertex(LootData.class);
        loot.setLootGoldMax(0);
        loot.setLootGoldMin(0);
        this.addLootData(loot);
        return loot;
    }

    public static void copyItem(ItemData dest, Item src) {
        try {
            PropertyUtils.copyProperties(dest, src);

            for(SpawnRuleData data : dest.getSpawnRuleDatas())
                data.remove();
            for(SpawnRule spawnRule : src.getSpawnRules())
                SpawnRuleData.copySpawnRule(dest.createSpawnRuleData(), spawnRule);

            if( src.getItemApplyStats() != null )
                StatData.copyStats((dest.getItemApplyStatData() != null ? dest.getItemApplyStatData() : dest.createItemApplyStatData()), src.getItemApplyStats());
            if(src.getLoot() != null )
                LootData.copyLoot((dest.getLootData() != null ? dest.getLootData() : dest.createLootData()), src.getLoot());
            if( src.getEquipment() != null )
                EquipmentData.copyEquipment((dest.getEquipmentData() != null ? dest.getEquipmentData() :dest.createEquipmentData()), src.getEquipment());
            if( src.getEffects() != null ) {
                for(EffectData data : dest.getEffectDatas())
                    data.remove();
                for (Effect effect : src.getEffects())
                    EffectData.copyEffect(dest.createEffectData(), effect);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Item copyItem(ItemData src) {
        Item retVal = new ItemImpl();
        try {
            PropertyUtils.copyProperties(retVal, src);

            Set<SpawnRule> rules = new HashSet<>();
            for(SpawnRuleData spawnRuleData : src.getSpawnRuleDatas())
                rules.add(SpawnRuleData.copySpawnRule(spawnRuleData));
            retVal.setSpawnRules(Collections.unmodifiableSet(rules));

            EquipmentData equipmentData = src.getEquipmentData();
            if(equipmentData != null)
                retVal.setEquipment(EquipmentData.copyEquipment(equipmentData));

            LootData lootData = src.getLootData();
            if(lootData != null)
                retVal.setLoot(LootData.copyLoot(lootData));

            StatData applyStats = src.getItemApplyStatData();
            if( applyStats != null )
                retVal.setItemApplyStats(StatData.copyStats(applyStats));

            if( src.getEffectDatas() != null ) {
                Set<Effect> effects = new HashSet<>();
                for (EffectData effect : src.getEffectDatas())
                    effects.add(EffectData.copyEffect(effect));
                retVal.setEffects(Collections.unmodifiableSet(effects));
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
