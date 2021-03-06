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
package com.syncleus.aethermud.bot;

import com.syncleus.aethermud.bot.command.commands.BotCommand;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.*;

import static com.syncleus.aethermud.server.communication.Color.MAGENTA;
import static com.syncleus.aethermud.server.communication.Color.RED;
import static com.syncleus.aethermud.server.communication.Color.RESET;

public class MyListener extends ListenerAdapter {

    private final GameManager gameManager;
    private final Integer bridgeRoomId;

    public MyListener(GameManager gameManager, Integer bridgeRoomId) {
        this.gameManager = gameManager;
        this.bridgeRoomId = bridgeRoomId;
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        PlayerManager playerManager = gameManager.getPlayerManager();

        try {
            if (!(event instanceof MessageEvent)) {
                return;
            }
            if (event.getMessage().startsWith("!!")) {
                ArrayList<String> originalMessageParts = Lists.newArrayList(Arrays.asList(event.getMessage().split("!!")));
                originalMessageParts.remove(0);
                final String msg = Joiner.on(" ").join(originalMessageParts);
                BotCommand command = gameManager.getBotCommandFactory().getCommand((MessageEvent) event, msg);
                List<String> response = command.process();
                for (String line: response) {
                    gameManager.getIrcBotService().getBot().getUserChannelDao().getChannel(gameManager.getAetherMudConfiguration().ircChannel).send().message(line);
                }
            }

            if (event.getMessage().startsWith("?gossip")) {
                ArrayList<String> originalMessageParts = Lists.newArrayList(Arrays.asList(event.getMessage().split(" ")));
                originalMessageParts.remove(0);
                final String msg = Joiner.on(" ").join(originalMessageParts);
                Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
                while (players.hasNext()) {
                    final Player next = players.next().getValue();
                    final String gossipMessage = new StringBuilder()
                            .append(MAGENTA).append("[")
                            .append(event.getUser().getNick()).append("-irc").append("] ")
                            .append(msg).append(RESET)
                            .toString();
                    gameManager.getChannelUtils().write(next.getPlayerId(), gossipMessage + "\r\n", true);
                }
                return;
            }
            Room bridgeRoom = gameManager.getRoomManager().getRoom(bridgeRoomId);
            Set<Player> presentPlayers = bridgeRoom.getPresentPlayers();
            for (Player presentPlayer : presentPlayers) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(RED);
                stringBuilder.append("<").append(event.getUser().getNick()).append("> ").append(event.getMessage());
                stringBuilder.append(RESET);
                gameManager.getChannelUtils().write(presentPlayer.getPlayerId(), stringBuilder.append("\r\n").toString(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SentryManager.logSentry(this.getClass(), e, "IRC Listener Exception!");
        }
    }
}


