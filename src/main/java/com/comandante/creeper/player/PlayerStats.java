package com.comandante.creeper.player;


import com.comandante.creeper.stat.StatsBuilder;

public class PlayerStats {

    public final static StatsBuilder DEFAULT_PLAYER = new StatsBuilder()
            .setStrength(10)
            .setWillpower(1)
            .setAim(1)
            .setAgile(1)
            .setArmorRating(2)
            .setMeleSkill(10)
            .setCurrentHealth(100)
            .setMaxHealth(100)
            .setWeaponRatingMin(3)
            .setWeaponRatingMax(5)
            .setNumberOfWeaponRolls(1)
            .setExperience(0)
            .setCurrentMana(100)
            .setMaxMana(100)
            .setForaging(0)
            .setInventorySize(10)
            .setMaxEffects(4);

}
