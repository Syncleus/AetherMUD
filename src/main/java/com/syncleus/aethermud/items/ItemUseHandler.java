/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
import org.apache.log4j.Logger;

import java.util.Optional;

public class ItemUseHandler {

    private static final Logger log = Logger.getLogger(ItemUseHandler.class);
    private GameManager gameManager;

    public ItemUseHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void handle(Player player, Item item, UseCommand.UseItemOn useItemOn) {
        ItemUseAction itemUseAction = null;
        Optional<ItemMetadata> itemMetadataOptional = gameManager.getItemStorage().get(item.getInternalItemName());
        if (!itemMetadataOptional.isPresent()) {
            return;
        }
        ItemMetadata itemMetadata = itemMetadataOptional.get();
        switch (itemMetadata.getInternalItemName()) {
            case "lightning spellbook":
                itemUseAction = new LightningSpellBookUseAction(itemMetadata);
                break;
            case "stick of justice":
                itemUseAction = new StickOfJusticeUseAction(itemMetadata);
                break;
            default:
                if ((item.getEffects() != null && item.getEffects().size() > 0) || (item.getItemApplyStats() != null)) {
                    itemUseAction = new DefaultApplyEffectsStats(itemMetadata);
                }
                break;
        }
        if (itemUseAction != null) {
            itemUseAction.executeAction(gameManager, player, item, useItemOn);
            itemUseAction.postExecuteAction(gameManager, player, item);
        }
    }

    public static void incrementUses(Item item) {
        item.setNumberOfUses(item.getNumberOfUses() + 1);
    }
}

