package com.comandante.creeper.command;


import com.comandante.creeper.bot.commands.BotCommand;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

import static com.comandante.creeper.server.Color.MAGENTA;
import static com.comandante.creeper.server.Color.RESET;

public class GossipCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("gossip", "g");
    final static String description = "Sends a message to the entire MUD.";
    final static String correctUsage = "gossip <message>";

    public GossipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() == 1) {
                write("Nothing to gossip about?");
                return;
            }
            originalMessageParts.remove(0);
            String msg = Joiner.on(" ").join(originalMessageParts);
            try {
                if (msg.startsWith("!!")) {
                    String botCommandOutput = getBotCommandOutput(msg);
                    msg = msg + "\r\n" + botCommandOutput;
                }
            } catch (Exception ex) {
                log.error("Problem executing bot command from gossip channel!", ex);
            }
            Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
            while (players.hasNext()) {
                final Player next = players.next().getValue();
                final String gossipMessage = new StringBuilder()
                        .append(MAGENTA).append("[")
                        .append(player.getPlayerName()).append("] ")
                        .append(msg).append(RESET)
                        .toString();
                if (next.getPlayerId().equals(playerId)) {
                    write(gossipMessage);
                } else {
                    channelUtils.write(next.getPlayerId(), gossipMessage + "\r\n", true);
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    private String getBotCommandOutput(String cmd) {
        ArrayList<String> originalMessageParts = Lists.newArrayList(Arrays.asList(cmd.split("!!")));
        originalMessageParts.remove(0);
        final String msg = Joiner.on(" ").join(originalMessageParts);
        BotCommand command = gameManager.getBotCommandFactory().getCommand(msg);
        if (command != null) {
            List<String> process = command.process();
            StringBuilder sb = new StringBuilder();
            for (String line : process) {
                sb.append(line).append("\r\n");
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
