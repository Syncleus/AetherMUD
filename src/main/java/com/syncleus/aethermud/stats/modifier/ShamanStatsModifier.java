/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
import com.syncleus.aethermud.stats.*;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class ShamanStatsModifier implements StatsModifier {

    private final GameManager gameManager;

    public ShamanStatsModifier(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private static double MELE_CONSTANT_MODIFIER = .4;
    private static double WILLPOWER_CONSTANT_MODIFIER = .9;
    private static double INTELLIGENCE_CONSTANT_MODIFIER = .7;
    private static double AGILE_CONSTANT_MODIFIER = .3;
    private static double AIM_CONSTANT_MODIFIER = .4;
    private static double HEALTH_CONSTANT_MODIFIER = 2;
    private static double ARMOR_CONSTANT_MODIFIER = .4;
    private static double STRENGTH_CONSTANT_MODIFIER = .4;
    private static double MANA_CONSTANT_MODIFIER = 5;

    public static int getMeleForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, MELE_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getWillpowerForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, WILLPOWER_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getIntelligenceForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, INTELLIGENCE_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getAgileForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, AGILE_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getAimForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, AIM_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getStrengthForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, STRENGTH_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getArmorForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, ARMOR_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getHealthForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, HEALTH_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    public static int getManaForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, MANA_CONSTANT_MODIFIER));
        return (int) Math.floor(v) + baseStat;
    }

    @Override
    public Stats modify(Player player) {
        Stats baseStats = player.getStats();
        int level = Levels.getLevel(baseStats.getExperience());
        int newMaxHealth = getHealthForLevel(baseStats.getMaxHealth(), level);
        int newArmorRating = getArmorForLevel(baseStats.getArmorRating(), level);
        int newStrengthRating = getStrengthForLevel(baseStats.getStrength(), level);
        int newMaxMana = getManaForLevel(baseStats.getMaxMana(), level);
        int newAimRating = getAimForLevel(baseStats.getAim(), level);
        int newWillpowerRating = getWillpowerForLevel(baseStats.getWillpower(), level);
        int newIntelligenceRating = getIntelligenceForLevel(baseStats.getIntelligence(), level);
        int newAgileRating = getAgileForLevel(baseStats.getAgile(), level);
        int newMeleRating = getMeleForLevel(baseStats.getMeleeSkill(), level);
        StatsBuilder statsBuilder = new StatsBuilder(baseStats);
        statsBuilder.setMaxHealth(newMaxHealth);
        statsBuilder.setArmorRating(newArmorRating);
        statsBuilder.setStrength(newStrengthRating);
        statsBuilder.setIntelligence(newIntelligenceRating);
        statsBuilder.setMaxMana(newMaxMana);
        statsBuilder.setAim(newAimRating);
        statsBuilder.setWillpower(newWillpowerRating);
        statsBuilder.setAgile(newAgileRating);
        statsBuilder.setMeleeSkill(newMeleRating);
        statsBuilder.setCurrentHealth(baseStats.getCurrentHealth());
        statsBuilder.setCurrentMana(baseStats.getCurrentMana());
        return statsBuilder.createStats();
    }
}
