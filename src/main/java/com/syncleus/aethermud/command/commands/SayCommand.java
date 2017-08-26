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

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.syncleus.aethermud.server.communication.Color.RED;
import static com.syncleus.aethermud.server.communication.Color.RESET;

public class SayCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("say");
    final static String description = "Say something to the current room.";
    final static String correctUsage = "say <message>";

    public SayCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            originalMessageParts.remove(0);
            String message = Joiner.on(" ").join(originalMessageParts);
            Set<Player> presentPlayers = currentRoom.getPresentPlayers();
            for (Player presentPlayer : presentPlayers) {
                StringBuilder sb = new StringBuilder();
                sb.append(RED);
                sb.append("<").append(player.getPlayerName()).append("> ").append(message);
                sb.append(RESET);
                if (presentPlayer.getPlayerId().equals(playerId)) {
                    write(sb.toString());
                } else {
                    channelUtils.write(presentPlayer.getPlayerId(), sb.append("\r\n").toString(), true);
                }
            }
            if (gameManager.getAetherMudConfiguration().isIrcEnabled && (Objects.equals(gameManager.getAetherMudConfiguration().ircBridgeRoomId, currentRoom.getRoomId()))) {
                if (gameManager.getIrcBotService().getBot().isConnected()) {
                    gameManager.getIrcBotService().getBot().getUserChannelDao().getChannel(gameManager.getAetherMudConfiguration().ircChannel).send().message(player.getPlayerName() + ": " + message);
                }
            }
        });
    }
}
