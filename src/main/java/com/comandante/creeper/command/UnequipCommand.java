package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UnequipCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("unequip");
    final static String description = "Un-Equip an item.";
    final static String correctUsage = "unequip <equipment item name>";

    public UnequipCommand(GameManager gameManager) {
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
            Set<Item> equipment = entityManager.getEquipment(player);
            for (Item item : equipment) {
                if (item.getItemTriggers().contains(itemTarget)) {
                    equipmentManager.unEquip(player, item);
                    return;
                }
            }
            write("Item is not currently equipped.");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}