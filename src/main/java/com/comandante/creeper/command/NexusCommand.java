package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class NexusCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("nexus", "nx");
    final static String description = "Say something in #nexus";
    final static String correctUsage = "nexus <words>";

    public NexusCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description,correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (gameManager.getCreeperConfiguration().isIrcEnabled) {
                originalMessageParts.remove(0);
                String transferPhrase = Joiner.on(" ").join(originalMessageParts);
                gameManager.getIrcBotService().getBot().getUserChannelDao().getChannel(gameManager.getCreeperConfiguration().ircChannel).send().message(player.getPlayerName() + ": " + transferPhrase);
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}