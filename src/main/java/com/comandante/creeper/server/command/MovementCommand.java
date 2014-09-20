package com.comandante.creeper.server.command;

import com.comandante.creeper.fight.FightManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class MovementCommand extends Command {

    final static String description = "Make a move.";

    public final static List<String> northTriggers = Arrays.asList("n", "north".toLowerCase());
    public final static List<String> southTriggers = Arrays.asList("s", "south".toLowerCase());
    public final static List<String> eastTriggers = Arrays.asList("e", "east".toLowerCase());
    public final static List<String> westTriggers = Arrays.asList("w", "west".toLowerCase());
    public final static List<String> upTriggers = Arrays.asList("u", "up".toLowerCase());
    public final static List<String> downTriggers = Arrays.asList("d", "down".toLowerCase());

    public final static ImmutableList validTriggers =
            new ImmutableList.Builder<String>()
                    .addAll(northTriggers)
                    .addAll(southTriggers)
                    .addAll(eastTriggers)
                    .addAll(westTriggers)
                    .addAll(upTriggers)
                    .addAll(downTriggers)
                    .build();

    public MovementCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (FightManager.isActiveFight(creeperSession)) {
                write("You can't not move while in a fight!");
                return;
            }
            final String command = getRootCommand(e);
            PlayerMovement playerMovement = null;
            if (!validTriggers.contains(command.toLowerCase())) {
                throw new RuntimeException("Malformed movement command.");
            }
            if (northTriggers.contains(command.toLowerCase())) {
                if (!currentRoom.getNorthId().isPresent()) {
                    printMap();
                    write("There's no northern exit.\r\n");
                    return;
                }
                Room destinationRoom = roomManager.getRoom(currentRoom.getNorthId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), this, "exited to the north.", "south");
            }
            if (southTriggers.contains(command.toLowerCase())) {
                if (!currentRoom.getSouthId().isPresent()) {
                    printMap();
                    write("There's no southern exit.\r\n");
                    return;
                }
                Room destinationRoom = roomManager.getRoom(currentRoom.getSouthId().get());
                printMap();
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), this, "exited to the south.", "north");
            }
            if (eastTriggers.contains(command.toLowerCase())) {
                if (!currentRoom.getEastId().isPresent()) {
                    printMap();
                    write("There's no eastern exit.\r\n");
                    return;
                }
                Room destinationRoom = roomManager.getRoom(currentRoom.getEastId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), this, "exited to the east.", "west");
            }
            if (westTriggers.contains(command.toLowerCase())) {
                if (!currentRoom.getWestId().isPresent()) {
                    printMap();
                    write("There's no western exit.\r\n");
                    return;
                }
                Room destinationRoom = roomManager.getRoom(currentRoom.getWestId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), this, "exited to the west.", "east");
            }
            if (upTriggers.contains(command.toLowerCase())) {
                if (!currentRoom.getUpId().isPresent()) {
                    printMap();
                    write("There's no up exit.\r\n");
                    return;
                }
                Room destinationRoom = roomManager.getRoom(currentRoom.getUpId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), this, "exited to the west.", "down");
            }
            if (downTriggers.contains(command.toLowerCase())) {
                if (!currentRoom.getDownId().isPresent()) {
                    printMap();
                    write("There's no down exit.\r\n");
                    return;
                }
                Room destinationRoom = roomManager.getRoom(currentRoom.getDownId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), this, "exited to the west.", "up");
            }
            gameManager.movePlayer(playerMovement);
            if (playerMovement != null) {
                player.setReturnDirection(Optional.of(playerMovement.getReturnDirection()));
                currentRoomLogic();
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
