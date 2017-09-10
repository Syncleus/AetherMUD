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
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GraphElement
public abstract class ItemData extends AbstractInterceptingVertexFrame implements Item {
    @Override
    @Property("ValidTimeOfDays")
    public abstract List<TimeTracker.TimeOfDay> getValidTimeOfDays();

    @Override
    @Property("ValidTimeOfDays")
    public abstract void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays);

    @Override
    @Property("ValidTimeOfDays")
    public abstract void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays);

    @Override
    @Property("Disposable")
    public abstract boolean isDisposable();

    @Override
    @Property("MaxUses")
    public abstract int getMaxUses();

    @Override
    @Property("WithPlayer")
    public abstract boolean isWithPlayer();

    @Override
    @Property("WithPlayer")
    public abstract void setWithPlayer(boolean isWithPlayer);

    @Override
    @Property("NumberOfUses")
    public abstract int getNumberOfUses();

    @Override
    @Property("NumberOfUses")
    public abstract void setNumberOfUses(int numberOfUses);

    @Override
    @Property("ItemId")
    public abstract String getItemId();

    @Override
    @Property("InternalItemName")
    public abstract String getInternalItemName();

    @Override
    @Property("ItemName")
    public abstract String getItemName();

    @Override
    @Property("ItemDescription")
    public abstract String getItemDescription();

    @Override
    @Property("ItemTriggers")
    public abstract List<String> getItemTriggers();

    @Override
    @Property("ItemTriggers")
    public abstract void setItemTriggers(List<String> itemTriggers);

    @Override
    @Property("RestingName")
    public abstract String getRestingName();

    @Override
    @Property("ItemHalfLifeTicks")
    public abstract int getItemHalfLifeTicks();

    @Override
    @Property("Loot")
    public abstract Loot getLoot();

    @Override
    @Property("Loot")
    public abstract void setLoot(Loot loot);

    @Override
    @Property("Equipment")
    public abstract Equipment getEquipment();

    @Override
    @Property("Equipment")
    public abstract void setEquipment(Equipment equipment);

    @Override
    @Property("HasBeenWithPlayer")
    public abstract void setHasBeenWithPlayer(boolean hasBeenWithPlayer);

    @Override
    @Property("Rarity")
    public abstract Rarity getRarity();

    @Override
    @Property("ValueInGold")
    public abstract int getValueInGold();

    @Override
    @Property("ItemName")
    public abstract void setItemName(String itemName);

    @Override
    @Property("ItemDescription")
    public abstract void setItemDescription(String itemDescription);

    @Override
    @Property("InternalItemName")
    public abstract void setInternalItemName(String internalItemName);

    @Override
    @Property("RestingName")
    public abstract void setRestingName(String restingName);

    @Override
    @Property("ItemId")
    public abstract void setItemId(String itemId);

    @Override
    @Property("ItemHalfLifeTicks")
    public abstract void setItemHalfLifeTicks(int itemHalfLifeTicks);

    @Override
    public void setRarity(Rarity rarity) {
        this.traverse((v) -> v.property("Rarity", rarity));
    }

    @Override
    @Property("ValueInGold")
    public abstract void setValueInGold(int valueInGold);

    @Override
    @Property("MaxUses")
    public abstract void setMaxUses(int maxUses);

    @Override
    @Property("Disposable")
    public abstract void setDisposable(boolean disposable);

    @Override
    @Property("HasBeenWithPlayer")
    public abstract boolean isHasBeenWithPlayer();

    @Adjacency(label = "Effect", direction = Direction.OUT)
    public abstract EffectData addEffect(EffectData effects);

    @Adjacency(label = "Effect", direction = Direction.OUT)
    public abstract void removeEffect(EffectData stats);

    @Adjacency(label = "Effect", direction = Direction.OUT)
    public abstract <N extends EffectData> Iterator<? extends N> getEffects(Class<? extends N> type);

    @Override
    public Set<Effect> getEffects() {
        Set<Effect> retVal = new HashSet<>();
        Iterator<? extends EffectData> iterator = this.getEffects(EffectData.class);
        while(iterator.hasNext()) {
            EffectData effectData = iterator.next();
            retVal.add(EffectData.copyEffect(effectData));
        }
        return Collections.unmodifiableSet(retVal);
    }

    @Override
    public void setEffects(Set<Effect> effects) {
        Iterator<? extends EffectData> existingAll = this.getEffects(EffectData.class);
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                EffectData existing = existingAll.next();
                this.removeEffect(existing);
                existing.remove();
            }

        }

        if( effects == null || effects.size() == 0 ) {
            return;
        }

        for( Effect effect : effects ) {
            try {
                PropertyUtils.copyProperties(this.createEffect(), effect);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not copy properties");
            }
        }
    }

    public EffectData createEffect() {
        final EffectData effect = this.getGraph().addFramedVertex(EffectData.class);
        this.addEffect(effect);
        return effect;
    }

    @Adjacency(label = "ItemApplyStats", direction = Direction.OUT)
    public abstract <N extends StatsData> Iterator<? extends N> getAllItemApplyStats(Class<? extends N> type);

    public Stats getItemApplyStats() {
        Iterator<? extends StatsData> allStats = this.getAllItemApplyStats(StatsData.class);
        if( allStats.hasNext() )
            return StatsData.copyStats(allStats.next());
        else
            return null;
    }

    @Adjacency(label = "ItemApplyStats", direction = Direction.OUT)
    public abstract StatsData addStats(StatsData stats);

    @Adjacency(label = "ItemApplyStats", direction = Direction.OUT)
    public abstract void removeStats(StatsData stats);

    public void setItemApplyStats(Stats stats) {
        Iterator<? extends StatsData> existingAll = this.getAllItemApplyStats(StatsData.class);
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                StatsData existing = existingAll.next();
                this.removeStats(existing);
                existing.remove();
            }

        }

        if( stats == null ) {
            this.createItemApplyStats();
            return;
        }

        try {
            PropertyUtils.copyProperties(this.createItemApplyStats(), stats);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties");
        }
    }

    public StatsData createItemApplyStats() {
        if( this.getItemApplyStats() != null )
            throw new IllegalStateException("Already has stats, can't create another");
        final StatsData stats = this.getGraph().addFramedVertex(StatsData.class);
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
        this.addStats(stats);
        return stats;
    }

}
