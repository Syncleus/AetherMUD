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
import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.items.ItemUseAction;
import com.syncleus.aethermud.player.Player;

import java.util.Optional;
import java.util.Set;

public class StickOfJusticeUseAction implements ItemUseAction {

    private final Item item;

    public StickOfJusticeUseAction(Item item) {
        this.item = item;
    }

    @Override
    public String getInternalItemName() {
        return item.getInternalItemName();
    }


    @Override
    public void executeAction(GameManager gameManager, Player player, ItemInstance item, UseCommand.UseItemOn useItemOn) {

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
    public void postExecuteAction(GameManager gameManager, Player player, ItemInstance item) {
    }

    @Override
    public Set<Effect> getEffects() {
        return null;
    }
}
