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
package com.syncleus.aethermud.items;


import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.world.model.Area;

import java.util.Set;

public class Forage extends AetherMudEntity {

    private final String internalItemName;
    private final int minLevel;
    private final double pctOfSuccess;
    private final int minAmt;
    private final int maxAmt;
    private final int forageExperience;
    private final int coolDownTicks;
    private int coolDownTicksLeft;
    private final Set<Area> forageAreas;

    public Forage(String internalItemName, int minLevel, double pctOfSuccess, int minAmt, int maxAmt, int forageExperience, int coolDownTicks, Set<Area> forageAreas) {
        if( forageAreas == null || forageAreas.isEmpty() )
            throw new IllegalArgumentException((internalItemName == null ? "(null)" : internalItemName) + ": forageAreas must not be null and must have at least one value.");

        this.internalItemName = internalItemName;
        this.minLevel = minLevel;
        this.pctOfSuccess = pctOfSuccess;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
        this.coolDownTicksLeft = 0;
        this.forageExperience = forageExperience;
        this.coolDownTicks = coolDownTicks;
        this.forageAreas = forageAreas;
    }

    public Set<Area> getForageAreas() {
        return forageAreas;
    }

    public String getInternalItemName() {
        return internalItemName;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public double getPctOfSuccess() {
        return pctOfSuccess;
    }

    public int getMinAmt() {
        return minAmt;
    }

    public int getMaxAmt() {
        return maxAmt;
    }

    public int getCoolDownTicks() {
        return coolDownTicks;
    }

    public int getForageExperience() {
        return forageExperience;
    }

    public int getCoolDownTicksLeft() {
        return coolDownTicksLeft;
    }

    public void setCoolDownTicksLeft(int coolDownTicksLeft) {
        this.coolDownTicksLeft = coolDownTicksLeft;
    }

    @Override
    public void run() {
        if (coolDownTicksLeft > 0) {
            coolDownTicksLeft--;
        }
    }
}
