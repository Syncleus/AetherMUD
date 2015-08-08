package com.comandante.creeper.jmx_management;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMetadata;
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