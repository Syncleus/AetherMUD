/**
 * Copyright 2017 Syncleus, Inc.
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
package com.syncleus.aethermud.items.use;

import com.syncleus.aethermud.command.commands.UseCommand;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.EffectPojo;
import com.syncleus.aethermud.items.ItemPojo;
import com.syncleus.aethermud.items.ItemMetadata;
import com.syncleus.aethermud.items.ItemUseAction;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.spells.LightningSpell;

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
    public void executeAction(GameManager gameManager, Player player, ItemPojo item, UseCommand.UseItemOn useItemOn) {
        if (player.getLearnedSpells().contains(LightningSpell.name)) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You already know how to use " + LightningSpell.name);
            dontDelete = true;
            return;
        }
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " reads a leatherbound aging spell book and gains knowledge about lightning spells.");
        player.addLearnedSpellByName(LightningSpell.name);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, ItemPojo item) {
        if (!dontDelete) {
            player.removeInventoryId(item.getItemId());
            gameManager.getEntityManager().removeItem(item);
        }
    }

    @Override
    public Set<EffectPojo> getEffects() {
        return null;
    }
}
