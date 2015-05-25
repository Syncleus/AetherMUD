package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.managers.GameManager;
import com.google.common.collect.Maps;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            List<Item> inventory = entityManager.getInventory(player);
            if (inventory == null) {
                write("You aren't carrying anything.");
                return;
            }
            StringBuilder inventoryString = new StringBuilder();
            inventoryString.append("You are carrying:\r\n");
            inventoryString.append(RESET);
            Map<String, Integer> itemAndCounts = Maps.newHashMap();
            for (Item item : inventory) {
                StringBuilder invItem = new StringBuilder();
                invItem.append(item.getItemName());
                int maxUses = ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses();
                if (maxUses > 0) {
                    int remainingUses = maxUses - item.getNumberOfUses();
                    invItem.append(" - ").append(remainingUses);
                    if (remainingUses == 1) {
                        invItem.append(" use left.");
                    } else {
                        invItem.append(" uses left.");
                    }
                }
                if (itemAndCounts.containsKey(invItem.toString())) {
                    Integer integer = itemAndCounts.get(invItem.toString());
                    integer = integer + 1;
                    itemAndCounts.put(invItem.toString(), integer);
                } else {
                    itemAndCounts.put(invItem.toString(), 1);
                }
            }
            for (Map.Entry<String, Integer> next : itemAndCounts.entrySet()) {
                if (next.getValue() > 1) {
                    inventoryString.append(next.getKey()).append(" (").append(next.getValue()).append(")").append("\r\n");
                } else {
                    inventoryString.append(next.getKey()).append("\r\n");
                }
            }
            write(inventoryString.toString());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
