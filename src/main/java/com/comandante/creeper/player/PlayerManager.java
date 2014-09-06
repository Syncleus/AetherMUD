package com.comandante.creeper.player;


import com.comandante.creeper.fight.FightManager;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.world.Room;
import com.comandante.creeper.stat.Stats;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.codec.binary.Base64;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    public String getLookString(Player player) {
        PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
        Stats playerStats = playerMetadata.getStats();
        return playerStats.toString();
    }

    public void updatePlayerHealth(String playerId, int amount) {
        synchronized (playerId) {
            PlayerMetadata playerMetadata = getPlayerMetadata(playerId);
            Stats stats = playerMetadata.getStats();
            stats.setCurrentHealth(stats.getCurrentHealth() + amount);
            savePlayerMetadata(playerMetadata);
        }
    }


    public String getPrompt(String playerId, Integer roomId) {
        boolean isFight = FightManager.isActiveFight(sessionManager.getSession(playerId));
        Player player = getPlayer(playerId);
        PlayerMetadata playerMetadata = getPlayerMetadata(playerId);
        int currentHealth = playerMetadata.getStats().getCurrentHealth();
        int maxHealth = playerMetadata.getStats().getMaxHealth();
        StringBuilder sb = new StringBuilder()
                .append("[")
                .append(player.getPlayerName())
                .append(" health: ")
                .append(currentHealth).append("/").append(maxHealth)
                .append(" roomId:")
                .append(roomId)
                .append((" users:"))
                .append(getNumberOfLoggedInUsers());
        if (isFight) {
            sb.append(" in battle! ");
        }
        sb.append("] ");
        return sb.toString();
    }

}
