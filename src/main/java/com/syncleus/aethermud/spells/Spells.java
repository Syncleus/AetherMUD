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
package com.syncleus.aethermud.spells;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.player.Player;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.util.Optional;

public class Spells {

    private GameManager gameManager;

    private static final Logger log = Logger.getLogger(Spells.class);

    public Spells(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void executeSpell(Player sourcePlayer, Optional<Npc> destinationNpc, Optional<Player> destinationPlayer, SpellRunnable spellRunnable) {
        try {
            spellRunnable.run(sourcePlayer, destinationNpc, destinationPlayer, gameManager);
        } catch (Exception e) {
            log.error("Problem executing spell.", e);
        }
    }

    public Optional<SpellRunnable> getSpellRunnable(String triggerName) {
        if (Sets.newHashSet("lightning", "l").contains(triggerName)) {
            return Optional.of(new LightningSpell(gameManager));
        }
        return Optional.empty();
    }
}


