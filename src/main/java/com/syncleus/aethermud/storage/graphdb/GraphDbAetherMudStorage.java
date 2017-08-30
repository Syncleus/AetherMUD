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
package com.syncleus.aethermud.storage.graphdb;

import com.google.common.util.concurrent.AbstractIdleService;
import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.EffectSerializer;
import com.syncleus.aethermud.storage.ItemSerializer;
import com.syncleus.aethermud.storage.MapDbAutoCommitService;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.File;
import java.io.IOException;
import java.util.*;

//TODO : multiple instances of this class could create conflicts in the DB
public class GraphDbAetherMudStorage extends AbstractIdleService implements AetherMudStorage {
    private static final Set<Class<?>> TEST_TYPES = new HashSet<>(Arrays.asList(new Class<?>[]{PlayerData.class}));
    private static final String DEFAULT_FLAT_FILE = "aethermud-graph.json";

    private final Graph graph = TinkerGraph.open();
    private final FramedGraph framedGraph = new DelegatingFramedGraph(graph, TEST_TYPES);
    private final DB db;
    private final MapDbAutoCommitService mapDbAutoCommitService;

    private final HTreeMap<String, Item> items;
    private final HTreeMap<String, Effect> effects;

    private final static String ITEM_MAP = "itemMap";
    private final static String EFFECTS_MAP = "effectsMap";
    private final boolean autoPersist;

    public GraphDbAetherMudStorage(DB db){
        this(db, true);
    }

    public GraphDbAetherMudStorage(DB db, boolean autoPersist) {
        this.db = db;
        this.autoPersist = autoPersist;
        this.items = db.hashMap(ITEM_MAP)
            .keySerializer(Serializer.STRING)
            .valueSerializer(new ItemSerializer())
            .createOrOpen();

        this.effects = db.hashMap(EFFECTS_MAP)
            .keySerializer(Serializer.STRING)
            .valueSerializer(new EffectSerializer())
            .createOrOpen();

        this.mapDbAutoCommitService = new MapDbAutoCommitService(db);

        File f = new File(DEFAULT_FLAT_FILE);
        if(f.exists() && !f.isDirectory()) {
            try {
                graph.io(IoCore.graphson()).readGraph(DEFAULT_FLAT_FILE);
            } catch (IOException e) {
                throw new IllegalStateException("Could not read from graph file despite being present.", e);
            }
        }
    }

    @Override
    public PlayerData newPlayerData(){
        PlayerData playerData = framedGraph.addFramedVertex(PlayerData.class);

        // TODO : remove this, not all players should be admins
        Set<PlayerRole> roles = new HashSet<>(3);
        roles.add(PlayerRole.ADMIN);
        roles.add(PlayerRole.GOD);
        roles.add(PlayerRole.TELEPORTER);
        playerData.setPlayerRoleSet(roles);

        return playerData;
    }

    @Override
    public Optional<PlayerData> getPlayerMetadata(String playerId) {
        final PlayerData data = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V().has("playerId", playerId), PlayerData.class)).nextOrDefault(PlayerData.class, null);
        return Optional.ofNullable(data);
    }

    @Override
    public Map<String, PlayerData> getAllPlayerMetadata() {
        final List<? extends PlayerData> datas = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), PlayerData.class)).toList(PlayerData.class);
        System.out.println("Count: " + datas.size());
        final Map<String, PlayerData> retVal = new HashMap<>(datas.size());
        for( PlayerData data : datas ) {
            retVal.put(data.getPlayerId(), data);
        }
        return retVal;
    }

    @Override
    public void removePlayerMetadata(String playerId) {
        final PlayerData data = framedGraph.traverse((g) -> g.V().has("playerId", playerId)).next(PlayerData.class);
        data.remove();

        if (this.autoPersist) {
            this.persist();
        }
    }

    @Override
    public void saveItemEntity(Item item) {
        this.items.put(item.getItemId(), item);
    }

    @Override
    public Optional<Item> getItemEntity(String itemId) {
        return Optional.ofNullable(this.items.get(itemId));
    }

    @Override
    public void removeItem(String itemId) {
        this.items.remove(itemId);
    }

    @Override
    public void persist() {
        try {
            graph.io(IoCore.graphson()).writeGraph(DEFAULT_FLAT_FILE);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write to graph file.", e);
        }
    }

    @Override
    protected void startUp() throws Exception {
        mapDbAutoCommitService.startAsync();
    }

    @Override
    protected void shutDown() throws Exception {
        mapDbAutoCommitService.stopAsync();
        mapDbAutoCommitService.awaitTerminated();
        db.commit();
    }
}
