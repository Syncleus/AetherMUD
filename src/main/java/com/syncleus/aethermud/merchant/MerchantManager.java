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
package com.syncleus.aethermud.merchant;


import com.codahale.metrics.Timer;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerUtil;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;

import java.util.Optional;

import static com.codahale.metrics.MetricRegistry.name;

public class MerchantManager {

    private final GameManager gameManager;
    private final Timer responses = Main.metrics.timer(name(MerchantManager.class, "purchase_time"));


    public MerchantManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void purchaseItem(Merchant merchant, int itemNo, Player player) {
        final Timer.Context context = responses.time();
        try {
            int i = 0;
            for (MerchantItemForSale merchantItemForSale : merchant.getMerchantItemForSales()) {
                i++;
                if (i == itemNo) {
                    String internalItemName = merchantItemForSale.getInternalItemName();
                    Item item;
                    try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
                        Optional<ItemData> itemOptional = tx.getStorage().getItem(internalItemName);
                        if (!itemOptional.isPresent()) {
                            continue;
                        }
                        item = ItemData.copyItem(itemOptional.get());
                    }
                    long maxInventorySize = player.getPlayerStatsWithEquipmentAndLevel().getInventorySize();
                    if (player.getInventory().size() >= maxInventorySize) {
                        gameManager.getChannelUtils().write(player.getPlayerId(), "Your inventory is full, drop some items and come back.\r\n");
                        return;
                    }
                    int price = merchantItemForSale.getCost();
                    PlayerUtil.consume(gameManager, player.getPlayerId(), playerData -> {
                        long availableGold = playerData.getGold();
                        if (availableGold >= price) {
                            ItemInstance itemInstance = new ItemBuilder().from(item).create();
                            gameManager.getEntityManager().saveItem(itemInstance);
                            gameManager.acquireItem(player, itemInstance.getItemId());
                            player.incrementGold(-price);
                            gameManager.getChannelUtils().write(player.getPlayerId(), "You have purchased: " + itemInstance.getItemName() + "\r\n");
                        } else {
                            gameManager.getChannelUtils().write(player.getPlayerId(), "You can't afford: " + item.getItemName() + "\r\n");
                        }
                    });
                }
            }
        } finally {
            context.stop();
        }
    }
}
