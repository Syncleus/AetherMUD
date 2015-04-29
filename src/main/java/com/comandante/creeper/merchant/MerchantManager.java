package com.comandante.creeper.merchant;


import com.codahale.metrics.Timer;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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
                    int price = next.getValue().getCost();
                    int availableGold = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getGold();
                    if (availableGold >= price) {
                        Item item = next.getValue().getItem().create();
                        gameManager.getEntityManager().addItem(item);
                        gameManager.acquireItem(player, item.getItemId());
                        gameManager.getPlayerManager().incrementGold(player.getPlayerId(), -price);
                        gameManager.getChannelUtils().write(player.getPlayerId(), "You have purchased: " + item.getItemName() + "\r\n");
                    } else {
                        gameManager.getChannelUtils().write(player.getPlayerId(), "You can't afford: " + next.getValue().getItem().getItemName() + "\r\n");
                    }
                }
            }
        } finally {
            context.stop();
        }
    }

    public void purchaseItems(Merchant merchant, int itemNo, Player player, int amt) {
        final Timer.Context context = responses.time();
        try {
            Iterator<Map.Entry<Integer, MerchantItemForSale>> merchantItemForSales = merchant.getMerchantItemForSales().entrySet().iterator();
            while (merchantItemForSales.hasNext()) {
                Map.Entry<Integer, MerchantItemForSale> next = merchantItemForSales.next();
                if (next.getKey().equals(itemNo)) {
                    int price = next.getValue().getCost() * amt;
                    int availableGold = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getGold();
                    if (availableGold >= price) {
                        Item item = next.getValue().getItem().create();
                        gameManager.getEntityManager().addItem(item);
                        gameManager.acquireItem(player, item.getItemId());
                        gameManager.getPlayerManager().incrementGold(player.getPlayerId(), -price);
                        gameManager.getChannelUtils().write(player.getPlayerId(), "You have purchased: " + item.getItemName() + "\r\n");
                    } else {
                        gameManager.getChannelUtils().write(player.getPlayerId(), "You can't afford: " + next.getValue().getItem().getItemName() + "\r\n");
                    }
                }
            }
        } finally {
            context.stop();
        }
    }
}
