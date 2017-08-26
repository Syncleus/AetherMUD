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
package com.syncleus.aethermud.items.use;

import com.syncleus.aethermud.command.commands.UseCommand;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.*;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.stats.Stats;
import org.apache.log4j.Logger;

import java.util.Set;

public class DefaultApplyEffectsStats implements ItemUseAction {

    private final String internalItemName;
    private final Set<Effect> effectSet;
    private final Stats itemApplyStats;
    private static final Logger log = Logger.getLogger(DefaultApplyEffectsStats.class);

    public DefaultApplyEffectsStats(ItemMetadata itemMetadata) {
        this.internalItemName = itemMetadata.getInternalItemName();
        this.effectSet = itemMetadata.getEffects();
        this.itemApplyStats = itemMetadata.getItemApplyStats();
    }

    @Override
    public String getInternalItemName() {
        return internalItemName;
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item, UseCommand.UseItemOn useItemOn) {
        String playerName = player.getPlayerName();

        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), playerName + " uses " + item.getItemName() + ".\r\n");
        if (itemApplyStats.getCurrentMana() > 0) {
            gameManager.getChannelUtils().write(player.getPlayerId(), itemApplyStats.getCurrentMana() + " mana is restored." + "\r\n");
        }
        if (itemApplyStats.getCurrentHealth() > 0) {
            gameManager.getChannelUtils().write(player.getPlayerId(), itemApplyStats.getCurrentHealth() + " health is restored." + "\r\n");
        }
        player.addMana(itemApplyStats.getCurrentMana());
        player.updatePlayerHealth(itemApplyStats.getCurrentHealth(), null);

        processEffects(gameManager, player, effectSet);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
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
        return effectSet;
    }

    public static void processEffects(GameManager gameManager, Player player, Set<Effect> effects) {
        if (effects == null) {
            return;
        }
        for (Effect effect : effects) {
            Effect nEffect = new Effect(effect);
            nEffect.setPlayerId(player.getPlayerId());
            boolean effectResult = player.addEffect(nEffect);
            if (effect.getDurationStats() != null) {
                if (effect.getDurationStats().getCurrentHealth() < 0) {
                    log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                    continue;
                }
            }
            if (effectResult) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "You feel " + effect.getEffectName() + "\r\n");
            } else {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Unable to apply effect.\r\n");
            }
        }
    }
}
