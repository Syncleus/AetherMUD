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

public class StatsHelper {

    public static Stats getDifference(Stats modifiedStats, Stats origStats) {
        StatsBuilder statsBuilder = new StatsBuilder();
        statsBuilder.setAgile(modifiedStats.getAgile() - origStats.getAgile());
        statsBuilder.setIntelligence(modifiedStats.getIntelligence() - origStats.getIntelligence());
        statsBuilder.setAim(modifiedStats.getAim() - origStats.getAim());
        statsBuilder.setArmorRating(modifiedStats.getArmorRating() - origStats.getArmorRating());
        statsBuilder.setCurrentHealth(modifiedStats.getCurrentHealth() - origStats.getCurrentHealth());
        statsBuilder.setMaxHealth(modifiedStats.getMaxHealth() - origStats.getMaxHealth());
        statsBuilder.setExperience(modifiedStats.getExperience() - origStats.getExperience());
        statsBuilder.setMeleSkill(modifiedStats.getMeleSkill() - origStats.getMeleSkill());
        statsBuilder.setNumberOfWeaponRolls(modifiedStats.getNumberOfWeaponRolls() - origStats.getNumberOfWeaponRolls());
        statsBuilder.setStrength(modifiedStats.getStrength() - origStats.getStrength());
        statsBuilder.setWeaponRatingMax(modifiedStats.getWeaponRatingMax() - origStats.getWeaponRatingMax());
        statsBuilder.setWeaponRatingMin(modifiedStats.getWeaponRatingMin() - origStats.getWeaponRatingMin());
        statsBuilder.setWillpower(modifiedStats.getWillpower() - origStats.getWillpower());
        statsBuilder.setCurrentMana(modifiedStats.getCurrentMana() - origStats.getCurrentMana());
        statsBuilder.setMaxMana(modifiedStats.getMaxMana() - origStats.getMaxMana());
        statsBuilder.setForaging(modifiedStats.getForaging() - origStats.getForaging());
        statsBuilder.setInventorySize(modifiedStats.getInventorySize() - origStats.getInventorySize());
        statsBuilder.setMaxEffects(modifiedStats.getMaxEffects() - origStats.getMaxEffects());
        return statsBuilder.createStats();
    }

    public static void combineStats(Stats orig, Stats combine) {
        orig.setAgile(orig.getAgile() + combine.getAgile());
        orig.setIntelligence(orig.getIntelligence() + combine.getIntelligence());
        orig.setAim(orig.getAim() + combine.getAim());
        orig.setArmorRating(orig.getArmorRating() + combine.getArmorRating());
        orig.setCurrentHealth(orig.getCurrentHealth() + combine.getCurrentHealth());
        orig.setMaxHealth(orig.getMaxHealth() + combine.getMaxHealth());
        orig.setExperience(orig.getExperience() + combine.getExperience());
        orig.setMeleSkill(orig.getMeleSkill() + combine.getMeleSkill());
        orig.setNumberOfWeaponRolls(orig.getNumberOfWeaponRolls() + combine.getNumberOfWeaponRolls());
        orig.setStrength(orig.getStrength() + combine.getStrength());
        orig.setWeaponRatingMax(orig.getWeaponRatingMax() + combine.getWeaponRatingMax());
        orig.setWeaponRatingMin(orig.getWeaponRatingMin() + combine.getWeaponRatingMin());
        orig.setWillpower(orig.getWillpower() + combine.getWillpower());
        orig.setCurrentMana(orig.getCurrentMana() + combine.getCurrentMana());
        orig.setMaxMana(orig.getMaxMana() + combine.getMaxMana());
        orig.setForaging(orig.getForaging() + combine.getForaging());
        orig.setInventorySize(orig.getInventorySize() + combine.getInventorySize());
        orig.setMaxEffects(orig.getMaxEffects() + combine.getMaxEffects());
    }

    public static void inverseStats(Stats stats) {
        stats.setAgile(-stats.getAgile());
        stats.setAim(-stats.getAim());
        stats.setIntelligence(-stats.getIntelligence());
        stats.setArmorRating(-stats.getArmorRating());
        stats.setCurrentHealth(-stats.getCurrentHealth());
        stats.setMaxHealth(-stats.getMaxHealth());
        stats.setExperience(-stats.getExperience());
        stats.setMeleSkill(-stats.getMeleSkill());
        stats.setNumberOfWeaponRolls(-stats.getNumberOfWeaponRolls());
        stats.setStrength(-stats.getStrength());
        stats.setWeaponRatingMax(-stats.getWeaponRatingMax());
        stats.setWeaponRatingMin(-stats.getWeaponRatingMin());
        stats.setWillpower(-stats.getWillpower());
        stats.setCurrentMana(-stats.getCurrentMana());
        stats.setMaxMana(-stats.getMaxMana());
        stats.setForaging(-stats.getForaging());
        stats.setInventorySize(-stats.getInventorySize());
        stats.setMaxEffects(-stats.getMaxEffects());
    }

}
