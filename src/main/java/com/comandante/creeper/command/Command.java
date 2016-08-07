package com.comandante.creeper.command;

import com.comandante.creeper.Items.LootManager;
import com.comandante.creeper.Main;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.player.PlayerRole;
import com.comandante.creeper.server.ChannelCommunicationUtils;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.world.*;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class Command extends SimpleChannelUpstreamHandler {

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
    public CreeperSession creeperSession;
    public Player player;
    public Room currentRoom;
    public String playerId;
    public MapMatrix mapMatrix;
    public Coords currentRoomCoords;
    public List<String> originalMessageParts;
    public WorldExporter worldExporter;
    public static final Logger log = Logger.getLogger(Command.class);
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
        this.worldExporter = new WorldExporter(roomManager, mapsManager, floorManager, entityManager, gameManager);
        this.lootManager = gameManager.getLootManager();
        this.roles = roles;
    }

    private void init(MessageEvent e) {
        this.creeperSession = (CreeperSession) e.getChannel().getAttachment();;
        this.originalMessageParts = getOriginalMessageParts(e);
        this.rootCommand = getRootCommand(e);
        this.player = playerManager.getPlayer(Main.createPlayerId(creeperSession.getUsername().get()));
        this.playerId = player.getPlayerId();
        this.currentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(player).get();
        this.mapMatrix = mapsManager.getFloorMatrixMaps().get(currentRoom.getFloorId());
        this.currentRoomCoords = mapMatrix.getCoords(currentRoom.getRoomId());
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

    private void removeCurrentHandlerAndWritePrompt(ChannelHandlerContext ctx, MessageEvent e) {
        removeCurrentHandlerAndWritePrompt(ctx, e, true);
    }

    public void removeCurrentHandlerAndWritePrompt(ChannelHandlerContext ctx, MessageEvent e, boolean newLine) {
        e.getChannel().getPipeline().remove(ctx.getHandler());
        if (creeperSession.getGrabMerchant().isPresent()) {
            return;
        }
        gameManager.getChannelUtils().write(playerId, getPrompt(), newLine);
    }

    private String getRootCommand(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        return origMessage.split(" ")[0].toLowerCase();
    }

    private List<String> getOriginalMessageParts(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        return new ArrayList<>(Arrays.asList(origMessage.split(" ")));
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

    public String getPrompt() {
        return gameManager.buildPrompt(playerId);
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