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

import com.syncleus.aethermud.core.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;


public class RecentGossipCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("recentgossip", "rg");
    final static String description = "Replay recent gossip.";
    final static String correctUsage = "recentgossip 30";

    public RecentGossipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            List<String> recent = null;
            if (originalMessageParts.size() > 1) {
                String size = originalMessageParts.get(1);
                int i;
                try {
                    i = Integer.parseInt(size);
                } catch (NumberFormatException ne) {
                    return;
                }
                recent = gameManager.getGossipCache().getRecent(i);
            } else {
                recent = gameManager.getGossipCache().getRecent(25);
            }
            for (String line : recent) {
                write(line + "\r\n");
            }
        });
    }
}
