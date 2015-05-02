package com.comandante.creeper.command.admin;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class InfoCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("info");
    final static String description = "Get additional info about a current room.";
    final static String correctUsage = "info";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public InfoCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            write("roomId: " + currentRoom.getRoomId());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}