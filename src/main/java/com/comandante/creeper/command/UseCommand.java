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

public class UseCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("use");
    final static String description = "Use an item.";
    private static final Logger log = Logger.getLogger(UseCommand.class);

    public UseCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
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
            String[] inventory = playerManager.getInventory(player);
            if (inventory != null) {
                for (String inventoryId : inventory) {
                    Item itemEntity = entityManager.getItemEntity(inventoryId);
                    if (itemEntity == null) {
                        log.info("THERE IS A BAD INENTORY ITEM TRYING TO BE USED BOOO");
                        continue;
                    }
                    if (itemEntity.getItemTriggers().contains(itemTarget)) {
                        new ItemUseHandler(itemEntity, creeperSession, gameManager, player).handle();
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
