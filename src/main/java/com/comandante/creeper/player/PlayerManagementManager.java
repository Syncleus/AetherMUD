/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.player;

import com.comandante.creeper.core_game.GameManager;
import org.apache.log4j.Logger;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Set;

public class PlayerManagementManager {

    private final GameManager gameManager;
    private static final Logger log = Logger.getLogger(PlayerManagementManager.class);


    public PlayerManagementManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void createAndRegisterAllPlayerManagementMBeans() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        Set<Map.Entry<String, PlayerMetadata>> entrySet = gameManager.getPlayerManager().getPlayerMetadataStore().entrySet();
        for (Map.Entry<String, PlayerMetadata> entry : entrySet) {
            registerPlayer(entry.getValue().getPlayerName(), entry.getValue().getPlayerId(), gameManager);
        }
    }

    public void processPlayersMarkedForDeletion(){
        Set<Map.Entry<String, PlayerMetadata>> entrySet = gameManager.getPlayerManager().getPlayerMetadataStore().entrySet();
        for (Map.Entry<String, PlayerMetadata> entry : entrySet) {
            String playerId = entry.getKey();
            PlayerMetadata playerMetadata = entry.getValue();
            if (playerMetadata.isMarkedForDelete()) {
                if (playerMetadata.getInventory() != null) {
                    for (String itemId : playerMetadata.getInventory()) {
                        gameManager.getEntityManager().removeItem(itemId);
                        log.info("Removed itemId from " + playerMetadata.getPlayerName() + "'s inventory: " + itemId);
                    }
                    for (String itemId : playerMetadata.getLockerInventory()) {
                        gameManager.getEntityManager().removeItem(itemId);
                        log.info("Removed itemId from " + playerMetadata.getPlayerName() + "'s locker inventory: " + itemId);
                    }
                }
                    gameManager.getPlayerManager().getPlayerMetadataStore().remove(playerId);
                    log.info(playerMetadata.getPlayerName() + " has been removed from the game.");
            }
        }
    }

    public static void registerPlayer(String playerName, String playerId, GameManager gameManager) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        PlayerManagement playerJMXManagement = new PlayerManagement(gameManager, playerId);
        platformMBeanServer.registerMBean(playerJMXManagement, new ObjectName("CreeperManagement:00=Players,name=" + playerName));
    }
}
