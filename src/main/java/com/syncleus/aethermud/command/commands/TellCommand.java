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
import com.syncleus.aethermud.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

import static com.syncleus.aethermud.server.communication.Color.RESET;
import static com.syncleus.aethermud.server.communication.Color.YELLOW;

public class TellCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("tell", "t");
    final static String description = "Send a private message to a player.";
    final static String correctUsage = "tell <player name> <message>";

    public TellCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() < 3) {
                write("tell failed, no message to send.");
                return;
            }
            //remove the literal 'tell'
            originalMessageParts.remove(0);
            String destinationUsername = originalMessageParts.get(0);
            Player desintationPlayer = playerManager.getPlayerByUsername(destinationUsername);
            if (desintationPlayer == null) {
                write("tell failed, unknown user.");
                return;
            }
            if (desintationPlayer.getPlayerId().equals(playerId)) {
                write("tell failed, you're talking to yourself.");
                return;
            }
            originalMessageParts.remove(0);
            String tellMessage = Joiner.on(" ").join(originalMessageParts);
            StringBuilder stringBuilder = new StringBuilder();
            String destinationPlayercolor = YELLOW;
            stringBuilder.append("*").append(player.getPlayerName()).append("* ");
            stringBuilder.append(tellMessage);
            stringBuilder.append(RESET);
            channelUtils.write(desintationPlayer.getPlayerId(), destinationPlayercolor + stringBuilder.append("\r\n").toString(), true);
            write(stringBuilder.toString());
        });
    }
}
