package com.comandante.creeper.npc;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Loot;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.storage.NpcMetadata;
import com.comandante.creeper.world.model.Area;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class NpcBuilder {
    private GameManager gameManager;
    private String name;
    private String colorName;
    private long lastPhraseTimestamp;
    private Stats stats;
    private String dieMessage;
    private Set<Area> roamAreas;
    private Set<String> validTriggers;
    private Loot loot;
    private Set<SpawnRule> spawnRules;
    private Temperament temperament;

    public NpcBuilder() {
    }

    public NpcBuilder(Npc npc) {
        this.name = npc.getName();
        this.colorName = npc.getColorName();
        this.lastPhraseTimestamp = npc.getLastPhraseTimestamp();
        this.stats = new Stats(npc.getStats());
        this.dieMessage = npc.getDieMessage();
        this.roamAreas = npc.getRoamAreas();
        this.validTriggers = npc.getValidTriggers();
        this.loot = npc.getLoot();
        this.spawnRules = npc.getSpawnRules();
        this.gameManager = npc.getGameManager();
        this.temperament = npc.getTemperament();
    }

    public NpcBuilder(NpcMetadata npcMetadata) {
        this.name = npcMetadata.getName();
        this.colorName = npcMetadata.getColorName();
        this.stats = new Stats(npcMetadata.getStats());
        this.dieMessage = npcMetadata.getDieMessage();
        this.roamAreas = npcMetadata.getRoamAreas();
        this.validTriggers = npcMetadata.getValidTriggers();
        this.loot = npcMetadata.getLoot();
        this.spawnRules = npcMetadata.getSpawnRules();
        this.temperament = npcMetadata.getTemperament();
    }

    public NpcBuilder setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
        return this;
    }

    public NpcBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NpcBuilder setColorName(String colorName) {
        this.colorName = colorName;
        return this;
    }

    public NpcBuilder setLastPhraseTimestamp(long lastPhraseTimestamp) {
        this.lastPhraseTimestamp = lastPhraseTimestamp;
        return this;
    }

    public NpcBuilder setStats(Stats stats) {
        this.stats = stats;
        return this;
    }

    public NpcBuilder setDieMessage(String dieMessage) {
        this.dieMessage = dieMessage;
        return this;
    }

    public NpcBuilder setTemperament(Temperament temperament) {
        this.temperament = temperament;
        return this;
    }

    public NpcBuilder setRoamAreas(Set<Area> roamAreas) {
        this.roamAreas = roamAreas;
        return this;
    }

    public NpcBuilder setValidTriggers(Set<String> validTriggers) {
        this.validTriggers = validTriggers;
        return this;
    }

    public NpcBuilder setLoot(Loot loot) {
        this.loot = loot;
        return this;
    }

    public NpcBuilder setSpawnRules(Set<SpawnRule> spawnRules) {
        this.spawnRules = spawnRules;
        return this;
    }

    public Npc createNpc() {
        checkNotNull(gameManager);
        if (loot.getLootGoldMin() > loot.getLootGoldMax()) {
            throw new RuntimeException("Invalid loot configuration.");
        }
        return new Npc(gameManager, name, colorName, lastPhraseTimestamp, stats, dieMessage, temperament, roamAreas, validTriggers, loot, spawnRules);
    }
}