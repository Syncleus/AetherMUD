package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.server.Color;
import com.google.common.collect.Lists;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

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
            new Thread(new PrintCountdown(playerManager, channelUtils)).start();
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    public static class PrintCountdown implements Runnable {

        private PlayerManager playerManager;
        private ChannelUtils channelUtils;

        public PrintCountdown(PlayerManager playerManager, ChannelUtils channelUtils) {
            this.playerManager = playerManager;
            this.channelUtils = channelUtils;
        }

        @Override
        public void run() {
            ArrayList<String> strings = Lists.newArrayList("... ***** COUNTDOWN ***** ...", ".             5             .", ".             4             .", ".             3             .", ".             2             .", ".             1             .", "... *****   SMOKE!  ***** ...");
            for (String s : strings) {
                Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
                while (players.hasNext()) {
                    Map.Entry<String, Player> next = players.next();
                    channelUtils.write(next.getValue().getPlayerId(), Color.BOLD_ON + Color.GREEN + s + Color.RESET + "\r\n", true);
                }
                try {
                    Thread.sleep(900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
