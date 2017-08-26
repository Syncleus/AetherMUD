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
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KillTallyCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("killtally", "kt", "tally");
    final static String description = "View your kill tally.";
    final static String correctUsage = "tally";

    public KillTallyCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        this.execCommand(ctx, e, () -> {
            Table table = new Table(2, BorderStyle.CLASSIC_COMPATIBLE, ShownBorders.HEADER_ONLY);
            table.setColumnWidth(0, 22, 30);
            table.setColumnWidth(1, 10, 20);
            table.addCell("Npc");
            table.addCell("# Killed");
            Map<String, Long> npcKillLog = player.getNpcKillLog();
            npcKillLog.forEach((s, aLong) -> {
                table.addCell(s);
                table.addCell(String.valueOf(aLong));
            });
            write(table.render());
        });
    }
}
