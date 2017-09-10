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

import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@GraphElement
public abstract class LootData extends AbstractInterceptingVertexFrame {
    @Property("InternalItemNames")
    public abstract List<String> getInternalItemNames();

    @Property("InternalItemNames")
    public abstract void setInternalItemNames(List<String> internalItemNames);

    @Property("GoldMax")
    public abstract int getLootGoldMax();

    @Property("GoldMin")
    public abstract int getLootGoldMin();

    @Property("GoldMax")
    public abstract void setLootGoldMax(int lootGoldMax);

    @Property("GoldMin")
    public abstract void setLootGoldMin(int lootGoldMin);

    public static void copyLoot(LootData dest, Loot src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties");
        }
    }

    public static Loot copyLoot(LootData src) {
        Loot retVal = new Loot();
        try {
            PropertyUtils.copyProperties(retVal, src);;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties");
        }
        return retVal;
    }
}
