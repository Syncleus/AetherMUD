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
package com.syncleus.aethermud.command.commands;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.server.communication.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LootCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("loot");
    final static String description = "Loot a corpse.";
    final static String correctUsage = "loot corpse";

    public LootCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() > 1) {
                for (Item item : player.getInventory()) {
                    if (item.getInternalItemName().equals(Item.CORPSE_INTENAL_NAME)) {
                        Loot loot = item.getLoot();
                        if (loot != null) {
                            long gold = lootManager.lootGoldAmountReturn(loot);
                            if (gold > 0) {
                                write("You looted " + NumberFormat.getNumberInstance(Locale.US).format(gold) + Color.YELLOW + " gold" + Color.RESET + " from a " + item.getItemName() + ".\r\n");
                                player.incrementGold(gold);
                            }
                            Set<Item> items = lootManager.lootItemsReturn(loot);
                            for (Item i: items) {
                                gameManager.acquireItem(player, i.getItemId(), true);
                                write("You looted " + i.getItemName() +  " from a " + item.getItemName() + ".\r\n");
                            }
                            if (gold <= 0 && items.size() == 0) {
                                write("You looted nothing from " + item.getItemName() + "\r\n");
                            }
                        }
                        player.removeInventoryId(item.getItemId());
                        entityManager.removeItem(item);
                        return;
                    }
                }
            }
        });
    }
}
