package com.comandante.creeper.spawner;

import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemType;
import com.comandante.creeper.Main;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.world.model.Area;
import com.comandante.creeper.world.model.Room;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class ItemSpawner extends CreeperEntity {

    private final ItemType spawnItemType;
    private final SpawnRule spawnRule;
    private final GameManager gameManager;
    private Integer roomId;
    private int noTicks = 0;
    private final Random random = new Random();
    private final Area spawnArea;


    public ItemSpawner(ItemType spawnItemType, SpawnRule spawnRule, GameManager gameManager) {
        this.spawnItemType = spawnItemType;
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
        incTicks();
        if (noTicks >= spawnRule.getSpawnIntervalTicks()) {
            int randomPercentage = spawnRule.getRandomChance();
            int numberOfAttempts = spawnRule.getMaxInstances() - counterNumberInArea();
            for (int i = 0; i < numberOfAttempts; i++) {
                if (random.nextInt(100) < randomPercentage || randomPercentage == 100) {
                    if (spawnItemType.getValidTimeOfDays().size() > 0) {
                        if (spawnItemType.getValidTimeOfDays().contains(gameManager.getTimeTracker().getTimeOfDay())) {
                            createAndAddItem();
                        }
                    } else {
                        createAndAddItem();
                    }
                }
                noTicks = 0;
            }
        }
    }

    private void createAndAddItem() {
        ArrayList<Room> rooms = Lists.newArrayList(Iterators.filter(gameManager.getRoomManager().getRoomsByArea(spawnArea).iterator(), getRoomsWithRoom()));
        Room room = rooms.get(random.nextInt(rooms.size()));
        Item item = spawnItemType.create();
        gameManager.getEntityManager().saveItem(item);
        gameManager.placeItemInRoom(room.getRoomId(), item.getItemId());
        Main.metrics.counter(MetricRegistry.name(ItemSpawner.class, item.getItemName() + "-spawn")).inc();
    }

    private int counterNumberInArea() {
        int numberCurrentlyInArea = 0;
        Set<Room> roomsByArea = gameManager.getRoomManager().getRoomsByArea(spawnArea);
        for (Room room : roomsByArea) {
            if (room.getAreas().contains(spawnArea)) {
                for (String i : room.getItemIds()) {
                    Optional<Item> currentItemOptional = gameManager.getEntityManager().getItemEntity(i);
                    if (!currentItemOptional.isPresent()) {
                        continue;
                    }
                    Item currentItem = currentItemOptional.get();
                    if (currentItem.getItemTypeId().equals(spawnItemType.getItemTypeCode())) {
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
                    Optional<Item> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
                    if (!itemOptional.isPresent()) {
                        continue;
                    }
                    Item item = itemOptional.get();
                    if (item.getItemTypeId().equals(spawnItemType.getItemTypeCode())) {
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
