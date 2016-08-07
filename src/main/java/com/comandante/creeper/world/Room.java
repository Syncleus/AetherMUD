package com.comandante.creeper.world;

import com.comandante.creeper.Items.Forage;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.spawner.ItemSpawner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Room extends CreeperEntity {

    private final Integer roomId;

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    private String roomTitle;
    private final Integer floorId;
    private java.util.Optional<Integer> northId;
    private java.util.Optional<Integer> westId;
    private java.util.Optional<Integer> eastId;
    private java.util.Optional<Integer> southId;
    private java.util.Optional<Integer> downId;
    private java.util.Optional<Integer> upId;
    private List<RemoteExit> enterExits = Lists.newArrayList();
    private String roomDescription;
    private final Set<String> presentPlayerIds = Sets.<String>newConcurrentHashSet();
    private final Set<String> npcIds = Sets.newConcurrentHashSet();
    private final Set<String> itemIds = Sets.newConcurrentHashSet();
    private List<ItemSpawner> itemSpawners = Lists.newArrayList();
    private Set<Area> areas = Sets.newConcurrentHashSet();
    private Optional<String> mapData = Optional.absent();
    private final Set<String> roomTags;
    private final Set<Merchant> merchants = Sets.newConcurrentHashSet();
    private Map<ItemType, Forage> forages = Maps.newHashMap();
    private final Map<String, String> notables;
    private final GameManager gameManager;

    public Room(Integer roomId,
                String roomTitle,
                Integer floorId,
                java.util.Optional<Integer> northId,
                java.util.Optional<Integer> southId,
                java.util.Optional<Integer> eastId,
                java.util.Optional<Integer> westId,
                java.util.Optional<Integer> upId,
                java.util.Optional<Integer> downId,
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

    public void setNorthId(java.util.Optional<Integer> northId) {
        this.northId = northId;
    }

    public void setWestId(java.util.Optional<Integer> westId) {
        this.westId = westId;
    }

    public void setEastId(java.util.Optional<Integer> eastId) {
        this.eastId = eastId;
    }

    public void setSouthId(java.util.Optional<Integer> southId) {
        this.southId = southId;
    }

    public void setDownId(java.util.Optional<Integer> downId) {
        this.downId = downId;
    }

    public void setUpId(java.util.Optional<Integer> upId) {
        this.upId = upId;
    }

    public void addTag(String tag) {
        roomTags.add(tag);
    }
    public Integer getFloorId() {
        return floorId;
    }

    public Optional<String> getMapData() {
        return mapData;
    }

    public void setMapData(Optional<String> mapData) {
        this.mapData = mapData;
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

    protected Set<String> getPresentPlayerIds() {
        // terrible null pointers will result if you call this shit directly.
        // People sign off and cause problems
        return presentPlayerIds;
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

    public java.util.Optional<Integer> getNorthId() {
        return northId;
    }

    public java.util.Optional<Integer> getWestId() {
        return westId;
    }

    public java.util.Optional<Integer> getEastId() {
        return eastId;
    }

    public java.util.Optional<Integer> getSouthId() {
        return southId;
    }

    public java.util.Optional<Integer> getUpId() {
        return upId;
    }

    public java.util.Optional<Integer> getDownId() {
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

    public Map<ItemType, Forage> getForages() {
        return forages;
    }

    public void addForage(Forage forage) {
        this.forages.put(forage.getItemType(), forage);
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

    @Override
    public void run() {
        for (String itemId : itemIds) {
            Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
            if (itemEntity == null) {
                removePresentItem(itemId);
                continue;
            }
            if (itemEntity.isHasBeenWithPlayer()) {
                continue;
            }
            Integer itemTypeId = gameManager.getEntityManager().getItemEntity(itemId).getItemTypeId();
            ItemType itemType = ItemType.itemTypeFromCode(itemTypeId);
            Set<TimeTracker.TimeOfDay> itemValidTimeOfDays = itemType.getValidTimeOfDays();
            TimeTracker.TimeOfDay timeOfDay = gameManager.getTimeTracker().getTimeOfDay();
            if (itemValidTimeOfDays.size() > 0 && !itemValidTimeOfDays.contains(timeOfDay)) {
                gameManager.getEntityManager().removeItem(itemId);
                removePresentItem(itemId);
                gameManager.writeToRoom(roomId, itemEntity.getItemName() + " turns to dust.\r\n");
            }
        }
    }
}
