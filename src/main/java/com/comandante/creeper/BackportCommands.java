package com.comandante.creeper;

import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.player.PlayerStats;
import com.comandante.creeper.stat.Stats;
import org.apache.log4j.Logger;

import java.util.Map;

public class BackportCommands {

    private static final Logger log = Logger.getLogger(BackportCommands.class);

    public static void configureDefaultInventorySize(EntityManager entityManager, GameManager gameManager) {
        for (Map.Entry<String, PlayerMetadata> next : gameManager.getPlayerManager().getPlayerMetadataStore().entrySet()) {
            PlayerMetadata playerMetadata = next.getValue();
            Stats stats = playerMetadata.getStats();
            if (stats.getInventorySize() == 0) {
                playerMetadata.getStats().setInventorySize(PlayerStats.DEFAULT_PLAYER.createStats().getInventorySize());
                gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
                log.info("Inventory size for player: " + playerMetadata.getPlayerName() + " was zero, setting to the default of: " + PlayerStats.DEFAULT_PLAYER.createStats().getInventorySize() + ".");
            }
        }
    }
}
