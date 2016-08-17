package com.comandante.creeper.Items.use;

import com.comandante.creeper.Items.Effect;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseAction;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.LightningSpellRunnable;

import java.util.Set;

public class LightningSpellBookUseAction implements ItemUseAction {

    private final ItemType itemType;

    public LightningSpellBookUseAction(ItemType itemType) {
        this.itemType = itemType;
    }

    private Boolean dontDelete = Boolean.FALSE;

    @Override
    public Integer getItemTypeId() {
        return itemType.getItemTypeCode();
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item) {
        if (player.getLearnedSpells().contains(LightningSpellRunnable.name)) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You already know how to use " + LightningSpellRunnable.name);
            dontDelete = true;
            return;
        }
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " reads a leatherbound aging spell book and gains knowledge about lightning spells.");
        player.addLearnedSpellByName(LightningSpellRunnable.name);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
        if (!dontDelete) {
            player.removeInventoryId(item.getItemId());
            gameManager.getEntityManager().removeItem(item);
        }
    }

    @Override
    public Set<Effect> getEffects() {
        return null;
    }
}
