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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TagRoomCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("tagRoom", "tr");
    final static String description = "Sets a tag on a room.";
    final static String correctUsage = "tag <tag> | tag list";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public TagRoomCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            originalMessageParts.remove(0);
            if (originalMessageParts.get(0).equalsIgnoreCase("list")) {
                StringBuilder sb = new StringBuilder();
                for (String tag : currentRoom.getRoomTags()) {
                    sb.append(tag).append("\n");
                }
                write("tag\n---");
                write(sb.toString());
                return;
            }
            currentRoom.addTag(originalMessageParts.get(0));
            write(String.format("tagged world with tag: \"%s\".", originalMessageParts.get(0)));
        });
    }
}
