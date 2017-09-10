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
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.aethermud.player.PlayerMovement;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class RecallCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("recall");
    final static String description = "Return to the lobby, once every 5 minutes.";
    final static String correctUsage = "back";

    public RecallCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (player.isActive(CoolDownType.DETAINMENT)) {
                write("You can not recall while being detained.");
                return;
            }
            if (player.isActiveFights()) {
                write("You can not recall while in a fight.");
                return;
            }
            if (player.isActive(CoolDownType.PLAYER_RECALL)) {
                write("You can not recall right now.");
                return;
            }
            PlayerMovement playerMovement = new PlayerMovement(player, player.getCurrentRoom().getRoomId(), GameManager.LOBBY_ID, "vanished into the ether.", "");
            player.addCoolDown(CoolDownType.PLAYER_RECALL);
            player.movePlayer(playerMovement);
        });
    }

}
