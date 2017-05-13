package com.comandante.creeper.command.commands;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Effect;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.world.model.RemoteExit;
import com.comandante.creeper.world.model.Room;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class MovementCommand extends Command {

    final static String description = "Move your player.";
    final static String correctUsage = "n|s|e|w|enter e-<name>";

    public final static List<String> northTriggers = Arrays.asList("n", "north".toLowerCase());
    public final static List<String> southTriggers = Arrays.asList("s", "south".toLowerCase());
    public final static List<String> eastTriggers = Arrays.asList("e", "east".toLowerCase());
    public final static List<String> westTriggers = Arrays.asList("w", "west".toLowerCase());
    public final static List<String> upTriggers = Arrays.asList("u", "up".toLowerCase());
    public final static List<String> downTriggers = Arrays.asList("d", "down".toLowerCase());
    public final static List<String> enterTriggers = Arrays.asList("enter", "enter".toLowerCase());

    public final static ImmutableList validTriggers = new ImmutableList.Builder<String>().addAll(northTriggers).addAll(southTriggers).addAll(eastTriggers).addAll(westTriggers).addAll(upTriggers).addAll(downTriggers).addAll(enterTriggers).build();

    public MovementCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        this.execCommand(ctx, e, () -> {
            if (player.isActiveFights()) {
                MovementCommand.this.write("You can't move while in a fight!");
                return;
            }
            if (player.isActive(CoolDownType.DEATH)) {
                MovementCommand.this.write("You are dead and can not move.");
                return;
            }
            if (player.areAnyAlertedNpcsInCurrentRoom()) {
                MovementCommand.this.write("You are unable to progress, but can return to where you came from by typing \"back\".");
                return;
            }
            java.util.Optional<PlayerMetadata> playerMetadataOptional = playerManager.getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            for (Effect effect : playerMetadataOptional.get().getEffects()) {
                if (effect.isFrozenMovement()) {
                    MovementCommand.this.write("You are frozen and can not move.");
                    return;
                }
            }
            final String command = rootCommand;
            PlayerMovement playerMovement = null;
            if (!validTriggers.contains(command.toLowerCase())) {
                throw new RuntimeException("Malformed movement command.");
            } else if (northTriggers.contains(command.toLowerCase()) && currentRoom.getNorthId().isPresent()) {
                Room destinationRoom = roomManager.getRoom(currentRoom.getNorthId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "exited to the north.", "south");
            } else if (southTriggers.contains(command.toLowerCase()) && currentRoom.getSouthId().isPresent()) {
                Room destinationRoom = roomManager.getRoom(currentRoom.getSouthId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "exited to the south.", "north");
            } else if (eastTriggers.contains(command.toLowerCase()) && currentRoom.getEastId().isPresent()) {
                Room destinationRoom = roomManager.getRoom(currentRoom.getEastId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "exited to the east.", "west");
            } else if (westTriggers.contains(command.toLowerCase()) && currentRoom.getWestId().isPresent()) {
                Room destinationRoom = roomManager.getRoom(currentRoom.getWestId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "exited to the west.", "east");
            } else if (upTriggers.contains(command.toLowerCase()) && currentRoom.getUpId().isPresent()) {
                Room destinationRoom = roomManager.getRoom(currentRoom.getUpId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "exited up.", "down");
            } else if (downTriggers.contains(command.toLowerCase()) && currentRoom.getDownId().isPresent()) {
                Room destinationRoom = roomManager.getRoom(currentRoom.getDownId().get());
                playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "exited down.", "up");
            } else if (enterTriggers.contains(command.toLowerCase())) {
                Optional<RemoteExit> remoteExitOptional = MovementCommand.this.doesEnterExitExist();
                if (remoteExitOptional.isPresent()) {
                    Room destinationRoom = roomManager.getRoom(remoteExitOptional.get().getRoomId());
                    playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), destinationRoom.getRoomId(), "entered " + remoteExitOptional.get().getExitDetail() + ".", "N/A");
                } else {
                    MovementCommand.this.write("There's no where to go with that name. (" + command + ")");
                    return;
                }
            } else {
                MovementCommand.this.write("There's no exit in that direction. (" + command + ")");
                return;
            }
            player.movePlayer(playerMovement);
        });
    }

    private Optional<RemoteExit> doesEnterExitExist() {
        if (originalMessageParts.size() > 1) {
            String enterExitName = originalMessageParts.get(1);
            for (RemoteExit remoteExit : currentRoom.getEnterExits()) {
                if (remoteExit.getExitDetail().equalsIgnoreCase(enterExitName)) {
                    return Optional.of(remoteExit);
                }
            }
        }
        return Optional.absent();
    }
}
