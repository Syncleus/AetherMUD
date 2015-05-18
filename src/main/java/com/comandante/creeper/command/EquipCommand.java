package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class EquipCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("equip");
    final static String description = "Equip an item.";
    final static String correctUsage = "equip <equipment item name>";


    public EquipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() == 1) {
                write("No equipment item specified.");
                return;
            }
            originalMessageParts.remove(0);
            String itemTarget = Joiner.on(" ").join(originalMessageParts);
            List<Item> inventory = entityManager.getInventory(player);
            if (inventory != null) {
                for (Item item : inventory) {
                    if (item.getItemTriggers().contains(itemTarget)) {
                        if (item.getEquipment() == null) {
                            write("Item is not equipable.");
                            return;
                        }
                        equipmentManager.equip(player, item);
                        return;
                    }
                }
            } else {
                write("Your inventory is empty.");
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}