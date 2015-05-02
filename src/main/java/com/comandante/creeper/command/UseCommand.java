package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseHandler;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UseCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("use");
    final static String description = "Use an item.";
    final static String correctUsage = "use <item name>";

    public UseCommand(GameManager gameManager) {
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
            Set<Item> inventory = entityManager.getInventory(player);
            if (inventory != null) {
                for (Item item : inventory) {
                    if (item.getItemTriggers().contains(itemTarget)) {
                        new ItemUseHandler(item, creeperSession, gameManager, player).handle();
                        return;
                    }
                }
                new ItemUseHandler(ItemType.UNKNOWN.create(), creeperSession, gameManager, player).handle();
            } else {
                write("Your inventory is empty.");
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
