package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.world.Room;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class BackCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("back", "b");
    final static String description = "Return to where you came from.";
    final static String correctUsage = "back";

    public BackCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (player.isActiveFights()) {
                write("You can't move while in a fight!");
                return;
            }
            Room returnRoom = player.getPreviousRoom();
            PlayerMovement playerMovement = new PlayerMovement(player, currentRoom.getRoomId(), returnRoom.getRoomId(), "fleed to where they came from.", "up");
            player.movePlayer(playerMovement);
        });
    }

}