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
    private long strength;
    private long intelligence;
    private long willpower;
    private long aim;
    private long agile;
    private long armorRating;
    private long meleSkill;
    private long currentHealth;
    private long maxHealth;
    private long weaponRatingMax;
    private long weaponRatingMin;
    private long numberOfWeaponRolls;
    private long experience;
    private long currentMana;
    private long maxMana;
    private long foraging;
    private long inventorySize;
    private long maxEffects;

    public StatsPojo(Stats stats) {
        this.strength = stats.getStrength();
        this.intelligence = stats.getIntelligence();
        this.willpower = stats.getWillpower();
        this.aim = stats.getAim();
        this.agile = stats.getAgile();
        this.armorRating = stats.getArmorRating();
        this.meleSkill = stats.getMeleSkill();
        this.currentHealth = stats.getCurrentHealth();
        this.maxHealth = stats.getMaxHealth();
        this.maxHealth = stats.getMaxHealth();
        this.weaponRatingMax = stats.getWeaponRatingMax();
        this.weaponRatingMin = stats.getWeaponRatingMin();
        this.numberOfWeaponRolls = stats.getNumberOfWeaponRolls();
        this.experience = stats.getExperience();
        this.currentMana = stats.getCurrentMana();
        this.foraging = stats.getForaging();
        this.maxMana = stats.getMaxMana();
        this.inventorySize = stats.getInventorySize();
        this.maxEffects = stats.getMaxEffects();
    }

    public StatsPojo(long strength,
                     long intelligence,
                     long willpower,
                     long aim,
                     long agile,
                     long armorRating,
                     long meleSkill,
                     long currentHealth,
                     long maxHealth,
                     long weaponRatingMax,
                     long weaponRatingMin,
                     long numberOfWeaponRolls,
                     long experience,
                     long currentMana,
                     long maxMana,
                     long foraging,
                     long inventorySize,
                     long maxEffects) {
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


    public long getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(long intelligence) {
        this.intelligence = intelligence;
    }

    public long getMaxEffects() {
        return maxEffects;
    }

    public void setMaxEffects(long maxEffects) {
        this.maxEffects = maxEffects;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public long getStrength() {
        return strength;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }

    public long getWillpower() {
        return willpower;
    }

    public void setWillpower(long willpower) {
        this.willpower = willpower;
    }

    public long getAim() {
        return aim;
    }

    public void setAim(long aim) {
        this.aim = aim;
    }

    public long getAgile() {
        return agile;
    }

    public void setAgile(long agile) {
        this.agile = agile;
    }

    public long getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(long armorRating) {
        this.armorRating = armorRating;
    }

    public long getMeleSkill() {
        return meleSkill;
    }

    public void setMeleSkill(long meleSkill) {
        this.meleSkill = meleSkill;
    }

    public long getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(long currentHealth) {
        this.currentHealth = currentHealth;
    }

    public long getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(long maxHealth) {
        this.maxHealth = maxHealth;
    }

    public long getWeaponRatingMax() {
        return weaponRatingMax;
    }

    public void setWeaponRatingMax(long weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
    }

    public long getWeaponRatingMin() {
        return weaponRatingMin;
    }

    public void setWeaponRatingMin(long weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
    }

    public long getNumberOfWeaponRolls() {
        return numberOfWeaponRolls;
    }

    public void setNumberOfWeaponRolls(long numberOfWeaponRolls) {
        this.numberOfWeaponRolls = numberOfWeaponRolls;
    }

    public long getCurrentMana() {
        return currentMana;
    }

    public long getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(long currentMana) {
        this.currentMana = currentMana;
    }

    public void setMaxMana(long maxMana) {
        this.maxMana = maxMana;
    }

    public long getForaging() {
        return foraging;
    }

    public void setForaging(long foraging) {
        this.foraging = foraging;
    }

    public long getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(long inventorySize) {
        this.inventorySize = inventorySize;
    }
}
