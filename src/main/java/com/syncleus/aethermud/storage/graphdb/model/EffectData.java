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
package com.syncleus.aethermud.storage.graphdb.model;

import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@GraphElement
public abstract class EffectData extends AbstractInterceptingVertexFrame {
    @Property("effectName")
    public abstract String getEffectName();

    @Property("effectDescription")
    public abstract String getEffectDescription();

    @Property("effectApplyMessages")
    public abstract List<String> getEffectApplyMessages();

    @Property("maxEffectApplications")
    public abstract int getMaxEffectApplications();

    @Property("frozenMovement")
    public abstract boolean isFrozenMovement();

    @Property("effectApplications")
    public abstract int getEffectApplications();

    @Property("effectApplications")
    public abstract void setEffectApplications(int effectApplications);

    @Property("playerId")
    public abstract String getPlayerId();

    @Property("playerId")
    public abstract void setPlayerId(String playerId);

    @Property("effectName")
    public abstract void setEffectName(String effectName);

    @Property("effectDescription")
    public abstract void setEffectDescription(String effectDescription);

    @Property("effectApplyMessages")
    public abstract void setEffectApplyMessages(List<String> effectApplyMessages);

    @Property("maxEffectApplications")
    public abstract void setMaxEffectApplications(int maxEffectApplications);

    @Property("frozenMovement")
    public abstract void setFrozenMovement(boolean frozenMovement);

    @Adjacency(label = "applyStatsOnTick", direction = Direction.OUT)
    public abstract <N extends StatData> Iterator<? extends N> getApplyStatOnTickDataIterator(Class<? extends N> type);

    public StatData getApplyStatOnTickData() {
        Iterator<? extends StatData> allStats = this.getApplyStatOnTickDataIterator(StatData.class);
        if (allStats.hasNext())
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "applyStatsOnTick", direction = Direction.OUT)
    public abstract StatData addApplyStatOnTickData(StatData stats);

    @Adjacency(label = "applyStatsOnTick", direction = Direction.OUT)
    public abstract void removeApplyStatOnTickData(StatData stats);

    public void setApplyStatOnTickData(StatData stat) {
        DataUtils.setAllElements(Collections.singletonList(stat), () -> this.getApplyStatOnTickDataIterator(StatData.class), applyStat -> this.addApplyStatOnTickData(applyStat), () -> createApplyStatOnTickData() );
    }

    private StatData createApplyStatOnTickData() {
        if (this.getApplyStatOnTickData() != null)
            throw new IllegalStateException("Already has stats, can't create another");
        StatData statData = this.createOrphanStats();
        this.addApplyStatOnTickData(statData);
        return statData;
    }

    @Adjacency(label = "durationStats", direction = Direction.OUT)
    public abstract <N extends StatData> Iterator<? extends N> getDurationStatDataIterator(Class<? extends N> type);

    public StatData getDurationStatData() {
        Iterator<? extends StatData> allStats = this.getDurationStatDataIterator(StatData.class);
        if (allStats.hasNext())
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "durationStats", direction = Direction.OUT)
    public abstract StatData addDurationStatData(StatData stats);

    @Adjacency(label = "durationStats", direction = Direction.OUT)
    public abstract void removeDurationStatData(StatData stats);

    public void setDurationStatData(StatData stat) {
        DataUtils.setAllElements(Collections.singletonList(stat), () -> this.getDurationStatDataIterator(StatData.class), durationStat -> this.addDurationStatData(durationStat), () -> createDurationStatData() );
    }

    private StatData createDurationStatData() {
        if (this.getDurationStatData() != null)
            throw new IllegalStateException("Already has stats, can't create another");
        StatData statData = this.createOrphanStats();
        this.addDurationStatData(statData);
        return statData;
    }

    private StatData createOrphanStats() {
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
        return stats;
    }

    public static void copyEffect(EffectData dest, Effect src) {
        try {
            PropertyUtils.copyProperties(dest, src);
            StatData.copyStats((dest.getApplyStatOnTickData() != null ? dest.getApplyStatOnTickData() : dest.createApplyStatOnTickData()), src.getApplyStatsOnTick());
            StatData.copyStats((dest.getDurationStatData() != null ? dest.getDurationStatData() : dest.createDurationStatData()), src.getDurationStats());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Effect copyEffect(EffectData src) {
        Effect retVal = new Effect();
        try {
            PropertyUtils.copyProperties(retVal, src);

            retVal.setDurationStats(StatData.copyStats(src.getDurationStatData()));
            retVal.setApplyStatsOnTick(StatData.copyStats(src.getApplyStatOnTickData()));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
