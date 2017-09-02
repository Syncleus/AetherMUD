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
package com.syncleus.aethermud.player;

import com.google.common.collect.Sets;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.items.ItemMetadata;
import com.syncleus.aethermud.server.communication.Color;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.base.Joiner;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.syncleus.aethermud.storage.graphdb.PlayerData;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerManagement implements PlayerManagementMBean {

    private final GameManager gameManager;
    private final String playerId;

    public PlayerManagement(GameManager gameManager, String playerId) {
        this.gameManager = gameManager;
        this.playerId = playerId;
    }

    @Override
    public void setMarkForDelete(boolean isMark) {
        Interner<String> interner = findInterner();
        synchronized (interner.intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.setIsMarkedForDelete(isMark);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public boolean getMarkForDelete() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return false;
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.isMarkedForDelete();
    }

    @Override
    public int getGold() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return 0;
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.getGold();
    }

    @Override
    public int getGoldInBankAmount() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return 0;
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.getGoldInBank();
    }

    @Override
    public void setGoldInBankAmount(int amt) {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.setGoldInBank(amt);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public void setGold(int amt) {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.setGold(amt);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public void setHealth(int amt) {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.getStats().setCurrentHealth(amt);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public String getPassword() {
        StringBuilder shadowPwd = new StringBuilder();
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return "";
        }
        PlayerData playerData = playerMetadataOptional.get();
        String password = playerData.getPassword();
        for (int i = 0; i < password.length(); i++) {
            shadowPwd.append("*");
        }
        return shadowPwd.toString();
    }

    @Override
    public void setPassword(String password) {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.setPassword(password);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public void setMana(int amt) {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.getStats().setCurrentMana(amt);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public int getHealth() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return 0;
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.getStats().getCurrentHealth();
    }

    @Override
    public void sendAdminMessage(String message) {
        gameManager.getChannelUtils().write(playerId, Color.BOLD_ON + Color.RED + "[ADMIN] " + message + Color.RESET + "\r\n", true);
    }

    @Override
    public int getMana() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return 0;
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.getStats().getCurrentMana();
    }

    @Override
    public void setExperience(int amt) {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.getStats().setExperience(amt);
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public int getExperience() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return 0;
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.getStats().getExperience();
    }

    @Override
    public void setRoles(String roles) {
        String[] split = roles.split(",");
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.resetPlayerRoles();
            for (String roleType : split) {
                PlayerRole byType = PlayerRole.getByType(roleType);
                if (byType == null) {
                    continue;
                }
                playerData.addPlayerRole(byType);
            }
            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public String getRoles() {
        List<String> rolesList = Lists.newArrayList();
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return "";
        }
        PlayerData playerData = playerMetadataOptional.get();
        Set<PlayerRole> playerRoleSet = Sets.newHashSet(playerData.getPlayerRoleSet());
        for (PlayerRole next : playerRoleSet) {
            rolesList.add(next.getRoleType());
        }
        return Joiner.on(",").join(rolesList);
    }

    @Override
    public Map<String, String> getInventory() {
        Map<String, String> inventoryContents = Maps.newHashMap();
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return inventoryContents;
        }
        PlayerData playerData = playerMetadataOptional.get();
        List<String> inventory = playerData.getInventory();
        for (String itemId : inventory) {
            Optional<Item> itemEntityOptional = gameManager.getEntityManager().getItemEntity(itemId);
            if (!itemEntityOptional.isPresent()) {
                continue;
            }
            Item itemEntity = itemEntityOptional.get();
            String itemName = itemEntity.getItemName();
            final String msgWithoutColorCodes =
                    itemName.replaceAll("\u001B\\[[;\\d]*m", "");
            inventoryContents.put(itemEntity.getItemId(), msgWithoutColorCodes);
        }
        return inventoryContents;
    }

    @Override
    public Map<String, String> getLockerInventory() {
        Map<String, String> inventoryContents = Maps.newHashMap();
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()){
            return inventoryContents;
        }
        PlayerData playerData = playerMetadataOptional.get();
        List<String> inventory = playerData.getLockerInventory();
        for (String itemId : inventory) {
            Optional<Item> itemEntityOptional = gameManager.getEntityManager().getItemEntity(itemId);
            if (!itemEntityOptional.isPresent()) {
                continue;
            }
            Item itemEntity = itemEntityOptional.get();
            String itemName = itemEntity.getItemName();
            final String msgWithoutColorCodes =
                    itemName.replaceAll("\u001B\\[[;\\d]*m", "");
            inventoryContents.put(itemEntity.getItemId(), msgWithoutColorCodes);
        }
        return inventoryContents;
    }

    @Override
    public String createItemInInventory(String internalItemName){
        Optional<ItemMetadata> itemMetadata = gameManager.getItemStorage().get(internalItemName);
        if (!itemMetadata.isPresent()) {
            return "No such item exists with internal name: " + internalItemName;
        }
        Item item = new ItemBuilder().from(itemMetadata.get()).create();
        gameManager.getEntityManager().saveItem(item);
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()){
                return "";
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.addInventoryEntityId(item.getItemId());
            gameManager.getPlayerManager().newPlayerData();
        }
        final String msgWithoutColorCodes = item.getItemName().replaceAll("\u001B\\[[;\\d]*m", "");
        return msgWithoutColorCodes + " created.";
    }

    @Override
    public void setPlayerClass(String playerClassName) {
        List<PlayerClass> collect = Arrays.stream(PlayerClass.values()).filter(playerClass -> playerClass.getIdentifier().equalsIgnoreCase(playerClassName)).collect(Collectors.toList());
        if (collect.size() == 0) {
            return;
        }
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.setPlayerClass(collect.get(0));
            gameManager.getPlayerManager().newPlayerData();
        }

    }

    @Override
    public String getPlayerClass() {
        Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        if (!playerMetadataOptional.isPresent()) {
            return "";
        }
        PlayerData playerData = playerMetadataOptional.get();
        return playerData.getPlayerClass().getIdentifier();
    }

    @Override
    public void clearAllCoolDowns() {
        synchronized (findInterner().intern(playerId)) {
            Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerData playerData = playerMetadataOptional.get();
            playerData.resetCoolDowns();

            gameManager.getPlayerManager().newPlayerData();
        }
    }

    @Override
    public void detain() {
        synchronized (findInterner().intern(playerId)) {
            gameManager.detainPlayer(gameManager.getPlayerManager().getPlayer(playerId));
        }
    }

    private Interner<String> findInterner() {
        Player player = gameManager.getPlayerManager().getPlayer(playerId);
        Interner<String> interner;
        if (player == null) {
            interner = Interners.newWeakInterner();
        } else {
            interner = player.getInterner();
        }
        return interner;
    }
}
