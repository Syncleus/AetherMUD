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
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.aethermud.world.model.Room;
import com.google.api.client.util.Maps;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class NpcLocationCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("npclocation", "nl");
    final static String description = "Displays location about current NPCS.";
    final static String correctUsage = "npclocation | nl";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public NpcLocationCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Map<String, Set<Room>> npcMap = Maps.newTreeMap();
            Iterator<Map.Entry<String, Npc>> iterator = entityManager.getNpcs().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Npc> next = iterator.next();
                Npc npc = next.getValue();
                if (npcMap.get(npc.getColorName()) == null) {
                    npcMap.put(npc.getColorName(), Sets.newHashSet(npc.getCurrentRoom()));
                } else {
                    npcMap.get(npc.getColorName()).add(npc.getCurrentRoom());
                }
            }
            Iterator<Map.Entry<String, Set<Room>>> iterator1 = npcMap.entrySet().iterator();
            StringBuilder resp = new StringBuilder();
            while (iterator1.hasNext()) {
                Map.Entry<String, Set<Room>> next = iterator1.next();
                resp.append(next.getKey()).append("\r\n");
                for (Room room : next.getValue()) {
                    if (room != null) {
                        String areas = "";
                        for (Area area : room.getAreas()) {
                            areas = areas + "," + area.getName();
                        }
                        areas = areas.startsWith(",") ? areas.substring(1) : areas;
                        resp.append("    ").append(Color.GREEN + "room name: " + Color.RESET + room.getRoomTitle()).append(" - "+Color.GREEN + "room area: " + Color.RESET + "(").append(areas).append(") - ").append(room.getRoomId());
                    } else {
                        resp.append("    NULL ROOM");
                    }
                    resp.append("\r\n");
                }
            }
            write(resp.toString());
        });
    }
}
