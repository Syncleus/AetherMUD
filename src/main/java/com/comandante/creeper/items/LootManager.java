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
package com.comandante.creeper.items;

import com.comandante.creeper.core_game.GameManager;
import com.google.common.collect.Sets;
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

    private long randInt(long min, long max) {
        return JVMRandom.nextLong((max - min) + 1) + min;
    }

    public boolean lootDropSuccess(double percent) {
        double rangeMin = 0;
        double rangeMax = 100;
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        return randomValue <= percent;
    }

    public long lootGoldAmountReturn(Loot loot) {
        return randInt(loot.getLootGoldMin(), loot.getLootGoldMax());
    }

    public Set<Item> lootItemsReturn(Loot loot) {
        Set<Item> lootItems = Sets.newHashSet();
        for (String internalItemName: loot.getInternalItemNames()) {
            Optional<ItemMetadata> itemMetadataOptional = gameManager.getItemStorage().get(internalItemName);
            if (!itemMetadataOptional.isPresent()) {
                continue;
            }
            ItemMetadata itemMetadata = itemMetadataOptional.get();
            if (lootDropSuccess(itemMetadata.getRarity().getPercentToLoot())) {
                Item i = new ItemBuilder().from(itemMetadata).create();
                gameManager.getEntityManager().saveItem(i);
                lootItems.add(i);
            }
        }
        return lootItems;
    }
}
