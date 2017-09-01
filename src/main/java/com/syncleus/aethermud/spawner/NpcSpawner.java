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
package com.syncleus.aethermud.spawner;


import com.codahale.metrics.MetricRegistry;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.aethermud.world.model.Room;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NpcSpawner extends AetherMudEntity {

    private final NpcSpawn npcSpawn;
    private final GameManager gameManager;
    private final SpawnRule spawnRule;
    private int noTicks;
    private final Random random = new Random();


    public NpcSpawner(NpcSpawn npcSpawn, GameManager gameManager, SpawnRule spawnRule) {
        this.npcSpawn = npcSpawn;
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
                    NpcSpawn currentNpcSpawn = gameManager.getEntityManager().getNpcEntity(i);
                    if (currentNpcSpawn.getName().equals(npcSpawn.getName())) {
                        numberCurrentlyInArea++;
                    }
                }
            }
        }
        return numberCurrentlyInArea;
    }

    private void createAndAddItem(Area spawnArea) {
        List<Room> rooms = gameManager.getRoomManager().getRoomsByArea(spawnArea).stream()
                .filter(findRoomsWithOccupancy(npcSpawn))
                .collect(Collectors.toList());
        Room room = rooms.get(random.nextInt(rooms.size()));
        NpcBuilder npcBuilder = new NpcBuilder(npcSpawn);
        NpcSpawn newNpcSpawn = npcBuilder.createNpc();
        newNpcSpawn.setCurrentRoom(room);
        gameManager.getEntityManager().addEntity(newNpcSpawn);
        room.addPresentNpc(newNpcSpawn.getEntityId());
        gameManager.writeToRoom(room.getRoomId(), newNpcSpawn.getColorName() + " appears." + "\r\n");
        room.getPresentPlayers().forEach(Player::processNpcAggro);
        Main.metrics.counter(MetricRegistry.name(NpcSpawner.class, npcSpawn.getName() + "-spawn")).inc();
    }

    private Predicate<Room> findRoomsWithOccupancy(NpcSpawn npcSpawn) {
        return room -> {
            long count = room.getNpcIds().stream()
                    .map(npcId -> gameManager.getEntityManager().getNpcEntity(npcId))
                    .filter(n -> n.getName().equals(npcSpawn.getName()))
                    .count();

            return count < spawnRule.getMaxPerRoom();
        };
    }
}
