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
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;


public class SpellsCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("spells");
    final static String description = "Lists the spells that a player has learned.";
    final static String correctUsage = "show <item_name>";

    public SpellsCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            StringBuilder sb = new StringBuilder();
            List<String> learnedSpells = player.getLearnedSpells();
            if (learnedSpells.size() == 0) {
                write("You haven't learned any spells." + "\r\n");
                return;
            }
            learnedSpells.forEach(s -> sb.append(s).append("\r\n"));
            write(sb.append("\r\n").toString());
        });
    }
}