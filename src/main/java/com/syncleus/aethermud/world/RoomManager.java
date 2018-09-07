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
package com.syncleus.aethermud.world;

import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {

    private final PlayerManager playerManager;
    private ConcurrentHashMap<Integer, Room> rooms = new ConcurrentHashMap<>();
    public RoomManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void addRoom(Room room) {
        rooms.put(room.getRoomId(), room);
    }

    public Optional<List<Integer>> getRoomsForTag(String tag) {
        List<Integer> matchedRooms = Lists.newArrayList();
        Iterator<Map.Entry<Integer, Room>> rooms = getRoomsIterator();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            if (next.getValue().getRoomTags().contains(tag)) {
                matchedRooms.add(next.getValue().getRoomId());
            }
        }

        if (matchedRooms.size() > 0) {
            return Optional.of(matchedRooms);
        } else return Optional.empty();
    }

    public Iterator<Map.Entry<Integer, Room>> getRoomsIterator() {
        return rooms.entrySet().iterator();
    }

    public void addMerchant(Merchant merchant) {
        for (Integer roomId: merchant.getRoomIds()) {
            getRoom(roomId).addMerchant(merchant);
        };
    }

    public Room getRoom(Integer roomId) {
        return rooms.get(roomId);
    }

    public void tagRoom(Integer roomId, String tag) {
        getRoom(roomId).addTag(tag);
    }

    public Set<String> getTagsForRoom(Integer roomId) {
        return getRoom(roomId).getRoomTags();
    }

    public Map<Integer, Room> getrooms() {
        return rooms;
    }

    public Set<Room> getRoomsByFloorId(Integer floorId) {
        Set<Room> rooms = Sets.newHashSet();
        Iterator<Map.Entry<Integer, Room>> rooms1 = getRoomsIterator();
        while (rooms1.hasNext()) {
            Map.Entry<Integer, Room> next = rooms1.next();
            if (next.getValue().getFloorId().equals(floorId)) {
                rooms.add(next.getValue());
            }
        }
        return rooms;
    }

    public Optional<Room> getPlayerCurrentRoom(Player player) {
        if (player.getCurrentRoom() != null) {
            return Optional.of(player.getCurrentRoom());
        }
        return getPlayerCurrentRoom(player.getPlayerId());
    }

    public Optional<Room> getPlayerCurrentRoom(String playerId) {
        Iterator<Map.Entry<Integer, Room>> rooms = getRoomsIterator();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            Room room = next.getValue();
            for (String searchPlayerId : room.getPresentPlayerIds()) {
                if (searchPlayerId.equals(playerId)) {
                    return Optional.of(room);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Room> getNpcCurrentRoom(NpcSpawn npcSpawn) {
        return Optional.of(npcSpawn.getCurrentRoom());
    }


    public Set<Room> getRoomsByArea(Area area) {
        Set<Room> rooms = Sets.newHashSet();
        Iterator<Map.Entry<Integer, Room>> rooms1 = getRoomsIterator();
        while (rooms1.hasNext()) {
            Map.Entry<Integer, Room> next = rooms1.next();
            if (next.getValue().getAreas().contains(area)) {
                rooms.add(next.getValue());
            }
        }
        return rooms;
    }

    public boolean doesRoomIdExist(Integer roomId) {
        Iterator<Map.Entry<Integer, Room>> rooms1 = getRoomsIterator();
        Set<Integer> roomIds = Sets.newHashSet();
        while (rooms1.hasNext()) {
            Map.Entry<Integer, Room> next = rooms1.next();
            if (next.getKey().equals(roomId)) {
                return true;
            }
        }
        return false;
    }

    public Room getRoomByItemId(String itemId) {
        Iterator<Map.Entry<Integer, Room>> rooms = getRoomsIterator();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            if (next.getValue().getItemIds().contains(itemId)) {
                return next.getValue();
            }
        }
        return null;
    }
}

