/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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


import com.codahale.metrics.Gauge;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.SessionManager;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.StatData;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.world.model.Room;
import org.apache.commons.codec.binary.Base64;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.codahale.metrics.MetricRegistry.name;

public class PlayerManager {

    private final GraphStorageFactory graphStorageFactory;
    private final SessionManager sessionManager;
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public PlayerManager(GraphStorageFactory graphStorageFactory, SessionManager sessionManager) {
        this.graphStorageFactory = graphStorageFactory;
        this.sessionManager = sessionManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public Player addPlayer(Player player) {
        return players.put(player.getPlayerId(), player);
    }

    public Iterator<java.util.Map.Entry<String, Player>> getPlayers() {
        return players.entrySet().iterator();
    }

    public Map<String, Player> getAllPlayersMap() {
        return players;
    }

    public void removePlayer(String username) {
        Player player = getPlayerByUsername(username);
        if (player.getChannel() != null && player.getChannel().isConnected()) {
            player.getChannel().disconnect();
        }
        players.remove(player.getPlayerId());
    }

    public Player getPlayerByUsername(String username) {
        return getPlayer(new String(Base64.encodeBase64(username.getBytes())));
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public Optional<Player> getPlayerByCommandTarget(Room room, String target) {
        Set<Player> presentPlayers = room.getPresentPlayers();
        for (Player presentPlayer : presentPlayers) {
            if (presentPlayer != null && presentPlayer.getPlayerName().equals(target)) {
                return Optional.of(presentPlayer);
            }
        }
        return Optional.empty();
    }

    public boolean doesPlayerExist(String username) {
        return players.containsKey(new String(Base64.encodeBase64(username.getBytes())));
    }

    public boolean hasRole(Player player, PlayerRole playerRole) {
        return player.getRoles().contains(playerRole);
    }

    public boolean hasAnyOfRoles(Player player, Set<PlayerRole> checkRoles) {
        return !Collections.disjoint(player.getRoles(), checkRoles);
    }

    public void createAllGauges() {
        try( GraphStorageFactory.AetherMudTx tx = this.graphStorageFactory.beginTransaction() ) {
            for (Map.Entry<String, PlayerData> next : tx.getStorage().getAllPlayerMetadata().entrySet()) {
                createGauges(next.getValue());
            }
        }
    }

    public void createGauges(final PlayerData playerData) {
        String playerId = playerData.getPlayerId();
        String guageName = name(PlayerManager.class, playerData.getPlayerName(), "gold");
        try( GraphStorageFactory.AetherMudTx tx = this.graphStorageFactory.beginTransaction() ) {
            if (!doesGaugeExist(guageName)) {
                Main.metrics.register(guageName,
                    (Gauge<Integer>) () -> {
                        Optional<PlayerData> playerMetadataOpt = tx.getStorage().getPlayerMetadata(playerId);
                        return playerMetadataOpt.map(PlayerData::getGold).orElse(0);
                    });
            }

            guageName = name(PlayerManager.class, playerData.getPlayerName(), "current-health");
            if (!doesGaugeExist(guageName)) {
                Main.metrics.register(name(PlayerManager.class, playerData.getPlayerName(), "current-health"),
                    (Gauge<Integer>) () -> {
                        Optional<PlayerData> playerMetadataOpt = tx.getStorage().getPlayerMetadata(playerId);
                        return playerMetadataOpt.map(PlayerData::getStatData).map(StatData::getCurrentHealth).orElse(0);
                    });
            }

            guageName = name(PlayerManager.class, playerData.getPlayerName(), "xp");
            if (!doesGaugeExist(guageName)) {
                Main.metrics.register(name(PlayerManager.class, playerData.getPlayerName(), "xp"),
                    (Gauge<Integer>) () -> {
                        Optional<PlayerData> playerMetadataOpt = tx.getStorage().getPlayerMetadata(playerId);
                        return playerMetadataOpt.map(PlayerData::getStatData).map(StatData::getExperience).orElse(0);
                    });
            }
        }
    }

    private boolean doesGaugeExist(String name) {
        return Main.metrics.getGauges().containsKey(name);
    }
}
