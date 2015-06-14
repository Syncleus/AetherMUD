package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class DropOffCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("drop", "d", "dropoff");
    final static String description = "Drop off an item in your locker.";

    public DropOffCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            originalMessageParts.remove(0);
            String desiredDropOffItem = Joiner.on(" ").join(originalMessageParts);
            for (Item item : gameManager.getEntityManager().getInventory(player)) {
                if (item.getItemTriggers().contains(desiredDropOffItem)) {
                    item.setWithPlayer(false);
                    gameManager.transferItemToLocker(player, item.getItemId());
                    gameManager.getEntityManager().saveItem(item);
                    write(item.getItemName() + " has been transfered to your locker.\r\n");
                    return;
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}