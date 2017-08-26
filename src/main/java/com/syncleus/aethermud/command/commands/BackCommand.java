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
import com.syncleus.aethermud.player.PlayerMovement;
import com.syncleus.aethermud.world.model.Room;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BackCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("back", "b");
    final static String description = "Return to where you came from.";
    final static String correctUsage = "back";

    public BackCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (player.isActiveFights()) {
                write("You can't move while in a fight!");
                return;
            }
            Optional<Room> returnRoom = player.getPreviousRoom();
            if (!returnRoom.isPresent()) {
                write("I don't know where you came from.");
                return;
            }
            PlayerMovement playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), returnRoom.get().getRoomId(), "returned to where they came from.", "N/A");
            player.movePlayer(playerMovement);
        });
    }

}
