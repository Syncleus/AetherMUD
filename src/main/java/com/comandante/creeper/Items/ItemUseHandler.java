package com.comandante.creeper.Items;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.spells.Effect;
import org.apache.log4j.Logger;

import java.util.Set;

public class ItemUseHandler {

    private static final Logger log = Logger.getLogger(ItemUseHandler.class);
    private GameManager gameManager;

    public ItemUseHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handle(Player player, Item item) {
        ItemUseAction itemUseAction = ItemUseRegistry.getItemUseAction(item.getItemTypeId());
        if (itemUseAction != null) {
            itemUseAction.executeAction(gameManager, player, item);
            itemUseAction.postExecuteAction(gameManager, player, item);
        }
    }
}
