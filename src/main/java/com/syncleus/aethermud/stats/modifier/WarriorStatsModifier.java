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
import com.syncleus.aethermud.storage.graphdb.PlayerData;
import com.syncleus.aethermud.stats.DefaultStats;
import com.syncleus.aethermud.stats.Levels;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.stats.StatsBuilder;

import java.util.Optional;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class WarriorStatsModifier implements StatsModifier {

    private final GameManager gameManager;

    public WarriorStatsModifier(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private static double MELE_CONSTANT_MODIFIER = .6;
    private static double WILLPOWER_CONSTANT_MODIFIER = .3;
    private static double INTELLIGENCE_CONSTANT_MODIFIER = .3;
    private static double AGILE_CONSTANT_MODIFIER = .5;
    private static double AIM_CONSTANT_MODIFIER = .4;
    private static double HEALTH_CONSTANT_MODIFIER = 4;
    private static double ARMOR_CONSTANT_MODIFIER = .3;
    private static double STRENGTH_CONSTANT_MODIFIER = .7;
    private static double MANA_CONSTANT_MODIFIER = 2;

    public static long getMeleForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, MELE_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getWillpowerForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, WILLPOWER_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getIntelligenceForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, INTELLIGENCE_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getAgileForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, AGILE_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getAimForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, AIM_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getStrengthForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, STRENGTH_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getArmorForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, ARMOR_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getHealthForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, HEALTH_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    public static long getManaForLevel(long baseStat, long level) {
        double v = (level) * sqrt(pow(level, MANA_CONSTANT_MODIFIER));
        return (long) Math.floor(v) + baseStat;
    }

    @Override
    public Stats modify(Player player) {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId());
        if (!playerMetadataOptional.isPresent()) {
            return DefaultStats.DEFAULT_PLAYER.createStats();
        }
        PlayerData playerData = playerMetadataOptional.get();
        Stats baseStats = playerData.getStats();
        long level = Levels.getLevel(baseStats.getExperience());
        long newMaxHealth = getHealthForLevel(baseStats.getMaxHealth(), level);
        long newArmorRating = getArmorForLevel(baseStats.getArmorRating(), level);
        long newStrengthRating = getStrengthForLevel(baseStats.getStrength(), level);
        long newMaxMana = getManaForLevel(baseStats.getMaxMana(), level);
        long newAimRating = getAimForLevel(baseStats.getAim(), level);
        long newWillpowerRating = getWillpowerForLevel(baseStats.getWillpower(), level);
        long newIntelligenceRating = getIntelligenceForLevel(baseStats.getIntelligence(), level);
        long newAgileRating = getAgileForLevel(baseStats.getAgile(), level);
        long newMeleRating = getMeleForLevel(baseStats.getMeleSkill(), level);
        StatsBuilder statsBuilder = new StatsBuilder(baseStats);
        statsBuilder.setMaxHealth(newMaxHealth);
        statsBuilder.setArmorRating(newArmorRating);
        statsBuilder.setStrength(newStrengthRating);
        statsBuilder.setIntelligence(newIntelligenceRating);
        statsBuilder.setMaxMana(newMaxMana);
        statsBuilder.setAim(newAimRating);
        statsBuilder.setWillpower(newWillpowerRating);
        statsBuilder.setAgile(newAgileRating);
        statsBuilder.setMeleSkill(newMeleRating);
        statsBuilder.setCurrentHealth(baseStats.getCurrentHealth());
        statsBuilder.setCurrentMana(baseStats.getCurrentMana());
        return statsBuilder.createStats();
    }
}
