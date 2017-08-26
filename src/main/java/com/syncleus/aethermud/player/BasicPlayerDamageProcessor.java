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
package com.syncleus.aethermud.player;

import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.stats.Stats;

import java.util.Random;

public class BasicPlayerDamageProcessor implements DamageProcessor {

    private final Random random = new Random();

    @Override
    public long getAttackAmount(Player player, Npc npc) {
        Stats playerStats = player.getPlayerStatsWithEquipmentAndLevel();
        Stats npcStats = npc.getStats();
        long rolls = 0;
        long totDamage = 0;
        while (rolls <= playerStats.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + randInt((int) playerStats.getWeaponRatingMin(), (int) playerStats.getWeaponRatingMax());
        }
        long i = playerStats.getStrength() + totDamage - npcStats.getArmorRating();
        if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }

    @Override
    public int getChanceToHit(Player player, Npc npc) {
        Stats playerStats = player.getPlayerStatsWithEquipmentAndLevel();
        Stats npcStats = npc.getStats();
        return (int) ((playerStats.getStrength() + playerStats.getMeleSkill()) * 5 - npcStats.getAgile() * 5);
    }

    @Override
    public int getCriticalChance(Player player, Npc npc) {
        //y =.20({x}) + 0
        return (int) (5 + (.20f * player.getPlayerStatsWithEquipmentAndLevel().getAim()));
    }

    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
