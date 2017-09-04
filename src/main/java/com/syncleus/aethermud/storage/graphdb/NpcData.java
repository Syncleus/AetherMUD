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

import com.google.api.client.util.Sets;
import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.npc.Temperament;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.Property;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class NpcData extends AbstractVertexFrame implements Npc {
    @Property("criticalAttackMessages")
    public abstract List<AetherMudMessage> getCriticalAttackMessages();

    @Property("criticalAttackMessages")
    public abstract void setCriticalAttackMessages(List<AetherMudMessage> criticalAttackMessages);

    @Property("battleMessages")
    public abstract List<AetherMudMessage> getBattleMessages();

    @Property("battleMessages")
    public abstract void setBattleMessages(List<AetherMudMessage> battleMessages);

    @Property("idleMessages")
    public abstract List<AetherMudMessage> getIdleMessages();

    @Property("idleMessages")
    public abstract void setIdleMessages(List<AetherMudMessage> idleMessages);

    @Property("attackMessages")
    public abstract List<AetherMudMessage> getAttackMessages();

    @Property("attackMessages")
    public abstract void setAttackMessages(List<AetherMudMessage> attackMessages);

    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

    @Property("temperament")
    public abstract Temperament getTemperament();

    @Property("temperament")
    public abstract void setTemperament(Temperament temperament);

    @Property("roamAreas")
    public abstract List<Area> getRoamAreas();

    @Property("roamAreas")
    public abstract void setRoamAreas(List<Area> roamAreas);

    @Property("validTriggers")
    public abstract List<String> getValidTriggers();

    @Property("validTriggers")
    public abstract void setValidTriggers(List<String> validTriggers);

    @Property("spawnRules")
    public abstract List<SpawnRule> getSpawnRules();

    @Property("spawnRules")
    public abstract void setSpawnRules(List<SpawnRule> spawnRules);

    @Property("loot")
    public abstract Loot getLoot();

    @Property("loot")
    public abstract void setLoot(Loot loot);

    public String getColorName() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(this.getProperty("colorName"));
    }

    public void setColorName(String colorName) {
        this.setProperty("colorName", ColorizedTextTemplate.renderToTemplateLanguage(colorName));
    }

    public String getDieMessage() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(this.getProperty("dieMessage"));
    }

    public void setDieMessage(String dieMessage) {
        this.setProperty("dieMessage", ColorizedTextTemplate.renderToTemplateLanguage(dieMessage));
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract <N extends StatsData> Iterator<? extends N> getAllStats(Class<? extends N> type);

    public StatsData getStats() {
        Iterator<? extends StatsData> allStats = this.getAllStats(StatsData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract StatsData addStats(StatsData stats);

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract void removeStats(StatsData stats);

    public void setStats(Stats stats) {
        Iterator<? extends StatsData> existingAll = this.getAllStats(StatsData.class);
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                StatsData existing = existingAll.next();
                this.removeStats(existing);
                existing.remove();
            }

        }

        if( stats == null ) {
            this.createStats();
            return;
        }

        StatsData statsData;
        if( stats instanceof StatsData ) {
            this.addStats((StatsData) stats);
        }
        else {
            try {
                PropertyUtils.copyProperties(this.createStats(), stats);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not copy properties")
;            }
        }
    }

    public StatsData createStats() {
        if( this.getStats() != null )
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


