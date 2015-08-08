package com.comandante.creeper.player;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public class PlayerManagement implements PlayerManagementMBean {

    private final GameManager gameManager;
    private final String playerId;

        public PlayerManagement(GameManager gameManager, String playerId) {
            this.gameManager = gameManager;
            this.playerId = playerId;
        }

        @Override
        public void toggleMarkForDelete() {
            Interner<String> interner = findInterner();
            synchronized (interner.intern(playerId)) {
                PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
                playerMetadata.setIsMarkedForDelete(true);
                gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
            }
        }

        @Override
        public boolean isMarkedForDelete() {
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
        gameManager.getChannelUtils().write(playerId, Color.BOLD_ON + Color.RED + "[ADMIN SPEAKS DIRECTLY TO YOU] " + message + "\r\n", true);
    }

    @Override
    public int getMana() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentMana();
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