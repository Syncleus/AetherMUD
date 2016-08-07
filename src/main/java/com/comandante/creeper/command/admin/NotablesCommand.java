package com.comandante.creeper.command.admin;

import com.comandante.creeper.command.Command;
import com.comandante.creeper.command.CommandRunnable;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class NotablesCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("notables");
    final static String description = "Display list of notables in area.";
    final static String correctUsage = "notables";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public NotablesCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            for (Map.Entry<String, String> notable : currentRoom.getNotables().entrySet()) {
                write(notable.getKey() + " : " + notable.getValue() + "\r\n");
            }
        });
    }
}
