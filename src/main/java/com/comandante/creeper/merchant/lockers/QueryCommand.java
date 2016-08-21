package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class QueryCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("query", "q");
    final static String description = "List your items.";

    public QueryCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            write("----LOCKER ITEMS\r\n");
            for (String rolledUpInvLine: player.getRolledUpLockerInventory()) {
                write(rolledUpInvLine);;
            }
            write("\r\n\r\n----PERSONAL INVENTORY\r\n");
            for (String rolledUpInvLine: player.getRolledUpIntentory()) {
                write(rolledUpInvLine);
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}