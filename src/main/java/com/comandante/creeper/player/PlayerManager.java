package com.comandante.creeper.player;


import com.codahale.metrics.Gauge;
import com.comandante.creeper.Main;
import com.comandante.creeper.MapDbAutoCommitService;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.stat.Stats;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.apache.commons.codec.binary.Base64;
import org.mapdb.DB;
import org.mapdb.HTreeMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
        MapDbAutoCommitService mapDbAutoCommitService = new MapDbAutoCommitService(db);
        mapDbAutoCommitService.startAsync();
        this.sessionManager = sessionManager;
    }

    public void addInventoryId(Player player, String inventoryId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.addInventoryEntityId(inventoryId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void transferItemToLocker(Player player, String inventoryId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.removeInventoryEntityId(inventoryId);
            playerMetadata.addLockerEntityId(inventoryId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void transferItemFromLocker(Player player, String entityId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.removeLockerEntityId(entityId);
            playerMetadata.addInventoryEntityId(entityId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void removeInventoryId(Player player, String inventoryId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.removeInventoryEntityId(inventoryId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void addEquipmentId(Player player, String equipmentId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.addEquipmentEntityId(equipmentId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void removeEquipmentId(Player player, String equipmentId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.removeEquipmentEntityId(equipmentId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void addEffect(Player player, String effectId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.addEffectId(effectId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void removeEffect(Player player, String effectId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.removeEffectId(effectId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void incrementGold(Player player, int amt) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(player.getPlayerId());
            playerMetadata.incrementGold(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void incrementGold(String playerId, int amt) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
            playerMetadata.incrementGold(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void transferGoldToBank(String playerId, int amt) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
            playerMetadata.transferGoldToBank(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void transferBankGoldToPlayer(String playerId, int amt) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
            playerMetadata.transferBankGoldToPlayer(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public PlayerMetadata getPlayerMetadata(String playerId) {
        PlayerMetadata playerMetadata = playerMetadataStore.get(playerId);
        if (playerMetadata == null) {
            return playerMetadata;
        }
        return new PlayerMetadata(playerMetadata);
    }

    public void savePlayerMetadata(PlayerMetadata playerMetadata) {
        playerMetadataStore.put(playerMetadata.getPlayerId(), playerMetadata);
    }

    public void addRole(Player player, PlayerRole playerRole) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
            playerMetadata.addPlayerRole(playerRole);
            savePlayerMetadata(playerMetadata);
        }
    }

    public Player addPlayer(Player player) {
        return players.put(player.getPlayerId(), player);
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

    public void updatePlayerHealth(Player player, int amount) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
            Stats stats = playerMetadata.getStats();
            if ((stats.getCurrentHealth() + amount) < 0) {
                stats.setCurrentHealth(0);
            } else {
                stats.setCurrentHealth(stats.getCurrentHealth() + amount);
            }
            savePlayerMetadata(playerMetadata);
        }
    }

    public void updatePlayerMana(Player player, int amount) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
            Stats stats = playerMetadata.getStats();
            stats.setCurrentMana(stats.getCurrentMana() + amount);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void updatePlayerForageExperience(Player player, int amount) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = getPlayerMetadata(player.getPlayerId());
            Stats stats = playerMetadata.getStats();
            stats.setForaging(stats.getForaging() + amount);
            savePlayerMetadata(playerMetadata);
        }
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

    public void createGauges(final PlayerMetadata playerMetadata) {
        String guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "gold");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(guageName,
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return getPlayerMetadata(playerMetadata.getPlayerId()).getGold();
                        }
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "current-health"),
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return getPlayerMetadata(playerMetadata.getPlayerId()).getStats().getCurrentHealth();
                        }
                    });
        }

        guageName = name(PlayerManager.class, playerMetadata.getPlayerName(), "xp");
        if (!doesGaugeExist(guageName)) {
            Main.metrics.register(name(PlayerManager.class, playerMetadata.getPlayerName(), "xp"),
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return getPlayerMetadata(playerMetadata.getPlayerId()).getStats().getExperience();
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
