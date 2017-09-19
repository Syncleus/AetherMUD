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

import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class DropCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("drop", "d");
    final static String description = "Drop an item.";
    final static String correctUsage = "drop <item name>";

    public DropCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() == 1) {
                write("No item specified.");
                return;
            }
            originalMessageParts.remove(0);
            String itemTarget = Joiner.on(" ").join(originalMessageParts);
            for (ItemInstance item : player.getInventory()) {
                if (item.getItemTriggers().contains(itemTarget)) {
                    item.setWithPlayer(false);
                    gameManager.placeItemInRoom(currentRoom.getRoomId(), item.getItemId());
                    player.removeInventoryId(item.getItemId());
                    gameManager.getItemDecayManager().addItem(item);
                    entityManager.saveItem(item);
                    gameManager.roomSay(currentRoom.getRoomId(), player.getPlayerName() + " dropped " + item.getItemName(), playerId);
                    return;
                }
            }
        });
    }
}
