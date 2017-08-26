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
package com.comandante.creeper.items.use;

import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Effect;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemMetadata;
import com.comandante.creeper.items.ItemUseAction;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.LightningSpell;

import java.util.Set;

public class LightningSpellBookUseAction implements ItemUseAction {

    private final ItemMetadata itemMetadata;

    public LightningSpellBookUseAction(ItemMetadata itemMetadata) {
        this.itemMetadata = itemMetadata;
    }

    private Boolean dontDelete = Boolean.FALSE;

    @Override
    public String getInternalItemName() {
        return itemMetadata.getInternalItemName();
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
