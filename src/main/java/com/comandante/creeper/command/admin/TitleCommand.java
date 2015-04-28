package com.comandante.creeper.command.admin;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class TitleCommand extends Command {


    final static List<String> validTriggers = Arrays.asList("title");
    final static String description = "Set the title. For admins only.";
    final static boolean isAdminOnly = true;

    public TitleCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, isAdminOnly);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (!hasRole(PlayerRole.ADMIN)){
                return;
            }
            originalMessageParts.remove(0);
            currentRoom.setRoomTitle(Joiner.on(" ").join(originalMessageParts));
            write("Titled saved.");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
