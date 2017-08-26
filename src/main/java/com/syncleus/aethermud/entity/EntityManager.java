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
package com.syncleus.aethermud.entity;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.world.RoomManager;
import com.syncleus.aethermud.world.model.Room;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.codahale.metrics.MetricRegistry.name;

public class EntityManager {

    private static final Logger log = Logger.getLogger(EntityManager.class);


    private final AetherMudStorage aetherMudStorage;
    private final RoomManager roomManager;
    private final PlayerManager playerManager;
    private final Map<String, Npc> npcs = new ConcurrentHashMap<>();
    private final Map<String, AetherMudEntity> entities = new ConcurrentHashMap<>();
    private final ExecutorService mainTickExecutorService = Executors.newFixedThreadPool(50);

    public EntityManager(AetherMudStorage aetherMudStorage, RoomManager roomManager, PlayerManager playerManager) {
        this.aetherMudStorage = aetherMudStorage;
        this.roomManager = roomManager;
        this.playerManager = playerManager;
        ExecutorService tickOrchestratorService = Executors.newFixedThreadPool(5);
        tickOrchestratorService.submit(new PlayerTicker());
        tickOrchestratorService.submit(new RoomTicker());
        tickOrchestratorService.submit(new NpcTicker());
        tickOrchestratorService.submit(new EntityTicker());
    }

    public Map<String, Npc> getNpcs() {
        return npcs;
    }

    public Map<String, AetherMudEntity> getEntities() {
        return entities;
    }

    public void addEntity(AetherMudEntity aetherMudEntity) {
        if (aetherMudEntity instanceof Npc) {
            Npc npc = (Npc) aetherMudEntity;
            npcs.put(aetherMudEntity.getEntityId(), npc);
        } else if (aetherMudEntity instanceof Room) {
            roomManager.addRoom((Room) aetherMudEntity);
        } else {
            entities.put(aetherMudEntity.getEntityId(), aetherMudEntity);
        }
    }

    public void saveItem(Item item) {
        aetherMudStorage.saveItemEntity(item);
    }

    public void removeItem(Item item) {
        aetherMudStorage.removeItem(item.getItemId());
    }

    public void removeItem(String itemId) {
        aetherMudStorage.removeItem(itemId);
    }

    public Optional<Item> getItemEntity(String itemId) {
        Optional<Item> item = aetherMudStorage.getItemEntity(itemId);
        return item.map(itemName -> new ItemBuilder().from(itemName).create());
    }

    public void deleteNpcEntity(String npcId) {
        npcs.remove(npcId);
    }

    public Npc getNpcEntity(String npcId) {
        return npcs.get(npcId);
    }

    public static final int SLEEP_MILLIS = 500;

    class PlayerTicker implements Runnable {
        private final com.codahale.metrics.Timer ticktime = Main.metrics.timer(name(EntityManager.class, "player_tick_time"));

        @Override
        public void run() {
            while (true) {
                try {
                    final com.codahale.metrics.Timer.Context context = ticktime.time();
                    Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
                    while (players.hasNext()) {
                        Map.Entry<String, Player> next = players.next();
                        mainTickExecutorService.submit(next.getValue());
                    }
                    context.stop();
                    Thread.sleep(SLEEP_MILLIS);
                } catch (Exception e) {
                    log.error("Problem with player ticker!", e);
                    SentryManager.logSentry(this.getClass(), e, "Problem with player ticker!");
                }
            }
        }
    }

    class RoomTicker implements Runnable {
        private final com.codahale.metrics.Timer ticktime = Main.metrics.timer(name(EntityManager.class, "room_tick_time"));

        @Override
        public void run() {
            while (true) {
                try {
                    final com.codahale.metrics.Timer.Context context = ticktime.time();
                    Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRoomsIterator();
                    while (rooms.hasNext()) {
                        Map.Entry<Integer, Room> next = rooms.next();
                        mainTickExecutorService.submit(next.getValue());
                    }
                    context.stop();
                    Thread.sleep(SLEEP_MILLIS);
                } catch (Exception e) {
                    log.error("Problem with room ticker!", e);
                    SentryManager.logSentry(this.getClass(), e, "Problem with room ticker!");
                }
            }
        }
    }

    class NpcTicker implements Runnable {
        private final com.codahale.metrics.Timer ticktime = Main.metrics.timer(name(EntityManager.class, "npc_tick_time"));

        @Override
        public void run() {
            while (true) {
                try {
                    final com.codahale.metrics.Timer.Context context = ticktime.time();
                    for (Map.Entry<String, Npc> next : npcs.entrySet()) {
                        mainTickExecutorService.submit(next.getValue());
                    }
                    context.stop();
                    Thread.sleep(SLEEP_MILLIS);
                } catch (Exception e) {
                    log.error("Problem with npc ticker!", e);
                    SentryManager.logSentry(this.getClass(), e, "Problem with npc ticker!");
                }
            }
        }
    }

    class EntityTicker implements Runnable {
        private final com.codahale.metrics.Timer ticktime = Main.metrics.timer(name(EntityManager.class, "entity_tick_time"));

        @Override
        public void run() {
            while (true) {
                try {
                    final com.codahale.metrics.Timer.Context context = ticktime.time();
                    for (Map.Entry<String, AetherMudEntity> next : entities.entrySet()) {
                        mainTickExecutorService.submit(next.getValue());
                    }
                    context.stop();
                    Thread.sleep(SLEEP_MILLIS);
                } catch (Exception e) {
                    log.error("Problem with entity ticker!", e);
                    SentryManager.logSentry(this.getClass(), e, "Problem with entity ticker!");
                }
            }
        }
    }
}
