package com.comandante.creeper.merchant;


import com.codahale.metrics.Timer;
import com.comandante.creeper.Main;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemBuilder;
import com.comandante.creeper.items.ItemMetadata;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMetadata;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static com.codahale.metrics.MetricRegistry.name;

public class MerchantManager {

    private final GameManager gameManager;
    private final Timer responses = Main.metrics.timer(name(MerchantManager.class, "purchase_time"));


    public MerchantManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void purchaseItem(Merchant merchant, int itemNo, Player player) {
        final Timer.Context context = responses.time();
        try {
            Iterator<Map.Entry<Integer, MerchantItemForSale>> merchantItemForSales = merchant.getMerchantItemForSales().entrySet().iterator();
            while (merchantItemForSales.hasNext()) {
                Map.Entry<Integer, MerchantItemForSale> next = merchantItemForSales.next();
                if (next.getKey().equals(itemNo)) {
                    String internalItemName = next.getValue().getInternalItemName();
                    Optional<ItemMetadata> itemMetadataOptional = gameManager.getItemStorage().get(internalItemName);
                    if (!itemMetadataOptional.isPresent()) {
                        continue;
                    }
                    ItemMetadata itemMetadata = itemMetadataOptional.get();
                    long maxInventorySize = player.getPlayerStatsWithEquipmentAndLevel().getInventorySize();
                    if (player.getInventory().size() >= maxInventorySize) {
                        gameManager.getChannelUtils().write(player.getPlayerId(), "Your inventory is full, drop some items and come back.\r\n");
                        return;
                    }
                    int price = next.getValue().getCost();
                    Optional<PlayerMetadata> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId());
                    if (!playerMetadataOptional.isPresent()) {
                        continue;
                    }
                    PlayerMetadata playerMetadata = playerMetadataOptional.get();
                    long availableGold = playerMetadata.getGold();
                    if (availableGold >= price) {
                        Item item = new ItemBuilder().from(itemMetadata).create();
                        gameManager.getEntityManager().saveItem(item);
                        gameManager.acquireItem(player, item.getItemId());
                        player.incrementGold(-price);
                        gameManager.getChannelUtils().write(player.getPlayerId(), "You have purchased: " + item.getItemName() + "\r\n");
                    } else {
                        gameManager.getChannelUtils().write(player.getPlayerId(), "You can't afford: " + itemMetadata.getItemName() + "\r\n");
                    }
                }
            }
        } finally {
            context.stop();
        }
    }
}
