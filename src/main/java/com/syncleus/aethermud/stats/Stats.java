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
    long getIntelligence();

    void setIntelligence(long intelligence);

    long getMaxEffects();

    void setMaxEffects(long maxEffects);

    long getExperience();

    void setExperience(long experience);

    long getStrength();

    void setStrength(long strength);

    long getWillpower();

    void setWillpower(long willpower);

    long getAim();

    void setAim(long aim);

    long getAgile();

    void setAgile(long agile);

    long getArmorRating();

    void setArmorRating(long armorRating);

    long getMeleSkill();

    void setMeleSkill(long meleSkill);

    long getCurrentHealth();

    void setCurrentHealth(long currentHealth);

    long getMaxHealth();

    void setMaxHealth(long maxHealth);

    long getWeaponRatingMax();

    void setWeaponRatingMax(long weaponRatingMax);

    long getWeaponRatingMin();

    void setWeaponRatingMin(long weaponRatingMin);

    long getNumberOfWeaponRolls();

    void setNumberOfWeaponRolls(long numberOfWeaponRolls);

    long getCurrentMana();

    long getMaxMana();

    void setCurrentMana(long currentMana);

    void setMaxMana(long maxMana);

    long getForaging();

    void setForaging(long foraging);

    long getInventorySize();

    void setInventorySize(long inventorySize);

    default long getLevel() {
        double v = 0.02 * sqrt(getExperience());
        return (long) Math.floor(v);
    }
}
