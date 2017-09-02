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
    private Integer strength;
    private Integer intelligence;
    private Integer willpower;
    private Integer aim;
    private Integer agile;
    private Integer armorRating;
    private Integer meleSkill;
    private Integer currentHealth;
    private Integer maxHealth;
    private Integer weaponRatingMax;
    private Integer weaponRatingMin;
    private Integer numberOfWeaponRolls;
    private Integer experience;
    private Integer currentMana;
    private Integer maxMana;
    private Integer foraging;
    private Integer inventorySize;
    private Integer maxEffects;

    public StatsPojo(Stats stats) {
        this.strength = ( stats == null ? 0 : stats.getStrength());
        this.intelligence = ( stats == null ? 0 : stats.getIntelligence());
        this.willpower = ( stats == null ? 0 : stats.getWillpower());
        this.aim = ( stats == null ? 0 : stats.getAim());
        this.agile = ( stats == null ? 0 : stats.getAgile());
        this.armorRating = ( stats == null ? 0 : stats.getArmorRating());
        this.meleSkill = ( stats == null ? 0 : stats.getMeleSkill());
        this.currentHealth = ( stats == null ? 0 : stats.getCurrentHealth());
        this.maxHealth = ( stats == null ? 0 : stats.getMaxHealth());
        this.weaponRatingMax = ( stats == null ? 0 : stats.getWeaponRatingMax());
        this.weaponRatingMin = ( stats == null ? 0 : stats.getWeaponRatingMin());
        this.numberOfWeaponRolls = ( stats == null ? 0 : stats.getNumberOfWeaponRolls());
        this.experience = ( stats == null ? 0 : stats.getExperience());
        this.currentMana = ( stats == null ? 0 : stats.getCurrentMana());
        this.foraging = ( stats == null ? 0 : stats.getForaging());
        this.maxMana = ( stats == null ? 0 : stats.getMaxMana());
        this.inventorySize = ( stats == null ? 0 : stats.getInventorySize());
        this.maxEffects = ( stats == null ? 0 : stats.getMaxEffects());
    }

    public StatsPojo(Integer strength,
                     Integer intelligence,
                     Integer willpower,
                     Integer aim,
                     Integer agile,
                     Integer armorRating,
                     Integer meleSkill,
                     Integer currentHealth,
                     Integer maxHealth,
                     Integer weaponRatingMax,
                     Integer weaponRatingMin,
                     Integer numberOfWeaponRolls,
                     Integer experience,
                     Integer currentMana,
                     Integer maxMana,
                     Integer foraging,
                     Integer inventorySize,
                     Integer maxEffects) {
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

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getMaxEffects() {
        return maxEffects;
    }

    public void setMaxEffects(Integer maxEffects) {
        this.maxEffects = maxEffects;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getWillpower() {
        return willpower;
    }

    public void setWillpower(Integer willpower) {
        this.willpower = willpower;
    }

    public Integer getAim() {
        return aim;
    }

    public void setAim(Integer aim) {
        this.aim = aim;
    }

    public Integer getAgile() {
        return agile;
    }

    public void setAgile(Integer agile) {
        this.agile = agile;
    }

    public Integer getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(Integer armorRating) {
        this.armorRating = armorRating;
    }

    public Integer getMeleSkill() {
        return meleSkill;
    }

    public void setMeleeSkill(Integer meleSkill) {
        this.meleSkill = meleSkill;
    }

    public Integer getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(Integer currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(Integer maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Integer getWeaponRatingMax() {
        return weaponRatingMax;
    }

    public void setWeaponRatingMax(Integer weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
    }

    public Integer getWeaponRatingMin() {
        return weaponRatingMin;
    }

    public void setWeaponRatingMin(Integer weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
    }

    public Integer getNumberOfWeaponRolls() {
        return numberOfWeaponRolls;
    }

    public void setNumberOfWeaponRolls(Integer numberOfWeaponRolls) {
        this.numberOfWeaponRolls = numberOfWeaponRolls;
    }

    public Integer getCurrentMana() {
        return currentMana;
    }

    public Integer getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(Integer currentMana) {
        this.currentMana = currentMana;
    }

    public void setMaxMana(Integer maxMana) {
        this.maxMana = maxMana;
    }

    public Integer getForaging() {
        return foraging;
    }

    public void setForaging(Integer foraging) {
        this.foraging = foraging;
    }

    public Integer getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(Integer inventorySize) {
        this.inventorySize = inventorySize;
    }
}
