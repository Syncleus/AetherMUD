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


import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.core.GameManager;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

import static com.syncleus.aethermud.server.communication.Color.RESET;

public class InventoryCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("inventory", "i");
    final static String description = "View your inventory.";
    final static String correctUsage = "inventory";

    public InventoryCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        this.execCommand(ctx, e, () -> {
            List<Item> inventory = player.getInventory();
            if (inventory == null) {
                write("You aren't carrying anything.");
                return;
            }
            StringBuilder inventoryString = new StringBuilder();
            inventoryString.append("You are carrying:\r\n");
            inventoryString.append(RESET);
            String join = StringUtils.join(player.getRolledUpIntentory().toArray(), "\r\n");
            inventoryString.append(join);
            write(inventoryString.toString());
        });
    }
}
