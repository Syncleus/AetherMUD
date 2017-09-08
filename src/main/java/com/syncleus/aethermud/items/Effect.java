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

public class Effect {

    private String effectName;
    private String effectDescription;
    private List<String> effectApplyMessages;
    private Stats applyStatsOnTick;
    private Stats durationStats;
    private int maxEffectApplications;
    private boolean frozenMovement;
    private int effectApplications;
    private String playerId;

    public Effect() {

    }

    public Effect(String effectName, String effectDescription, List<String> effectApplyMessages, Stats applyStatsOnTick, Stats durationStats, int maxEffectApplications, boolean frozenMovement) {
        this.effectName = effectName;
        this.effectDescription = effectDescription;
        this.effectApplyMessages = effectApplyMessages;
        this.applyStatsOnTick = applyStatsOnTick;
        this.durationStats = durationStats;
        this.maxEffectApplications = maxEffectApplications;
        this.frozenMovement = frozenMovement;
        this.effectApplications = 0;
    }

    public Effect(Effect effect) {
        this.effectName = effect.effectName;
        this.effectDescription = effect.effectDescription;
        this.effectApplyMessages = effect.effectApplyMessages;
        this.applyStatsOnTick = effect.applyStatsOnTick;
        this.durationStats = effect.durationStats;
        this.maxEffectApplications = effect.maxEffectApplications;
        this.frozenMovement = effect.frozenMovement;
        this.effectApplications = effect.effectApplications;
    }


    public String getEffectName() {
        return effectName;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    public List<String> getEffectApplyMessages() {
        return effectApplyMessages;
    }

    public Stats getApplyStatsOnTick() {
        return applyStatsOnTick;
    }

    public int getMaxEffectApplications() {
        return maxEffectApplications;
    }

    public boolean isFrozenMovement() {
        return frozenMovement;
    }

    public int getEffectApplications() {
        return effectApplications;
    }

    public void setEffectApplications(int effectApplications) {
        this.effectApplications = effectApplications;
    }

    public Stats getDurationStats() {
        return durationStats;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public void setEffectDescription(String effectDescription) {
        this.effectDescription = effectDescription;
    }

    public void setEffectApplyMessages(List<String> effectApplyMessages) {
        this.effectApplyMessages = effectApplyMessages;
    }

    public void setApplyStatsOnTick(Stats applyStatsOnTick) {
        this.applyStatsOnTick = applyStatsOnTick;
    }

    public void setDurationStats(Stats durationStats) {
        this.durationStats = durationStats;
    }

    public void setMaxEffectApplications(int maxEffectApplications) {
        this.maxEffectApplications = maxEffectApplications;
    }

    public void setFrozenMovement(boolean frozenMovement) {
        this.frozenMovement = frozenMovement;
    }
}
