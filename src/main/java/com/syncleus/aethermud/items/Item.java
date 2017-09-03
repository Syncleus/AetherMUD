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

import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.stats.Stats;

import java.util.List;
import java.util.Set;

public interface Item {
    Stats getItemApplyStats();

    List<TimeTracker.TimeOfDay> getValidTimeOfDays();

    void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays);

    void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays);

    boolean isDisposable();

    int getMaxUses();

    boolean isWithPlayer();

    void setWithPlayer(boolean isWithPlayer);

    int getNumberOfUses();

    void setNumberOfUses(int numberOfUses);

    String getItemId();

    String getInternalItemName();

    String getItemName();

    String getItemDescription();

    List<String> getItemTriggers();

    void setItemTriggers(List<String> itemTriggers);

    String getRestingName();

    int getItemHalfLifeTicks();

    Loot getLoot();

    void setEquipment(Equipment equipment);

    void setHasBeenWithPlayer(boolean hasBeenWithPlayer);

    Equipment getEquipment();

    Rarity getRarity();

    int getValueInGold();

    void setEffects(Set<EffectPojo> effects);

    Set<EffectPojo> getEffects();

    void setItemName(String itemName);

    void setItemDescription(String itemDescription);

    void setInternalItemName(String internalItemName);

    void setRestingName(String restingName);

    void setItemId(String itemId);

    void setLoot(Loot loot);

    void setItemHalfLifeTicks(int itemHalfLifeTicks);

    void setRarity(Rarity rarity);

    void setValueInGold(int valueInGold);

    void setMaxUses(int maxUses);

    void setDisposable(boolean disposable);

    void setItemApplyStats(Stats itemApplyStats);

    boolean isHasBeenWithPlayer();
}
