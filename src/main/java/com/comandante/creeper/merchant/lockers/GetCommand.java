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
package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.items.Item;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class GetCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("get", "g");
    final static String description = "Get an item from your locker.t";

    public GetCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            originalMessageParts.remove(0);
            String desiredRetrieveOption = Joiner.on(" ").join(originalMessageParts);
            Optional<PlayerMetadata> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerMetadata playerMetadata = playerMetadataOptional.get();
            for (String entityId: playerMetadata.getLockerInventory()) {
                Optional<Item> itemEntityOptional = gameManager.getEntityManager().getItemEntity(entityId);
                if (!itemEntityOptional.isPresent()) {
                    continue;
                }
                Item itemEntity = itemEntityOptional.get();
                if (itemEntity.getItemTriggers().contains(desiredRetrieveOption)) {
                    player.transferItemFromLocker(entityId);
                    write(itemEntity.getItemName() + " retrieved from locker.\r\n");
                    return;
                }
            }
            write("Item not found in locker.\r\n");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}