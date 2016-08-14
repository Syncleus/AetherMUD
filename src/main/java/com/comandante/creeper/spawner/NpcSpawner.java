package com.comandante.creeper.spawner;


import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.Main;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcBuilder;
import com.comandante.creeper.world.Area;
import com.comandante.creeper.world.Room;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NpcSpawner extends CreeperEntity {

    private final Npc npc;
    private final GameManager gameManager;
    private final SpawnRule spawnRule;
    private int noTicks;
    private final Random random = new Random();


    public NpcSpawner(Npc npc, GameManager gameManager, SpawnRule spawnRule) {
        this.npc = npc;
        this.gameManager = gameManager;
        this.spawnRule = spawnRule;
        this.noTicks = spawnRule.getSpawnIntervalTicks();
    }

    public void incTicks() {
        noTicks++;
    }

    @Override
    public void run() {
        incTicks();
        if (noTicks >= spawnRule.getSpawnIntervalTicks()) {
            int randomPercentage = spawnRule.getRandomChance();
            int numberOfAttempts = spawnRule.getMaxInstances() - counterNumberInArea(spawnRule.getArea());
            for (int i = 0; i < numberOfAttempts; i++) {
                if (random.nextInt(100) < randomPercentage || randomPercentage == 100) {
                    createAndAddItem(spawnRule.getArea());
                }
            }
            noTicks = 0;
        }
    }

    private int counterNumberInArea(Area spawnArea) {
        int numberCurrentlyInArea = 0;
        Set<Room> roomsByArea = gameManager.getRoomManager().getRoomsByArea(spawnArea);
        for (Room room : roomsByArea) {
            if (room.getAreas().contains(spawnArea)) {
                for (String i : room.getNpcIds()) {
                    Npc currentNpc = gameManager.getEntityManager().getNpcEntity(i);
                    if (currentNpc.getName().equals(npc.getName())) {
                        numberCurrentlyInArea++;
                    }
                }
            }
        }
        return numberCurrentlyInArea;
    }

    private void createAndAddItem(Area spawnArea) {
        List<Room> rooms = gameManager.getRoomManager().getRoomsByArea(spawnArea).stream().filter(getRoomsWithRoom()).collect(Collectors.toList());
        Room room = rooms.get(random.nextInt(rooms.size()));
        NpcBuilder npcBuilder = new NpcBuilder(npc);
        Npc newNpc = npcBuilder.createNpc();
        newNpc.setCurrentRoom(room);
        gameManager.getEntityManager().addEntity(newNpc);
        room.addPresentNpc(newNpc.getEntityId());
        gameManager.writeToRoom(room.getRoomId(), newNpc.getColorName() + " appears." + "\r\n");
        Main.metrics.counter(MetricRegistry.name(NpcSpawner.class, npc.getName() + "-spawn")).inc();
    }

    private Predicate<Room> getRoomsWithRoomNew() {
        return room -> {
            int count = room.getNpcIds().stream().filter(npcId -> {
                Npc npcEntity = gameManager.getEntityManager().getNpcEntity(npcId);
                return npcEntity.getName().equals(npc.getName());
            }).collect(Collectors.toList()).size();
            return count < spawnRule.getMaxPerRoom();
        };
    }

    private Predicate<Room> getRoomsWithRoom() {
        return room -> {
            int count = 0;
            Set<String> npcIds = room.getNpcIds();
            for (String npcId : npcIds) {
                Npc npcEntity = gameManager.getEntityManager().getNpcEntity(npcId);
                if (npcEntity.getName().equals(npc.getName())) {
                    count++;
                }
            }
            if (count < spawnRule.getMaxPerRoom()) {
                return true;
            }
            return false;
        };
    }
}