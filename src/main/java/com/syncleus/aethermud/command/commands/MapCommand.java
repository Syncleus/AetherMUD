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
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.world.model.Coords;
import com.syncleus.aethermud.world.model.Room;
import org.apache.commons.lang.math.NumberUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class MapCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("map", "m");
    final static String description = "Display the map.";
    final static String correctUsage = "map <size number>";


    public MapCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() > 1 && NumberUtils.isNumber(originalMessageParts.get(1))) {
                int max = Integer.parseInt(originalMessageParts.get(1));
                write(mapsManager.drawMap(currentRoom.getRoomId(), new Coords(max, max)));
            } else {
                Player player1 = playerManager.getPlayer(playerId);
                final Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player1).get();
                StringBuilder sb = new StringBuilder();

                if (playerCurrentRoom.getMapData().isPresent()) {
                    sb.append(playerCurrentRoom.getMapData().get()).append("\r\n");
                    channelUtils.write(player1.getPlayerId(), sb.toString());
                } else {
                    channelUtils.write(player1.getPlayerId(), "You are in an uncharted location.");
                }
            }
        });
    }
}

