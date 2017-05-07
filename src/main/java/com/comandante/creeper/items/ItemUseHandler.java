package com.comandante.creeper.items;


import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.use.DefaultApplyStatsAction;
import com.comandante.creeper.items.use.LightningSpellBookUseAction;
import com.comandante.creeper.items.use.StickOfJusticeUseAction;
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

    public void handle(Player player, Item item, UseCommand.UseItemOn useItemOn) {
        ItemUseAction itemUseAction = null;
        switch (ItemType.itemTypeFromCode(item.getItemTypeId())) {
            case LIGHTNING_SPELLBOOKNG:
                itemUseAction = new LightningSpellBookUseAction(ItemType.LIGHTNING_SPELLBOOKNG);
                break;
            case PURPLE_DRANK:
                itemUseAction = new DefaultApplyStatsAction(ItemType.PURPLE_DRANK, buildStats(120, 0), Sets.newHashSet());
                break;
            case MARIJUANA:
                itemUseAction = new DefaultApplyStatsAction(ItemType.MARIJUANA, buildStats(50, 50), Sets.newHashSet());
                break;
            case SMALL_HEALTH_POTION:
                itemUseAction = new DefaultApplyStatsAction(ItemType.SMALL_HEALTH_POTION, buildStats(25, 0), Sets.newHashSet());
                break;
            case STICK_OF_JUSTICE:
                itemUseAction = new StickOfJusticeUseAction(ItemType.STICK_OF_JUSTICE);
        }
        if (itemUseAction != null) {
            itemUseAction.executeAction(gameManager, player, item, useItemOn);
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

