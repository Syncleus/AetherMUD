package com.comandante.creeper.Items.use;

import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.Items.*;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;

import java.util.Set;

public class DefaultApplyStatsAction implements ItemUseAction {

    private final Integer itemTypeId;
    private final Stats stats;
    private final Set<Effect> effectSet;

    public DefaultApplyStatsAction(ItemType itemType, Stats stats, Set<Effect> effects) {
        this.itemTypeId = itemType.getItemTypeCode();
        this.stats = stats;
        this.effectSet = effects;
    }

    @Override
    public Integer getItemTypeId() {
        return itemTypeId;
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item) {
        String playerName = player.getPlayerName();
        ItemType itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), playerName + " uses " + itemType.getItemName() + ".\r\n");
        if (stats.getCurrentMana() > 0) {
            gameManager.getChannelUtils().write(player.getPlayerId(), stats.getCurrentMana() + " mana is restored." + "\r\n");
        }
        if (stats.getCurrentHealth() > 0) {
            gameManager.getChannelUtils().write(player.getPlayerId(), stats.getCurrentHealth() + " health is restored." + "\r\n");
        }
        player.addMana(stats.getCurrentMana());
        player.updatePlayerHealth(stats.getCurrentHealth(), null);
        ItemUseRegistry.processEffects(gameManager, player, effectSet);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
        ItemUseRegistry.incrementUses(item);
        if (ItemType.itemTypeFromCode(item.getItemTypeId()).isDisposable()) {
            if (item.getNumberOfUses() < ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses()) {
                gameManager.getEntityManager().saveItem(item);
            } else {
                player.removeInventoryId(item.getItemId());
                gameManager.getEntityManager().removeItem(item);
            }
        }
    }

    @Override
    public Set<Effect> getEffects() {
        return effectSet;
    }
}
