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
package com.comandante.creeper.command.commands;


import com.comandante.creeper.core_game.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class NexusCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("nexus", "nx");
    final static String description = "Say something in #nexus";
    final static String correctUsage = "nexus <words>";

    public NexusCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description,correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (gameManager.getCreeperConfiguration().isIrcEnabled) {
                originalMessageParts.remove(0);
                String transferPhrase = Joiner.on(" ").join(originalMessageParts);
                if (gameManager.getIrcBotService().getBot().isConnected()) {
                    gameManager.getIrcBotService().getBot().getUserChannelDao().getChannel(gameManager.getCreeperConfiguration().ircChannel).send().message(player.getPlayerName() + ": " + transferPhrase);
                }
            }
        });
    }
}