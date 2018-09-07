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
package com.syncleus.aethermud.npc;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.CoolDown;
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class NpcMover {

    private final GameManager gameManager;
    private final Random random = new Random();
    private static final Logger log = Logger.getLogger(NpcMover.class);

    public NpcMover(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void roam(String npcId) {
        final NpcSpawn npcSpawnEntity = gameManager.getEntityManager().getNpcEntity(npcId);
        if (npcSpawnEntity == null) {
            return;
        }
        if (!gameManager.getRoomManager().getNpcCurrentRoom(npcSpawnEntity).isPresent()) {
            return;
        }
        Room npcCurrentRoom = gameManager.getRoomManager().getNpcCurrentRoom(npcSpawnEntity).get();
        Set<Integer> possibleExits = getPossibleExits(npcCurrentRoom);
        Predicate<Integer> roamableByArea = roomId -> {
            Room room = gameManager.getRoomManager().getRoom(roomId);
            for (Area roomArea : room.getAreas()) {
                if (npcSpawnEntity.getRoamAreas().contains(roomArea)) {
                    if (doesRoomHaveEmptyNpcsSpots(room, npcSpawnEntity, roomArea)){
                        return true;
                    }
                }
            }
            return false;
        };
        List<Integer> canRoam = Lists.newArrayList(possibleExits.stream().filter(roamableByArea::apply).collect(Collectors.toList()));
        if (canRoam.size() <= 0) {
            return;
        }
        Integer destinationRoomId = canRoam.get(random.nextInt(canRoam.size()));
        String exitMessage = getExitMessage(npcCurrentRoom, destinationRoomId);
        npcCurrentRoom.getNpcIds().remove(npcId);
        gameManager.roomSay(npcCurrentRoom.getRoomId(), npcSpawnEntity.getColorName() + " " + exitMessage, "");
        Room destinationRoom = gameManager.getRoomManager().getRoom(destinationRoomId);
        npcSpawnEntity.setCurrentRoom(destinationRoom);
        destinationRoom.getNpcIds().add(npcId);
        npcSpawnEntity.addCoolDown(new CoolDown(CoolDownType.NPC_ROAM));
        gameManager.roomSay(destinationRoomId, npcSpawnEntity.getColorName() + " has arrived.", "");
        destinationRoom.getPresentPlayers().forEach(Player::processNpcAggro);
    }

    private boolean doesRoomHaveEmptyNpcsSpots(Room room, NpcSpawn npcSpawn, Area area) {
        Set<Area> roamAreas = npcSpawn.getRoamAreas();
        for (Area ar : roamAreas) {
            if (ar.equals(area)) {
                Optional<SpawnRule> spawnRuleByArea = npcSpawn.getSpawnRuleByArea(area);
                if (spawnRuleByArea.isPresent()) {
                    int maxPerRoom = spawnRuleByArea.get().getMaxPerRoom();
                    int numberOfNpcInRoom = numberOfNpcInRoom(npcSpawn, room);
                    if (numberOfNpcInRoom < maxPerRoom) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int numberOfNpcInRoom(NpcSpawn npcSpawn, Room room) {
        int count = 0;
        for (String npcId : room.getNpcIds()) {
            NpcSpawn npcSpawnEntity = gameManager.getEntityManager().getNpcEntity(npcId);
            if (npcSpawnEntity == null) {
                continue;
            }
            if (npcSpawn.getName().equals(npcSpawnEntity.getName())) {
                count++;
            }
        }
        return count;
    }

    public String getExitMessage(Room room, Integer exitRoomId) {
        if (room.getUpId().isPresent() && room.getUpId().get().equals(exitRoomId)) {
            return "exited up.";
        }
        if (room.getDownId().isPresent() && room.getDownId().get().equals(exitRoomId)) {
            return "exited down.";
        }
        if (room.getNorthId().isPresent() && room.getNorthId().get().equals(exitRoomId)) {
            return "exited to the north.";
        }
        if (room.getSouthId().isPresent() && room.getSouthId().get().equals(exitRoomId)) {
            return "exited to the south.";
        }
        if (room.getEastId().isPresent() && room.getEastId().get().equals(exitRoomId)) {
            return "exited to the east.";
        }
        if (room.getWestId().isPresent() && room.getWestId().get().equals(exitRoomId)) {
            return "exited to the west..";
        }
        return "";
    }

    public Set<Integer> getPossibleExits(Room room) {
        List<Optional<Integer>> opts = Lists.newArrayList();
        opts.add(room.getDownId());
        opts.add(room.getUpId());
        opts.add(room.getNorthId());
        opts.add(room.getSouthId());
        opts.add(room.getEastId());
        opts.add(room.getWestId());

        Set<Integer> exits = Sets.newHashSet();
        for (Optional<Integer> opt : opts) {
            if (opt.isPresent()) {
                exits.add(opt.get());
            }
        }
        return exits;
    }

}
