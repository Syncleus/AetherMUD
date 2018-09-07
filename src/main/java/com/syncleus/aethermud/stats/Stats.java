/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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

import static java.lang.StrictMath.sqrt;

public class Stats {
    private Integer strength;
    private Integer intelligence;
    private Integer willpower;
    private Integer aim;
    private Integer agile;
    private Integer armorRating;
    private Integer meleeSkill;
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

    public Stats() {

    }

    public Stats(Stats stats) {
        this.strength = ( stats == null ? 0 : stats.getStrength());
        this.intelligence = ( stats == null ? 0 : stats.getIntelligence());
        this.willpower = ( stats == null ? 0 : stats.getWillpower());
        this.aim = ( stats == null ? 0 : stats.getAim());
        this.agile = ( stats == null ? 0 : stats.getAgile());
        this.armorRating = ( stats == null ? 0 : stats.getArmorRating());
        this.meleeSkill = ( stats == null ? 0 : stats.getMeleeSkill());
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

    public Stats(Integer strength,
                 Integer intelligence,
                 Integer willpower,
                 Integer aim,
                 Integer agile,
                 Integer armorRating,
                 Integer meleeSkill,
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
        this.strength = (strength == null ? 0 : strength);
        this.intelligence = (intelligence == null ? 0 : intelligence);
        this.willpower = (willpower == null ? 0 : willpower);
        this.aim = (aim == null ? 0 : aim);
        this.agile = (agile == null ? 0 : agile);
        this.armorRating = (armorRating == null ? 0 : armorRating);
        this.meleeSkill = (meleeSkill == null ? 0 : meleeSkill);
        this.currentHealth = (currentHealth == null ? 0 : currentHealth);
        this.maxHealth = (maxHealth == null ? 0 : maxHealth);
        this.weaponRatingMax = (weaponRatingMax == null ? 0 : weaponRatingMax);
        this.weaponRatingMin = (weaponRatingMin == null ? 0 : weaponRatingMin);
        this.numberOfWeaponRolls = (numberOfWeaponRolls == null ? 0 : numberOfWeaponRolls);
        this.experience = (experience == null ? 0 : experience);
        this.currentMana = (currentMana == null ? 0 : currentMana);
        this.maxMana = (maxMana == null ? 0 : maxMana);
        this.foraging = (foraging == null ? 0 : foraging);
        this.inventorySize = (inventorySize == null ? 0 : inventorySize);
        this.maxEffects = (maxEffects == null ? 0 : maxEffects);
    }


    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = (intelligence == null ? 0 : intelligence);
    }

    public Integer getMaxEffects() {
        return maxEffects;
    }

    public void setMaxEffects(Integer maxEffects) {
        this.maxEffects = (maxEffects == null ? 0 : maxEffects);
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = (experience == null ? 0 : experience);
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = (strength == null ? 0 : strength);
    }

    public Integer getWillpower() {
        return willpower;
    }

    public void setWillpower(Integer willpower) {
        this.willpower = (willpower == null ? 0 : willpower);
    }

    public Integer getAim() {
        return aim;
    }

    public void setAim(Integer aim) {
        this.aim = (aim == null ? 0 : aim);
    }

    public Integer getAgile() {
        return agile;
    }

    public void setAgile(Integer agile) {
        this.agile = (agile == null ? 0 : agile);
    }

    public Integer getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(Integer armorRating) {
        this.armorRating = (armorRating == null ? 0 : armorRating);
    }

    public Integer getMeleeSkill() {
        return meleeSkill;
    }

    public void setMeleeSkill(Integer meleeSkill) {
        this.meleeSkill = (meleeSkill == null ? 0 : meleeSkill);
    }

    public Integer getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(Integer currentHealth) {
        this.currentHealth = (currentHealth == null ? 0 : currentHealth);
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(Integer maxHealth) {
        this.maxHealth = (maxHealth == null ? 0 : maxHealth);
    }

    public Integer getWeaponRatingMax() {
        return weaponRatingMax;
    }

    public void setWeaponRatingMax(Integer weaponRatingMax) {
        this.weaponRatingMax = (weaponRatingMax == null ? 0 : weaponRatingMax);
    }

    public Integer getWeaponRatingMin() {
        return weaponRatingMin;
    }

    public void setWeaponRatingMin(Integer weaponRatingMin) {
        this.weaponRatingMin = (weaponRatingMin == null ? 0 : weaponRatingMin);
    }

    public Integer getNumberOfWeaponRolls() {
        return numberOfWeaponRolls;
    }

    public void setNumberOfWeaponRolls(Integer numberOfWeaponRolls) {
        this.numberOfWeaponRolls = (numberOfWeaponRolls == null ? 0 : numberOfWeaponRolls);
    }

    public Integer getCurrentMana() {
        return currentMana;
    }

    public Integer getMaxMana() {
        return maxMana;
    }

    public void setCurrentMana(Integer currentMana) {
        this.currentMana = (currentMana == null ? 0 : currentMana);
    }

    public void setMaxMana(Integer maxMana) {
        this.maxMana = (maxMana == null ? 0 : maxMana);
    }

    public Integer getForaging() {
        return foraging;
    }

    public void setForaging(Integer foraging) {
        this.foraging = (foraging == null ? 0 : foraging);
    }

    public Integer getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(Integer inventorySize) {
        this.inventorySize = (inventorySize == null ? 0 : inventorySize);
    }

    public Integer getLevel() {
        double v = 0.02 * sqrt(getExperience());
        return Double.valueOf(Math.floor(v)).intValue();
    }
}
