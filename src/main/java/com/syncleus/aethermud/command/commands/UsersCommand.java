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

import com.syncleus.aethermud.common.FriendlyTime;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class UsersCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("users");
    final static String description = "Display extended inforation about who is logged in.";
    final static String correctUsage = "users";

    public UsersCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, null);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Table t = new Table(4, BorderStyle.BLANKS,
                    ShownBorders.NONE);
            t.setColumnWidth(0, 14, 24);
            t.setColumnWidth(1, 18, 18);
            t.setColumnWidth(2, 21, 21);
            t.addCell("Player");
            t.addCell("IP");
            t.addCell("Logged in since");
            t.addCell("Idle");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());

                InetSocketAddress remoteAddress = (InetSocketAddress) allPlayer.getChannel().getRemoteAddress();
                String remoteUsersHost = remoteAddress.getHostString();
                t.addCell(remoteUsersHost);

                SimpleDateFormat loggedInFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String loginTime = loggedInFormat.format(new Date(playerManager.getSessionManager().getSession(allPlayer.getPlayerId()).getInitialLoginTime()));
                t.addCell(loginTime);

                long lastActivity = playerManager.getSessionManager().getSession(allPlayer.getPlayerId()).getLastActivity();
                String idleTime = getFriendlyTime(new Date(lastActivity));
                t.addCell(idleTime);
            }
            write(t.render());
        });
    }


    public static String getFriendlyTime(Date dateTime) {
        Date current = Calendar.getInstance().getTime();
        long seconds = (current.getTime() - dateTime.getTime()) / 1000;

        return new FriendlyTime(seconds).getFriendlyFormattedShort();
    }
}
