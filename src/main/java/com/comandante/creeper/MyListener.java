package com.comandante.creeper;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.world.Room;
import com.comandante.creeper.world.RoomManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericChannelEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.*;

import static com.comandante.creeper.server.Color.MAGENTA;
import static com.comandante.creeper.server.Color.RED;
import static com.comandante.creeper.server.Color.RESET;

public class MyListener extends ListenerAdapter {

    private final GameManager gameManager;
    private final Integer bridgeRoomId;

    public MyListener(GameManager gameManager, Integer bridgeRoomId) {
        this.gameManager = gameManager;
        this.bridgeRoomId = bridgeRoomId;
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        //When someone says ?helloworld respond with "Hello World"'
        PlayerManager playerManager = gameManager.getPlayerManager();
        if (event.getMessage().startsWith("?gossip")) {
            ArrayList<String> originalMessageParts = Lists.newArrayList(Arrays.asList(event.getMessage().split(" ")));
            originalMessageParts.remove(0);
            final String msg = Joiner.on(" ").join(originalMessageParts);
            Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
            while (players.hasNext()) {
                final Player next = players.next().getValue();
                final String gossipMessage = new StringBuilder()
                        .append(MAGENTA).append("[")
                        .append(event.getUser().getNick()).append("-irc").append("] ")
                        .append(msg).append(RESET)
                        .toString();
                gameManager.getChannelUtils().write(next.getPlayerId(), gossipMessage + "\r\n", true);
            }
            return;
        }
        Room bridgeRoom = gameManager.getRoomManager().getRoom(bridgeRoomId);
        Set<Player> presentPlayers = gameManager.getPlayerManager().getPresentPlayers(bridgeRoom);
        for (Player presentPlayer : presentPlayers) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(RED);
            stringBuilder.append("<").append(event.getUser().getNick()).append("> ").append(event.getMessage());
            stringBuilder.append(RESET);
            gameManager.getChannelUtils().write(presentPlayer.getPlayerId(), stringBuilder.append("\r\n").toString(), true);
        }
    }
}


