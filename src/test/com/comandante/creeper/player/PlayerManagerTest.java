package com.comandante.creeper.player;

import com.comandante.creeper.core_game.SessionManager;
import com.comandante.creeper.world.model.Room;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class PlayerManagerTest {

    private PlayerManager playerManager;

    @Mock
    SessionManager sessionManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        DB db = DBMaker.memoryDB().transactionEnable().make();
        playerManager = new PlayerManager(db, sessionManager);
    }

    @Test
    public void testGetPlayerByCommandTargetWhenNullNoPlayers() throws Exception {
        Room room = mock(Room.class);
        Optional<Player> playerByCommandTarget = playerManager.getPlayerByCommandTarget(room, null);
        Assert.assertFalse(playerByCommandTarget.isPresent());
    }

    @Test
    public void testGetPlayerByCommandTargetWhenNullSomePlayers() throws Exception {
        Room room = mock(Room.class);
        HashSet<Player> players = Sets.newHashSet();
        Player player = mock(Player.class);
        players.add(player);
        when(player.getPlayerName()).thenReturn(UUID.randomUUID().toString());
        when(room.getPresentPlayers()).thenReturn(players);
        Optional<Player> playerByCommandTarget = playerManager.getPlayerByCommandTarget(room, null);
        Assert.assertFalse(playerByCommandTarget.isPresent());
    }
}