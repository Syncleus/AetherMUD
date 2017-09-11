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
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.npc.Temperament;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@GraphElement
public abstract class NpcData extends AbstractInterceptingVertexFrame {
    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

    @Property("temperament")
    public abstract Temperament getTemperament();

    @Property("temperament")
    public abstract void setTemperament(Temperament temperament);

    @Property("roamArea")
    public abstract List<Area> getRoamAreas();

    @Property("roamArea")
    public abstract void setRoamAreas(List<Area> roamAreas);

    @Property("validTrigger")
    public abstract List<String> getValidTriggers();

    @Property("validTrigger")
    public abstract void setValidTriggers(List<String> validTriggers);

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
    public abstract <N extends StatData> Iterator<? extends N> getAllStatsData(Class<? extends N> type);

    public StatData getStatsData() {
        Iterator<? extends StatData> allStats = this.getAllStatsData(StatData.class);
        if( allStats.hasNext() )
            return allStats.next();
        else
            return null;
    }

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract StatData addStatsData(StatData stats);

    @Adjacency(label = "stats", direction = Direction.OUT)
    public abstract void removeStatsData(StatData stats);

    public void setStatsData(StatData stats) {
        DataUtils.setAllElements(Collections.singletonList(stats), () -> this.getAllStatsData(StatData.class), statsData -> this.addStatsData(statsData), () -> this.createStatsData() );
    }

    public StatData createStatsData() {
        if( this.getStatsData() != null )
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
        this.addStatsData(stats);
        return stats;
    }

    @Adjacency(label = "AttackMessage", direction = Direction.OUT)
    public abstract <N extends AetherMudMessageData> Iterator<? extends N> getAttackMessageDataIterator(Class<? extends N> type);

    public List<AetherMudMessageData> getAttackMessageDatas() {
        return Lists.newArrayList(this.getIdleMessageDataIterator(AetherMudMessageData.class));
    }

    @Adjacency(label = "AttackMessage", direction = Direction.OUT)
    public abstract void addAttackMessageData(AetherMudMessageData message);

    @Adjacency(label = "AttackMessage", direction = Direction.OUT)
    public abstract void removeAttackMessageData(AetherMudMessageData message);

    public void setAttackMessageDatas(List<AetherMudMessageData> messages) {
        DataUtils.setAllElements(messages, () -> this.getAttackMessageDataIterator(AetherMudMessageData.class), message -> this.addAttackMessageData(message), () -> this.createAttackMessageData() );
    }

    public AetherMudMessageData createAttackMessageData() {
        AetherMudMessageData message = this.createMessageData();
        this.addAttackMessageData(message);
        return message;
    }

    @Adjacency(label = "IdleMessage", direction = Direction.OUT)
    public abstract <N extends AetherMudMessageData> Iterator<? extends N> getIdleMessageDataIterator(Class<? extends N> type);

    public List<AetherMudMessageData> getIdleMessageDatas() {
        return Lists.newArrayList(this.getIdleMessageDataIterator(AetherMudMessageData.class));
    }

    @Adjacency(label = "IdleMessage", direction = Direction.OUT)
    public abstract void addIdleMessageData(AetherMudMessageData message);

    @Adjacency(label = "IdleMessage", direction = Direction.OUT)
    public abstract void removeIdleMessageData(AetherMudMessageData message);

    public void setIdleMessageDatas(List<AetherMudMessageData> messages) {
        DataUtils.setAllElements(messages, () -> this.getIdleMessageDataIterator(AetherMudMessageData.class), message -> this.addIdleMessageData(message), () -> this.createIdleMessageData() );
    }

    public AetherMudMessageData createIdleMessageData() {
        AetherMudMessageData message = this.createMessageData();
        this.addIdleMessageData(message);
        return message;
    }

    @Adjacency(label = "BattleMessage", direction = Direction.OUT)
    public abstract <N extends AetherMudMessageData> Iterator<? extends N> getBattleMessageDataIterator(Class<? extends N> type);

    public List<AetherMudMessageData> getBattleMessageDatas() {
        return Lists.newArrayList(this.getBattleMessageDataIterator(AetherMudMessageData.class));
    }

    @Adjacency(label = "BattleMessage", direction = Direction.OUT)
    public abstract void addBattleMessageData(AetherMudMessageData message);

    @Adjacency(label = "BattleMessage", direction = Direction.OUT)
    public abstract void removeBattleMessageData(AetherMudMessageData message);

    public void setBattleMessageDatas(List<AetherMudMessageData> messages) {
        DataUtils.setAllElements(messages, () -> this.getBattleMessageDataIterator(AetherMudMessageData.class), message -> this.addBattleMessageData(message), () -> this.createBattleMessageData() );
    }

    public AetherMudMessageData createBattleMessageData() {
        AetherMudMessageData message = this.createMessageData();
        this.addBattleMessageData(message);
        return message;
    }

    @Adjacency(label = "CriticalAttackMessageData", direction = Direction.OUT)
    public abstract <N extends AetherMudMessageData> Iterator<? extends N> getCriticalAttackMessageDataIterator(Class<? extends N> type);

    public List<AetherMudMessageData> getCriticalAttackMessageDatas() {
        return Lists.newArrayList(this.getCriticalAttackMessageDataIterator(AetherMudMessageData.class));
    }

    @Adjacency(label = "CriticalAttackMessageData", direction = Direction.OUT)
    public abstract void addCriticalAttackMessageData(AetherMudMessageData message);

    @Adjacency(label = "CriticalAttackMessageData", direction = Direction.OUT)
    public abstract void removeCriticalAttackMessageData(AetherMudMessageData message);

    public void setCriticalAttackMessageDatas(List<AetherMudMessageData> messages) {
        DataUtils.setAllElements(messages, () -> this.getCriticalAttackMessageDataIterator(AetherMudMessageData.class), message -> this.addCriticalAttackMessageData(message), () -> this.createCriticalAttackMessageData() );
    }

    public AetherMudMessageData createCriticalAttackMessageData() {
        AetherMudMessageData message = this.createMessageData();
        this.addCriticalAttackMessageData(message);
        return message;
    }

    private AetherMudMessageData createMessageData() {
        final AetherMudMessageData message = this.getGraph().addFramedVertex(AetherMudMessageData.class);
        message.setType(AetherMudMessage.Type.NORMAL);
        message.setMessage("");
        return message;
    }

    public static void copyNpc(NpcData dest, Npc src) {
        try {
            PropertyUtils.copyProperties(dest, src);
            LootData.copyLoot(dest.createLootData(), src.getLoot());
            StatData.copyStats(dest.createStatsData(), src.getStats());
            for(SpawnRule spawnRule : src.getSpawnRules())
                SpawnRuleData.copySpawnRule(dest.createSpawnRuleData(), spawnRule);
            for(AetherMudMessage message : src.getAttackMessages())
                AetherMudMessageData.copyAetherMudMessage(dest.createAttackMessageData(), message);
            for(AetherMudMessage message : src.getBattleMessages())
                AetherMudMessageData.copyAetherMudMessage(dest.createBattleMessageData(), message);
            for(AetherMudMessage message : src.getCriticalAttackMessages())
                AetherMudMessageData.copyAetherMudMessage(dest.createCriticalAttackMessageData(), message);
            for(AetherMudMessage message : src.getIdleMessages())
                AetherMudMessageData.copyAetherMudMessage(dest.createIdleMessageData(), message);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Npc copyNpc(NpcData src) {
        Npc retVal = new Npc();
        try {
            PropertyUtils.copyProperties(retVal, src);
            retVal.setLoot(LootData.copyLoot(src.getLootData()));
            retVal.setStats(StatData.copyStats(src.getStatsData()));

            List<SpawnRule> rules = new ArrayList<>();
            for(SpawnRuleData spawnRuleData : src.getSpawnRuleDatas())
                rules.add(SpawnRuleData.copySpawnRule(spawnRuleData));
            retVal.setSpawnRules(Collections.unmodifiableList(rules));

            List<AetherMudMessage> attackMessages = new ArrayList<>();
            for(AetherMudMessageData message : src.getAttackMessageDatas())
                attackMessages.add(AetherMudMessageData.copyAetherMudMessage(message));
            retVal.setAttackMessages(Collections.unmodifiableList(attackMessages));

            List<AetherMudMessage> criticalAttackMessages = new ArrayList<>();
            for(AetherMudMessageData message : src.getCriticalAttackMessageDatas())
                criticalAttackMessages.add(AetherMudMessageData.copyAetherMudMessage(message));
            retVal.setCriticalAttackMessages(Collections.unmodifiableList(criticalAttackMessages));

            List<AetherMudMessage> battleMessages = new ArrayList<>();
            for(AetherMudMessageData message : src.getBattleMessageDatas())
                battleMessages.add(AetherMudMessageData.copyAetherMudMessage(message));
            retVal.setBattleMessages(Collections.unmodifiableList(battleMessages));

            List<AetherMudMessage> idleMessages = new ArrayList<>();
            for(AetherMudMessageData message : src.getIdleMessageDatas())
                idleMessages.add(AetherMudMessageData.copyAetherMudMessage(message));
            retVal.setIdleMessages(Collections.unmodifiableList(idleMessages));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}


