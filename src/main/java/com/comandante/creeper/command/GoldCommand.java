package com.comandante.creeper.command;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class GoldCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("gold");
    final static String description = "Prints the amount of gold you hold.";

    public GoldCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
          write("You have " + playerManager.getPlayerMetadata(playerId).getGold() + Color.YELLOW + " gold." + Color.RESET);
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}