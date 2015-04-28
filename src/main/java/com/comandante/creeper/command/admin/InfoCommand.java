package com.comandante.creeper.command.admin;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.player.PlayerRole;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class InfoCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("info");
    final static String description = "Get additional info. For admins only.";
    final static boolean isAdminOnly = true;

    public InfoCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, isAdminOnly);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (!hasRole(PlayerRole.ADMIN)){
                return;
            }
            write("roomId: " + currentRoom.getRoomId());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}