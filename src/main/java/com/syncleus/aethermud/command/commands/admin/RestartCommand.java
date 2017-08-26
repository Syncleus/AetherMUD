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
package com.syncleus.aethermud.command.commands.admin;

import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RestartCommand extends Command {



    final static List<String> validTriggers = Arrays.asList("restart");
    final static String description = "restart server.";
    final static String correctUsage = "restart";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.GOD);

    public RestartCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandThreadSafe(ctx, e, BounceIrcBotCommand.class, () -> {
            playerManager.getAllPlayersMap().values().stream()
                    .filter(player -> player.getChannel().isConnected())
                    .forEach(player -> gameManager.getChannelUtils().write(player.getPlayerId(),
                            "                      88                                          \n" +
                    "                      88                                   ,d     \n" +
                    "                      88                                   88     \n" +
                    "8b,dPPYba,  ,adPPYba, 88,dPPYba,   ,adPPYba,   ,adPPYba, MM88MMM  \n" +
                    "88P'   \"Y8 a8P_____88 88P'    \"8a a8\"     \"8a a8\"     \"8a  88     \n" +
                    "88         8PP\"\"\"\"\"\"\" 88       d8 8b       d8 8b       d8  88     \n" +
                    "88         \"8b,   ,aa 88b,   ,a8\" \"8a,   ,a8\" \"8a,   ,a8\"  88,    \n" +
                    "88          `\"Ybbd8\"' 8Y\"Ybbd8\"'   `\"YbbdP\"'   `\"YbbdP\"'   \"Y888  \n" +
                    "                                                                  "));
            gameManager.getMapDBCreeperStorage().stopAsync();
            gameManager.getMapDBCreeperStorage().awaitTerminated();
            System.exit(0);
        });
    }
}
