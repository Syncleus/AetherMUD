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
package com.syncleus.aethermud.merchant.playerclass_selector;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.common.AetherMudUtils;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerClass;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.server.model.AetherMudSession;
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

public class PlayerClassCommand extends SimpleChannelUpstreamHandler {

    public final GameManager gameManager;
    public final PlayerManager playerManager;
    public final ChannelCommunicationUtils channelUtils;
    public AetherMudSession aetherMudSession;
    public Player player;
    public String playerId;
    public Room currentRoom;
    public List<String> originalMessageParts;
    public String rootCommand;
    public String description;

    public PlayerClassCommand(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerManager = gameManager.getPlayerManager();
        this.channelUtils = gameManager.getChannelUtils();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            AetherMudSession aetherMudSession = extractAetherMudSession(e.getChannel());
            e.getChannel().getPipeline().remove("executed_command");
            e.getChannel().getPipeline().remove("executed_playerclass_command");
            gameManager.getChannelUtils().write(playerId, PlayerClassCommand.getPrompt(), true);
            if (aetherMudSession.getGrabMerchant().isPresent()) {
                return;
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    public void configure(MessageEvent e) {
        this.aetherMudSession = extractAetherMudSession(e.getChannel());
        this.player = playerManager.getPlayer(extractPlayerId(aetherMudSession));
        this.playerId = player.getPlayerId();
        this.currentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(player).get();
        this.originalMessageParts = getOriginalMessageParts(e);
        rootCommand = getRootCommand(e);
    }

    public AetherMudSession extractAetherMudSession(Channel channel) {
        return (AetherMudSession) channel.getAttachment();
    }


    public String extractPlayerId(AetherMudSession aetherMudSession) {
        return Main.createPlayerId(aetherMudSession.getUsername().get());
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
        StringBuilder sb = new StringBuilder();
        sb.append("Choose from one of the following classes.  Make your choice wisely, as you won't be able to change your mind.").append("\r\n").append("\r\n");
        Arrays.stream(PlayerClass.values()).forEach(playerClass -> sb.append(AetherMudUtils.capitalize(playerClass.getIdentifier())).append(" - ").append(playerClass.getDescription()).append("\r\n"));
        sb.append("\r\n");
        sb.append("[").append(Color.GREEN).append("Enter a desired player class or \"leave\" to choose later").append(Color.RESET).append("] ");
        return sb.toString();
    }

    public <T> T createObj(String nameclass) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<T> clazz = (Class<T>) Class.forName(nameclass);

        // assumes the target class has a no-args Constructor
        return clazz.getConstructor(GameManager.class).newInstance(gameManager);
    }

}
