package com.comandante.creeper.items;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;

import java.util.Set;

public interface ItemUseAction {
    Integer getItemTypeId();

    void executeAction(GameManager gameManager, Player player, Item item);

    void postExecuteAction(GameManager gameManager, Player player, Item item);

    Set<Effect> getEffects();

}
