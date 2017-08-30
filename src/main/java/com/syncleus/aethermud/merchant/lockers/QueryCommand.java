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
package com.syncleus.aethermud.merchant.lockers;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.storage.graphdb.PlayerData;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class QueryCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("query", "q");
    final static String description = "List your items.";

    public QueryCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            write("----LOCKER ITEMS\r\n");
            for (String rolledUpInvLine: player.getRolledUpLockerInventory()) {
                write(rolledUpInvLine);;
            }
            write("\r\n\r\n----PERSONAL INVENTORY\r\n");
            for (String rolledUpInvLine: player.getRolledUpIntentory()) {
                write(rolledUpInvLine);
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
