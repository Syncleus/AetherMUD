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
package com.syncleus.aethermud.storage.graphdb;

import com.syncleus.aethermud.stats.Stats;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Property;

public abstract class StatsData extends AbstractVertexFrame implements Stats {
    @Override
    @Property("intelligence")
    public abstract long getIntelligence();
    @Override
    @Property("intelligence")
    public abstract void setIntelligence(long intelligence);

    @Override
    @Property("maxEffects")
    public abstract long getMaxEffects();

    @Override
    @Property("maxEffects")
    public abstract void setMaxEffects(long maxEffects);

    @Override
    @Property("experience")
    public abstract long getExperience();

    @Override
    @Property("experience")
    public abstract void setExperience(long experience);

    @Override
    @Property("strength")
    public abstract long getStrength();

    @Override
    @Property("strength")
    public abstract void setStrength(long strength);

    @Override
    @Property("willpower")
    public abstract long getWillpower();

    @Override
    @Property("willpower")
    public abstract void setWillpower(long willpower);

    @Override
    @Property("aim")
    public abstract long getAim();

    @Override
    @Property("aim")
    public abstract void setAim(long aim);

    @Override
    @Property("agile")
    public abstract long getAgile();

    @Override
    @Property("agile")
    public abstract void setAgile(long agile);

    @Override
    @Property("armorRating")
    public abstract long getArmorRating();

    @Override
    @Property("armorRating")
    public abstract void setArmorRating(long armorRating);

    @Override
    @Property("meleSkill")
    public abstract long getMeleSkill();

    @Override
    @Property("meleSkill")
    public abstract void setMeleSkill(long meleSkill);

    @Override
    @Property("currentHealth")
    public abstract long getCurrentHealth();

    @Override
    @Property("currentHealth")
    public abstract void setCurrentHealth(long currentHealth);

    @Override
    @Property("maxHealth")
    public abstract long getMaxHealth();

    @Override
    @Property("maxHealth")
    public abstract void setMaxHealth(long maxHealth);

    @Override
    @Property("weaponRatingMax")
    public abstract long getWeaponRatingMax();

    @Override
    @Property("weaponRatingMax")
    public abstract void setWeaponRatingMax(long weaponRatingMax);

    @Override
    @Property("weaponRatingMin")
    public abstract long getWeaponRatingMin();

    @Override
    @Property("weaponRatingMin")
    public abstract void setWeaponRatingMin(long weaponRatingMin);

    @Override
    @Property("numberOfWeaponRolls")
    public abstract long getNumberOfWeaponRolls();

    @Override
    @Property("numberOfWeaponRolls")
    public abstract void setNumberOfWeaponRolls(long numberOfWeaponRolls);

    @Override
    @Property("currentMana")
    public abstract long getCurrentMana();

    @Override
    @Property("maxMana")
    public abstract long getMaxMana();

    @Override
    @Property("currentMana")
    public abstract void setCurrentMana(long currentMana);

    @Override
    @Property("maxMana")
    public abstract void setMaxMana(long maxMana);

    @Override
    @Property("foraging")
    public abstract long getForaging();

    @Override
    @Property("foraging")
    public abstract void setForaging(long foraging);

    @Override
    @Property("inventorySize")
    public abstract long getInventorySize();

    @Override
    @Property("inventorySize")
    public abstract void setInventorySize(long inventorySize);
}
