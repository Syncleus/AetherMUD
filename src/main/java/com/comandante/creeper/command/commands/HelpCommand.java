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


import com.comandante.creeper.configuration.ConfigureCommands;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HelpCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("help", "h");
    final static String description = "The help command.";
    final static String correctUsage = "help";


    public HelpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            StringBuilder sb = new StringBuilder();
            Table t = new Table(2, BorderStyle.CLASSIC_COMPATIBLE,
                    ShownBorders.HEADER_FIRST_AND_LAST_COLLUMN);
            t.setColumnWidth(0, 10, 30);
            t.setColumnWidth(1, 30, 55);
            t.addCell("commands");
            t.addCell("description");
            Set<Command> creeperCommands = ConfigureCommands.creeperCommandRegistry.getCreeperCommands();
            for (Command command : creeperCommands) {
                Joiner.on(" ").join(validTriggers);
                if (command.roles != null) {
                    if (command.roles.contains(PlayerRole.ADMIN)) {
                        if (!playerManager.hasRole(player, PlayerRole.ADMIN)) {
                            continue;
                        }
                    }
                }
                t.addCell(command.correctUsage);
                t.addCell(command.description);
            }
            sb.append(t.render());
            sb.append("\r\n");
            write(sb.toString());
        });
    }
}
