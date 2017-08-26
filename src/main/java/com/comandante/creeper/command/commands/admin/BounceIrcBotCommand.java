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
package com.comandante.creeper.command.commands.admin;

import com.comandante.creeper.command.commands.Command;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.pircbotx.MultiBotManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BounceIrcBotCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("bounce");
    final static String description = "Restart IRC Bot.";
    final static String correctUsage = "bounce";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public BounceIrcBotCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandThreadSafe(ctx, e, BounceIrcBotCommand.class, () -> {
            try {
                MultiBotManager manager = gameManager.getIrcBotService().getManager();
                write("IRC Bot Service shutting down.\r\n");
                manager.stopAndWait();
                write("IRC Bot Service stopped.\r\n");
                MultiBotManager multiBotManager = gameManager.getIrcBotService().newBot();
                multiBotManager.start();
                gameManager.getIrcBotService().setManager(multiBotManager);
                write("IRC Bot Service started.\r\n");
            } catch (Exception ex) {
                log.error("Unable to restart IRC service", ex);
            }
        });
    }
}