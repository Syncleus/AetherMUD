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
package com.syncleus.aethermud.storage.graphdb;

import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.Property;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public abstract class EffectData extends AbstractVertexFrame implements Effect {
    @Override
    @Property("EffectName")
    public abstract String getEffectName();

    @Override
    @Property("EffectDescription")
    public abstract String getEffectDescription();

    @Override
    @Property("EffectApplyMessages")
    public abstract List<String> getEffectApplyMessages();

    @Override
    @Property("MaxEffectApplications")
    public abstract int getMaxEffectApplications();

    @Override
    @Property("FrozenMovement")
    public abstract boolean isFrozenMovement();

    @Override
    @Property("EffectApplications")
    public abstract int getEffectApplications();

    @Override
    @Property("EffectApplications")
    public abstract void setEffectApplications(int effectApplications);

    @Override
    @Property("PlayerId")
    public abstract String getPlayerId();

    @Override
    @Property("PlayerId")
    public abstract void setPlayerId(String playerId);

    @Override
    @Property("EffectName")
    public abstract void setEffectName(String effectName);

    @Override
    @Property("EffectDescription")
    public abstract void setEffectDescription(String effectDescription);

    @Override
    @Property("EffectApplyMessages")
    public abstract void setEffectApplyMessages(List<String> effectApplyMessages);

    @Override
    @Property("MaxEffectApplications")
    public abstract void setMaxEffectApplications(int maxEffectApplications);

    @Override
    @Property("FrozenMovement")
    public abstract void setFrozenMovement(boolean frozenMovement);

    @Adjacency(label = "ApplyStatsOnTick", direction = Direction.OUT)
    public abstract <N extends StatsData> Iterator<? extends N> getAllApplyStatsOnTick(Class<? extends N> type);

    @Override
    public Stats getApplyStatsOnTick() {
        Iterator<? extends StatsData> allStats = this.getAllApplyStatsOnTick(StatsData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "ApplyStatsOnTick", direction = Direction.OUT)
    public abstract StatsData addApplyStatsOnTick(StatsData stats);

    @Adjacency(label = "ApplyStatsOnTick", direction = Direction.OUT)
    public abstract void removeApplyStatsOnTick(StatsData stats);

    @Override
    public void setApplyStatsOnTick(Stats stats) {
        Iterator<? extends StatsData> existingAll = this.getAllApplyStatsOnTick(StatsData.class);
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                StatsData existing = existingAll.next();
                this.removeApplyStatsOnTick(existing);
                existing.remove();
            }

        }

        if( stats == null ) {
            this.addApplyStatsOnTick(this.createStats());
            return;
        }

        StatsData statsData;
        if( stats instanceof StatsData ) {
            this.addApplyStatsOnTick((StatsData) stats);
        }
        else {
            StatsData createdData = this.createStats();
            try {
                PropertyUtils.copyProperties(createdData, stats);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not copy properties");
            }
            this.addApplyStatsOnTick(createdData);
        }
    }

    @Adjacency(label = "DurationStats", direction = Direction.OUT)
    public abstract <N extends StatsData> Iterator<? extends N> getAllDurationStats(Class<? extends N> type);

    @Override
    public Stats getDurationStats() {
        Iterator<? extends StatsData> allStats = this.getAllDurationStats(StatsData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "DurationStats", direction = Direction.OUT)
    public abstract StatsData addDurationStats(StatsData stats);

    @Adjacency(label = "DurationStats", direction = Direction.OUT)
    public abstract void removeDurationStats(StatsData stats);

    @Override
    public void setDurationStats(Stats stats) {
        Iterator<? extends StatsData> existingAll = this.getAllDurationStats(StatsData.class);
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                StatsData existing = existingAll.next();
                this.removeDurationStats(existing);
                existing.remove();
            }

        }

        if( stats == null ) {
            this.addApplyStatsOnTick(this.createStats());
            return;
        }

        StatsData statsData;
        if( stats instanceof StatsData ) {
            this.addDurationStats((StatsData) stats);
        }
        else {
            StatsData createdData = this.createStats();
            try {
                PropertyUtils.copyProperties(createdData, stats);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not copy properties");
            }
            this.addDurationStats(createdData);
        }
    }

    private StatsData createStats() {
        if( this.getDurationStats() != null )
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
        return stats;
    }
}
