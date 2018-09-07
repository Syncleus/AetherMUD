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
package com.syncleus.aethermud.storage.graphdb.model;
import com.google.common.collect.Lists;

import com.syncleus.aethermud.items.Loot;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@GraphElement
public abstract class LootData extends AbstractInterceptingVertexFrame {
    @Property("internalItemNames")
    public abstract List<String> getInternalItemNames();

    @Property("internalItemNames")
    public abstract void setInternalItemNames(List<String> internalItemNames);

    @Property("goldMax")
    public abstract int getLootGoldMax();

    @Property("goldMin")
    public abstract int getLootGoldMin();

    @Property("goldMax")
    public abstract void setLootGoldMax(int lootGoldMax);

    @Property("goldMin")
    public abstract void setLootGoldMin(int lootGoldMin);

    public static void copyLoot(LootData dest, Loot src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Loot copyLoot(LootData src) {
        Loot retVal = new Loot();

        try {
            PropertyUtils.copyProperties(retVal, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
