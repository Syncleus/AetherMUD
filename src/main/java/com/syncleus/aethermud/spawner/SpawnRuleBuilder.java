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
package com.syncleus.aethermud.spawner;
import com.syncleus.aethermud.world.model.Area;

public class SpawnRuleBuilder {
    private Area area;
    private int spawnIntervalTicks;
    private int maxInstances;
    private int maxPerRoom;
    private int randomPercent;

    public SpawnRuleBuilder setArea(Area area) {
        this.area = area;
        return this;
    }

    public SpawnRuleBuilder setSpawnIntervalTicks(int spawnIntervalTicks) {
        this.spawnIntervalTicks = spawnIntervalTicks;
        return this;
    }

    public SpawnRuleBuilder setMaxInstances(int maxInstances) {
        this.maxInstances = maxInstances;
        return this;
    }

    public SpawnRuleBuilder setMaxPerRoom(int maxPerRoom) {
        this.maxPerRoom = maxPerRoom;
        return this;
    }

    public SpawnRuleBuilder setRandomPercent(int randomPercent) {
        this.randomPercent = randomPercent;
        return this;
    }


    public SpawnRule createSpawnRule() {
        return new SpawnRule(area, spawnIntervalTicks, maxInstances, maxPerRoom, randomPercent);
    }
}
