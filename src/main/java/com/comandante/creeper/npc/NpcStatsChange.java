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
package com.comandante.creeper.npc;

import com.comandante.creeper.player.Player;
import com.comandante.creeper.stats.Stats;

import java.util.List;

public class NpcStatsChange {

    private final Stats stats;
    private final List<String> damageStrings;
    private final List<String> playerDamageStrings;
    private final Player player;
    private final Stats playerStatsChange;
    private boolean isItemDamage;

    public NpcStatsChange(Stats stats, List<String> damageStrings, Player player, Stats playerStatsChange, List<String> playerDamageStrings, boolean isItemDamage) {
        this.stats = stats;
        this.damageStrings = damageStrings;
        this.player = player;
        this.playerStatsChange = playerStatsChange;
        this.playerDamageStrings = playerDamageStrings;
        this.isItemDamage = isItemDamage;
    }

    public Stats getStats() {
        return stats;
    }

    public List<String> getDamageStrings() {
        return damageStrings;
    }

    public Player getPlayer() {
        return player;
    }

    public Stats getPlayerStatsChange() {
        return playerStatsChange;
    }

    public List<String> getPlayerDamageStrings() {
        return playerDamageStrings;
    }

    public boolean isItemDamage() {
        return isItemDamage;
    }
}
