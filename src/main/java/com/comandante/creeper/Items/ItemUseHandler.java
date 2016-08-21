package com.comandante.creeper.items;


import com.comandante.creeper.items.use.DefaultApplyStatsAction;
import com.comandante.creeper.items.use.LightningSpellBookUseAction;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.stats.StatsBuilder;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

public class ItemUseHandler {

    private static final Logger log = Logger.getLogger(ItemUseHandler.class);
    private GameManager gameManager;

    public ItemUseHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handle(Player player, Item item) {
        ItemUseAction itemUseAction = null;
        switch (ItemType.itemTypeFromCode(item.getItemTypeId())) {
            case LIGHTNING_SPELLBOOKNG:
                itemUseAction = new LightningSpellBookUseAction(ItemType.LIGHTNING_SPELLBOOKNG);
                break;
            case PURPLE_DRANK:
                itemUseAction = new DefaultApplyStatsAction(ItemType.PURPLE_DRANK, buildStats(500, 0), Sets.newHashSet());
                break;
            case MARIJUANA:
                itemUseAction = new DefaultApplyStatsAction(ItemType.MARIJUANA, buildStats(500, 500), Sets.newHashSet());
                break;
            case SMALL_HEALTH_POTION:
                itemUseAction = new DefaultApplyStatsAction(ItemType.SMALL_HEALTH_POTION, buildStats(100, 0), Sets.newHashSet());
                break;
        }
        if (itemUseAction != null) {
            itemUseAction.executeAction(gameManager, player, item);
            itemUseAction.postExecuteAction(gameManager, player, item);
        }
    }

    private static Stats buildStats(int health, int mana) {
        StatsBuilder statsBuilder = new StatsBuilder();
        statsBuilder.setCurrentHealth(health);
        statsBuilder.setCurrentMana(mana);
        return statsBuilder.createStats();
    }

    public static void incrementUses(Item item) {
        item.setNumberOfUses(item.getNumberOfUses() + 1);
    }
}

