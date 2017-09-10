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
package com.syncleus.aethermud.storage.graphdb.model;

import com.syncleus.aethermud.stats.Stats;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

import static java.lang.StrictMath.sqrt;

@GraphElement
public abstract class StatsData extends AbstractInterceptingVertexFrame {
    @Property("intelligence")
    public abstract Integer getIntelligence();

    @Property("intelligence")
    public abstract void setIntelligence(Integer intelligence);

    @Property("maxEffects")
    public abstract Integer getMaxEffects();

    @Property("maxEffects")
    public abstract void setMaxEffects(Integer maxEffects);

    @Property("experience")
    public abstract Integer getExperience();

    @Property("experience")
    public abstract void setExperience(Integer experience);

    @Property("strength")
    public abstract Integer getStrength();

    @Property("strength")
    public abstract void setStrength(Integer strength);

    @Property("willpower")
    public abstract Integer getWillpower();

    @Property("willpower")
    public abstract void setWillpower(Integer willpower);

    @Property("aim")
    public abstract Integer getAim();

    @Property("aim")
    public abstract void setAim(Integer aim);

    @Property("agile")
    public abstract Integer getAgile();

    @Property("agile")
    public abstract void setAgile(Integer agile);

    @Property("armorRating")
    public abstract Integer getArmorRating();

    @Property("armorRating")
    public abstract void setArmorRating(Integer armorRating);

    @Property("meleSkill")
    public abstract Integer getMeleeSkill();

    @Property("meleSkill")
    public abstract void setMeleeSkill(Integer meleSkill);

    @Property("currentHealth")
    public abstract Integer getCurrentHealth();

    @Property("currentHealth")
    public abstract void setCurrentHealth(Integer currentHealth);

    @Property("maxHealth")
    public abstract Integer getMaxHealth();

    @Property("maxHealth")
    public abstract void setMaxHealth(Integer maxHealth);

    @Property("weaponRatingMax")
    public abstract Integer getWeaponRatingMax();

    @Property("weaponRatingMax")
    public abstract void setWeaponRatingMax(Integer weaponRatingMax);

    @Property("weaponRatingMin")
    public abstract Integer getWeaponRatingMin();

    @Property("weaponRatingMin")
    public abstract void setWeaponRatingMin(Integer weaponRatingMin);

    @Property("numberOfWeaponRolls")
    public abstract Integer getNumberOfWeaponRolls();

    @Property("numberOfWeaponRolls")
    public abstract void setNumberOfWeaponRolls(Integer numberOfWeaponRolls);

    @Property("currentMana")
    public abstract Integer getCurrentMana();

    @Property("maxMana")
    public abstract Integer getMaxMana();

    @Property("currentMana")
    public abstract void setCurrentMana(Integer currentMana);

    @Property("maxMana")
    public abstract void setMaxMana(Integer maxMana);

    @Property("foraging")
    public abstract Integer getForaging();

    @Property("foraging")
    public abstract void setForaging(Integer foraging);

    @Property("inventorySize")
    public abstract Integer getInventorySize();

    @Property("inventorySize")
    public abstract void setInventorySize(Integer inventorySize);

    public static void copyStats(StatsData dest, Stats src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties");
        }
    }

    public static Stats copyStats(StatsData src) {
        Stats retVal = new Stats();
        try {
            PropertyUtils.copyProperties(retVal, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties");
        }
        return retVal;
    }

    public Integer getLevel() {
        double v = 0.02 * sqrt(getExperience());
        return Double.valueOf(Math.floor(v)).intValue();
    }
}
