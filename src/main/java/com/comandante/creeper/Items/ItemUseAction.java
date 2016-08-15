package com.comandante.creeper.Items;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.Effect;

import java.util.Set;

public interface ItemUseAction {
    Integer getItemTypeId();

    void executeAction(GameManager gameManager, Player player, Item item);

    void postExecuteAction(GameManager gameManager, Player player, Item item);

    Set<Effect> getEffects();

}
