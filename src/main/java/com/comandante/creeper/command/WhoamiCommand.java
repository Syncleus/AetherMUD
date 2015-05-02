package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class WhoamiCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("whoami");
    final static String description = "Display information about your character.";
    final static String correctUsage = "whoami";

    public WhoamiCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            write(player.getPlayerName());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
