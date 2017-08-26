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

public class NpcStatsChangeBuilder {
    private Stats stats;
    private List<String> damageStrings;
    private Player player;
    private Stats playerStatsChange;
    private List<String> playerDamageStrings;
    private boolean isItemDamage;

    public NpcStatsChangeBuilder setStats(Stats stats) {
        this.stats = stats;
        return this;
    }

    public NpcStatsChangeBuilder setDamageStrings(List<String> damageStrings) {
        this.damageStrings = damageStrings;
        return this;
    }

    public NpcStatsChangeBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public NpcStatsChangeBuilder setPlayerStatsChange(Stats playerStatsChange) {
        this.playerStatsChange = playerStatsChange;
        return this;
    }

    public NpcStatsChangeBuilder setPlayerDamageStrings(List<String> playerDamageStrings) {
        this.playerDamageStrings = playerDamageStrings;
        return this;
    }

    public NpcStatsChangeBuilder setIsItemDamage(boolean isItemDamage) {
        this.isItemDamage = isItemDamage;
        return this;
    }

    public NpcStatsChange createNpcStatsChange() {
        return new NpcStatsChange(stats, damageStrings, player, playerStatsChange, playerDamageStrings, isItemDamage);
    }
}