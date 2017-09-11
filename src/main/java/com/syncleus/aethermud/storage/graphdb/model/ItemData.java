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

import com.google.common.collect.Sets;
import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.items.*;
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
    @Property("ValidTimeOfDays")
    public abstract List<TimeTracker.TimeOfDay> getValidTimeOfDays();

    @Property("ValidTimeOfDays")
    public abstract void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays);

    @Property("ValidTimeOfDays")
    public abstract void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays);

    @Property("Disposable")
    public abstract boolean isDisposable();

    @Property("MaxUses")
    public abstract int getMaxUses();

    @Property("WithPlayer")
    public abstract boolean isWithPlayer();

    @Property("WithPlayer")
    public abstract void setWithPlayer(boolean isWithPlayer);

    @Property("NumberOfUses")
    public abstract int getNumberOfUses();

    @Property("NumberOfUses")
    public abstract void setNumberOfUses(int numberOfUses);

    @Property("ItemId")
    public abstract String getItemId();

    @Property("InternalItemName")
    public abstract String getInternalItemName();

    @Property("ItemName")
    public abstract String getItemName();

    @Property("ItemDescription")
    public abstract String getItemDescription();

    @Property("ItemTriggers")
    public abstract List<String> getItemTriggers();

    @Property("ItemTriggers")
    public abstract void setItemTriggers(List<String> itemTriggers);

    @Property("RestingName")
    public abstract String getRestingName();

    @Property("ItemHalfLifeTicks")
    public abstract int getItemHalfLifeTicks();

    @Property("Equipment")
    public abstract Equipment getEquipment();

    @Property("Equipment")
    public abstract void setEquipment(Equipment equipment);

    @Property("HasBeenWithPlayer")
    public abstract void setHasBeenWithPlayer(boolean hasBeenWithPlayer);

    @Property("Rarity")
    public abstract Rarity getRarity();

    @Property("Rarity")
    public abstract void setRarity(Rarity rarity);

    @Property("ValueInGold")
    public abstract int getValueInGold();

    @Property("ItemName")
    public abstract void setItemName(String itemName);

    @Property("ItemDescription")
    public abstract void setItemDescription(String itemDescription);

    @Property("InternalItemName")
    public abstract void setInternalItemName(String internalItemName);

    @Property("RestingName")
    public abstract void setRestingName(String restingName);

    @Property("ItemId")
    public abstract void setItemId(String itemId);

    @Property("ItemHalfLifeTicks")
    public abstract void setItemHalfLifeTicks(int itemHalfLifeTicks);

    @Property("ValueInGold")
    public abstract void setValueInGold(int valueInGold);

    @Property("MaxUses")
    public abstract void setMaxUses(int maxUses);

    @Property("Disposable")
    public abstract void setDisposable(boolean disposable);

    @Property("HasBeenWithPlayer")
    public abstract boolean isHasBeenWithPlayer();

    @Adjacency(label = "Effect", direction = Direction.OUT)
    public abstract EffectData addEffectData(EffectData effects);

    @Adjacency(label = "Effect", direction = Direction.OUT)
    public abstract void removeEffectData(EffectData stats);

    @Adjacency(label = "Effect", direction = Direction.OUT)
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

    @Adjacency(label = "ItemApplyStats", direction = Direction.OUT)
    public abstract <N extends StatData> Iterator<? extends N> getItemApplyStatDatasIterator(Class<? extends N> type);

    public StatData getItemApplyStatData() {
        Iterator<? extends StatData> allStats = this.getItemApplyStatDatasIterator(StatData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "ItemApplyStats", direction = Direction.OUT)
    public abstract StatData addStatData(StatData stats);

    @Adjacency(label = "ItemApplyStats", direction = Direction.OUT)
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

    @Adjacency(label = "Loot", direction = Direction.OUT)
    public abstract <N extends LootData> Iterator<? extends N> getLootDatasIterator(Class<? extends N> type);

    public LootData getLootData() {
        Iterator<? extends LootData> allStats = this.getLootDatasIterator(LootData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "Loot", direction = Direction.OUT)
    public abstract LootData addLootData(LootData loot);

    @Adjacency(label = "Loot", direction = Direction.OUT)
    public abstract void removeLootData(LootData loot);

    public void setLootData(LootData loot) {
        DataUtils.setAllElements(Collections.singletonList(loot), () -> this.getLootDatasIterator(LootData.class), lootData -> this.addLootData(lootData), () -> createLoottData() );
    }

    public LootData createLoottData() {
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
            StatData.copyStats(dest.createItemApplyStatData(), src.getItemApplyStats());
            LootData.copyLoot(dest.createLoottData(), src.getLoot());
            for(Effect effect : src.getEffects())
                EffectData.copyEffect(dest.createEffectData(), effect);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Item copyItem(ItemData src) {
        Item retVal = new Item();
        try {
            PropertyUtils.copyProperties(retVal, src);

            LootData lootData = src.getLootData();
            if(lootData != null)
                retVal.setLoot(LootData.copyLoot(lootData));

            StatData applyStats = src.getItemApplyStatData();
            if( applyStats != null )
                retVal.setItemApplyStats(StatData.copyStats(applyStats));

            Set<Effect> effects = new HashSet<>();
            for(EffectData effect : src.getEffectDatas())
                effects.add(EffectData.copyEffect(effect));
            retVal.setEffects(Collections.unmodifiableSet(effects));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
