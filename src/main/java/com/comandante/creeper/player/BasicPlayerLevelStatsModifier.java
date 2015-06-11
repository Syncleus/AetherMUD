package com.comandante.creeper.player;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class BasicPlayerLevelStatsModifier implements StatsModifier {

    private final GameManager gameManager;

    public BasicPlayerLevelStatsModifier(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private static double WILLPOWER_CONSTANT_MODIFIER = 1.01;
    private static double AGILE_CONSTANT_MODIFIER = 1.01;
    private static double AIM_CONSTANT_MODIFIER = 1.01;
    private static double HEALTH_CONSTANT_MODIFIER = 4;
    private static double ARMOR_CONSTANT_MODIFIER = 1.01;
    private static double STRENGTH_CONSTANT_MODIFIER = 1.4;
    private static double MANA_CONSTANT_MODIFIER = 2;

    public static int getWillpowerForLevel(int baseStat, int level) {
        double v = (level) * sqrt(pow(level, AGILE_CONSTANT_MODIFIER));
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
        PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId());
        Stats baseStats = playerMetadata.getStats();
        int level = Levels.getLevel(baseStats.getExperience());
        int newMaxHealth = getHealthForLevel(baseStats.getMaxHealth(), level);
        int newArmorRating = getArmorForLevel(baseStats.getArmorRating(), level);
        int newStrengthRating = getStrengthForLevel(baseStats.getStrength(), level);
        int newMaxMana = getManaForLevel(baseStats.getMaxMana(), level);
        int newAimRating = getAimForLevel(baseStats.getAim(), level);
        int newWillpowerRating = getWillpowerForLevel(baseStats.getWillpower(), level);
        int newAgileRating = getAgileForLevel(baseStats.getAgile(), level);
        StatsBuilder statsBuilder = new StatsBuilder(baseStats);
        statsBuilder.setMaxHealth(newMaxHealth);
        statsBuilder.setArmorRating(newArmorRating);
        statsBuilder.setStrength(newStrengthRating);
        statsBuilder.setMaxMana(newMaxMana);
        statsBuilder.setAim(newAimRating);
        statsBuilder.setWillpower(newWillpowerRating);
        statsBuilder.setAgile(newAgileRating);
        return statsBuilder.createStats();
    }
}