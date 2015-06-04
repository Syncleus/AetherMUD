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
    private String password;
    private final String playerId;
    private Stats stats;
    private String[] inventory;
    private int gold;
    private int goldInBank;
    private Set<PlayerRole> playerRoleSet;
    private String[] playerEquipment;
    protected String[] effects;

    public PlayerMetadata(String playerName, String password, String playerId, Stats stats, int gold, Set<PlayerRole> playerRoleSet, String[] playerEquipment, int goldInBank, String[] effects) {
        this.playerName = playerName;
        this.password = password;
        this.playerId = playerId;
        this.stats = stats;
        this.gold = gold;
        this.playerRoleSet = playerRoleSet;
        this.playerEquipment = playerEquipment;
        this.goldInBank = goldInBank;
        this.effects = effects;
    }

    public PlayerMetadata(PlayerMetadata playerMetadata) {
        this.playerName = playerMetadata.playerName;
        this.password = playerMetadata.password;
        this.playerId = playerMetadata.playerId;
        this.stats = new Stats(playerMetadata.stats);
        if (playerMetadata.inventory != null) {
            this.inventory = Arrays.copyOf(playerMetadata.inventory, playerMetadata.inventory.length);
        }
        this.gold = new Integer(playerMetadata.gold);
        this.goldInBank = new Integer(playerMetadata.goldInBank);
        if (playerMetadata.playerRoleSet != null) {
            this.playerRoleSet = Sets.newHashSet(playerMetadata.playerRoleSet);
        }
        if (playerMetadata.playerEquipment != null) {
            this.playerEquipment = Arrays.copyOf(playerMetadata.playerEquipment, playerMetadata.playerEquipment.length);
        }
        if (playerMetadata.effects != null) {
            this.effects = Arrays.copyOf(playerMetadata.effects, playerMetadata.effects.length);
        }
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

    protected void addEffectId(String effectId) {
        if (effects == null) {
            effects = new String[0];
        }
        String[] result = Arrays.copyOf(effects, effects.length + 1);
        result[effects.length] = effectId;
        this.effects = result;
    }

    protected void removeEffectId(String effectId) {
        List<String> effectsKeep = new ArrayList<String>(Arrays.asList(effects));
        effectsKeep.remove(effectId);
        String[] newEffects = new String[effectId.length()];
        int i = 0;
        for (String id : effectsKeep) {
            newEffects[i] = id;
            i++;
        }
        this.effects = newEffects;
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

    public int getGoldInBank() {
        return goldInBank;
    }

    protected void incrementGold(int amt) {
        this.gold = gold + amt;
    }

    protected void transferGoldToBank(int amt) {
        this.gold = gold - amt;
        this.goldInBank = goldInBank + amt;
    }

    protected void transferBankGoldToPlayer(int amt) {
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

    public String[] getPlayerEquipment() {
        return playerEquipment;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getEffects() {
        return effects;
    }
}
