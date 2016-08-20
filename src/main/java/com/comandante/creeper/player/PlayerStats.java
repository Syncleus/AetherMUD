package com.comandante.creeper.player;


import com.comandante.creeper.stat.StatsBuilder;

public class PlayerStats {

    public final static StatsBuilder DEFAULT_PLAYER = new StatsBuilder()
            .setStrength(1)
            .setIntelligence(1)
            .setWillpower(1)
            .setAim(1)
            .setAgile(1)
            .setArmorRating(1)
            .setMeleSkill(1)
            .setCurrentHealth(100)
            .setMaxHealth(100)
            .setWeaponRatingMin(1)
            .setWeaponRatingMax(2)
            .setNumberOfWeaponRolls(1)
            .setExperience(0)
            .setCurrentMana(100)
            .setMaxMana(100)
            .setForaging(0)
            .setInventorySize(10)
            .setMaxEffects(4);
}
