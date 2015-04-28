package com.comandante.creeper.command.admin;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.player.PlayerRole;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class SaveWorldCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("saveworld");
    final static String description = "Saves the current world to disk.";
    final static boolean isAdminOnly = true;


    public SaveWorldCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, isAdminOnly);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (!hasRole(PlayerRole.ADMIN)){
                return;
            }
            worldExporter.saveWorld();
            write("World saved.");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
