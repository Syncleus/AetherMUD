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
package com.syncleus.aethermud.command.commands;


import com.syncleus.aethermud.bot.command.commands.BotCommand;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.syncleus.aethermud.server.communication.Color.*;

public class GossipCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("gossip", "g");
    final static String description = "Sends a message to the entire MUD.";
    final static String correctUsage = "gossip <message>";

    public GossipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (!player.isChatModeOn()) {
                if (originalMessageParts.size() == 1) {
                    write("Nothing to gossip about?");
                    return;
                }
                originalMessageParts.remove(0);
            }
            String msg = Joiner.on(" ").join(originalMessageParts);
            try {
                if (msg.startsWith("!!")) {
                    String botCommandOutput = getBotCommandOutput(msg);
                    msg = msg + "\r\n" + botCommandOutput;
                }
            } catch (Exception ex) {
                log.error("Problem executing bot command from gossip channel!", ex);
            }
            String gossipMessage = WHITE + "[" + RESET + MAGENTA + this.player.getPlayerName() + WHITE + "] " + RESET + CYAN + msg + RESET;
            playerManager.getAllPlayersMap().forEach((s, destinationPlayer) -> {
                if (destinationPlayer.getPlayerId().equals(playerId)) {
                    write(gossipMessage);
                } else {
                    channelUtils.write(destinationPlayer.getPlayerId(), gossipMessage + "\r\n", true);
                }
            });
            gameManager.getGossipCache().addGossipLine(gossipMessage);
        });
    }

    private String getBotCommandOutput(String cmd) {
        ArrayList<String> originalMessageParts = Lists.newArrayList(Arrays.asList(cmd.split("!!")));
        originalMessageParts.remove(0);
        final String msg = Joiner.on(" ").join(originalMessageParts);
        BotCommand command = gameManager.getBotCommandFactory().getCommand(null, msg);
        if (command != null) {
            List<String> process = command.process();
            StringBuilder sb = new StringBuilder();
            for (String line : process) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
