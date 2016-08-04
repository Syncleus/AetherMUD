package com.comandante.creeper.Items.use;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseAction;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcStatsChangeBuilder;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.spells.LightningSpell;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Room;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class LightningSpellBookUseAction implements ItemUseAction {

    private final ItemType itemType;

    public LightningSpellBookUseAction(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Integer getItemTypeId() {
        return itemType.getItemTypeCode();
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item) {
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " reads a leatherbound aging spell book and gains knowledge about lightning spells.");
        player.addLearnedSpellByName(LightningSpell.NAME);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
        player.removeInventoryId(item.getItemId());
        gameManager.getEntityManager().removeItem(item);
    }

    @Override
    public Set<Effect> getEffects() {
        return null;
    }
}
