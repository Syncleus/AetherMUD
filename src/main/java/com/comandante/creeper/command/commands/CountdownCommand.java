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
import com.comandante.creeper.server.player_communication.Color;
import com.google.common.collect.Lists;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountdownCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("countdown", "??");
    final static String description = "a countdown.";
    final static String correctUsage = "?? countdown";

    public CountdownCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandBackgroundThread(ctx, e, () -> {
            ArrayList<String> countDownMessages =
                    Lists.newArrayList("... ***** COUNTDOWN ***** ...",
                            ".             5             .",
                            ".             4             .",
                            ".             3             .",
                            ".             2             .",
                            ".             1             .",
                            "... *****   SMOKE!  ***** ...");


            countDownMessages.forEach(message -> {
                writeMessageToEveryPlayer(message);
                try {
                    Thread.sleep(900);
                } catch (InterruptedException ex) {
                    log.error("Problem while printing countdown message", ex);
                }
            });
        });
    }

    private void writeMessageToEveryPlayer(String message) {
        playerManager.getAllPlayersMap().forEach((playerId1, player1) -> channelUtils.write(playerId1, Color.BOLD_ON + Color.GREEN + message + Color.RESET + "\r\n", true));
    }
}
