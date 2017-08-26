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

import com.syncleus.aethermud.stats.Stats;

import java.util.List;

public class EffectBuilder {
    private String effectName;
    private String effectDescription;
    private List<String> effectApplyMessages;
    private Stats applyStatsOnTick;
    private Stats durationStats;
    private int lifeSpanTicks;
    private boolean frozenMovement;

    public EffectBuilder setEffectName(String effectName) {
        this.effectName = effectName;
        return this;
    }

    public EffectBuilder setEffectDescription(String effectDescription) {
        this.effectDescription = effectDescription;
        return this;
    }

    public EffectBuilder setEffectApplyMessages(List<String> effectApplyMessages) {
        this.effectApplyMessages = effectApplyMessages;
        return this;
    }

    public EffectBuilder setApplyStatsOnTick(Stats applyStatsOnTick) {
        this.applyStatsOnTick = applyStatsOnTick;
        return this;
    }

    public EffectBuilder setDurationStats(Stats durationStats) {
        this.durationStats = durationStats;
        return this;
    }

    public EffectBuilder setLifeSpanTicks(int lifeSpanTicks) {
        this.lifeSpanTicks = lifeSpanTicks;
        return this;
    }

    public EffectBuilder setFrozenMovement(boolean frozenMovement) {
        this.frozenMovement = frozenMovement;
        return this;
    }

    public Effect createEffect() {
        return new Effect(effectName, effectDescription, effectApplyMessages, applyStatsOnTick, durationStats, lifeSpanTicks, frozenMovement);
    }
}
