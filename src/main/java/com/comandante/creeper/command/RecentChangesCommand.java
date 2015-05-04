package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.kohsuke.github.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
            GitHub github = GitHub.connectAnonymously();
            GHRepository repo = github.getRepository("chriskearney/creeper");
            PagedIterable<GHCommit> list = repo.queryCommits().list();
            int i = 1;
            for (GHCommit ghCommit : list.asList()) {
                if (i < 10) {
                    write("Change #" + i + " | " + ghCommit.getCommitShortInfo().getMessage() + "\r\n");
                } else {
                    return;
                }
                i++;
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
