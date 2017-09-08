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
package com.syncleus.aethermud.entity;

import com.google.common.base.Function;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.items.ItemPojo;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.graphdb.GraphDbAetherMudStorage;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.world.RoomManager;
import com.syncleus.aethermud.world.model.Room;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.codahale.metrics.MetricRegistry.name;

public class EntityManager {

    private static final Logger log = Logger.getLogger(EntityManager.class);

    private final GraphStorageFactory graphStorageFactory;
    private final RoomManager roomManager;
    private final PlayerManager playerManager;
    private final Map<String, NpcSpawn> npcs = new ConcurrentHashMap<>();
    private final Map<String, AetherMudEntity> entities = new ConcurrentHashMap<>();
    private final ExecutorService mainTickExecutorService = Executors.newFixedThreadPool(50);

    public EntityManager(GraphStorageFactory graphStorageFactory, RoomManager roomManager, PlayerManager playerManager) {
        this.graphStorageFactory = graphStorageFactory;
        this.roomManager = roomManager;
        this.playerManager = playerManager;
        ExecutorService tickOrchestratorService = Executors.newFixedThreadPool(5);
        tickOrchestratorService.submit(new PlayerTicker());
        tickOrchestratorService.submit(new RoomTicker());
        tickOrchestratorService.submit(new NpcTicker());
        tickOrchestratorService.submit(new EntityTicker());
    }

    public Map<String, NpcSpawn> getNpcs() {
        return npcs;
    }

    public Map<String, AetherMudEntity> getEntities() {
        return entities;
    }

    public void addEntity(AetherMudEntity aetherMudEntity) {
        if (aetherMudEntity instanceof NpcSpawn) {
            NpcSpawn npcSpawn = (NpcSpawn) aetherMudEntity;
            npcs.put(aetherMudEntity.getEntityId(), npcSpawn);
        } else if (aetherMudEntity instanceof Room) {
            roomManager.addRoom((Room) aetherMudEntity);
        } else {
            entities.put(aetherMudEntity.getEntityId(), aetherMudEntity);
        }
    }

    public ItemData saveItem(ItemPojo item) {
        return this.transact(storage -> storage.saveItem(item));
    }

    public void removeItem(ItemPojo item) {
        this.consume(storage -> storage.removeItem(item.getItemId()));
    }

    public void removeItem(String itemId) {
        this.consume(storage -> storage.removeItem(itemId));
    }

    public Optional<ItemPojo> getItemEntity(String itemId) {
        return this.transactRead(storage -> {
            Optional<ItemData> item = storage.getItemEntity(itemId);
            return item.map(itemName -> new ItemBuilder().from(itemName).create());
        });
    }

    public void deleteNpcEntity(String npcId) {
        npcs.remove(npcId);
    }

    public NpcSpawn getNpcEntity(String npcId) {
        return npcs.get(npcId);
    }

    public static final int SLEEP_MILLIS = 500;

    private <T> T transact(Function<GraphDbAetherMudStorage, T> func) {
        try( GraphStorageFactory.AetherMudTx tx = this.graphStorageFactory.beginTransaction() ) {
            T retVal = func.apply(tx.getStorage());
            tx.success();
            return retVal;
        }
    }

    private void consume(Consumer<GraphDbAetherMudStorage> func) {
        try( GraphStorageFactory.AetherMudTx tx = this.graphStorageFactory.beginTransaction() ) {
            func.accept(tx.getStorage());
            tx.success();
        }
    }

    private <T> T transactRead(Function<GraphDbAetherMudStorage, T> func) {
        try( GraphStorageFactory.AetherMudTx tx = this.graphStorageFactory.beginTransaction() ) {
            return func.apply(tx.getStorage());
        }
    }

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
                    for (Map.Entry<String, NpcSpawn> next : npcs.entrySet()) {
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
