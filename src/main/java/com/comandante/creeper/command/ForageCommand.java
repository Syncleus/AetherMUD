package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class ForageCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("forage", "f");
    final static String description = "Forage for supplies.";
    final static String correctUsage = "forage";

    public ForageCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            gameManager.getForageManager().getForageForRoom(currentRoom, player);
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}