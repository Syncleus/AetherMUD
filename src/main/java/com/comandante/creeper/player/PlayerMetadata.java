package com.comandante.creeper.player;


import com.comandante.creeper.stat.Stats;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PlayerMetadata implements Serializable {

    private final String playerName;
    private final String password;
    private final String playerId;
    private Stats stats;
    private String[] inventory;
    private int gold;
    private Set<PlayerRole> playerRoleSet;
    private String[] playerEquipment;

    public PlayerMetadata(String playerName, String password, String playerId, Stats stats, int gold, Set<PlayerRole> playerRoleSet, String[] playerEquipment) {
        this.playerName = playerName;
        this.password = password;
        this.playerId = playerId;
        this.stats = stats;
        this.gold = gold;
        this.playerRoleSet = playerRoleSet;
        this.playerEquipment = playerEquipment;
    }

    public PlayerMetadata(PlayerMetadata playerMetadata) {
        this.playerName = playerMetadata.playerName;
        this.password = playerMetadata.password;
        this.playerId = playerMetadata.playerId;
        this.stats = new Stats(playerMetadata.stats);
        this.inventory = Arrays.copyOf(playerMetadata.inventory, playerMetadata.inventory.length);
        this.gold = new Integer(playerMetadata.gold);
        this.playerRoleSet = Sets.newHashSet(playerMetadata.playerRoleSet);
        this.playerEquipment = Arrays.copyOf(playerMetadata.playerEquipment, playerMetadata.playerEquipment.length);
    }

    public String[] getInventory() {
        return inventory;
    }

    protected void addInventoryEntityId(String newEntityId) {
        if (inventory == null) {
            inventory = new String[0];
        }
        String[] result = Arrays.copyOf(inventory, inventory.length + 1);
        result[inventory.length] = newEntityId;
        this.inventory = result;
    }

    protected void removeInventoryEntityId(String itemId) {
        List<String> itemsIdKeep = new ArrayList<String>(Arrays.asList(inventory));
        itemsIdKeep.remove(itemId);
        String[] newItems = new String[itemsIdKeep.size()];
        int i = 0;
        for (String id : itemsIdKeep) {
            newItems[i] = id;
            i++;
        }
        this.inventory = newItems;
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

    public String getPlayerName() {
        return playerName;
    }

    public String getPassword() {
        return password;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Stats getStats() {
        return stats;
    }

    public int getGold() {
        return gold;
    }

    protected void incrementGold(int amt) {
        this.gold = gold + amt;
    }

    public Set<PlayerRole> getPlayerRoleSet() {
        return playerRoleSet;
    }

    protected void addPlayerRole(PlayerRole playerRole) {
        if (this.playerRoleSet == null) {
            playerRoleSet = Sets.newHashSet();
        }
        playerRoleSet.add(playerRole);
    }

    public String[] getPlayerEquipment() {
        return playerEquipment;
    }
}
