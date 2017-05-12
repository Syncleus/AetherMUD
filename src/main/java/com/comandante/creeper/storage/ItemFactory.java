package com.comandante.creeper.storage;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemMetadata;

public class ItemFactory  {


    private final GameManager gameManager;

    public ItemFactory(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Item create(ItemMetadata itemMetadata) {

    }
}
