package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;


public class RecentGossipCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("recentgossip", "rg");
    final static String description = "Replay recent gossip.";
    final static String correctUsage = "recentgossip 30";

    public RecentGossipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            List<String> recent = null;
            if (originalMessageParts.size() > 1) {
                String size = originalMessageParts.get(1);
                int i;
                try {
                    i = Integer.parseInt(size);
                } catch (NumberFormatException ne) {
                    return;
                }
                recent = gameManager.getGossipCache().getRecent(i);
            } else {
                recent = gameManager.getGossipCache().getRecent(25);
            }
            for (String line : recent) {
                write(line + "\r\n");
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
