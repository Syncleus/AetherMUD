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

    void setIntelligence(Integer Integerelligence);

    Integer getMaxEffects();

    void setMaxEffects(Integer maxEffects);

    Integer getExperience();

    void setExperience(Integer experience);

    Integer getStrength();

    void setStrength(Integer strength);

    Integer getWillpower();

    void setWillpower(Integer willpower);

    Integer getAim();

    void setAim(Integer aim);

    Integer getAgile();

    void setAgile(Integer agile);

    Integer getArmorRating();

    void setArmorRating(Integer armorRating);

    Integer getMeleeSkill();

    void setMeleeSkill(Integer meleSkill);

    Integer getCurrentHealth();

    void setCurrentHealth(Integer currentHealth);

    Integer getMaxHealth();

    void setMaxHealth(Integer maxHealth);

    Integer getWeaponRatingMax();

    void setWeaponRatingMax(Integer weaponRatingMax);

    Integer getWeaponRatingMin();

    void setWeaponRatingMin(Integer weaponRatingMin);

    Integer getNumberOfWeaponRolls();

    void setNumberOfWeaponRolls(Integer numberOfWeaponRolls);

    Integer getCurrentMana();

    Integer getMaxMana();

    void setCurrentMana(Integer currentMana);

    void setMaxMana(Integer maxMana);

    Integer getForaging();

    void setForaging(Integer foraging);

    Integer getInventorySize();

    void setInventorySize(Integer inventorySize);

    default Integer getLevel() {
        double v = 0.02 * sqrt(getExperience());
        return Double.valueOf(Math.floor(v)).intValue();
    }
}
