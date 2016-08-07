package com.comandante.creeper.command.admin;

import com.comandante.creeper.ConfigureNpc;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;



public class ReloadNpcsCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("reloadnpcs");
    final static String description = "Reload npcs from disk.";
    final static String correctUsage = "reloadnpcs";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public ReloadNpcsCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ;
        try {
            gameManager.removeAllNpcs();
            ConfigureNpc.configureAllNpcs(gameManager);
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}