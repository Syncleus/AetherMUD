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

import java.util.Optional;
import java.util.Set;

public class StickOfJusticeUseAction implements ItemUseAction {

    private final ItemMetadata itemMetadata;

    public StickOfJusticeUseAction(ItemMetadata itemMetadata) {
        this.itemMetadata = itemMetadata;
    }

    @Override
    public String getInternalItemName() {
        return itemMetadata.getInternalItemName();
    }


    @Override
    public void executeAction(GameManager gameManager, Player player, ItemPojo item, UseCommand.UseItemOn useItemOn) {

        if (!useItemOn.getTarget().isPresent()) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You must use the Stick Of Justice on someone who deserves it.");
            return;
        }

        Optional<Player> playerByCommandTarget = gameManager.getPlayerManager().getPlayerByCommandTarget(player.getCurrentRoom(), useItemOn.getTarget().get());
        if (!playerByCommandTarget.isPresent()) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You must use the Stick Of Justice on someone who deserves it.");
            return;
        }

        Player targetedPlayer = playerByCommandTarget.get();

        gameManager.detainPlayer(targetedPlayer);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, ItemPojo item) {
    }

    @Override
    public Set<EffectPojo> getEffects() {
        return null;
    }
}
