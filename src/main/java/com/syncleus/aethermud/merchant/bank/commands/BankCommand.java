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
package com.syncleus.aethermud.merchant.bank.commands;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.server.model.CreeperSession;
import com.syncleus.aethermud.server.communication.ChannelCommunicationUtils;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.world.model.Room;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankCommand extends SimpleChannelUpstreamHandler {

    public final List<String> validTriggers;
    public final GameManager gameManager;
    public final PlayerManager playerManager;
    public final ChannelCommunicationUtils channelUtils;
    public CreeperSession creeperSession;
    public Player player;
    public String playerId;
    public Room currentRoom;
    public List<String> originalMessageParts;
    public String rootCommand;
    public String description;

    public BankCommand(GameManager gameManager, List<String> validTriggers, String description) {
        this.gameManager = gameManager;
        this.playerManager = gameManager.getPlayerManager();
        this.channelUtils = gameManager.getChannelUtils();
        this.validTriggers = validTriggers;
        this.description = description;

    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            CreeperSession creeperSession = extractCreeperSession(e.getChannel());
            e.getChannel().getPipeline().remove("executed_command");
            e.getChannel().getPipeline().remove("executed_bank_command");
            gameManager.getChannelUtils().write(playerId, BankCommand.getPrompt(), true);
            if (creeperSession.getGrabMerchant().isPresent()) {
                return;
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    public void configure(MessageEvent e) {
        this.creeperSession = extractCreeperSession(e.getChannel());
        this.player = playerManager.getPlayer(extractPlayerId(creeperSession));
        this.playerId = player.getPlayerId();
        this.currentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(player).get();
        this.originalMessageParts = getOriginalMessageParts(e);
        rootCommand = getRootCommand(e);
    }

    public CreeperSession extractCreeperSession(Channel channel) {
        return (CreeperSession) channel.getAttachment();
    }


    public String extractPlayerId(CreeperSession creeperSession) {
        return Main.createPlayerId(creeperSession.getUsername().get());
    }

    public String getRootCommand(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        return origMessage.split(" ")[0].toLowerCase();
    }

    public List<String> getOriginalMessageParts(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        return new ArrayList<>(Arrays.asList(origMessage.split(" ")));
    }

    public void write(String msg) {
        channelUtils.write(playerId, msg);
    }

    public static String getPrompt() {
        return "[" + Color.GREEN + "BANK" + Color.RESET + " - WITHDRAWAL | DEPOSIT | QUERY | DONE] ";
    }

    public <T> T createObj(String nameclass) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<T> clazz = (Class<T>) Class.forName(nameclass);

        // assumes the target class has a no-args Constructor
        return clazz.getConstructor(GameManager.class).newInstance(gameManager);
    }

}
