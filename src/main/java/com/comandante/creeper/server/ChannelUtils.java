package com.comandante.creeper.server;

import com.comandante.creeper.CreeperConfiguration;
import com.comandante.creeper.IrcBotService;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.world.Room;
import com.comandante.creeper.world.RoomManager;

import java.util.Objects;
import java.util.Set;

public class ChannelUtils {

    private final PlayerManager playerManager;
    private final RoomManager roomManager;

    public ChannelUtils(PlayerManager playerManager, RoomManager roomManager) {
        this.playerManager = playerManager;
        this.roomManager = roomManager;
    }

    public void writeToRoom(Integer roomId, String message) {
        Room room = roomManager.getRoom(roomId);
        Set<String> presentPlayerIds = room.getPresentPlayerIds();
        for (String id : presentPlayerIds) {
            Player presentPlayer = playerManager.getPlayer(id);
            write(presentPlayer.getPlayerId(), message, true);
        }
    }

    public void write(String playerId, String message) {
        write(playerId, message, false);
    }

    public void write(String playerId, String message, boolean leadingBlankLine) {
        if (playerManager.getSessionManager().getSession(playerId).getGrabMultiLineInput().isPresent()) {
            return;
        }
        Player player = playerManager.getPlayer(playerId);
        String lastMessage = playerManager.getSessionManager().getSession(playerId).getLastMessage();
        StringBuilder sb = new StringBuilder();
        if (lastMessage != null && !lastMessage.substring(lastMessage.length() - 2).equals("\r\n")) {
            if (leadingBlankLine) {
                sb.append("\r\n");
            }
        }
        sb.append(message);
        playerManager.getSessionManager().getSession(playerId).setLastMessage(sb.toString());
        player.getChannel().write(sb.toString());
    }
}

