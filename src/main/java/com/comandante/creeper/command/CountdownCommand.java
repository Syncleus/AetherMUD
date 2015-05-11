package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountdownCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("countdown", "??");
    final static String description = "a countdown.";
    final static String correctUsage = "?? countdown";

    public CountdownCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            ArrayList<String> strings = Lists.newArrayList("... ***** COUNTDOWN ***** ...", ".             5             .", ".             4             .", ".             3             .", ".             2             .", ".             1             .", "... *****   SMOKE!  ***** ...");
            for (String s: strings) {
                write(Color.BOLD_ON + Color.GREEN + s + Color.RESET + "\r\n");
                Thread.sleep(900);
            }

        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
