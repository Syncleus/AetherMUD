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
    public abstract Integer getIntelligence();

    @Override
    @Property("intelligence")
    public abstract void setIntelligence(Integer intelligence);

    @Override
    @Property("maxEffects")
    public abstract Integer getMaxEffects();

    @Override
    @Property("maxEffects")
    public abstract void setMaxEffects(Integer maxEffects);

    @Override
    @Property("experience")
    public abstract Integer getExperience();

    @Override
    @Property("experience")
    public abstract void setExperience(Integer experience);

    @Override
    @Property("strength")
    public abstract Integer getStrength();

    @Override
    @Property("strength")
    public abstract void setStrength(Integer strength);

    @Override
    @Property("willpower")
    public abstract Integer getWillpower();

    @Override
    @Property("willpower")
    public abstract void setWillpower(Integer willpower);

    @Override
    @Property("aim")
    public abstract Integer getAim();

    @Override
    @Property("aim")
    public abstract void setAim(Integer aim);

    @Override
    @Property("agile")
    public abstract Integer getAgile();

    @Override
    @Property("agile")
    public abstract void setAgile(Integer agile);

    @Override
    @Property("armorRating")
    public abstract Integer getArmorRating();

    @Override
    @Property("armorRating")
    public abstract void setArmorRating(Integer armorRating);

    @Override
    @Property("meleSkill")
    public abstract Integer getMeleSkill();

    @Override
    @Property("meleSkill")
    public abstract void setMeleeSkill(Integer meleSkill);

    @Override
    @Property("currentHealth")
    public abstract Integer getCurrentHealth();

    @Override
    @Property("currentHealth")
    public abstract void setCurrentHealth(Integer currentHealth);

    @Override
    @Property("maxHealth")
    public abstract Integer getMaxHealth();

    @Override
    @Property("maxHealth")
    public abstract void setMaxHealth(Integer maxHealth);

    @Override
    @Property("weaponRatingMax")
    public abstract Integer getWeaponRatingMax();

    @Override
    @Property("weaponRatingMax")
    public abstract void setWeaponRatingMax(Integer weaponRatingMax);

    @Override
    @Property("weaponRatingMin")
    public abstract Integer getWeaponRatingMin();

    @Override
    @Property("weaponRatingMin")
    public abstract void setWeaponRatingMin(Integer weaponRatingMin);

    @Override
    @Property("numberOfWeaponRolls")
    public abstract Integer getNumberOfWeaponRolls();

    @Override
    @Property("numberOfWeaponRolls")
    public abstract void setNumberOfWeaponRolls(Integer numberOfWeaponRolls);

    @Override
    @Property("currentMana")
    public abstract Integer getCurrentMana();

    @Override
    @Property("maxMana")
    public abstract Integer getMaxMana();

    @Override
    @Property("currentMana")
    public abstract void setCurrentMana(Integer currentMana);

    @Override
    @Property("maxMana")
    public abstract void setMaxMana(Integer maxMana);

    @Override
    @Property("foraging")
    public abstract Integer getForaging();

    @Override
    @Property("foraging")
    public abstract void setForaging(Integer foraging);

    @Override
    @Property("inventorySize")
    public abstract Integer getInventorySize();

    @Override
    @Property("inventorySize")
    public abstract void setInventorySize(Integer inventorySize);
}
