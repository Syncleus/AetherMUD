/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.spawner;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.storage.graphdb.model.ItemInstanceData;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class ItemSpawner extends AetherMudEntity {
    private static final Logger LOGGER = Logger.getLogger(ItemSpawner.class);
    private final Item item;
    private final SpawnRule spawnRule;
    private final GameManager gameManager;
    private Integer roomId;
    private int noTicks = 0;
    private final Random random = new Random();
    private final Area spawnArea;
    private final Interner<String> interner = Interners.newWeakInterner();


    public ItemSpawner(Item item, SpawnRule spawnRule, GameManager gameManager) {
        this.item = item;
        this.spawnRule = spawnRule;
        this.gameManager = gameManager;
        this.noTicks = spawnRule.getSpawnIntervalTicks();
        this.spawnArea = spawnRule.getArea();
    }

    public void incTicks() {
        noTicks++;
    }

    @Override
    public void run() {
        try {
            incTicks();
            if (noTicks >= spawnRule.getSpawnIntervalTicks()) {
                int randomPercentage = spawnRule.getRandomChance();
                int numberOfAttempts = spawnRule.getMaxInstances() - counterNumberInArea();
                for (int i = 0; i < numberOfAttempts; i++) {
                    if (random.nextInt(100) < randomPercentage || randomPercentage == 100) {
                        if (item.getValidTimeOfDays() != null && item.getValidTimeOfDays().size() > 0) {
                            if (item.getValidTimeOfDays().contains(gameManager.getTimeTracker().getTimeOfDay())) {
                                createAndAddItem();
                            }
                        } else {
                            createAndAddItem();
                        }
                    }
                    noTicks = 0;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Problem with item spawner ticker!", e);
            SentryManager.logSentry(this.getClass(), e, "Problem with item spawner!");
        }
    }

    private void createAndAddItem() {
        ArrayList<Room> rooms = Lists.newArrayList(Iterators.filter(gameManager.getRoomManager().getRoomsByArea(spawnArea).iterator(), getRoomsWithRoom()));
        Room room = rooms.get(random.nextInt(rooms.size()));
        synchronized (interner.intern(this.item.getInternalItemName())) {
            ItemInstance itemInstance = new ItemBuilder().from(this.item).create();
            gameManager.getEntityManager().saveItem(itemInstance);
            gameManager.placeItemInRoom(room.getRoomId(), itemInstance.getItemId());
            Main.metrics.counter(MetricRegistry.name(ItemSpawner.class, itemInstance.getItemName() + "-spawn")).inc();
        }
    }

    private int counterNumberInArea() {
        int numberCurrentlyInArea = 0;
        Set<Room> roomsByArea = gameManager.getRoomManager().getRoomsByArea(spawnArea);
        for (Room room : roomsByArea) {
            if (room.getAreas().contains(spawnArea)) {
                for (String i : room.getItemIds()) {
                    Optional<ItemInstance> currentItemOptional = gameManager.getEntityManager().getItemEntity(i);
                    if (!currentItemOptional.isPresent()) {
                        continue;
                    }
                    ItemInstance currentItem = currentItemOptional.get();
                    if (currentItem.getInternalItemName().equals(item.getInternalItemName())) {
                        numberCurrentlyInArea++;
                    }
                }
            }
        }
        return numberCurrentlyInArea;
    }

    private Predicate<Room> getRoomsWithRoom() {
        return new Predicate<Room>() {
            @Override
            public boolean apply(Room room) {
                int count = 0;
                Set<String> itemIds = room.getItemIds();
                for (String itemId : itemIds) {
                    Optional<ItemInstance> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
                    if (!itemOptional.isPresent()) {
                        continue;
                    }
                    ItemInstance item = itemOptional.get();
                    if (item.getInternalItemName().equals(ItemSpawner.this.item.getInternalItemName())) {
                        count++;
                    }
                }
                if (count < spawnRule.getMaxPerRoom()) {
                    return true;
                }
                return false;
            }
        };
    }
}
