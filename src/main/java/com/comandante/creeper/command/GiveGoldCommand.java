package com.comandante.creeper.command;

import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GiveGoldCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("givegold");
    final static String description = "Give Gold to a Player";
    final static String correctUsage = "givegold <player name> <amt>";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);


    public GiveGoldCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (!player.getPlayerName().equals("fibs")) {
                write("This attempt to cheat has been logged.");
                return;
            }
            if (originalMessageParts.size() > 2) {
                String destinationPlayerName = originalMessageParts.get(1);
                String amt = originalMessageParts.get(2);
                if (!isInteger(amt)) {
                    write("Third option to givegold needs to be an integer amount.");
                    return;
                }
                PlayerMetadata destinationPlayer = playerManager.getPlayerMetadata(Main.createPlayerId(destinationPlayerName));
                if (destinationPlayer == null) {
                    write("Player does not exist.");
                    return;
                }
                playerManager.incrementGold(destinationPlayer.getPlayerId(), Integer.parseInt(amt));
                write("The amount of " + amt + " gold has been placed into " + destinationPlayerName + "'s inventory.");
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}