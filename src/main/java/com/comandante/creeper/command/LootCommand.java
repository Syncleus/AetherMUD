package com.comandante.creeper.command;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

public class LootCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("loot");
    final static String description = "Loot a corpse.";
    final static String correctUsage = "loot corpse";

    public LootCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() > 1) {
                player.getInventory().forEach(item -> {
                    if (item.getItemTypeId() == Item.CORPSE_ID_RESERVED) {
                        Loot loot = item.getLoot();
                        if (loot != null) {
                            long gold = lootManager.lootGoldAmountReturn(loot);
                            if (gold > 0) {
                                write("You looted " + NumberFormat.getNumberInstance(Locale.US).format(gold) + Color.YELLOW + " gold" + Color.RESET + " from a " + item.getItemName() + ".\r\n");
                                player.incrementGold(gold);
                            }
                            Set<Item> items = lootManager.lootItemsReturn(loot);
                            items.forEach(i -> {
                                gameManager.acquireItem(player, i.getItemId());
                                write("You looted " + i.getItemName() + " from a " + item.getItemName() + ".\r\n");
                            });
                            if (gold < 0 && items.size() == 0) {
                                write("You looted nothing from " + item.getItemName() + "\r\n");
                            }
                        }
                        player.removeInventoryId(item.getItemId());
                        entityManager.removeItem(item);
                    }
                });
            }
        });
    }
}
