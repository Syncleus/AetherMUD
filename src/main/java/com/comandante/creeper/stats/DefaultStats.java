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
package com.comandante.creeper.stats;


public class DefaultStats {

    public final static StatsBuilder DEFAULT_PLAYER = new StatsBuilder()
            .setStrength(9)
            .setIntelligence(9)
            .setWillpower(9)
            .setAim(9)
            .setAgile(9)
            .setArmorRating(4)
            .setMeleSkill(9)
            .setCurrentHealth(100)
            .setMaxHealth(100)
            .setWeaponRatingMin(4)
            .setWeaponRatingMax(6)
            .setNumberOfWeaponRolls(1)
            .setExperience(0)
            .setCurrentMana(100)
            .setMaxMana(100)
            .setForaging(0)
            .setInventorySize(10)
            .setMaxEffects(4);
}
