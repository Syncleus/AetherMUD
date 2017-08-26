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
package com.syncleus.aethermud.player;


public class CoolDown {

    private int numberOfTicks;
    private final String name;
    private final CoolDownType coolDownType;
    private final int originalNumberOfTicks;

    public CoolDown(CoolDownType coolDownType) {
        this.name = coolDownType.getName();
        this.numberOfTicks = coolDownType.getTicks();
        this.coolDownType = coolDownType;
        this.originalNumberOfTicks = coolDownType.getTicks();
    }

    public CoolDown(String name, int numberOfTicks, CoolDownType coolDownType){
        this.name = name;
        this.numberOfTicks = numberOfTicks;
        this.coolDownType = coolDownType;
        this.originalNumberOfTicks = numberOfTicks;
    }

    public void decrementTick() {
        if (numberOfTicks > 0) {
            this.numberOfTicks = numberOfTicks - 1;
        }
    }

    public boolean isActive() {
        return numberOfTicks > 0;
    }

    public CoolDownType getCoolDownType() {
        return coolDownType;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfTicks() {
        return numberOfTicks;
    }

    public int getOriginalNumberOfTicks() {
        return originalNumberOfTicks;
    }

    public void setNumberOfTicks(int numberOfTicks) {
        this.numberOfTicks = numberOfTicks;
    }
}
