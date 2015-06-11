package com.comandante.creeper.stat;

public class StatsBuilder {
    private int strength;
    private int willpower;
    private int aim;
    private int agile;
    private int armorRating;
    private int meleSkill;
    private int currentHealth;
    private int maxHealth;
    private int weaponRatingMax;
    private int weaponRatingMin;
    private int numberOfWeaponRolls;
    private int experience;
    private int currentMana;
    private int maxMana;
    private int foraging;

    public StatsBuilder() {
    }

    public StatsBuilder(Stats stats) {
        this.strength = stats.getStrength();
        this.willpower = stats.getWillpower();
        this.aim = stats.getAim();
        this.agile = stats.getAgile();
        this.armorRating = stats.getArmorRating();
        this.meleSkill = stats.getMeleSkill();
        this.currentHealth = stats.getCurrentHealth();
        this.maxHealth = stats.getMaxHealth();
        this.weaponRatingMax = stats.getWeaponRatingMax();
        this.weaponRatingMin = stats.getWeaponRatingMin();
        this.numberOfWeaponRolls = stats.getNumberOfWeaponRolls();
        this.experience = stats.getExperience();
        this.currentHealth = stats.getCurrentHealth();
        this.maxMana = stats.getMaxMana();
        this.foraging = stats.getForaging();
        this.currentMana = stats.getCurrentMana();
    }

    public StatsBuilder setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public StatsBuilder setWillpower(int willpower) {
        this.willpower = willpower;
        return this;
    }

    public StatsBuilder setAim(int aim) {
        this.aim = aim;
        return this;
    }

    public StatsBuilder setAgile(int agile) {
        this.agile = agile;
        return this;
    }

    public StatsBuilder setArmorRating(int armorRating) {
        this.armorRating = armorRating;
        return this;
    }

    public StatsBuilder setMeleSkill(int meleSkill) {
        this.meleSkill = meleSkill;
        return this;
    }

    public StatsBuilder setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
        return this;
    }

    public StatsBuilder setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public StatsBuilder setWeaponRatingMax(int weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
        return this;
    }

    public StatsBuilder setWeaponRatingMin(int weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
        return this;
    }

    public StatsBuilder setNumberOfWeaponRolls(int numberOfWeaponRolls) {
        this.numberOfWeaponRolls = numberOfWeaponRolls;
        return this;
    }

    public StatsBuilder setExperience(int experience) {
        this.experience = experience;
        return this;
    }

    public StatsBuilder setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
        return this;
    }

    public StatsBuilder setMaxMana(int maxMana) {
        this.maxMana = maxMana;
        return this;
    }

    public StatsBuilder setForaging(int foraging) {
        this.foraging = foraging;
        return this;
    }

    public Stats createStats() {
        return new Stats(strength, willpower, aim, agile, armorRating, meleSkill, currentHealth, maxHealth, weaponRatingMax, weaponRatingMin, numberOfWeaponRolls, experience, currentMana, maxMana, foraging);
    }
}