package com.comandante.creeper.items.use;

import com.comandante.creeper.items.Effect;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemType;
import com.comandante.creeper.items.ItemUseAction;
import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.LightningSpell;

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
    public void executeAction(GameManager gameManager, Player player, Item item, UseCommand.UseItemOn useItemOn) {
        if (player.getLearnedSpells().contains(LightningSpell.name)) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You already know how to use " + LightningSpell.name);
            dontDelete = true;
            return;
        }
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " reads a leatherbound aging spell book and gains knowledge about lightning spells.");
        player.addLearnedSpellByName(LightningSpell.name);
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
