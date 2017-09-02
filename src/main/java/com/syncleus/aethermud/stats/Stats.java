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

import static java.lang.StrictMath.sqrt;

public interface Stats {
    Integer getIntelligence();

    void setIntelligence(int intelligence);

    Integer getMaxEffects();

    void setMaxEffects(int maxEffects);

    Integer getExperience();

    void setExperience(int experience);

    Integer getStrength();

    void setStrength(int strength);

    Integer getWillpower();

    void setWillpower(int willpower);

    Integer getAim();

    void setAim(int aim);

    Integer getAgile();

    void setAgile(int agile);

    Integer getArmorRating();

    void setArmorRating(int armorRating);

    Integer getMeleSkill();

    void setMeleSkill(int meleSkill);

    Integer getCurrentHealth();

    void setCurrentHealth(int currentHealth);

    Integer getMaxHealth();

    void setMaxHealth(int maxHealth);

    Integer getWeaponRatingMax();

    void setWeaponRatingMax(int weaponRatingMax);

    Integer getWeaponRatingMin();

    void setWeaponRatingMin(int weaponRatingMin);

    Integer getNumberOfWeaponRolls();

    void setNumberOfWeaponRolls(int numberOfWeaponRolls);

    Integer getCurrentMana();

    Integer getMaxMana();

    void setCurrentMana(int currentMana);

    void setMaxMana(int maxMana);

    Integer getForaging();

    void setForaging(int foraging);

    Integer getInventorySize();

    void setInventorySize(int inventorySize);

    default Integer getLevel() {
        double v = 0.02 * sqrt(getExperience());
        return Double.valueOf(Math.floor(v)).intValue();
    }
}
