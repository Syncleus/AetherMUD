package com.comandante.creeper.player;


import com.codahale.metrics.Gauge;
import com.comandante.creeper.Main;
import com.comandante.creeper.core_game.SessionManager;
import com.comandante.creeper.storage.MapDbAutoCommitService;
import com.comandante.creeper.world.model.Room;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.codahale.metrics.MetricRegistry.name;

public class PlayerManager {
    private final SessionManager sessionManager;
    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<String, Player>();
    private HTreeMap<String, PlayerMetadata> playerMetadataStore;

    private final static String PLAYER_METADATA_MAP = "playerMetadata";

    public PlayerManager(DB db, SessionManager sessionManager) {
        this.playerMetadataStore = db.hashMap(PLAYER_METADATA_MAP)
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(new PlayerMetadataSerializer())
                    .createOrOpen();
        this.sessionManager = sessionManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void savePlayerMetadata(PlayerMetadata playerMetadata) {
        playerMetadataStore.put(playerMetadata.getPlayerId(), playerMetadata);
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
        PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
        Set<PlayerRole> playerRoleSet = playerMetadata.getPlayerRoleSet();
        if (playerRoleSet != null) {
            return playerMetadata.getPlayerRoleSet().contains(playerRole);
        } else {
            return false;
        }
    }

    public PlayerMetadata getPlayerMetadata(String playerId) {
        PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
        if (playerMetadata == null) {
            return playerMetadata;
        }
        return new PlayerMetadata(playerMetadata);
    }

    public boolean hasAnyOfRoles(Player player, Set<PlayerRole> checkRoles) {
        PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
        Set<PlayerRole> playerRoleSet = playerMetadata.getPlayerRoleSet();
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
        Iterator<Map.Entry<String, PlayerMetadata>> iterator = playerMetadataStore.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PlayerMetadata> next = iterator.next();
            createGauges(next.getValue());
        }
    }

    public void createGauges(final PlayerMetadata playerMetadata) {
        String guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "gold");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(guageName,
                    new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return getPlayerMetadata(playerMetadata.getPlayerId()).getGold();
                        }
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health"),
                    new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return getPlayerMetadata(playerMetadata.getPlayerId()).getStats().getCurrentHealth();
                        }
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "xp");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "xp"),
                    new Gauge<Long>() {
                        @Override
                        public Long getValue() {
                            return getPlayerMetadata(playerMetadata.getPlayerId()).getStats().getExperience();
                        }
                    });
        }
    }

    private boolean doesGaugeExist(String name) {
        return Main.metrics.getGauges().containsKey(name);
    }

    public HTreeMap<String, PlayerMetadata> getPlayerMetadataStore() {
        return playerMetadataStore;
    }
}
