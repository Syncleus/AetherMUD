package com.comandante.creeper.player;


import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.comandante.creeper.Main;
import com.comandante.creeper.fight.FightManager;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.world.Room;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.codec.binary.Base64;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.codahale.metrics.MetricRegistry.name;

public class PlayerManager {

    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<String, Player>();
    private HTreeMap<String, PlayerMetadata> playerMetadataStore;
    private final DB db;
    private final SessionManager sessionManager;

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public PlayerManager(DB db, SessionManager sessionManager) {
        this.db = db;
        if (db.exists("playerMetadata")) {
            this.playerMetadataStore = db.get("playerMetadata");
        } else {
            this.playerMetadataStore = db.createHashMap("playerMetadata").valueSerializer(new PlayerMetadataSerializer()).make();
        }
        this.sessionManager = sessionManager;
    }

    public Set<Player> getPresentPlayers(Room room) {
        Set<String> presentPlayerIds = room.getPresentPlayerIds();
        Set<Player> players = Sets.newHashSet();
        for (String playerId : presentPlayerIds) {
            players.add(getPlayer(playerId));
        }
        return ImmutableSet.copyOf(players);
    }

    public int getNumberOfLoggedInUsers() {
        int cnt = 0;
        Iterator<Map.Entry<String, Player>> players = getPlayers();
        while (players.hasNext()) {
            Map.Entry<String, Player> next = players.next();
            cnt++;
        }
        return cnt;
    }

    public void addInventoryId(String playerId, String inventoryId) {
        PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
        playerMetadata.addInventoryEntityId(inventoryId);
        savePlayerMetadata(playerMetadata);
    }

    public void removeInventoryId(String playerId, String inventoryId) {
        PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
        playerMetadata.removeInventoryEntityId(inventoryId);
        savePlayerMetadata(playerMetadata);
    }

    public void incrementGold(String playerId, int amt) {
        PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
        playerMetadata.incrementGold(amt);
        savePlayerMetadata(playerMetadata);
    }

    public PlayerMetadata getPlayerMetadata(String playerId) {
        return playerMetadataStore.get(playerId);
    }

    public void savePlayerMetadata(PlayerMetadata playerMetadata) {
        playerMetadataStore.put(playerMetadata.getPlayerId(), playerMetadata);
        db.commit();
    }

    public Player addPlayer(Player player) {
        return players.putIfAbsent(player.getPlayerId(), player);
    }

    public Player getPlayerByUsername(String username) {
        return getPlayer(new String(Base64.encodeBase64(username.getBytes())));
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public Iterator<java.util.Map.Entry<String, Player>> getPlayers() {
        return players.entrySet().iterator();
    }

    public void removePlayer(String username) {
        Player player = getPlayerByUsername(username);
        if (player.getChannel() != null && player.getChannel().isConnected()) {
            player.getChannel().disconnect();
        }
        players.remove(player.getPlayerId());
    }

    public boolean doesPlayerExist(String username) {
        return players.containsKey(new String(Base64.encodeBase64(username.getBytes())));
    }

    public void updatePlayerHealth(String playerId, int amount) {
        synchronized (playerId) {
            PlayerMetadata playerMetadata = getPlayerMetadata(playerId);
            Stats stats = playerMetadata.getStats();
            stats.setCurrentHealth(stats.getCurrentHealth() + amount);
            savePlayerMetadata(playerMetadata);
        }
    }


    public String buildPrompt(String playerId) {
        boolean isFight = FightManager.isActiveFight(sessionManager.getSession(playerId));
        Player player = getPlayer(playerId);
        PlayerMetadata playerMetadata = getPlayerMetadata(playerId);
        int currentHealth = playerMetadata.getStats().getCurrentHealth();
        int maxHealth = playerMetadata.getStats().getMaxHealth();
        StringBuilder sb = new StringBuilder()
                .append("[")
                .append(player.getPlayerName())
                .append("@")
                .append(Main.MUD_NAME)
                .append(" ")
                .append(currentHealth).append("/").append(maxHealth);
        if (isFight) {
            sb.append(Color.RED + " ! " + Color.RESET);
        }
        sb.append("] ");
        return sb.toString();
    }

    public void createGauges(final PlayerMetadata playerMetadata) {
        String guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "gold");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(guageName,
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return playerMetadata.getGold();
                        }
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health"),
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return playerMetadata.getStats().getCurrentHealth();
                        }
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "xp");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "xp"),
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return playerMetadata.getStats().getExperience();
                        }
                    });
        }
    }

    public void createAllGauges() {
        Iterator<Map.Entry<String, PlayerMetadata>> iterator = playerMetadataStore.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, PlayerMetadata> next = iterator.next();
            createGauges(next.getValue());
        }
    }

    private boolean doesGaugeExist(String name) {
        return Main.metrics.getGauges().containsKey(name);
    }
}
