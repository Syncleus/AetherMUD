package com.comandante.creeper.entity;

import com.comandante.creeper.Main;
import com.comandante.creeper.core_game.SentryManager;
import com.comandante.creeper.items.Effect;
import com.comandante.creeper.items.EffectSerializer;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemSerializer;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.world.RoomManager;
import com.comandante.creeper.world.model.Room;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.codahale.metrics.MetricRegistry.name;

public class EntityManager {

    private static final Logger log = Logger.getLogger(EntityManager.class);
    private final ConcurrentHashMap<String, Npc> npcs = new ConcurrentHashMap<>();
    private final HTreeMap<String, Item> items;
    private final HTreeMap<String, Effect> effects;
    private final ConcurrentHashMap<String, CreeperEntity> entities = new ConcurrentHashMap<>();
    private final ExecutorService mainTickExecutorService = Executors.newFixedThreadPool(50);
    private final RoomManager roomManager;
    private final PlayerManager playerManager;

    private final static String ITEM_MAP = "itemMap";
    private final static String EFFECTS_MAP = "effectsMap";

    public EntityManager(RoomManager roomManager, PlayerManager playerManager, DB db) {
        this.roomManager = roomManager;
        this.items = db.hashMap(ITEM_MAP)
                .keySerializer(Serializer.STRING)
                .valueSerializer(new ItemSerializer())
                .createOrOpen();

        this.effects = db.hashMap(EFFECTS_MAP)
                .keySerializer(Serializer.STRING)
                .valueSerializer(new EffectSerializer())
                .createOrOpen();

        this.playerManager = playerManager;
        ExecutorService tickOrchestratorService = Executors.newFixedThreadPool(5);
        tickOrchestratorService.submit(new PlayerTicker());
        tickOrchestratorService.submit(new RoomTicker());
        tickOrchestratorService.submit(new NpcTicker());
        tickOrchestratorService.submit(new EntityTicker());
    }

    public ConcurrentHashMap<String, Npc> getNpcs() {
        return npcs;
    }

    public ConcurrentHashMap<String, CreeperEntity> getEntities() {

        return entities;
    }

    public void addEntity(CreeperEntity creeperEntity) {
        if (creeperEntity instanceof Npc) {
            Npc npc = (Npc) creeperEntity;
            npcs.put(creeperEntity.getEntityId(), npc);
        } else if (creeperEntity instanceof Room) {
            roomManager.addRoom((Room) creeperEntity);
        } else {
            entities.put(creeperEntity.getEntityId(), creeperEntity);
        }
    }

    public void saveItem(Item item) {
        items.put(item.getItemId(), item);
    }

    public void removeItem(Item item) {
        items.remove(item.getItemId());
    }

    public void removeItem(String itemId) {
        items.remove(itemId);
    }

    public void saveEffect(Effect effect) {
        effects.put(effect.getEntityId(), effect);
    }

    public void removeEffect(Effect effect) {
        effects.remove(effect.getEntityId());
    }

    public void deleteNpcEntity(String npcId) {
        npcs.remove(npcId);
    }

    public Npc getNpcEntity(String npcId) {
        return npcs.get(npcId);
    }

    public Item getItemEntity(String itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            return null;
        }
        return new Item(item);
    }

    public Effect getEffectEntity(String effectId) {
        Effect effect = effects.get(effectId);
        if (effect == null) {
            return null;
        }
        return new Effect(effect);
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
                    for (Map.Entry<String, CreeperEntity> next : entities.entrySet()) {
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
