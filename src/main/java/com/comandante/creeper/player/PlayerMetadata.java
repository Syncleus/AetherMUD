package com.comandante.creeper.player;


import com.comandante.creeper.stat.Stats;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.*;

public class PlayerMetadata implements Serializable {

    private final String playerName;
    private String password;
    private final String playerId;
    private Stats stats;
    private List<String> inventory;
    private List<String> lockerInventory;
    private long gold;
    private long goldInBank;
    private Set<PlayerRole> playerRoleSet;
    private String[] playerEquipment;
    private List<String> effects;
    private boolean isMarkedForDelete;
    private Map<String, String> playerSettings;

    public PlayerMetadata(String playerName, String password, String playerId, Stats stats, int gold, Set<PlayerRole> playerRoleSet, String[] playerEquipment, int goldInBank) {
        this.playerName = playerName;
        this.password = password;
        this.playerId = playerId;
        this.stats = stats;
        this.gold = gold;
        this.playerRoleSet = playerRoleSet;
        this.playerEquipment = playerEquipment;
        this.goldInBank = goldInBank;
    }

    public PlayerMetadata(PlayerMetadata playerMetadata) {
        this.playerName = playerMetadata.playerName;
        this.password = playerMetadata.password;
        this.playerId = playerMetadata.playerId;
        this.stats = new Stats(playerMetadata.stats);
        if (playerMetadata.inventory != null) {
            this.inventory = Lists.newArrayList(playerMetadata.getInventory());
        }
        if (playerMetadata.lockerInventory != null) {
            this.lockerInventory = Lists.newArrayList(playerMetadata.getLockerInventory());
        }
        this.gold = new Long(playerMetadata.gold);
        this.goldInBank = new Long(playerMetadata.goldInBank);
        if (playerMetadata.playerRoleSet != null) {
            this.playerRoleSet = Sets.newHashSet(playerMetadata.playerRoleSet);
        }
        if (playerMetadata.playerEquipment != null) {
            this.playerEquipment = Arrays.copyOf(playerMetadata.playerEquipment, playerMetadata.playerEquipment.length);
        }
        if (playerMetadata.effects != null) {
            this.effects = Lists.newArrayList(playerMetadata.getEffects());
        }
        if (playerMetadata.playerSettings != null) {
            this.playerSettings = new HashMap<String, String>(playerMetadata.getPlayerSettings());
        }
        this.isMarkedForDelete = new Boolean(playerMetadata.isMarkedForDelete);
    }

    public List<String> getInventory() {
        if (inventory == null) {
            inventory = Lists.newArrayList();
        }
        return inventory;
    }

    protected void addInventoryEntityId(String newEntityId) {
        if (inventory == null) {
            inventory = Lists.newArrayList();
        }
        inventory.add(newEntityId);
    }

    public List<String> getLockerInventory() {
        if (lockerInventory == null) {
            lockerInventory = Lists.newArrayList();
        }
        return lockerInventory;
    }

    protected void addLockerEntityId(String newEntityId) {
        if (lockerInventory == null) {
            lockerInventory = Lists.newArrayList();
        }
        lockerInventory.add(newEntityId);
    }


    protected void removeLockerEntityId(String newEntityId) {
        lockerInventory.remove(newEntityId);
    }

    protected void removeInventoryEntityId(String itemId) {
        inventory.remove(itemId);
    }

    protected void addEquipmentEntityId(String equipmentItemId) {
        if (playerEquipment == null) {
            playerEquipment = new String[0];
        }
        String[] result = Arrays.copyOf(playerEquipment, playerEquipment.length + 1);
        result[playerEquipment.length] = equipmentItemId;
        this.playerEquipment = result;
    }

    protected void removeEquipmentEntityId(String equipmentItemId) {
        List<String> equipMendItemsKeep = new ArrayList<String>(Arrays.asList(playerEquipment));
        equipMendItemsKeep.remove(equipmentItemId);
        String[] newItems = new String[equipMendItemsKeep.size()];
        int i = 0;
        for (String id : equipMendItemsKeep) {
            newItems[i] = id;
            i++;
        }
        this.playerEquipment = newItems;
    }

    protected void addEffectId(String effectId) {
        if (effects == null) {
            effects = Lists.newArrayList();
        }
        effects.add(effectId);
    }

    protected void removeEffectID(String effectId) {
        effects.remove(effectId);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Stats getStats() {
        return stats;
    }

    public long getGold() {
        return gold;
    }

    public long getGoldInBank() {
        return goldInBank;
    }

    protected void setGold(long amt) {
        this.gold = amt;
    }

    protected void setGoldInBank(long amt) {
        this.goldInBank = amt;
    }

    protected void incrementGold(int amt) {
        this.gold = gold + amt;
    }

    protected void transferGoldToBank(long amt) {
        this.gold = gold - amt;
        this.goldInBank = goldInBank + amt;
    }

    protected void transferBankGoldToPlayer(long amt) {
        this.goldInBank = goldInBank - amt;
        this.gold = gold + amt;
    }

    public Set<PlayerRole> getPlayerRoleSet() {
        return playerRoleSet;
    }

    public void addPlayerRole(PlayerRole playerRole) {
        if (this.playerRoleSet == null) {
            playerRoleSet = Sets.newHashSet();
        }
        playerRoleSet.add(playerRole);
    }

    public void resetPlayerRoles() {
        this.playerRoleSet = Sets.newHashSet();
    }

    public String[] getPlayerEquipment() {
        return playerEquipment;
    }

    public List<String> getEffects() {
        if (effects==null) {
            effects = Lists.newArrayList();
        }
        return effects;
    }

    public boolean isMarkedForDelete() {
        return isMarkedForDelete;
    }

    public void setIsMarkedForDelete(boolean isMarkedForDelete) {
        this.isMarkedForDelete = isMarkedForDelete;
    }

    protected void resetEffects(){
        this.effects = Lists.newArrayList();
    }

    public boolean setSetting(String key, String value) {
        if (playerSettings.size() >= 100) {
            return false;
        }
        if (playerSettings == null) {
            this.playerSettings = Maps.newHashMap();
        }
        PlayerSettings byKey = PlayerSettings.getByKey(key);
        if (byKey == null) {
            return false;
        }
        if (byKey.getType().equals(Integer.TYPE)) {
            try {
                int i = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        playerSettings.put(key, value);
        return true;
    }

    public String getSetting(String key) {
        if (playerSettings == null) {
            playerSettings = Maps.newHashMap();
        }
        return playerSettings.get(key);
    }

    public Map<String, String> getPlayerSettings() {
        return playerSettings;
    }

    public void deleteSetting(String key) {
        playerSettings.remove(key);
    }
}
