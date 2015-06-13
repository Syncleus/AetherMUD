package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kearney on 6/12/15.
 */
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
            for (String lockerItemId: playerMetadata.getLockerInventory()) {
                Item itemEntity = gameManager.getEntityManager().getItemEntity(lockerItemId);
                write(itemEntity.getItemName() + "\r\n");
            }
            write("----PERSONAL INVENTORY\r\n");
            for (String inventoryId: playerMetadata.getInventory()) {
                Item itemEntity = gameManager.getEntityManager().getItemEntity(inventoryId);
                write(itemEntity.getItemName() + "\r\n");
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}