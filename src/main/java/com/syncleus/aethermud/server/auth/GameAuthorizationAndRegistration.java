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
package com.syncleus.aethermud.server.auth;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.player.PlayerMetadata;
import com.syncleus.aethermud.world.model.Room;
import org.jboss.netty.channel.Channel;

import java.util.Optional;

public class GameAuthorizationAndRegistration implements AetherMudAuthenticator {

    private final GameManager gameManager;

    public GameAuthorizationAndRegistration(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean authenticateAndRegisterPlayer(String username, String password, Channel channel) {
        Optional<PlayerMetadata> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(Main.createPlayerId(username));
        if (!playerMetadataOptional.isPresent()) {
            return false;
        }
        PlayerMetadata playerMetadata = playerMetadataOptional.get();
        if (!playerMetadata.getPassword().equals(password)) {
            return false;
        }
        Room currentRoom = null;
        if (gameManager.getPlayerManager().doesPlayerExist(username)) {
            currentRoom = gameManager.getPlayerManager().getPlayerByUsername(username).getCurrentRoom();
            gameManager.getPlayerManager().removePlayer(username);
        } else {
            Optional<Room> playerCurrentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(Main.createPlayerId(username));
            if (playerCurrentRoom.isPresent()) {
                currentRoom = playerCurrentRoom.get();
            }
        }

        Player player = new Player(username, gameManager);
        if (currentRoom != null) {
            player.setCurrentRoomAndPersist(currentRoom);
            currentRoom.addPresentPlayer(player.getPlayerId());
        } else {
            currentRoom = player.getCurrentRoom();
            if (currentRoom != null) {
                currentRoom.addPresentPlayer(player.getPlayerId());
                player.setCurrentRoom(player.getCurrentRoom());
            }
        }
        player.setChannel(channel);
        gameManager.getPlayerManager().addPlayer(player);
        if (currentRoom == null) {
            gameManager.placePlayerInLobby(player);
        }
        return true;
    }
}
