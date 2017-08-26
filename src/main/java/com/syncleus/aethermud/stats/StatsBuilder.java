/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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

    public StatsBuilder() {
    }

    public StatsBuilder(Stats stats) {
        this.strength = stats.getStrength();
        this.intelligence = stats.getIntelligence();
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
        this.inventorySize = stats.getInventorySize();
        this.maxEffects = stats.getMaxEffects();
    }

    public StatsBuilder setIntelligence(long intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public StatsBuilder setStrength(long strength) {
        this.strength = strength;
        return this;
    }

    public StatsBuilder setWillpower(long willpower) {
        this.willpower = willpower;
        return this;
    }

    public StatsBuilder setAim(long aim) {
        this.aim = aim;
        return this;
    }

    public StatsBuilder setAgile(long agile) {
        this.agile = agile;
        return this;
    }

    public StatsBuilder setArmorRating(long armorRating) {
        this.armorRating = armorRating;
        return this;
    }

    public StatsBuilder setMeleSkill(long meleSkill) {
        this.meleSkill = meleSkill;
        return this;
    }

    public StatsBuilder setCurrentHealth(long currentHealth) {
        this.currentHealth = currentHealth;
        return this;
    }

    public StatsBuilder setMaxHealth(long maxHealth) {
        this.maxHealth = maxHealth;
        return this;
    }

    public StatsBuilder setWeaponRatingMax(long weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
        return this;
    }

    public StatsBuilder setWeaponRatingMin(long weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
        return this;
    }

    public StatsBuilder setNumberOfWeaponRolls(long numberOfWeaponRolls) {
        this.numberOfWeaponRolls = numberOfWeaponRolls;
        return this;
    }

    public StatsBuilder setExperience(long experience) {
        this.experience = experience;
        return this;
    }

    public StatsBuilder setCurrentMana(long currentMana) {
        this.currentMana = currentMana;
        return this;
    }

    public StatsBuilder setMaxMana(long maxMana) {
        this.maxMana = maxMana;
        return this;
    }

    public StatsBuilder setForaging(long foraging) {
        this.foraging = foraging;
        return this;
    }

    public StatsBuilder setInventorySize(long inventorySize) {
        this.inventorySize = inventorySize;
        return this;
    }

    public StatsBuilder setMaxEffects(long maxEffects) {
        this.maxEffects = maxEffects;
        return this;
    }

    public Stats createStats() {
        return new Stats(strength, intelligence, willpower, aim, agile, armorRating, meleSkill, currentHealth, maxHealth, weaponRatingMax, weaponRatingMin, numberOfWeaponRolls, experience, currentMana, maxMana, foraging, inventorySize, maxEffects);
    }
}
