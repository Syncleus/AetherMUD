package com.comandante.creeper.player;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.base.Joiner;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public int getGold() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getGold();
    }

    @Override
    public int getGoldInBankAmount() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getGoldInBank();
    }

    @Override
    public void setGoldInBankAmount(int amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setGoldInBank(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public void setGold(int amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.setGold(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public void setHealth(int amt) {
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
    public void setMana(int amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.getStats().setCurrentMana(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public int getHealth() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentHealth();
    }

    @Override
    public void sendMessageFromGod(String message) {
        gameManager.getChannelUtils().write(playerId, Color.BOLD_ON + Color.RED + "[ADMIN SPEAKS DIRECTLY TO YOU] " + message + Color.RESET + "\r\n", true);
    }

    @Override
    public int getMana() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentMana();
    }

    @Override
    public void setExperience(int amt) {
        synchronized (findInterner().intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            playerMetadata.getStats().setExperience(amt);
            gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
        }
    }

    @Override
    public int getExperience() {
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
            inventoryContents.put(msgWithoutColorCodes, itemEntity.getItemId());
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
            inventoryContents.put(msgWithoutColorCodes, itemEntity.getItemId());
        }
        return inventoryContents;
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