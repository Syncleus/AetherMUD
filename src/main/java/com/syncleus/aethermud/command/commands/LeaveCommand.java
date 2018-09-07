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
import com.syncleus.aethermud.player.PlayerMovement;
import com.syncleus.aethermud.world.model.RemoteExit;
import com.syncleus.aethermud.world.model.Room;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class LeaveCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("leave");
    final static String description = "Enters a Leave exit";
    final static String correctUsage = "leave";

    public LeaveCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            PlayerMovement playerMovement;
            List<RemoteExit> leave = currentRoom.getEnterExits().stream().filter(remoteExit -> remoteExit.getExitDetail().equalsIgnoreCase("leave")).collect(Collectors.toList());
            if (leave.size() > 0) {
                Room destinationRoom = roomManager.getRoom(leave.get(0).getRoomId());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "entered " + leave.get(0).getExitDetail() + ".", "N/A");
                player.movePlayer(playerMovement);
                return;
            }
            write("There is no Leave exit." + "\r\n");
        });
    }
}
