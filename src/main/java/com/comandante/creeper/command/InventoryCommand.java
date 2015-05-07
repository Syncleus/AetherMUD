package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.comandante.creeper.server.Color.RESET;

public class InventoryCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("inventory", "i");
    final static String description = "View your inventory.";
    final static String correctUsage = "inventory";

    public InventoryCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            Set<Item> inventory = entityManager.getInventory(player);
            if (inventory == null) {
                write("You aren't carrying anything.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("You are carrying:\r\n");
            sb.append(RESET);
            for (Item item : inventory) {
                sb.append(item.getItemName());
                int maxUses = ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses();
                if (maxUses > 0) {
                    int remainingUses = maxUses - item.getNumberOfUses();
                    sb.append(" - ").append(remainingUses);
                    if (remainingUses == 1) {
                        sb.append(" use left.");
                    } else {
                        sb.append(" uses left.");
                    }
                }
                sb.append("\r\n");
            }
            write(sb.toString());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
