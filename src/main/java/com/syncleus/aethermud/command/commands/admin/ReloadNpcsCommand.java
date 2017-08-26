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
import com.syncleus.aethermud.configuration.ConfigureNpc;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ReloadNpcsCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("reloadnpcs");
    final static String description = "Reload npcs from disk.";
    final static String correctUsage = "reloadnpcs";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public ReloadNpcsCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandThreadSafe(ctx, e, ReloadNpcsCommand.class, () -> {
            gameManager.removeAllNpcs();
            try {
                ConfigureNpc.configureAllNpcs(gameManager);
            } catch (IOException ex) {
                log.error("Unable to configure NPCS from disk.");
            }
        });
    }
}
