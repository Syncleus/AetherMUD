package com.comandante.creeper.entity;

import com.comandante.creeper.Items.*;
import com.comandante.creeper.Items.EffectSerializer;
import com.comandante.creeper.Main;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.world.Room;
import com.comandante.creeper.world.RoomManager;
import com.google.common.collect.*;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.codahale.metrics.MetricRegistry.name;

public class EntityManager {

    private final ConcurrentHashMap<String, Npc> npcs = new ConcurrentHashMap<>();
    private final HTreeMap<String, Item> items;
    private final HTreeMap<String, Effect> effects;
    private final ConcurrentHashMap<String, CreeperEntity> entities = new ConcurrentHashMap<>();
    private final ExecutorService tickService = Executors.newFixedThreadPool(1);
    private final ExecutorService ticketRunnerService = Executors.newFixedThreadPool(10);
    private final RoomManager roomManager;
    private final PlayerManager playerManager;
    private final DB db;
    private final ChannelUtils channelUtils;
    private final ItemDecayManager itemDecayManager;
    private static final Logger log = Logger.getLogger(EntityManager.class);


    public EntityManager(RoomManager roomManager, PlayerManager playerManager, DB db, ChannelUtils channelUtils) {
        this.roomManager = roomManager;
        if (db.exists("itemMap")) {
            this.items = db.get("itemMap");
        } else {
            this.items = db.createHashMap("itemMap").valueSerializer(new ItemSerializer()).make();
        }
        if (db.exists("effectsMap")) {
            this.effects = db.get("effectsMap");
        } else {
            this.effects = db.createHashMap("effectsMap").valueSerializer(new EffectSerializer()).make();
        }
        this.playerManager = playerManager;
        this.db = db;
        tickService.submit(new Ticker());
        this.channelUtils = channelUtils;
        this.itemDecayManager = new ItemDecayManager(this);
        addEntity(itemDecayManager);
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

    public void saveEffect(Effect effect) {
        effects.put(effect.getEntityId(), effect);
    }

    public void removeEffect(Effect effect) {
        effects.remove(effect.getEntityId());
    }

    public void deleteNpcEntity(String npcId) {
        roomManager.getNpcCurrentRoom(getNpcEntity(npcId)).get().removePresentNpc(npcId);
        npcs.remove(npcId);
    }

    public Item getInventoryItem(Player player, String itemKeyword) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
            for (String itemId : playerMetadata.getInventory()) {
                Item itemEntity = getItemEntity(itemId);
                if (itemEntity == null) {
                    log.info("Orphaned inventoryId:" + itemId + " player: " + player.getPlayerName());
                    continue;
                }
                if (itemEntity.getItemTriggers().contains(itemKeyword)) {
                    return itemEntity;
                }
            }
            return null;
        }
    }

    public List<Item> getInventory(Player player) {
        PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
        List<Item> inventoryItems = Lists.newArrayList();
        List<String> inventory = playerMetadata.getInventory();
        if (inventory != null) {
            for (String itemId : inventory) {
                Item itemEntity = getItemEntity(itemId);
                if (itemEntity == null) {
                    log.info("Orphaned inventoryId:" + itemId + " player: " + player.getPlayerName());
                    continue;
                }
                inventoryItems.add(itemEntity);
            }
        }
        Collections.sort(inventoryItems, new Comparator<Item>() {
            @Override
            public int compare(final Item object1, final Item object2) {
                return object1.getItemName().compareTo(object2.getItemName());
            }
        });
        return inventoryItems;
    }

    public List<String> getRolledUpIntentory(Player player) {
        List<String> rolledUp = Lists.newArrayList();
        List<Item> inventory = getInventory(player);
        Map<String, Integer> itemAndCounts = Maps.newHashMap();
        if (inventory != null) {
            for (Item item : inventory) {
                StringBuilder invItem = new StringBuilder();
                invItem.append(item.getItemName());
                int maxUses = ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses();
                if (maxUses > 0) {
                    int remainingUses = maxUses - item.getNumberOfUses();
                    invItem.append(" - ").append(remainingUses);
                    if (remainingUses == 1) {
                        invItem.append(" use left.");
                    } else {
                        invItem.append(" uses left.");
                    }
                }
                if (itemAndCounts.containsKey(invItem.toString())) {
                    Integer integer = itemAndCounts.get(invItem.toString());
                    integer = integer + 1;
                    itemAndCounts.put(invItem.toString(), integer);
                } else {
                    itemAndCounts.put(invItem.toString(), 1);
                }
            }
            StringBuilder inventoryLine = new StringBuilder();
            for (Map.Entry<String, Integer> next : itemAndCounts.entrySet()) {
                if (next.getValue() > 1) {
                    inventoryLine.append(next.getKey()).append(" (").append(next.getValue()).append(")").append("\r\n");
                } else {
                    inventoryLine.append(next.getKey()).append("\r\n");
                }
            }
            rolledUp.add(inventoryLine.toString());
        }
        return rolledUp;
    }

        public Set<Item> getEquipment (Player player){
            PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
            Set<Item> equipmentItems = Sets.newHashSet();
            String[] equipment = playerMetadata.getPlayerEquipment();
            if (equipment != null) {
                for (String itemId : equipment) {
                    Item itemEntity = getItemEntity(itemId);
                    if (itemEntity == null) {
                        log.info("Orphaned equipmentId:" + itemId + " player: " + player.getPlayerName());
                        continue;
                    }
                    equipmentItems.add(itemEntity);
                }
            }
            return equipmentItems;
        }

        public List<Effect> getEffects (Player player){
            PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
            List<Effect> effectList = Lists.newArrayList();
            String[] effects = playerMetadata.getEffects();
            if (effects != null) {
                for (String effectId : effects) {
                    Effect effect = getEffect(effectId);
                    if (effect == null) {
                        log.info("Orphaned effectId:" + effectId + " player: " + player.getPlayerName());
                        continue;
                    }
                    effectList.add(effect);
                }
            }
            return effectList;
        }

    public Npc getNpcEntity(String npcId) {
        return npcs.get(npcId);
    }

    public Item getItemEntity(String itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            return item;
        }
        return new Item(item);
    }

    public Effect getEffect(String effectId) {
        Effect effect = effects.get(effectId);
        if (effect == null) {
            return effect;
        }
        return new Effect(effect);
    }


    class Ticker implements Runnable {

        private final com.codahale.metrics.Timer ticktime = Main.metrics.timer(name(EntityManager.class, "tick_time"));

        @Override
        public void run() {
            while (true) {
                try {
                    final com.codahale.metrics.Timer.Context context = ticktime.time();
                    Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRooms();
                    while (rooms.hasNext()) {
                        Map.Entry<Integer, Room> next = rooms.next();
                        ticketRunnerService.submit(next.getValue());
                    }
                    Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
                    while (players.hasNext()) {
                        Map.Entry<String, Player> next = players.next();
                        ticketRunnerService.submit(next.getValue());
                    }
                    for (Map.Entry<String, Npc> next : npcs.entrySet()) {
                        ticketRunnerService.submit(next.getValue());
                    }
                    for (Map.Entry<String, CreeperEntity> next : entities.entrySet()) {
                        ticketRunnerService.submit(next.getValue());
                    }
                    for (Map.Entry<String, Effect> next : effects.entrySet()) {
                        ticketRunnerService.submit(next.getValue());
                    }
                    context.stop();
                    Thread.sleep(5000);
                } catch (Exception e) {
                   log.error("Problem with ticker!", e);
                    throw new RuntimeException("Problem with the ticker!", e);
                }
            }
        }
    }

}
