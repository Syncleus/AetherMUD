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


import com.codahale.metrics.Gauge;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.SessionManager;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.world.model.Room;
import org.apache.commons.codec.binary.Base64;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.codahale.metrics.MetricRegistry.name;

public class PlayerManager {

    private final AetherMudStorage aetherMudStorage;
    private final SessionManager sessionManager;
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public PlayerManager(AetherMudStorage aetherMudStorage, SessionManager sessionManager) {
        this.aetherMudStorage = aetherMudStorage;
        this.sessionManager = sessionManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void savePlayerMetadata(PlayerMetadata playerMetadata) {
        aetherMudStorage.savePlayerMetadata(playerMetadata);
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
        Optional<PlayerMetadata> playerMetadata = getPlayerMetadata(player.getPlayerId());
        if (!playerMetadata.isPresent()) {
            return false;
        }
        Set<PlayerRole> playerRoleSet = playerMetadata.get().getPlayerRoleSet();
        return playerRoleSet != null && playerMetadata.get().getPlayerRoleSet().contains(playerRole);
    }

    public Optional<PlayerMetadata> getPlayerMetadata(String playerId) {
        Optional<PlayerMetadata> playerMetadataOptional = aetherMudStorage.getPlayerMetadata(playerId);
        return playerMetadataOptional.map(PlayerMetadata::new);
    }

    public boolean hasAnyOfRoles(Player player, Set<PlayerRole> checkRoles) {
        Optional<PlayerMetadata> playerMetadata = getPlayerMetadata(player.getPlayerId());
        if (!playerMetadata.isPresent()) {
            return false;
        }
        Set<PlayerRole> playerRoleSet = playerMetadata.get().getPlayerRoleSet();
        if (playerRoleSet != null) {
            for (PlayerRole checkRole : checkRoles) {
                if (playerRoleSet.contains(checkRole)) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public void createAllGauges() {
        for (Map.Entry<String, PlayerMetadata> next : aetherMudStorage.getAllPlayerMetadata().entrySet()) {
            createGauges(next.getValue());
        }
    }

    public void createGauges(final PlayerMetadata playerMetadata) {
        String playerId = playerMetadata.getPlayerId();
        String guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "gold");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(guageName,
                    (Gauge<Long>) () -> {
                        Optional<PlayerMetadata> playerMetadataOpt = aetherMudStorage.getPlayerMetadata(playerId);
                        return playerMetadataOpt.map(PlayerMetadata::getGold).orElse(0L);
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health"),
                    (Gauge<Long>) () -> {
                        Optional<PlayerMetadata> playerMetadataOpt = aetherMudStorage.getPlayerMetadata(playerId);
                        return playerMetadataOpt.map(PlayerMetadata::getStats).map(Stats::getCurrentHealth).orElse(0L);
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "xp");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "xp"),
                    (Gauge<Long>) () -> {
                        Optional<PlayerMetadata> playerMetadataOpt = aetherMudStorage.getPlayerMetadata(playerId);
                        return playerMetadataOpt.map(PlayerMetadata::getStats).map(Stats::getExperience).orElse(0L);
                    });
        }
    }

    private boolean doesGaugeExist(String name) {
        return Main.metrics.getGauges().containsKey(name);
    }

    public Map<String, PlayerMetadata> getPlayerMetadataStore() {
        return aetherMudStorage.getAllPlayerMetadata();
    }
}
