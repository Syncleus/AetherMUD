package com.comandante.creeper.storage;

import com.comandante.creeper.common.AttackMessage;
import com.comandante.creeper.items.Loot;
import com.comandante.creeper.npc.Temperament;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.world.model.Area;

import java.util.Set;

public class NpcMetadata {

    private String name;
    private String colorName;
    private Stats stats;
    private String dieMessage;
    private Temperament temperament;
    private Set<Area> roamAreas;
    private Set<String> validTriggers;
    private Set<SpawnRule> spawnRules;
    private Loot loot;
    private Set<AttackMessage> attackMessages;

    public NpcMetadata() {
    }

    public Set<AttackMessage> getAttackMessages() {
        return attackMessages;
    }

    public void setAttackMessages(Set<AttackMessage> attackMessages) {
        this.attackMessages = attackMessages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public String getDieMessage() {
        return dieMessage;
    }

    public void setDieMessage(String dieMessage) {
        this.dieMessage = dieMessage;
    }

    public Temperament getTemperament() {
        return temperament;
    }

    public void setTemperament(Temperament temperament) {
        this.temperament = temperament;
    }

    public Set<Area> getRoamAreas() {
        return roamAreas;
    }

    public void setRoamAreas(Set<Area> roamAreas) {
        this.roamAreas = roamAreas;
    }

    public Set<String> getValidTriggers() {
        return validTriggers;
    }

    public void setValidTriggers(Set<String> validTriggers) {
        this.validTriggers = validTriggers;
    }

    public Set<SpawnRule> getSpawnRules() {
        return spawnRules;
    }

    public void setSpawnRules(Set<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
    }

    public Loot getLoot() {
        return loot;
    }

    public void setLoot(Loot loot) {
        this.loot = loot;
    }
}


