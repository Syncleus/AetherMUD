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

public interface Effect {
    String getEffectName();

    String getEffectDescription();

    List<String> getEffectApplyMessages();

    Stats getApplyStatsOnTick();

    int getMaxEffectApplications();

    boolean isFrozenMovement();

    int getEffectApplications();

    void setEffectApplications(int effectApplications);

    Stats getDurationStats();

    String getPlayerId();

    void setPlayerId(String playerId);

    void setEffectName(String effectName);

    void setEffectDescription(String effectDescription);

    void setEffectApplyMessages(List<String> effectApplyMessages);

    void setApplyStatsOnTick(Stats applyStatsOnTick);

    void setDurationStats(Stats durationStats);

    void setMaxEffectApplications(int maxEffectApplications);

    void setFrozenMovement(boolean frozenMovement);
}
