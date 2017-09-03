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

public class EffectPojo implements Effect {

    private String effectName;
    private String effectDescription;
    private List<String> effectApplyMessages;
    private Stats applyStatsOnTick;
    private Stats durationStats;
    private int maxEffectApplications;
    private boolean frozenMovement;
    private int effectApplications;
    private String playerId;

    public EffectPojo(String effectName, String effectDescription, List<String> effectApplyMessages, Stats applyStatsOnTick, Stats durationStats, int maxEffectApplications, boolean frozenMovement) {
        this.effectName = effectName;
        this.effectDescription = effectDescription;
        this.effectApplyMessages = effectApplyMessages;
        this.applyStatsOnTick = applyStatsOnTick;
        this.durationStats = durationStats;
        this.maxEffectApplications = maxEffectApplications;
        this.frozenMovement = frozenMovement;
        this.effectApplications = 0;
    }

    public EffectPojo(EffectPojo effect) {
        this.effectName = effect.effectName;
        this.effectDescription = effect.effectDescription;
        this.effectApplyMessages = effect.effectApplyMessages;
        this.applyStatsOnTick = effect.applyStatsOnTick;
        this.durationStats = effect.durationStats;
        this.maxEffectApplications = effect.maxEffectApplications;
        this.frozenMovement = effect.frozenMovement;
        this.effectApplications = effect.effectApplications;
    }


    @Override
    public String getEffectName() {
        return effectName;
    }

    @Override
    public String getEffectDescription() {
        return effectDescription;
    }

    @Override
    public List<String> getEffectApplyMessages() {
        return effectApplyMessages;
    }

    @Override
    public Stats getApplyStatsOnTick() {
        return applyStatsOnTick;
    }

    @Override
    public int getMaxEffectApplications() {
        return maxEffectApplications;
    }

    @Override
    public boolean isFrozenMovement() {
        return frozenMovement;
    }

    @Override
    public int getEffectApplications() {
        return effectApplications;
    }

    @Override
    public void setEffectApplications(int effectApplications) {
        this.effectApplications = effectApplications;
    }

    @Override
    public Stats getDurationStats() {
        return durationStats;
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    @Override
    public void setEffectDescription(String effectDescription) {
        this.effectDescription = effectDescription;
    }

    @Override
    public void setEffectApplyMessages(List<String> effectApplyMessages) {
        this.effectApplyMessages = effectApplyMessages;
    }

    @Override
    public void setApplyStatsOnTick(Stats applyStatsOnTick) {
        this.applyStatsOnTick = applyStatsOnTick;
    }

    @Override
    public void setDurationStats(Stats durationStats) {
        this.durationStats = durationStats;
    }

    @Override
    public void setMaxEffectApplications(int maxEffectApplications) {
        this.maxEffectApplications = maxEffectApplications;
    }

    @Override
    public void setFrozenMovement(boolean frozenMovement) {
        this.frozenMovement = frozenMovement;
    }
}
