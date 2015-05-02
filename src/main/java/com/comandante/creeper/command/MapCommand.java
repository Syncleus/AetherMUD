package com.comandante.creeper.command;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.world.Coords;
import com.comandante.creeper.world.Room;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class MapCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("map", "m");
    final static String description = "Display the map.";
    final static String correctUsage = "map <size number>";


    public MapCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() > 1 && isInteger(originalMessageParts.get(1))) {
                int max = Integer.parseInt(originalMessageParts.get(1));
                write(mapsManager.drawMap(currentRoom.getRoomId(), new Coords(max, max)));
            } else {
                Player player = playerManager.getPlayer(playerId);
                final Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
                StringBuilder sb = new StringBuilder();

                if (playerCurrentRoom.getMapData().isPresent()) {
                    sb.append(playerCurrentRoom.getMapData().get()).append("\r\n");
                    channelUtils.write(player.getPlayerId(), sb.toString());
                } else {
                    channelUtils.write(player.getPlayerId(), "No map data.");
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}

