package com.comandante.creeper.command;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

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
            List<Item> inventory = player.getInventory();
            if (inventory == null) {
                write("You aren't carrying anything.");
                return;
            }
            StringBuilder inventoryString = new StringBuilder();
            inventoryString.append("You are carrying:\r\n");
            inventoryString.append(RESET);
            String join = StringUtils.join(player.getRolledUpIntentory().toArray(), "\r\n");
            inventoryString.append(join);
            write(inventoryString.toString());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
