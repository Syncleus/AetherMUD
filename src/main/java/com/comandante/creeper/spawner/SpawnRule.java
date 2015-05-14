package com.comandante.creeper.spawner;

import com.comandante.creeper.world.Area;

public class SpawnRule {

    private final Area area;
    private int randomChance;
    private final int spawnIntervalTicks;
    private final int maxInstances;
    private final int maxPerRoom;

    public SpawnRule(Area area, int spawnIntervalTicks, int maxInstances, int maxPerRoom, int randomPercent) {
        this.area = area;
        this.spawnIntervalTicks = spawnIntervalTicks;
        this.maxInstances = maxInstances;
        this.maxPerRoom = maxPerRoom;
        this.randomChance = randomPercent;
    }

    public int getRandomChance() {
        return randomChance;
    }

    public int getMaxPerRoom() {
        return maxPerRoom;
    }

    public int getSpawnIntervalTicks() {
        return spawnIntervalTicks;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public Area getArea() {
        return area;
    }

    @Override
    public String toString() {
        return "SpawnRule{" +
                "area=" + area +
                ", randomChance=" + randomChance +
                ", spawnIntervalTicks=" + spawnIntervalTicks +
                ", maxInstances=" + maxInstances +
                ", maxPerRoom=" + maxPerRoom +
                '}';
    }
}

