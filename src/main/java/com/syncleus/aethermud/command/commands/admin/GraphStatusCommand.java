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
package com.syncleus.aethermud.command.commands.admin;

import com.google.common.collect.Sets;
import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.GraphInfo;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GraphStatusCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("graphstatus");
    final static String description = "Display some statistics on the GraphDB for debugging.";
    final static String correctUsage = "graphstatus";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public GraphStatusCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {

            try (GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction()) {
                AetherMudStorage storage = tx.getStorage();
                GraphInfo info = storage.getGraphInfo();

                write("Number of nodes: " + info.getNumberOfNodes() + "\r\n");
                write("Number of items: " + info.getNumberOfItems() + "\r\n");
                write("Number of item instances: " + info.getNumberOfItemInstances() + "\r\n");
                write("Number of item internal names:\r\n");
                for(String internalName : info.getItemInternalNames())
                    write(internalName + "\r\n");
            }

        });
    }

}



