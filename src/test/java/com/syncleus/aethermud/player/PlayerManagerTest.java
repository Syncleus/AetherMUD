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

import com.syncleus.aethermud.core.SessionManager;
import com.syncleus.aethermud.storage.graphdb.GraphDbAetherMudStorage;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.collect.Sets;
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
        GraphDbAetherMudStorage graphStorage = new GraphDbAetherMudStorage(db, false);
        playerManager = new PlayerManager(graphStorage, sessionManager);
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
