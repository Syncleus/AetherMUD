package com.comandante.creeper.command.commands.admin;

import com.comandante.creeper.command.commands.Command;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RestartCommand extends Command {



    final static List<String> validTriggers = Arrays.asList("restart");
    final static String description = "restart server.";
    final static String correctUsage = "restart";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.GOD);

    public RestartCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandThreadSafe(ctx, e, BounceIrcBotCommand.class, () -> {
            gameManager.getMapDBCreeperStorage().stopAsync();
            gameManager.getMapDBCreeperStorage().awaitTerminated();
            System.exit(0);
        });
    }
}