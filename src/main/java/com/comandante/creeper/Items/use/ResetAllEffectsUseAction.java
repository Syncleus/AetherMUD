package com.comandante.creeper.items.use;

import com.comandante.creeper.items.*;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;

import java.util.Set;

public class ResetAllEffectsUseAction implements ItemUseAction {

    private final ItemType itemType;

    public ResetAllEffectsUseAction(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Integer getItemTypeId() {
        return itemType.getItemTypeCode();
    }


    @Override
    public void executeAction(GameManager gameManager, Player player, Item item) {
        player.resetEffects();
        gameManager.getChannelUtils().write(player.getPlayerId(), "All Effects are removed." + "\r\n");
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
        ItemUseHandler.incrementUses(item);
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
        return null;
    }

}
