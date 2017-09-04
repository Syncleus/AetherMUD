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

import com.syncleus.aethermud.world.model.Area;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Property;

public abstract class SpawnRuleData extends AbstractVertexFrame {
    @Property("RandomChance")
    public abstract int getRandomChance();

    @Property("MaxPerRoom")
    public abstract int getMaxPerRoom();

    @Property("IntervalTicks")
    public abstract int getSpawnIntervalTicks();

    @Property("MaxInstances")
    public abstract int getMaxInstances();

    @Property("Area")
    public abstract Area getArea();

    @Property("Area")
    public abstract void setArea(Area area);

    @Property("RandomChance")
    public abstract void setRandomChance(int randomChance);

    @Property("IntervalTicks")
    public abstract void setSpawnIntervalTicks(int spawnIntervalTicks);

    @Property("MaxInstances")
    public abstract void setMaxInstances(int maxInstances);

    @Property("MaxPerRoom")
    public abstract void setMaxPerRoom(int maxPerRoom);
}
