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
package com.syncleus.aethermud.stats;

public class StatsPojo implements Stats {
    private int strength;
    private int intelligence;
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
    private int inventorySize;
    private int maxEffects;

    public StatsPojo(Stats stats) {
        this.strength = ( stats == null ? 0 : getStrength());
        this.intelligence = ( stats == null ? 0 : getIntelligence());
        this.willpower = ( stats == null ? 0 : getWillpower());
        this.aim = ( stats == null ? 0 : getAim());
        this.agile = ( stats == null ? 0 : getAgile());
        this.armorRating = ( stats == null ? 0 : getArmorRating());
        this.meleSkill = ( stats == null ? 0 : getMeleSkill());
        this.currentHealth = ( stats == null ? 0 : getCurrentHealth());
        this.maxHealth = ( stats == null ? 0 : getMaxHealth());
        this.weaponRatingMax = ( stats == null ? 0 : getWeaponRatingMax());
        this.weaponRatingMin = ( stats == null ? 0 : getWeaponRatingMin());
        this.numberOfWeaponRolls = ( stats == null ? 0 : getNumberOfWeaponRolls());
        this.experience = ( stats == null ? 0 : getExperience());
        this.currentMana = ( stats == null ? 0 : getCurrentMana());
        this.foraging = ( stats == null ? 0 : getForaging());
        this.maxMana = ( stats == null ? 0 : getMaxMana());
        this.inventorySize = ( stats == null ? 0 : getInventorySize());
        this.maxEffects = ( stats == null ? 0 : getMaxEffects());
    }

    public StatsPojo(int strength,
                     int intelligence,
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
                     int maxMana,
                     int foraging,
                     int inventorySize,
                     int maxEffects) {
        this.strength = strength;
        this.intelligence = intelligence;
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
        this.foraging = foraging;
        this.inventorySize = inventorySize;
        this.maxEffects = maxEffects;
    }


    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getMaxEffects() {
        return maxEffects;
    }

    public void setMaxEffects(int maxEffects) {
        this.maxEffects = maxEffects;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public Integer getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public Integer getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public Integer getAgile() {
        return agile;
    }

    public void setAgile(int agile) {
        this.agile = agile;
    }

    public Integer getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(int armorRating) {
        this.armorRating = armorRating;
    }

    public Integer getMeleSkill() {
        return meleSkill;
    }

    public void setMeleeSkill(int meleSkill) {
        this.meleSkill = meleSkill;
    }

    public Integer getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Integer getWeaponRatingMax() {
        return weaponRatingMax;
    }

    public void setWeaponRatingMax(int weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
    }

    public Integer getWeaponRatingMin() {
        return weaponRatingMin;
    }

    public void setWeaponRatingMin(int weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
    }

    public Integer getNumberOfWeaponRolls() {
        return numberOfWeaponRolls;
    }

    public void setNumberOfWeaponRolls(int numberOfWeaponRolls) {
        this.numberOfWeaponRolls = numberOfWeaponRolls;
    }

    public Integer getCurrentMana() {
        return currentMana;
    }

    public Integer getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public Integer getForaging() {
        return foraging;
    }

    public void setForaging(int foraging) {
        this.foraging = foraging;
    }

    public Integer getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(int inventorySize) {
        this.inventorySize = inventorySize;
    }
}
