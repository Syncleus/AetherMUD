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
package com.syncleus.aethermud.items;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import org.apache.commons.lang.math.JVMRandom;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class LootManager {
    private final GameManager gameManager;
    public LootManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    private final Random  random = new Random();
    private final Interner<String> interner = Interners.newWeakInterner();

    private int randInt(int min, int max) {
        return (int) JVMRandom.nextLong((max - min) + 1) + min;
    }

    public boolean lootDropSuccess(double percent) {
        double rangeMin = 0;
        double rangeMax = 100;
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        return randomValue <= percent;
    }

    public int lootGoldAmountReturn(Loot loot) {
        return randInt(loot.getLootGoldMin(), loot.getLootGoldMax());
    }

    public Set<ItemInstance> lootItemsReturn(Loot loot) {
        Set<ItemInstance> lootItems = Sets.newHashSet();
        for (String internalItemName: loot.getInternalItemNames()) {
            synchronized (interner.intern(internalItemName)) {
                try (GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction()) {
                    Optional<ItemData> itemOptional = tx.getStorage().getItem(internalItemName);
                    if (!itemOptional.isPresent()) {
                        continue;
                    }
                    ItemData item = itemOptional.get();
                    if (lootDropSuccess(item.getRarity().getPercentToLoot())) {
                        ItemInstance i = new ItemBuilder().from(ItemData.copyItem(item)).create();
                        tx.getStorage().saveItemEntity(i);
                        lootItems.add(i);
                    }
                    tx.success();
                }
            }
        }
        return lootItems;
    }
}
