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
package com.syncleus.aethermud.items;

import com.google.common.collect.Lists;

import java.util.UUID;

public interface ItemInstance extends Item {
    String CORPSE_INTENAL_NAME = "corpse";

    Item getItem();

    void setItem(Item item);

    boolean isWithPlayer();

    void setWithPlayer(boolean isWithPlayer);

    int getNumberOfUses();

    void setNumberOfUses(int numberOfUses);

    String getItemId();

    void setHasBeenWithPlayer(boolean hasBeenWithPlayer);

    void setItemId(String itemId);

    boolean isHasBeenWithPlayer();

    static ItemInstance createCorpseItem(String name, Loot loot) {

        ItemInstance item = new ItemBuilder()
            .internalItemName(CORPSE_INTENAL_NAME)
            .itemName(name + " corpse")
            .itemDescription("a bloody corpse")
            .itemTriggers(Lists.newArrayList("corpse", "c", name, name + " corpse"))
            .itemId(UUID.randomUUID().toString())
            .itemHalfLifeTicks(120)
            .rarity(Rarity.BASIC)
            .valueInGold(5)
            .isDisposable(false)
            .restingName("a corpse lies on the ground.")
            .loot(loot)
            .create();

        return item;

    }
}
