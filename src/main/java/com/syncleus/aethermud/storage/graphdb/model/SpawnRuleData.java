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

import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

@GraphElement
public abstract class SpawnRuleData extends AbstractInterceptingVertexFrame {
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

    public static void copySpawnRule(SpawnRuleData dest, SpawnRule src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static SpawnRule copySpawnRule(SpawnRuleData src) {
        SpawnRule retVal = new SpawnRule();
        try {
            PropertyUtils.copyProperties(retVal, src);;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
