package com.comandante.creeper.player;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.server.player_communication.Color;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.base.Joiner;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setIsMarkedForDelete(isMark);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public boolean getMarkForDelete() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).isMarkedForDelete();
    }

    @Override
    public long getGold() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getGold();
    }

    @Override
    public long getGoldInBankAmount() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getGoldInBank();
    }

    @Override
    public void setGoldInBankAmount(long amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setGoldInBank(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public void setGold(long amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setGold(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public void setHealth(long amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.getStats().setCurrentHealth(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public String getPassword() {
        StringBuilder shadowPwd = new StringBuilder();
        String password = gameManager.getPlayerManager().getPlayerMetadata(playerId).getPassword();
        for (int i = 0; i < password.length(); i++) {
            shadowPwd.append("*");
        }
        return shadowPwd.toString();
    }

    @Override
    public void setPassword(String password) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setPassword(password);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public void setMana(long amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.getStats().setCurrentMana(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public long getHealth() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentHealth();
    }

    @Override
    public void sendMessageFromGod(String message) {
        gameManager.getChannelUtils().write(playerId, Color.BOLD_ON + Color.RED + "[ADMIN SPEAKS DIRECTLY TO YOU] " + message + Color.RESET + "\r\n", true);
    }

    @Override
    public long getMana() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentMana();
    }

    @Override
    public void setExperience(long amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.getStats().setExperience(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public long getExperience() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getExperience();
    }

    @Override
    public void setRoles(String roles) {
        String[] split = roles.split(",");
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.resetPlayerRoles();
            for (String roleType : split) {
                PlayerRole byType = PlayerRole.getByType(roleType);
                if (byType == null) {
                    continue;
                }
                playerMetadata.addPlayerRole(byType);
            }
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public String getRoles() {
        List<String> rolesList = Lists.newArrayList();
        Set<PlayerRole> playerRoleSet = gameManager.getPlayerManager().getPlayerMetadata(playerId).getPlayerRoleSet();
        for (PlayerRole next : playerRoleSet) {
            rolesList.add(next.getRoleType());
        }
        return Joiner.on(",").join(rolesList);
    }

    @Override
    public Map<String, String> getInventory() {
        Map<String, String> inventoryContents = Maps.newHashMap();
        List<String> inventory = gameManager.getPlayerManager().getPlayerMetadata(playerId).getInventory();
        for (String itemId : inventory) {
            Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
            if (itemEntity == null) {
                continue;
            }
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
        List<String> inventory = gameManager.getPlayerManager().getPlayerMetadata(playerId).getLockerInventory();
        for (String itemId : inventory) {
            Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
            if (itemEntity == null) {
                continue;
            }
            String itemName = itemEntity.getItemName();
            final String msgWithoutColorCodes =
                    itemName.replaceAll("\u001B\\[[;\\d]*m", "");
            inventoryContents.put(itemEntity.getItemId(), msgWithoutColorCodes);
        }
        return inventoryContents;
    }

    @Override
    public String createItemInInventory(int itemTypeId){
        ItemType itemType = ItemType.itemTypeFromCode(itemTypeId);
        if (itemType.equals(ItemType.UNKNOWN)) {
            return "No such item exists with id: " + itemTypeId;
        }
        Item item = itemType.create();
        gameManager.getEntityManager().saveItem(item);
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.addInventoryEntityId(item.getItemId());
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
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
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setPlayerClass(collect.get(0));
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }

    }

    @Override
    public String getPlayerClass() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getPlayerClass().getIdentifier();
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