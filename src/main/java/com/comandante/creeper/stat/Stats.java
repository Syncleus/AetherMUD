package com.comandante.creeper.stat;

import java.io.Serializable;

public class Stats implements Serializable {
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

    public Stats(Stats stats) {
        this.strength = stats.strength;
        this.willpower = stats.willpower;
        this.aim = stats.aim;
        this.agile = stats.agile;
        this.armorRating = stats.armorRating;
        this.meleSkill = stats.meleSkill;
        this.currentHealth = stats.currentHealth;
        this.maxHealth = stats.maxHealth;
        this.currentHealth = stats.currentHealth;
        this.maxHealth = stats.maxHealth;
        this.weaponRatingMax = stats.weaponRatingMax;
        this.weaponRatingMin = stats.weaponRatingMin;
        this.numberOfWeaponRolls = stats.numberOfWeaponRolls;
        this.experience = stats.experience;
        this.currentMana = stats.currentMana;
        this.maxMana = 100;
    }

    public Stats(int strength,
                 int willpower,
                 int aim,
                 int agile,
                 int armorRating,
                 int meleSkill,
                 int currentHealth,
                 int maxHealth,
                 int weaponRatingMax,
                 int weaponRatingMin,
                 int numberOfWeaponRolls,
                 int experience,
                 int currentMana,
                 int maxMana) {
        this.strength = strength;
        this.willpower = willpower;
        this.aim = aim;
        this.agile = agile;
        this.armorRating = armorRating;
        this.meleSkill = meleSkill;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.weaponRatingMax = weaponRatingMax;
        this.weaponRatingMin = weaponRatingMin;
        this.numberOfWeaponRolls = numberOfWeaponRolls;
        this.experience = experience;
        this.currentMana = currentMana;
        this.maxMana = maxMana;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getAgile() {
        return agile;
    }

    public void setAgile(int agile) {
        this.agile = agile;
    }

    public int getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(int armorRating) {
        this.armorRating = armorRating;
    }

    public int getMeleSkill() {
        return meleSkill;
    }

    public void setMeleSkill(int meleSkill) {
        this.meleSkill = meleSkill;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getWeaponRatingMax() {
        return weaponRatingMax;
    }

    public void setWeaponRatingMax(int weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
    }

    public int getWeaponRatingMin() {
        return weaponRatingMin;
    }

    public void setWeaponRatingMin(int weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
    }

    public int getNumberOfWeaponRolls() {
        return numberOfWeaponRolls;
    }

    public void setNumberOfWeaponRolls(int numberOfWeaponRolls) {
        this.numberOfWeaponRolls = numberOfWeaponRolls;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }
}
