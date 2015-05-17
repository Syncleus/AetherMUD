package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.comandante.creeper.server.Color.RED;
import static com.comandante.creeper.server.Color.RESET;

public class SayCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("say");
    final static String description = "Say something to the current room.";
    final static String correctUsage = "say <message>";

    public SayCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            originalMessageParts.remove(0);
            String message = Joiner.on(" ").join(originalMessageParts);
            Set<Player> presentPlayers = roomManager.getPresentPlayers(currentRoom);
            for (Player presentPlayer : presentPlayers) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(RED);
                stringBuilder.append("<").append(player.getPlayerName()).append("> ").append(message);
                stringBuilder.append(RESET);
                if (presentPlayer.getPlayerId().equals(playerId)) {
                    write(stringBuilder.toString());
                } else {
                    channelUtils.write(presentPlayer.getPlayerId(), stringBuilder.append("\r\n").toString(), true);
                }
            }
            if (gameManager.getCreeperConfiguration().isIrcEnabled && (Objects.equals(gameManager.getCreeperConfiguration().ircBridgeRoomId, currentRoom.getRoomId()))) {
                gameManager.getIrcBotService().getBot().getUserChannelDao().getChannel(gameManager.getCreeperConfiguration().ircChannel).send().message(player.getPlayerName() + ": " + message);
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
