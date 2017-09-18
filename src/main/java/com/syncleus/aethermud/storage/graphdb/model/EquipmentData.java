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

import com.syncleus.aethermud.items.Equipment;
import com.syncleus.aethermud.items.EquipmentSlotType;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;

@GraphElement
public abstract class EquipmentData extends AbstractVertexFrame {
    @Property("equipmentSlotType")
    public abstract void setEquipmentSlotType(EquipmentSlotType slotType);

    @Property("equipmentSlotType")
    public abstract EquipmentSlotType getEquipmentSlotType();

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract <N extends StatData> Iterator<? extends N> getStatDataIterator(Class<? extends N> type);

    public StatData getStatData() {
        Iterator<? extends StatData> allStats = this.getStatDataIterator(StatData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract StatData addStatData(StatData stats);

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract void removeStatData(StatData stats);

    public void setStatsData(StatData stats) {
        DataUtils.setAllElements(Collections.singletonList(stats), () -> this.getStatDataIterator(StatData.class), statsData -> this.addStatData(statsData), () -> createStatData() );
    }

    public StatData createStatData() {
        if( this.getStatData() != null )
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

    public static void copyEquipment(EquipmentData dest, Equipment src) {
        try {
            PropertyUtils.copyProperties(dest, src);
            StatData.copyStats((dest.getStatData() != null ? dest.getStatData() : dest.createStatData()), src.getStats());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Equipment copyEquipment(EquipmentData src) {
        return new Equipment(src.getEquipmentSlotType(), StatData.copyStats(src.getStatData()));
    }
}
