package com.comandante.creeper.Items;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.Effect;

import java.util.Set;

public interface ItemUseAction {
    public Integer getItemTypeId();

    public void executeAction(GameManager gameManager, Player player, Item item);

    public void postExecuteAction(GameManager gameManager, Player player, Item item);

    public Set<Effect> getEffects();

}
