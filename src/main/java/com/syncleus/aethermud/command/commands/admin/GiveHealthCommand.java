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
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerRole;
import com.google.common.collect.Sets;
import org.apache.commons.lang.math.NumberUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GiveHealthCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("givehealth");
    final static String description = "Give Health to a Player";
    final static String correctUsage = "givehealth <player name> <amt>";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);


    public GiveHealthCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (!player.getPlayerName().equals("fibs")) {
                write("This attempt to cheat has been logged.");
                return;
            }
            if (originalMessageParts.size() > 2) {
                String destinationPlayerName = originalMessageParts.get(1);
                String amt = originalMessageParts.get(2);
                if (!NumberUtils.isNumber(amt)) {
                    write("Third option to givehealth needs to be an integer amount.");
                    return;
                }
                Player playerByUsername = gameManager.getPlayerManager().getPlayerByUsername(destinationPlayerName);
                if (playerByUsername == null) {
                    write("Player does not exist.");
                    return;
                }
                playerByUsername.incrementGold(Integer.parseInt(amt));
                write("The amount of " + amt + " gold has been placed into " + destinationPlayerName + "'s inventory.");
            }
        });
    }
}
