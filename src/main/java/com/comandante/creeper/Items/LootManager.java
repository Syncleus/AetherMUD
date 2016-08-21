package com.comandante.creeper.Items;

import com.comandante.creeper.core_game.GameManager;
import com.google.common.collect.Sets;
import org.apache.commons.lang.math.JVMRandom;

import java.util.Random;
import java.util.Set;

public class LootManager {

    private final GameManager gameManager;

    public LootManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private final Random  random = new Random();

    private long randInt(long min, long max) {
        return JVMRandom.nextLong((max - min) + 1) + min;
    }

    public boolean lootDropSuccess(double percent) {
        double rangeMin = 0;
        double rangeMax = 100;
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        return randomValue <= percent;
    }

    public long lootGoldAmountReturn(Loot loot) {
        return randInt(loot.getLootGoldMin(), loot.getLootGoldMax());
    }

    public Set<Item> lootItemsReturn(Loot loot) {
        Set<Item> lootItems = Sets.newHashSet();
        for (ItemType item: loot.getItems()) {
            if (lootDropSuccess(item.getRarity().getPercentToLoot())) {
                Item i = item.create();
                gameManager.getEntityManager().saveItem(i);
                lootItems.add(i);
            }
        }
        return lootItems;
    }
}
