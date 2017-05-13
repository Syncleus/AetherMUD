package com.comandante.creeper.items;


import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.use.DefaultApplyEffectAction;
import com.comandante.creeper.items.use.LightningSpellBookUseAction;
import com.comandante.creeper.items.use.StickOfJusticeUseAction;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.stats.StatsBuilder;
import org.apache.log4j.Logger;

import java.util.Optional;

public class ItemUseHandler {

    private static final Logger log = Logger.getLogger(ItemUseHandler.class);
    private GameManager gameManager;

    public ItemUseHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handle(Player player, Item item, UseCommand.UseItemOn useItemOn) {
        ItemUseAction itemUseAction = null;
        Optional<ItemMetadata> itemMetadataOptional = gameManager.getItemStorage().get(item.getInternalItemName());
        if (!itemMetadataOptional.isPresent()) {
            return;
        }
        ItemMetadata itemMetadata = itemMetadataOptional.get();
        switch (itemMetadata.getInternalItemName()) {
            case "Lighting Spell":
                itemUseAction = new LightningSpellBookUseAction(itemMetadata);
                break;
            case "Purple Drank":
                itemUseAction = new DefaultApplyEffectAction(itemMetadata);
                break;
            case "Marijuana":
                itemUseAction = new DefaultApplyEffectAction(itemMetadata);
                break;
            case "Small Health Potion":
                itemUseAction = new DefaultApplyEffectAction(itemMetadata);
                break;
            case "Stick Of Justice":
                itemUseAction = new StickOfJusticeUseAction(itemMetadata);
        }
        if (itemUseAction == null && item.getEffects() != null && item.getEffects().size() > 0) {
            itemUseAction = new DefaultApplyEffectAction(itemMetadata);
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

