package com.comandante.creeper.command;

import com.comandante.creeper.RecentChangesManager;
import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class RecentChangesCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("recentchanges");
    final static String description = "Print the recent changes to the creeper codebase.";
    final static String correctUsage = "recentchanges";

    public RecentChangesCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            write(RecentChangesManager.getRecentChanges());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
