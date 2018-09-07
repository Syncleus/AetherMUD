/**
 * Copyright 2017 - 2018 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.items;


import com.syncleus.aethermud.command.commands.UseCommand;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.use.DefaultApplyEffectsStats;
import com.syncleus.aethermud.items.use.LightningSpellBookUseAction;
import com.syncleus.aethermud.items.use.StickOfJusticeUseAction;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import org.apache.log4j.Logger;

import java.util.Optional;

public class ItemUseHandler {

    private static final Logger log = Logger.getLogger(ItemUseHandler.class);
    private GameManager gameManager;

    public ItemUseHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handle(Player player, ItemInstance itemInstance, UseCommand.UseItemOn useItemOn) {
        ItemUseAction itemUseAction = null;
        Item item;
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            Optional<ItemData> itemOptional = tx.getStorage().getItem(itemInstance.getInternalItemName());
            if (!itemOptional.isPresent()) {
                return;
            }
            item = ItemData.copyItem(itemOptional.get());
        }
        switch (item.getInternalItemName()) {
            case "lightning spellbook":
                itemUseAction = new LightningSpellBookUseAction(item);
                break;
            case "stick of justice":
                itemUseAction = new StickOfJusticeUseAction(item);
                break;
            default:
                if ((itemInstance.getEffects() != null && itemInstance.getEffects().size() > 0) || (itemInstance.getItemApplyStats() != null)) {
                    itemUseAction = new DefaultApplyEffectsStats(item);
                }
                break;
        }
        if (itemUseAction != null) {
            itemUseAction.executeAction(gameManager, player, itemInstance, useItemOn);
            itemUseAction.postExecuteAction(gameManager, player, itemInstance);
        }
    }

    public static void incrementUses(ItemInstance item) {
        item.setNumberOfUses(item.getNumberOfUses() + 1);
    }
}
