package com.comandante.creeper.player;

import com.comandante.creeper.managers.GameManager;
import org.apache.log4j.Logger;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Map;

public class PlayerManagementManager {

    private final GameManager gameManager;
    private final MBeanServer mbs;
    private static final Logger log = Logger.getLogger(PlayerManagementManager.class);


    public PlayerManagementManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.mbs = ManagementFactory.getPlatformMBeanServer();
    }

    public void createAndRegisterAllPlayerManagementMBeans() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        for (Map.Entry<String, PlayerMetadata> entry : gameManager.getPlayerManager().getPlayerMetadataStore().entrySet()) {
            String playerId = entry.getKey();
            PlayerMetadata playerMetadata = entry.getValue();
            PlayerManagement playerManagement = new PlayerManagement(gameManager, playerId);
            mbs.registerMBean(playerManagement, new ObjectName("CreeperManagement:00=Players,name=" + playerMetadata.getPlayerName()));
        }
    }

    public void processPlayersMarkedForDeletion(){
        for (Map.Entry<String, PlayerMetadata> entry : gameManager.getPlayerManager().getPlayerMetadataStore().entrySet()) {
            String playerId = entry.getKey();
            PlayerMetadata playerMetadata = entry.getValue();
            if (playerMetadata.isMarkedForDelete()) {
                if (playerMetadata.getInventory() != null) {
                    for (String itemId : playerMetadata.getInventory()) {
                        gameManager.getEntityManager().removeItem(itemId);
                        log.info("Removed itemId from " + playerMetadata.getPlayerName() + "'s inventory: " + itemId);
                    }
                }
                    gameManager.getPlayerManager().getPlayerMetadataStore().remove(playerId);
                    log.info(playerMetadata.getPlayerName() + " has been removed from the game.");
            }
        }
    }
}
