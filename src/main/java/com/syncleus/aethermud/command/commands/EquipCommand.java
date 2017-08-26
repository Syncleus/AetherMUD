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
package com.syncleus.aethermud.command.commands;

import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class EquipCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("equip");
    final static String description = "Equip an item.";
    final static String correctUsage = "equip <equipment item name>";


    public EquipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() == 1) {
                write("No equipment item specified.");
                return;
            }
            originalMessageParts.remove(0);
            String itemTarget = Joiner.on(" ").join(originalMessageParts);
            List<Item> inventory = player.getInventory();
            if (inventory != null) {
                for (Item item : inventory) {
                    if (item.getItemTriggers().contains(itemTarget)) {
                        if (item.getEquipment() == null) {
                            write("Item is not equipable.");
                            return;
                        }
                        player.equip(item);
                        return;
                    }
                }
            } else {
                write("Your inventory is empty.");
            }
        });
    }
}
