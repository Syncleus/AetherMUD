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
import com.comandante.creeper.core_game.RecentChangesManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecentChangesCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("recentchanges");
    final static String description = "Print the recent changes to the creeper codebase.";
    final static String correctUsage = "recentchanges";

    public RecentChangesCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandBackgroundThread(ctx, e, () -> {
            try {
                write(RecentChangesManager.getRecentChanges());
            } catch (ExecutionException ex) {
                log.error("Unable to retrieve recent changes.", ex);
            }
        });
    }
}
