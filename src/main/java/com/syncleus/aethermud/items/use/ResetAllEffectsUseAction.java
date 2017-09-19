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
import com.syncleus.aethermud.items.*;
import com.syncleus.aethermud.player.Player;

import java.util.Set;

public class ResetAllEffectsUseAction implements ItemUseAction {

    private final Item item;

    public ResetAllEffectsUseAction(Item item) {
        this.item = item;
    }

    @Override
    public String getInternalItemName() {
        return item.getInternalItemName();
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, ItemInstance item, UseCommand.UseItemOn useItemOn) {
        player.resetEffects();
        gameManager.getChannelUtils().write(player.getPlayerId(), "All Effects are removed." + "\r\n");
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, ItemInstance item) {
        ItemUseHandler.incrementUses(item);
        if (item.isDisposable()) {
            if (item.getNumberOfUses() < item.getMaxUses()) {
                gameManager.getEntityManager().saveItem(item);
            } else {
                player.removeInventoryId(item.getItemId());
                gameManager.getEntityManager().removeItem(item);
            }
        }
    }

    @Override
    public Set<Effect> getEffects() {
        return null;
    }

}
