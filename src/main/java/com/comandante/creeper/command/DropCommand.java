package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class DropCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("drop", "d");
    final static String description = "Drop an item.";
    final static String correctUsage = "drop <item name>";

    public DropCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() == 1) {
                write("No item specified.");
                return;
            }
            originalMessageParts.remove(0);
            String itemTarget = Joiner.on(" ").join(originalMessageParts);
            for (Item item : entityManager.getInventory(player)) {
                if (item.getItemTriggers().contains(itemTarget)) {
                    item.setWithPlayer(false);
                    gameManager.placeItemInRoom(currentRoom.getRoomId(), item.getItemId());
                    player.removeInventoryId(item.getItemId());
                    gameManager.getItemDecayManager().addItem(item);
                    entityManager.saveItem(item);
                    gameManager.roomSay(currentRoom.getRoomId(), player.getPlayerName() + " dropped " + item.getItemName(), playerId);
                    return;
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
