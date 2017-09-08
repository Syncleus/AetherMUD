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

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
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
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            Set<Map.Entry<String, PlayerData>> entrySet = tx.getStorage().getAllPlayerMetadata().entrySet();
            for (Map.Entry<String, PlayerData> entry : entrySet) {
                registerPlayer(entry.getValue().getPlayerName(), entry.getValue().getPlayerId(), gameManager);
            }
        }
    }

    public void processPlayersMarkedForDeletion(){
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            Set<Map.Entry<String, PlayerData>> entrySet = tx.getStorage().getAllPlayerMetadata().entrySet();
            for (Map.Entry<String, PlayerData> entry : entrySet) {
                String playerId = entry.getKey();
                PlayerData playerData = entry.getValue();
                if (playerData.isMarkedForDelete()) {
                    if (playerData.getInventory() != null) {
                        for (String itemId : playerData.getInventory()) {
                            gameManager.getEntityManager().removeItem(itemId);
                            log.info("Removed itemId from " + playerData.getPlayerName() + "'s inventory: " + itemId);
                        }
                        for (String itemId : playerData.getLockerInventory()) {
                            gameManager.getEntityManager().removeItem(itemId);
                            log.info("Removed itemId from " + playerData.getPlayerName() + "'s locker inventory: " + itemId);
                        }
                    }
                    tx.getStorage().getAllPlayerMetadata().get(playerId).remove();
                    log.info(playerData.getPlayerName() + " has been removed from the game.");
                }
            }
        }
    }

    public static void registerPlayer(String playerName, String playerId, GameManager gameManager) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        PlayerManagement playerJMXManagement = new PlayerManagement(gameManager, playerId);
        platformMBeanServer.registerMBean(playerJMXManagement, new ObjectName("AetherMudManagement:00=Players,name=" + playerName));
    }
}
