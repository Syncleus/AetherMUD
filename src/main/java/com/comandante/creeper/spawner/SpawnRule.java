/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.spawner;

import com.comandante.creeper.world.model.Area;

public class SpawnRule {

    private Area area;
    private int randomChance;
    private int spawnIntervalTicks;
    private int maxInstances;
    private int maxPerRoom;

    public SpawnRule(Area area, int spawnIntervalTicks, int maxInstances, int maxPerRoom, int randomPercent) {
        this.area = area;
        this.spawnIntervalTicks = spawnIntervalTicks;
        this.maxInstances = maxInstances;
        this.maxPerRoom = maxPerRoom;
        this.randomChance = randomPercent;
    }

    public SpawnRule() {
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

    public void setArea(Area area) {
        this.area = area;
    }

    public void setRandomChance(int randomChance) {
        this.randomChance = randomChance;
    }

    public void setSpawnIntervalTicks(int spawnIntervalTicks) {
        this.spawnIntervalTicks = spawnIntervalTicks;
    }

    public void setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
    }

    public void setMaxPerRoom(int maxPerRoom) {
        this.maxPerRoom = maxPerRoom;
    }
}

