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
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

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

    public void configure(MessageEvent e) {
        this.creeperSession = extractCreeperSession(e.getChannel());
        this.player = playerManager.getPlayer(extractPlayerId(creeperSession));
        this.playerId = player.getPlayerId();
        this.currentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(player).get();
        this.mapMatrix = mapsManager.getFloorMatrixMaps().get(currentRoom.getFloorId());
        this.currentRoomCoords = mapMatrix.getCoords(currentRoom.getRoomId());
        this.originalMessageParts = getOriginalMessageParts(e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            CreeperSession creeperSession = extractCreeperSession(e.getChannel());
            e.getChannel().getPipeline().remove(ctx.getHandler());
            if (creeperSession.getGrabMerchant().isPresent()) {
                return;
            }
            String playerId = extractPlayerId(creeperSession);
            String prompt = gameManager.buildPrompt(playerId);
            gameManager.getChannelUtils().write(playerId, prompt, true);
        } finally {
            super.messageReceived(ctx, e);
        }
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

    public void write(String msg, boolean leadingBlankLine) {
        channelUtils.write(playerId, msg, leadingBlankLine);
    }

    public void writeToRoom(String msg) {
        gameManager.writeToPlayerCurrentRoom(playerId, msg);
    }

    public void currentRoomLogic() {
        gameManager.currentRoomLogic(playerId);
    }

    public void currentRoomLogic(Room playerCurrentRoom) {
        gameManager.currentRoomLogic(playerId, playerCurrentRoom);
    }

    public String getPrompt() {
        return gameManager.buildPrompt(playerId);
    }

    public String getDescription() {
        return description;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public Command copy() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<Command> clazz = (Class<Command>) this.getClass();
        return clazz.getConstructor(GameManager.class).newInstance(gameManager);
    }

    public <T> T createObj(String nameclass) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class<T> clazz = (Class<T>) Class.forName(nameclass);

        // assumes the target class has a no-args Constructor
        return clazz.getConstructor(GameManager.class).newInstance(gameManager);
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}