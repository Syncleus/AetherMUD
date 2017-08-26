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
package com.syncleus.aethermud.stats.modifier;


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.stats.Stats;

public class StatsModifierFactory {

    private final GameManager gameManager;

    public StatsModifierFactory(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Stats getStatsModifier(Player player) {
        StatsModifier modifer = new BasicPlayerLevelStatsModifier(gameManager);
        switch (player.getPlayerClass()) {
            case WARRIOR:
                modifer = new WarriorStatsModifier(gameManager);
                break;
            case WIZARD:
                modifer = new WizardStatsModifier(gameManager);
                break;
            case RANGER:
                modifer = new RangerStatsModifier(gameManager);
                break;
            case SHAMAN:
                modifer = new ShamanStatsModifier(gameManager);
                break;
            default:
                modifer = new BasicPlayerLevelStatsModifier(gameManager);
                break;

        }
        return modifer.modify(player);
    }
}
