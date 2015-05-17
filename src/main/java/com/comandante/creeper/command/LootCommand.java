package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class LootCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("loot");
    final static String description = "Loot a corpse.";
    final static String correctUsage = "loot corpse";

    public LootCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() > 1) {
                for (Item item : entityManager.getInventory(player)) {
                    if (item.getItemTypeId() == Item.CORPSE_ID_RESERVED) {
                        Loot loot = item.getLoot();
                        if (loot != null) {
                            int gold = lootManager.lootGoldAmountReturn(loot);
                            if (gold > 0) {
                                write("You looted " + gold + Color.YELLOW + " gold" + Color.RESET + " from a " + item.getItemName() + ".\r\n");
                                playerManager.incrementGold(player, gold);
                            } else {
                                write("You looted nothing from " + item.getItemName() + "\r\n");
                            }
                        }
                        playerManager.removeInventoryId(player, item.getItemId());
                        entityManager.removeItem(item);
                        return;
                    }
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}