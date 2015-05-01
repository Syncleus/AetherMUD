package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseHandler;
import com.comandante.creeper.managers.GameManager;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class EquipCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("equip");
    final static String description = "Equip an item.";

    public EquipCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
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
            String[] inventory = playerManager.getInventory(player);
            if (inventory != null) {
                for (String inventoryId : inventory) {
                    final Item itemEntity = entityManager.getItemEntity(inventoryId);
                    if (itemEntity.getItemTriggers().contains(itemTarget)) {
                        if (itemEntity.getEquipment() == null) {
                            write("Item is not equipable.");
                            return;
                        }
                        equipmentManager.equip(player, itemEntity);
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