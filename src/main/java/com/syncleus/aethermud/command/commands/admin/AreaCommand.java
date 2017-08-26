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
package com.syncleus.aethermud.command.commands.admin;

import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.world.model.Area;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class AreaCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("area", "a");
    final static String description = "Alter area settings for the current room.";
    final static String correctUsage = "area <area name>,<area name>";
    final static Set<PlayerRole> roles = Sets.newHashSet();

    public AreaCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() == 1) {
                Set<Area> areas = currentRoom.getAreas();
                for (Area area : areas) {
                    write(area.getName() + "\r\n");
                }
                return;
            }
            String s = originalMessageParts.get(1);
            List<String> strings = Arrays.asList(s.split(","));
            Set<Area> newAreas = Sets.newConcurrentHashSet();
            for (String string : strings) {
                String trim = string.trim();
                Area byName = Area.getByName(trim);
                if (byName != null) {
                    newAreas.add(byName);
                    write("added area: " + trim + "\r\n");
                    currentRoom.setAreas(newAreas);
                } else {
                    write(byName + " is not a known area in the code base.");
                }
            }
        });
    }
}
