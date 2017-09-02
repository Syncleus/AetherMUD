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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

public abstract class NpcData extends AbstractVertexFrame implements Npc {
    @Property("criticalAttackMessages")
    public abstract Set<AetherMudMessage> getCriticalAttackMessages();

    @Property("criticalAttackMessages")
    public abstract void setCriticalAttackMessages(Set<AetherMudMessage> criticalAttackMessages);

    @Property("battleMessages")
    public abstract Set<AetherMudMessage> getBattleMessages();

    @Property("battleMessages")
    public abstract void setBattleMessages(Set<AetherMudMessage> battleMessages);

    @Property("idleMessages")
    public abstract Set<AetherMudMessage> getIdleMessages();

    @Property("idleMessages")
    public abstract void setIdleMessages(Set<AetherMudMessage> idleMessages);

    @Property("attackMessages")
    public abstract Set<AetherMudMessage> getAttackMessages();

    @Property("attackMessages")
    public abstract void setAttackMessages(Set<AetherMudMessage> attackMessages);

    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

    @Property("temperament")
    public abstract Temperament getTemperament();

    @Property("temperament")
    public abstract void setTemperament(Temperament temperament);

    @Property("roamAreas")
    public abstract Set<Area> getRoamAreas();

    @Property("roamAreas")
    public abstract void setRoamAreas(Set<Area> roamAreas);

    @Property("validTriggers")
    public abstract Set<String> getValidTriggers();

    @Property("validTriggers")
    public abstract void setValidTriggers(Set<String> validTriggers);

    @Property("spawnRules")
    public abstract Set<SpawnRule> getSpawnRules();

    @Property("spawnRules")
    public abstract void setSpawnRules(Set<SpawnRule> spawnRules);

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

        if( stats == null )
            return;

        StatsData statsData;
        if( stats instanceof StatsData ) {
            this.addStats((StatsData) stats);
        }
        else {
            try {
                BeanUtils.copyProperties(this.createStats(), stats);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not copy properties")
;            }
        }
    }

    public StatsData createStats() {
        if( this.getStats() != null )
            throw new IllegalStateException("Already has stats, can't create another");
        final StatsData stats = this.getGraph().addFramedVertex(StatsData.class);
        this.setStats(stats);
        return stats;
    }
}


