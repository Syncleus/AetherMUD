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
package com.syncleus.aethermud.merchant.lockers;

import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class PutCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("put", "p");
    final static String description = "Put an item in your locker.";

    public PutCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            originalMessageParts.remove(0);
            String desiredDropOffItem = Joiner.on(" ").join(originalMessageParts);
            for (ItemInstance item : player.getInventory()) {
                if (item.getItemTriggers().contains(desiredDropOffItem)) {
                    item.setWithPlayer(false);
                    player.transferItemToLocker(item.getItemId());
                    gameManager.getEntityManager().saveItem(item);
                    write(item.getItemName() + " has been transfered to your locker.\r\n");
                    return;
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
