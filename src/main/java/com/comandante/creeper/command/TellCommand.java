package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

import static com.comandante.creeper.server.Color.RESET;
import static com.comandante.creeper.server.Color.YELLOW;

public class TellCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("tell", "t");
    final static String description = "Send a private message to a player.";
    final static String correctUsage = "tell <player name> <message>";

    public TellCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() < 3) {
                write("tell failed, no message to send.");
                return;
            }
            //remove the literal 'tell'
            originalMessageParts.remove(0);
            String destinationUsername = originalMessageParts.get(0);
            Player desintationPlayer = playerManager.getPlayerByUsername(destinationUsername);
            if (desintationPlayer == null) {
                write("tell failed, unknown user.");
                return;
            }
            if (desintationPlayer.getPlayerId().equals(playerId)) {
                write("tell failed, you're talking to yourself.");
                return;
            }
            originalMessageParts.remove(0);
            String tellMessage = Joiner.on(" ").join(originalMessageParts);
            StringBuilder stringBuilder = new StringBuilder();
            String destinationPlayercolor = YELLOW;
            stringBuilder.append("*").append(player.getPlayerName()).append("* ");
            stringBuilder.append(tellMessage);
            stringBuilder.append(RESET);
            channelUtils.write(desintationPlayer.getPlayerId(), destinationPlayercolor + stringBuilder.append("\r\n").toString(), true);
            write(stringBuilder.toString());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
