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
package com.syncleus.aethermud.world.model;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.items.Forage;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.spawner.ItemSpawner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Room extends AetherMudEntity {

    private final Integer roomId;

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    private String roomTitle;
    private final Integer floorId;
    private Optional<Integer> northId;
    private Optional<Integer> westId;
    private Optional<Integer> eastId;
    private Optional<Integer> southId;
    private Optional<Integer> downId;
    private Optional<Integer> upId;
    private List<RemoteExit> enterExits = Lists.newArrayList();
    private String roomDescription;
    private final Set<String> presentPlayerIds = Sets.<String>newConcurrentHashSet();
    private final Set<String> npcIds = Sets.newConcurrentHashSet();
    private final Set<String> itemIds = Sets.newConcurrentHashSet();
    private List<ItemSpawner> itemSpawners = Lists.newArrayList();
    private Set<Area> areas = Sets.newConcurrentHashSet();
    private final Set<String> roomTags;
    private final Set<Merchant> merchants = Sets.newConcurrentHashSet();
    private Map<String, Forage> forages = Maps.newHashMap();
    private final Map<String, String> notables;
    private final GameManager gameManager;

    public Room(Integer roomId,
                String roomTitle,
                Integer floorId,
                Optional<Integer> northId,
                Optional<Integer> southId,
                Optional<Integer> eastId,
                Optional<Integer> westId,
                Optional<Integer> upId,
                Optional<Integer> downId,
                List<RemoteExit> enterExits,
                String roomDescription, Set<String> roomTags,
                Set<Area> areas,
                Map<String, String> notables,
                GameManager gameManager) {
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.floorId = floorId;
        this.northId = northId;
        this.westId = westId;
        this.eastId = eastId;
        this.southId = southId;
        this.upId = upId;
        this.downId = downId;
        this.roomDescription = roomDescription;
        this.roomTags = roomTags;
        this.areas = areas;
        this.enterExits = enterExits;
        this.notables = notables;
        this.gameManager = gameManager;
    }

    public List<ItemSpawner> getItemSpawners() {
        return itemSpawners;
    }

    public void setItemSpawners(List<ItemSpawner> itemSpawners) {
        this.itemSpawners = itemSpawners;
    }

    public Set<Merchant> getMerchants() {
        return merchants;
    }

    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
    }

    public Set<String> getRoomTags() {
        return roomTags;
    }

    public void setNorthId(Optional<Integer> northId) {
        this.northId = northId;
    }

    public void setWestId(Optional<Integer> westId) {
        this.westId = westId;
    }

    public void setEastId(Optional<Integer> eastId) {
        this.eastId = eastId;
    }

    public void setSouthId(Optional<Integer> southId) {
        this.southId = southId;
    }

    public void setDownId(Optional<Integer> downId) {
        this.downId = downId;
    }

    public void setUpId(Optional<Integer> upId) {
        this.upId = upId;
    }

    public void addTag(String tag) {
        roomTags.add(tag);
    }
    public Integer getFloorId() {
        return floorId;
    }

    public Optional<String> getMapData() {
        return gameManager.getMapsManager().generateMap(this);
    }

    public Set<Area> getAreas() {
        return areas;
    }

    public void setAreas(Set<Area> areas) {
        this.areas = areas;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void addPresentItem(String itemId) {
        itemIds.add(itemId);
    }

    public Set<String> getItemIds() {
        return itemIds;
    }

    public void removePresentItem(String itemId) {
        itemIds.remove(itemId);

    }

    public void addPresentNpc(String npcId) {
        npcIds.add(npcId);
    }

    public void removePresentNpc(String npcId) {
        npcIds.remove(npcId);
    }

    public Set<String> getNpcIds() {
        return npcIds;
    }

    public Set<String> getPresentPlayerIds() {
        return presentPlayerIds.stream().filter(playerId -> gameManager.getPlayerManager().getPlayer(playerId) != null).collect(Collectors.toSet());
    }

    public Set<Player> getPresentPlayers() {
        Set<Player> players = Sets.newHashSet();
        for (String playerId : presentPlayerIds) {
            Player player = gameManager.getPlayerManager().getPlayer(playerId);
            if (player != null) {
                players.add(player);
            }
        }
        return ImmutableSet.copyOf(players);
    }

    public void addPresentPlayer(String playerId) {
        presentPlayerIds.add(playerId);

    }

    public void removePresentPlayer(String playerId) {
        presentPlayerIds.remove(playerId);
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Optional<Integer> getNorthId() {
        return northId;
    }

    public Optional<Integer> getWestId() {
        return westId;
    }

    public Optional<Integer> getEastId() {
        return eastId;
    }

    public Optional<Integer> getSouthId() {
        return southId;
    }

    public Optional<Integer> getUpId() {
        return upId;
    }

    public Optional<Integer> getDownId() {
        return downId;
    }

    public List<RemoteExit> getEnterExits() {
        return enterExits;
    }

    public void addEnterExit(RemoteExit remoteExit) {
        enterExits.add(remoteExit);
    }

    public void setEnterExits(List<RemoteExit> enterExits) {
        this.enterExits = enterExits;
    }

    public void addItemSpawner(ItemSpawner itemSpawner) {
        itemSpawners.add(itemSpawner);
    }

    public Map<String, Forage> getForages() {
        return forages;
    }

    public void addForage(Forage forage) {
        this.forages.put(forage.getInternalItemName(), forage);
    }

    public Map<String, String> getNotables() {
        return notables;
    }

    public void addNotable(String notableName, String description) {
        notables.put(notableName, description);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public List<NpcSpawn> getPresentNpcs() {
        return npcIds.stream().map(s -> gameManager.getEntityManager().getNpcEntity(s)).collect(Collectors.toList());
    }

    @Override
    public void run() {
        for (String itemId : itemIds) {
            Optional<ItemInstance> itemOptional = gameManager.getEntityManager().getItemEntity(itemId);
            if (!itemOptional.isPresent()) {
                removePresentItem(itemId);
                continue;
            }
            ItemInstance itemEntity = itemOptional.get();
            if (itemEntity.isHasBeenWithPlayer()) {
                continue;
            }
            List<TimeTracker.TimeOfDay> itemValidTimeOfDays = itemEntity.getValidTimeOfDays();
            TimeTracker.TimeOfDay timeOfDay = gameManager.getTimeTracker().getTimeOfDay();
            if (itemValidTimeOfDays.size() > 0 && !itemValidTimeOfDays.contains(timeOfDay)) {
                gameManager.getEntityManager().removeItem(itemId);
                removePresentItem(itemId);
                gameManager.writeToRoom(roomId, itemEntity.getItemName() + " turns to dust.\r\n");
            }
        }
    }
}
