package com.comandante.creeper.managers;


import com.comandante.creeper.model.Movement;
import com.comandante.creeper.model.Player;
import com.comandante.creeper.model.Room;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.server.CreeperSession;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Interners;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.fusesource.jansi.Ansi;
import org.jboss.netty.channel.MessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GameManager {

    public static String LOGO = "\r\n" +
            "               .------..                            _------__--___.__.\r\n" +
            "            /            \\_                       /            `  `    \\\r\n" +
            "          /                \\                     |.                     \\\r\n" +
            "         /                   \\                   \\                       |\r\n" +
            "        /    .--._    .---.   |                   \\                      |\r\n" +
            "        |  /      -__-     \\   |                    ~-/--`-`-`-\\         |\r\n" +
            "        | |                |  |                     |          \\        |\r\n" +
            "         ||                  ||                     |            |       |\r\n" +
            "         ||     ,_   _.      ||                     |            |       |\r\n" +
            "         ||      e   e      ||  Hey Beavis,         |   _--    |       |\r\n" +
            "          ||     _  |_      ||   pull my finger!     _| =-.    |.-.    |\r\n" +
            "         @|     (o\\_/o)     |@   Heh,Heh!!!          o|/o/       _.   |\r\n" +
            "           |     _____     |                        /  ~          \\ |\r\n" +
            "            \\ ( /uuuuu\\ ) /             No way!    (/___@)  ___~    |\r\n" +
            "             \\  `====='  /              Ass wipe!!    |_===~~~.`    |\r\n" +
            "              \\  -___-  /                         _______.--~     |\r\n" +
            "               |       |            //             \\________       |\r\n" +
            "               /-_____-\\       .  _//_                      \\      |\r\n" +
            "             /           \\     \\\\/////                    __/-___-- -_\r\n" +
            "           /               \\    \\   /                    /            __\\\r\n" +
            "          /__|  AC / DC  |__\\   / /                      -| Metallica|| |\r\n" +
            "          | ||           |\\ \\  / /                       ||          || |\r\n" +
            "          | ||           | \\ \\/ /                        ||          || |\r\n";

    public static String VERSION = "0.1-SNAPSHOT";

    private final RoomManager roomManager;
    private final PlayerManager playerManager;
    private final NewUserRegistrationManager newUserRegistrationManager;
    private final EntityManager entityManager;
    //private final EntityManager entityManager;

    public NewUserRegistrationManager getNewUserRegistrationManager() {
        return newUserRegistrationManager;
    }

    public GameManager(RoomManager roomManager, PlayerManager playerManager, EntityManager entityManager) {
        this.roomManager = roomManager;
        this.playerManager = playerManager;
        this.entityManager = entityManager;
        this.newUserRegistrationManager = new NewUserRegistrationManager(playerManager);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    private static final Integer LOBBY_ID = 1;

    public Optional<Room> getPlayerCurrentRoom(Player player) {
        Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRooms();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            Room room = next.getValue();
            for (String searchPlayerId : room.getPresentPlayerIds()) {
                if (searchPlayerId.equals(player.getPlayerId())) {
                    return Optional.of(room);
                }
            }
        }
        return Optional.absent();
    }

    public void who(Player player) {
        Set<Player> allPlayers = getAllPlayers();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new Ansi().fg(Ansi.Color.CYAN).toString());
        stringBuilder.append("----------------------\r\n");
        stringBuilder.append("|--active users------|\r\n");
        stringBuilder.append("----------------------\r\n");
        for (Player allPlayer : allPlayers) {
            stringBuilder.append(allPlayer.getPlayerName()).append("\r\n");
        }
        stringBuilder.append(new Ansi().reset().toString());
        player.getChannel().write(stringBuilder.toString() + "\r\n");
    }

    public void tell(Player sourcePlayer, String rawMessage) {
        ArrayList<String> parts = new ArrayList<>(Arrays.asList(rawMessage.split(" ")));
        if (parts.size() < 3) {
            sourcePlayer.getChannel().write(("tell failed, no message to send.\r\n"));
            return;
        }
        //remove the literal 'tell'
        parts.remove(0);
        String destinationUsername = parts.get(0);
        Player desintationPlayer = getPlayerManager().getPlayerByUsername(destinationUsername);
        if (desintationPlayer == null) {
            sourcePlayer.getChannel().write(("tell failed, unknown user.\r\n"));
            return;
        }
        if (desintationPlayer.getPlayerId().equals(sourcePlayer.getPlayerId())) {
            sourcePlayer.getChannel().write(("tell failed, you're talking to yourself.\r\n"));
            return;
        }
        parts.remove(0);

        String tellMessage = StringUtils.join(parts, " ");
        privateMessage(sourcePlayer, desintationPlayer, tellMessage);

    }

    private void privateMessage(Player sourcePlayer, Player destinationPlayer, String message) {
        StringBuilder stringBuilder = new StringBuilder();
        String sourcePlayerColor = new Ansi().fg(Ansi.Color.WHITE).toString();
        String destinationPlayercolor = new Ansi().fg(Ansi.Color.YELLOW).toString();
        stringBuilder.append("*").append(sourcePlayer.getPlayerName()).append("* ");
        stringBuilder.append(message);
        stringBuilder.append(new Ansi().reset().toString());
        destinationPlayer.getChannel().write(destinationPlayercolor + stringBuilder.toString() + "\r\n");
        sourcePlayer.getChannel().write(sourcePlayerColor + stringBuilder.toString() + "\r\n");
    }

    public Set<Player> getAllPlayers() {
        ImmutableSet.Builder<Player> builder = ImmutableSet.builder();
        Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRooms();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            Room room = next.getValue();
            Set<Player> presentPlayers = playerManager.getPresentPlayers(room);
            for (Player player : presentPlayers) {
                builder.add(player);
            }
        }
        return builder.build();
    }

    public void setPlayerAfk(String username) {
        Player playerByUsername = playerManager.getPlayerByUsername(username);
        Optional<Room> playerCurrentRoom = getPlayerCurrentRoom(playerByUsername);
        playerCurrentRoom.get().getPresentPlayerIds().remove(playerByUsername.getPlayerId());
        playerCurrentRoom.get().addAfkPlayer(playerByUsername.getPlayerId());
    }

    public void movePlayer(Movement movement) {
        synchronized (Interners.newStrongInterner()) {
            Room sourceRoom = roomManager.getRoom(movement.getSourceRoomId());
            Room destinationRoom = roomManager.getRoom(movement.getDestinationRoomId());
            sourceRoom.removePresentPlayer(movement.getPlayer().getPlayerId());
            for (Player next : playerManager.getPresentPlayers(sourceRoom)) {
                StringBuilder sb = new StringBuilder();
                sb.append(movement.getPlayer().getPlayerName());
                sb.append(" ").append(movement.getRoomExitMessage()).append("\r\n");
                next.getChannel().write(sb.toString());
            }
            for (Player next : playerManager.getPresentPlayers(destinationRoom)) {
                next.getChannel().write(movement.getPlayer().getPlayerName() + " arrived.\r\n");
            }
            destinationRoom.addPresentPlayer(movement.getPlayer().getPlayerId());
        }
    }

    public void placePlayerInLobby(Player player) {
        Room room = roomManager.getRoom(LOBBY_ID);
        room.addPresentPlayer(player.getPlayerId());
        for (Player next : playerManager.getPresentPlayers(room)) {
            if (next.getPlayerId().equals(player.getPlayerId())) {
                continue;
            }
            next.getChannel().write(player.getPlayerName() + " arrived.\r\n");
        }
    }

    public void say(Player sourcePlayer, String message) {
        Optional<Room> playerCurrentRoomOpt = getPlayerCurrentRoom(sourcePlayer);
        if (!playerCurrentRoomOpt.isPresent()) {
            throw new RuntimeException("playerCurrentRoom is missing!");
        }

        Room playerCurrentRoom = playerCurrentRoomOpt.get();
        Set<Player> presentPlayers = playerManager.getPresentPlayers(playerCurrentRoom);

        for (Player presentPlayer : presentPlayers) {
            StringBuilder stringBuilder = new StringBuilder();
            if (presentPlayer.getPlayerId().equals(sourcePlayer.getPlayerId())) {
                stringBuilder.append(new Ansi().fg(Ansi.Color.WHITE).toString());
            } else {
                stringBuilder.append(new Ansi().fg(Ansi.Color.RED).toString());
            }
            stringBuilder.append("<").append(sourcePlayer.getPlayerName()).append("> ").append(message).append("\r\n");
            stringBuilder.append(new Ansi().reset().toString());
            presentPlayer.getChannel().write(stringBuilder.toString());
        }
    }

    public void gossip(Player sourcePlayer, String message) {
        Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
        while (players.hasNext()) {
            StringBuilder stringBuilder = new StringBuilder();
            Player player = players.next().getValue();
            if (player.getPlayerId().equals(sourcePlayer.getPlayerId())) {
                stringBuilder.append(new Ansi().fg(Ansi.Color.WHITE).toString());
            } else {
                stringBuilder.append(new Ansi().fg(Ansi.Color.MAGENTA).toString());
            }
            stringBuilder.append("[").append(sourcePlayer.getPlayerName()).append("] ").append(message).append("\r\n");
            stringBuilder.append(new Ansi().reset().toString());
            player.getChannel().write(stringBuilder.toString());
        }
    }

    private String getExits(Room room, Player player) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ Exits: ");
        stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
        if (!player.getReturnDirection().isPresent()) {
            player.setReturnDirection(Optional.of("-"));
        }
        if (room.getNorthId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("north")) {
                stringBuilder.append(new Ansi().fgBright(Ansi.Color.GREEN).toString());
                stringBuilder.append("North ");
                stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
            } else {
                stringBuilder.append("North ");
            }
        }
        if (room.getSouthId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("south")) {
                stringBuilder.append(new Ansi().fgBright(Ansi.Color.GREEN).toString());
                stringBuilder.append("South ");
                stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
            } else {
                stringBuilder.append("South ");
            }
        }
        if (room.getEastId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("east")) {
                stringBuilder.append(new Ansi().fgBright(Ansi.Color.GREEN).toString());
                stringBuilder.append("East ");
                stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
            } else {
                stringBuilder.append("East ");
            }
        }
        if (room.getWestId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("west")) {
                stringBuilder.append(new Ansi().fgBright(Ansi.Color.GREEN).toString());
                stringBuilder.append("West ");
                stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
            } else {
                stringBuilder.append("West ");
            }
        }
        if (room.getUpId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("up")) {
                stringBuilder.append(new Ansi().fgBright(Ansi.Color.GREEN).toString());
                stringBuilder.append("Up ");
                stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
            } else {
                stringBuilder.append("Up ");
            }
        }
        if (room.getDownId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("down")) {
                stringBuilder.append(new Ansi().fgBright(Ansi.Color.GREEN).toString());
                stringBuilder.append("Down ");
                stringBuilder.append(new Ansi().fg(Ansi.Color.GREEN).toString());
            } else {
                stringBuilder.append("Down ");
            }
        }
        stringBuilder.append(new Ansi().reset().toString()).append("]\r\n");
        return stringBuilder.toString();
    }

    public void currentRoomLogic(String playerId) {
        Player player = playerManager.getPlayer(playerId);
        final Room playerCurrentRoom = getPlayerCurrentRoom(player).get();
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n");
        sb.append(new Ansi().fg(Ansi.Color.GREEN).toString());
        sb.append(playerCurrentRoom.getRoomTitle()).append("\r\n\r\n");
        sb.append(new Ansi().reset().toString());
        sb.append(WordUtils.wrap(playerCurrentRoom.getRoomDescription(), 70)).append("\r\n");
        sb.append(getExits(playerCurrentRoom, player));
        for (String searchPlayerId : playerCurrentRoom.getPresentPlayerIds()) {
            if (searchPlayerId.equals(player.getPlayerId())) {
                continue;
            }
            Player searchPlayer = playerManager.getPlayer(searchPlayerId);
            sb.append(searchPlayer.getPlayerName()).append(" is here.\r\n");
        }
        for (String npcId : playerCurrentRoom.getNpcIds()) {
            Npc npcEntity = entityManager.getNpcEntity(npcId);
            sb.append("A ").append(npcEntity.getColorName()).append(" is here.\r\n");
        }
        for (String itemId : playerCurrentRoom.getItemIds()) {
            sb.append(entityManager.getItemEntity(itemId).getItemName()).append( " is on the ground.\r\n");
        }
        player.getChannel().write(sb.toString());
    }

    public void currentRoomLogic(CreeperSession creeperSession, MessageEvent e) {
        final String player = playerManager.getPlayerByUsername(creeperSession.getUsername().get()).getPlayerId();
        currentRoomLogic(player);
    }

    public void roomSay(Integer roomId, String message) {
        Set<String> presentPlayerIds = roomManager.getRoom(roomId).getPresentPlayerIds();
        for (String playerId : presentPlayerIds) {
            Player player = playerManager.getPlayer(playerId);
            player.getChannel().write(message);
        }
    }

    public void placeItemInRoom(Integer roomId, String itemId) {
        roomManager.getRoom(roomId).addPresentItem(entityManager.getItemEntity(itemId).getItemId());
    }

    public void acquireItem(Player player, String itemId) {
        Room playerCurrentRoom = getPlayerCurrentRoom(player).get();
        playerCurrentRoom.getItemIds().remove(itemId);
        playerManager.addInventoryId(player.getPlayerId(), itemId);
        player.getChannel().write("You acquired " + entityManager.getItemEntity(itemId).getItemName() + "\r\n");
    }
}
