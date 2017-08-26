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
import java.util.Optional;
import java.util.Set;

public class PickUpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("pickup", "pick", "p");
    final static String description = "Pick up an item.";
    final static String correctUsage = "pickup <item name>";

    public PickUpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description,correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Set<String> itemIds = currentRoom.getItemIds();
            originalMessageParts.remove(0);
            String desiredPickUpItem = Joiner.on(" ").join(originalMessageParts);
            for (String next : itemIds) {
                Optional<Item> itemEntityOptional = entityManager.getItemEntity(next);
                if (!itemEntityOptional.isPresent()) {
                    continue;
                }
                if (itemEntityOptional.get().getItemTriggers().contains(desiredPickUpItem)) {
                    if (gameManager.acquireItemFromRoom(player, next)) {
                        String playerName = player.getPlayerName();
                        gameManager.roomSay(currentRoom.getRoomId(), playerName + " picked up " + itemEntityOptional.get().getItemName(), playerId);
                        return;
                    } else {
                        return;
                    }
                }
            }
        });
    }
}
