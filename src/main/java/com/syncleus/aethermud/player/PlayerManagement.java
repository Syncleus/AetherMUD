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

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.ItemPojo;
import com.syncleus.aethermud.items.ItemBuilder;
import com.syncleus.aethermud.items.ItemMetadata;
import com.syncleus.aethermud.server.communication.Color;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.base.Joiner;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;

import java.util.*;
import java.util.function.Consumer;
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
            this.consume(playerData -> playerData.setIsMarkedForDelete(isMark));
        }
    }

    @Override
    public boolean getMarkForDelete() {
        return this.transactRead(playerData -> playerData.isMarkedForDelete());
    }

    @Override
    public int getGold() {
        return this.transactRead(playerData -> playerData.getGold());
    }

    @Override
    public int getGoldInBankAmount() {
        return this.transactRead(playerData -> playerData.getGoldInBank());
    }

    @Override
    public void setGoldInBankAmount(int amt) {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.setGoldInBank(amt));
        }
    }

    @Override
    public void setGold(int amt) {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.setGold(amt));
        }
    }

    @Override
    public void setHealth(int amt) {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.getStats().setCurrentHealth(amt));
        }
    }

    @Override
    public String getPassword() {
        return this.transactRead(playerData -> {
            StringBuilder shadowPwd = new StringBuilder();
            String password = playerData.getPassword();
            for (int i = 0; i < password.length(); i++) {
                shadowPwd.append("*");
            }
            return shadowPwd.toString();
        });
    }

    @Override
    public void setPassword(String password) {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.setPassword(password));
        }
    }

    @Override
    public void setMana(int amt) {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.getStats().setCurrentMana(amt));
        }
    }

    @Override
    public int getHealth() {
        return this.transactRead(playerData -> playerData.getStats().getCurrentHealth());
    }

    @Override
    public void sendAdminMessage(String message) {
        gameManager.getChannelUtils().write(playerId, Color.BOLD_ON + Color.RED + "[ADMIN] " + message + Color.RESET + "\r\n", true);
    }

    @Override
    public int getMana() {
        return this.transactRead(playerData -> playerData.getStats().getCurrentMana());
    }

    @Override
    public void setExperience(int amt) {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.getStats().setExperience(amt));
        }
    }

    @Override
    public int getExperience() {
        return this.transactRead(playerData -> playerData.getStats().getExperience());
    }

    @Override
    public void setRoles(String roles) {
        String[] split = roles.split(",");
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> {
                playerData.resetPlayerRoles();
                for (String roleType : split) {
                    PlayerRole byType = PlayerRole.getByType(roleType);
                    if (byType == null) {
                        continue;
                    }
                    playerData.addPlayerRole(byType);
                }
            });
        }
    }

    @Override
    public String getRoles() {
        return this.transactRead(playerData -> {
            List<String> rolesList = Lists.newArrayList();
            Set<PlayerRole> playerRoleSet = Sets.newHashSet(playerData.getPlayerRoles());
            for (PlayerRole next : playerRoleSet) {
                rolesList.add(next.getRoleType());
            }
            return Joiner.on(",").join(rolesList);
        });
    }

    @Override
    public Map<String, String> getInventory() {
        return this.transactRead(playerData -> {
            Map<String, String> inventoryContents = Maps.newHashMap();
            List<String> inventory = playerData.getInventory();
            for (String itemId : inventory) {
                Optional<ItemPojo> itemEntityOptional = gameManager.getEntityManager().getItemEntity(itemId);
                if (!itemEntityOptional.isPresent()) {
                    continue;
                }
                ItemPojo itemEntity = itemEntityOptional.get();
                String itemName = itemEntity.getItemName();
                final String msgWithoutColorCodes =
                    itemName.replaceAll("\u001B\\[[;\\d]*m", "");
                inventoryContents.put(itemEntity.getItemId(), msgWithoutColorCodes);
            }
            return inventoryContents;
        });
    }

    @Override
    public Map<String, String> getLockerInventory() {
        return this.transactRead(playerData -> {
            Map<String, String> inventoryContents = Maps.newHashMap();
            List<String> inventory = playerData.getLockerInventory();
            for (String itemId : inventory) {
                Optional<ItemPojo> itemEntityOptional = gameManager.getEntityManager().getItemEntity(itemId);
                if (!itemEntityOptional.isPresent()) {
                    continue;
                }
                ItemPojo itemEntity = itemEntityOptional.get();
                String itemName = itemEntity.getItemName();
                final String msgWithoutColorCodes =
                    itemName.replaceAll("\u001B\\[[;\\d]*m", "");
                inventoryContents.put(itemEntity.getItemId(), msgWithoutColorCodes);
            }
            return inventoryContents;
        });
    }

    @Override
    public String createItemInInventory(String internalItemName){
        Optional<ItemMetadata> itemMetadata = gameManager.getItemStorage().get(internalItemName);
        if (!itemMetadata.isPresent()) {
            return "No such item exists with internal name: " + internalItemName;
        }
        ItemPojo item = new ItemBuilder().from(itemMetadata.get()).create();
        gameManager.getEntityManager().saveItem(item);
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.addInventoryEntityId(item.getItemId()));
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
            this.consume(playerData -> playerData.setPlayerClass(collect.get(0)));
        }

    }

    @Override
    public String getPlayerClass() {
        return this.transact(playerData -> playerData.getPlayerClass().getIdentifier());
    }

    @Override
    public void clearAllCoolDowns() {
        synchronized (findInterner().intern(playerId)) {
            this.consume(playerData -> playerData.resetCoolDowns());
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

    private <T> T transact(Function<PlayerData, T> func) {
        return PlayerUtil.transact(this.gameManager, this.playerId, func);
    }

    private void consume(Consumer<PlayerData> func) {
        PlayerUtil.consume(this.gameManager, this.playerId, func);
    }

    private <T> T transactRead(Function<PlayerData, T> func) {
        return PlayerUtil.transactRead(this.gameManager, this.playerId, func);
    }

    private void consumeRead(Consumer<PlayerData> func) {
        PlayerUtil.consumeRead(this.gameManager, this.playerId, func);
    }
}
