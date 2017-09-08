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
import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.npc.Temperament;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.ElementFrame;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@GraphElement
public abstract class NpcData extends AbstractInterceptingVertexFrame {
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

    @Adjacency(label = "spawnRules", direction = Direction.OUT)
    public abstract <N extends SpawnRuleData> Iterator<? extends N> getSpawnRulesDataIterator(Class<? extends N> type);

    public List<SpawnRuleData> getSpawnRulesData() {
        return Lists.newArrayList(this.getSpawnRulesDataIterator(SpawnRuleData.class));
    }

    @Adjacency(label = "spawnRules", direction = Direction.OUT)
    public abstract void addSpawnRuleData(SpawnRuleData spawnRule);

    @Adjacency(label = "spawnRules", direction = Direction.OUT)
    public abstract void removeSpawnRuleData(SpawnRuleData spawnRule);

    public void setSpawnRulesData(List<SpawnRuleData> spawnRules) {
        DataUtils.setAllElements(spawnRules, () -> this.getSpawnRulesDataIterator(SpawnRuleData.class), ruleData -> this.addSpawnRuleData(ruleData), () -> this.createSpawnRuleData() );
    }

    public SpawnRuleData createSpawnRuleData() {
        final SpawnRuleData rule = this.getGraph().addFramedVertex(SpawnRuleData.class);
        this.addSpawnRuleData(rule);
        return rule;
    }

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

    @Adjacency(label = "loot", direction = Direction.OUT)
    public abstract <N extends LootData> Iterator<? extends N> getAllLootDatas(Class<? extends N> type);

    public LootData getLootData() {
        Iterator<? extends LootData> allLoots = this.getAllLootDatas(LootData.class);
        if( allLoots.hasNext() )
            return allLoots.next();
        else
            return null;
    }

    @Adjacency(label = "loot", direction = Direction.OUT)
    public abstract LootData addLootData(LootData loot);

    @Adjacency(label = "loot", direction = Direction.OUT)
    public abstract void removeLootData(LootData loot);

    public void setLootData(LootData loot) {
        DataUtils.setAllElements(Collections.singletonList(loot), () -> this.getAllLootDatas(LootData.class), lootData -> this.addLootData(lootData), () -> this.createLootData() );
    }

    public LootData createLootData() {
        if( this.getLootData() != null )
            throw new IllegalStateException("Already has stats, can't create another");
        final LootData loot = this.getGraph().addFramedVertex(LootData.class);
        loot.setInternalItemNames(Lists.newArrayList());
        loot.setLootGoldMax(0);
        loot.setLootGoldMin(0);
        this.addLootData(loot);
        return loot;
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

    public void setStats(StatsData stats) {
        DataUtils.setAllElements(Collections.singletonList(stats), () -> this.getAllStats(StatsData.class), statsData -> this.addStats(statsData), () -> this.createStats() );
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


