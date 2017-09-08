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

public class StatsBuilder {
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

    public StatsBuilder() {
    }

    public StatsBuilder(Stats stats) {
        this.strength = stats.getStrength();
        this.intelligence = stats.getIntelligence();
        this.willpower = stats.getWillpower();
        this.aim = stats.getAim();
        this.agile = stats.getAgile();
        this.armorRating = stats.getArmorRating();
        this.meleSkill = stats.getMeleeSkill();
        this.currentHealth = stats.getCurrentHealth();
        this.maxHealth = stats.getMaxHealth();
        this.weaponRatingMax = stats.getWeaponRatingMax();
        this.weaponRatingMin = stats.getWeaponRatingMin();
        this.numberOfWeaponRolls = stats.getNumberOfWeaponRolls();
        this.experience = stats.getExperience();
        this.maxMana = stats.getMaxMana();
        this.foraging = stats.getForaging();
        this.currentMana = stats.getCurrentMana();
        this.inventorySize = stats.getInventorySize();
        this.maxEffects = stats.getMaxEffects();
    }

    public StatsBuilder setIntelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
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

    public StatsBuilder setInventorySize(int inventorySize) {
        this.inventorySize = inventorySize;
        return this;
    }

    public StatsBuilder setMaxEffects(int maxEffects) {
        this.maxEffects = maxEffects;
        return this;
    }

    public Stats createStats() {
        return new StatsPojo(strength, intelligence, willpower, aim, agile, armorRating, meleSkill, currentHealth, maxHealth, weaponRatingMax, weaponRatingMin, numberOfWeaponRolls, experience, currentMana, maxMana, foraging, inventorySize, maxEffects);
    }
}
