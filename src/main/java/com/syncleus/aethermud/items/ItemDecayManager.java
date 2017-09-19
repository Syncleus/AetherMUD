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

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.entity.EntityManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ItemDecayManager extends AetherMudEntity {

    private final ConcurrentHashMap<String, DecayProgress> itemDecayTracker = new ConcurrentHashMap<String, DecayProgress>();
    private final EntityManager entityManager;
    private final GameManager gameManager;
    private static final Logger log = Logger.getLogger(ItemDecayManager.class);
    private int tickBucket = 0;

    public ItemDecayManager(EntityManager entityManager, GameManager gameManager) {
        this.entityManager = entityManager;
        this.gameManager = gameManager;
    }

    public void addItem(ItemInstance item) {
        item.setWithPlayer(false);
        itemDecayTracker.put(item.getItemId(), new DecayProgress(item.getItemHalfLifeTicks()));
    }

    public void removeItemFromDecayManager(String itemId) {
        itemDecayTracker.remove(itemId);
    }

    public ConcurrentHashMap<String, DecayProgress> getItemDecayTracker() {
        return itemDecayTracker;
    }

    @Override
    public void run() {
        try {
            if (tickBucket == 10) {
                ConcurrentHashMap<String, DecayProgress> itemDecayTracker1 = getItemDecayTracker();
                for (Map.Entry<String, DecayProgress> next : itemDecayTracker1.entrySet()) {
                    DecayProgress decayProgress = next.getValue();
                    Optional<ItemInstance> itemOptional = entityManager.getItemEntity(next.getKey());
                    if (!itemOptional.isPresent()) {
                        removeItemFromDecayManager(next.getKey());
                        continue;
                    }
                    ItemInstance item = itemOptional.get();
                    if (item.isWithPlayer()) {
                        removeItemFromDecayManager(item.getItemId());
                        continue;
                    }
                    decayProgress.incTick();
                    if (decayProgress.getCurrentTicks() >= decayProgress.getNumberOfTicks()) {
                        removeItemFromDecayManager(item.getItemId());
                        entityManager.removeItem(item.getItemId());
                        gameManager.writeToRoom(gameManager.getRoomManager().getRoomByItemId(item.getItemId()).getRoomId(), item.getItemName() + " turns to dust.\r\n");
                    }
                }
                tickBucket = 0;
            } else {
                tickBucket = tickBucket + 1;
            }
        } catch (Exception e) {
            log.error("Problem with item decay manager!", e);
            SentryManager.logSentry(this.getClass(), e, "Exception caught in item decay manager!");
        }
    }

    class DecayProgress {
        private final int numberOfTicks;
        private int currentTicks = 0;

        DecayProgress(int numberOfTicks) {
            this.numberOfTicks = numberOfTicks;
        }

        public void incTick() {
            this.currentTicks++;
        }

        public int getNumberOfTicks() {
            return numberOfTicks;
        }

        public int getCurrentTicks() {
            return currentTicks;
        }
    }
}



