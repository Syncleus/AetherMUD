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
package com.syncleus.aethermud.server.communication;

import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.world.RoomManager;

public class ChannelUtils implements ChannelCommunicationUtils {

    private final PlayerManager playerManager;
    private final RoomManager roomManager;

    public ChannelUtils(PlayerManager playerManager, RoomManager roomManager) {
        this.playerManager = playerManager;
        this.roomManager = roomManager;
    }

    public void write(String playerId, String message) {
        write(playerId, message, false);
    }

    public void write(String playerId, String message, boolean leadingBlankLine) {
        if (playerManager.getSessionManager().getSession(playerId).getGrabMultiLineInput().isPresent()) {
            return;
        }
        Player player = playerManager.getPlayer(playerId);
        String lastMessage = playerManager.getSessionManager().getSession(playerId).getLastMessage();
        StringBuilder sb = new StringBuilder();
        if (lastMessage != null && (lastMessage.length() >= "\r\n".length())) {
            if (!lastMessage.substring(lastMessage.length() - 2).equals("\r\n")) {
                if (leadingBlankLine) {
                    sb.append("\r\n");
                }
            }
        }
        sb.append(message);
        playerManager.getSessionManager().getSession(playerId).setLastMessage(sb.toString());
        player.getChannel().write(sb.toString());
    }
}

