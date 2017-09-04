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

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.LootManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.server.model.AetherMudSession;
import com.syncleus.aethermud.server.communication.ChannelCommunicationUtils;
import com.syncleus.aethermud.storage.WorldStorage;
import com.syncleus.aethermud.world.FloorManager;
import com.syncleus.aethermud.world.MapMatrix;
import com.syncleus.aethermud.world.MapsManager;
import com.syncleus.aethermud.world.RoomManager;
import com.syncleus.aethermud.world.model.Coords;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Command extends SimpleChannelUpstreamHandler {

    public static final Logger log = Logger.getLogger(Command.class);
    public final List<String> validTriggers;
    public final String description;
    public final Set<PlayerRole> roles;
    public final GameManager gameManager;
    public final FloorManager floorManager;
    public final MapsManager mapsManager;
    public final EntityManager entityManager;
    public final RoomManager roomManager;
    public final PlayerManager playerManager;
    public final ChannelCommunicationUtils channelUtils;
    public final LootManager lootManager;
    public final String correctUsage;
    public AetherMudSession aetherMudSession;
    public Player player;
    public Room currentRoom;
    public String playerId;
    public Optional<MapMatrix> mapMatrix = Optional.empty();
    public Optional<Coords> currentRoomCoords = Optional.empty();
    public List<String> originalMessageParts;
    public WorldStorage worldExporter;
    public String rootCommand;

    protected Command(GameManager gameManager, List<String> validTriggers, String description, String correctUsage) {
        this(gameManager, validTriggers, description, correctUsage, Sets.<PlayerRole>newHashSet());
    }

    protected Command(GameManager gameManager, List<String> validTriggers, String description, String correctUsage, Set<PlayerRole> roles) {
        this.gameManager = gameManager;
        this.validTriggers = validTriggers;
        this.description = description;
        this.correctUsage = correctUsage;
        this.floorManager = gameManager.getFloorManager();
        this.mapsManager = gameManager.getMapsManager();
        this.roomManager = gameManager.getRoomManager();
        this.entityManager = gameManager.getEntityManager();
        this.playerManager = gameManager.getPlayerManager();
        this.channelUtils = gameManager.getChannelUtils();
        this.worldExporter = new WorldStorage(roomManager, mapsManager, floorManager, entityManager, gameManager);
        this.lootManager = gameManager.getLootManager();
        this.roles = roles;
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        try {
            if (e instanceof MessageEvent) {
                init((MessageEvent) e);
            }
        } finally {
            super.handleUpstream(ctx, e);
        }
    }

    private void init(MessageEvent e) {
        this.aetherMudSession = (AetherMudSession) e.getChannel().getAttachment();;
        this.originalMessageParts = getOriginalMessageParts(e);
        this.rootCommand = getRootCommand(e);
        this.rootCommand = rootCommand.startsWith("/") ? rootCommand.substring(1) : rootCommand;
        this.player = playerManager.getPlayer(Main.createPlayerId(aetherMudSession.getUsername().get()));
        this.playerId = player.getPlayerId();
        this.currentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(player).get();
        this.mapMatrix = Optional.ofNullable(mapsManager.getFloorMatrixMaps().get(currentRoom.getFloorId()));
        mapMatrix.ifPresent(mapMatrix -> this.currentRoomCoords = Optional.ofNullable(mapMatrix.getCoords(currentRoom.getRoomId())));
    }

    private List<String> getOriginalMessageParts(MessageEvent e) {
        return getOriginalMessageParts((String) e.getMessage());
    }

    public static List<String> getOriginalMessageParts(String originalMessage) {
        return new ArrayList<>(Arrays.asList(originalMessage.split(" ")));
    }

    private String getRootCommand(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        if (origMessage.trim().isEmpty()) {
            return " ";
        }
        return origMessage.split(" ")[0].toLowerCase();
    }

    public void execCommandThreadSafe(ChannelHandlerContext ctx, MessageEvent e, Class c, CommandRunnable commandRunnable) throws Exception {
        synchronized (c) {
            try {
                commandRunnable.run();
            } catch (Exception ex) {
                log.error("Problem running command.", ex);
            } finally {
                removeCurrentHandlerAndWritePrompt(ctx, e);
                ctx.sendUpstream(e);
            }
        }
    }

    private void removeCurrentHandlerAndWritePrompt(ChannelHandlerContext ctx, MessageEvent e) {
        removeCurrentHandlerAndWritePrompt(ctx, e, true);
    }

    public void removeCurrentHandlerAndWritePrompt(ChannelHandlerContext ctx, MessageEvent e, boolean newLine) {
        e.getChannel().getPipeline().remove(ctx.getHandler());
        if (aetherMudSession.getGrabMerchant().isPresent()) {
            return;
        }
        gameManager.getChannelUtils().write(playerId, getPrompt(), newLine);
    }

    public String getPrompt() {
        return gameManager.buildPrompt(playerId);
    }

    public void execCommandBackgroundThread(ChannelHandlerContext ctx, MessageEvent e, CommandRunnable commandRunnable) throws Exception {
        try {
            new Thread(() -> {
                try {
                    commandRunnable.run();
                } catch (Exception ex) {
                    log.error("Problem running command.", ex);
                }
            }).start();
        } finally {
            removeCurrentHandlerAndWritePrompt(ctx, e);
            ctx.sendUpstream(e);
        }
    }

    public void execCommand(ChannelHandlerContext ctx, MessageEvent e, CommandRunnable commandRunnable) throws Exception {
        try {
            commandRunnable.run();
        } finally {
            removeCurrentHandlerAndWritePrompt(ctx, e);
            ctx.sendUpstream(e);
        }
    }

    public void write(String msg) {
        channelUtils.write(playerId, msg);
    }

    public void write(String msg, boolean leadingBlankLine) {
        channelUtils.write(playerId, msg, leadingBlankLine);
    }

    public void writeToRoom(String msg) {
        gameManager.writeToPlayerCurrentRoom(playerId, msg);
    }

    public void printCurrentRoomInformation() {
        gameManager.currentRoomLogic(playerId);
    }

    public void printCurrentRoomInformation(Room playerCurrentRoom) {
        gameManager.currentRoomLogic(playerId, playerCurrentRoom);
    }

    public String getDescription() {
        return description;
    }

    public Command copy() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<Command> clazz = (Class<Command>) this.getClass();
        return clazz.getConstructor(GameManager.class).newInstance(gameManager);
    }
}
