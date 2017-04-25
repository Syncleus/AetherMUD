package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.items.Item;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;


public class GetCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("get", "g");
    final static String description = "Get an item from your locker.t";

    public GetCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            originalMessageParts.remove(0);
            String desiredRetrieveOption = Joiner.on(" ").join(originalMessageParts);
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            for (String entityId: playerMetadata.getLockerInventory()) {
                Item itemEntity = gameManager.getEntityManager().getItemEntity(entityId);
                if (itemEntity.getItemTriggers().contains(desiredRetrieveOption)) {
                    player.transferItemFromLocker(entityId);
                    write(itemEntity.getItemName() + " retrieved from locker.\r\n");
                    return;
                }
            }
            write("Item not found in locker.\r\n");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}