package com.comandante.creeper.command.commands;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class UseCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("use");
    final static String description = "Use an item.";
    final static String correctUsage = "use <item name>";

    public UseCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() == 1) {
                write("No item specified.");
                return;
            }
            originalMessageParts.remove(0);
            String itemTarget = Joiner.on(" ").join(originalMessageParts);
            Item inventoryItem = player.getInventoryItem(itemTarget);
            if (inventoryItem == null) {
                write("Useable item is not found in your inventory.\r\n");
                return;
            }
            gameManager.getItemUseHandler().handle(player, inventoryItem);
        });
    }
}
