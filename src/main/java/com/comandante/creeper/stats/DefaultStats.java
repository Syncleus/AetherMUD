package com.comandante.creeper.stats;


public class DefaultStats {

    public final static StatsBuilder DEFAULT_PLAYER = new StatsBuilder()
            .setStrength(9)
            .setIntelligence(2)
            .setWillpower(3)
            .setAim(3)
            .setAgile(4)
            .setArmorRating(3)
            .setMeleSkill(2)
            .setCurrentHealth(100)
            .setMaxHealth(100)
            .setWeaponRatingMin(3)
            .setWeaponRatingMax(4)
            .setNumberOfWeaponRolls(1)
            .setExperience(0)
            .setCurrentMana(100)
            .setMaxMana(100)
            .setForaging(0)
            .setInventorySize(10)
            .setMaxEffects(4);
}
