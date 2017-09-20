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

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import com.syncleus.aethermud.storage.graphdb.model.ItemInstanceData;
import com.syncleus.aethermud.storage.graphdb.model.NpcData;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.ferma.WrappedFramedGraph;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class GraphDbAetherMudStorage implements AetherMudStorage {
    private static final Logger LOGGER = Logger.getLogger(GraphDbAetherMudStorage.class);
    private final WrappedFramedGraph<? extends Graph> framedGraph;
    private final Interner<String> interner = Interners.newWeakInterner();


    public GraphDbAetherMudStorage(WrappedFramedGraph<? extends Graph> framedGraph) {
        this.framedGraph = framedGraph;
    }

    @Override
    public PlayerData newPlayerData(){
        return framedGraph.addFramedVertex(PlayerData.class);
    }

    @Override
    public Optional<PlayerData> getPlayerMetadata(String playerId) {
        final PlayerData data = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V().has("playerId", playerId), PlayerData.class)).nextOrDefault(PlayerData.class, null);
        return Optional.ofNullable(data);
    }

    @Override
    public Map<String, PlayerData> getAllPlayerMetadata() {
        final List<? extends PlayerData> datas = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), PlayerData.class)).toList(PlayerData.class);
        final Map<String, PlayerData> retVal = new HashMap<>(datas.size());
        for( PlayerData data : datas ) {
            retVal.put(data.getPlayerId(), data);
        }
        return retVal;
    }

    @Override
    public Optional<ItemInstanceData> getItemEntity(String itemId) {
        synchronized (interner.intern(itemId)) {
            return Optional.ofNullable(framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), ItemInstanceData.class).has("itemId", itemId)).nextOrDefault(ItemInstanceData.class, null));
        }
    }

    @Override
    public void removeItemEntity(String itemId) {
        synchronized (interner.intern(itemId)) {
            this.getItemEntity(itemId).ifPresent((i) -> i.remove());
        }
    }

    @Override
    public ItemInstanceData saveItemEntity(ItemInstance itemInstance) {
        synchronized (interner.intern(itemInstance.getItemId())) {
            Optional<ItemInstanceData> existing = this.getItemEntity(itemInstance.getItemId());
            ItemInstanceData itemInstanceData;
            if (existing.isPresent())
                itemInstanceData = existing.get();
            else
                itemInstanceData = framedGraph.addFramedVertex(ItemInstanceData.class);
            ItemInstanceData.copyItem(itemInstanceData, itemInstance, this.getItem(itemInstance.getItem().getInternalItemName()).get());
            return itemInstanceData;
        }
    }

    @Override
    public ItemData saveItem(Item item) {
        synchronized (interner.intern(item.getInternalItemName())) {
            Optional<ItemData> existing = this.getItem(item.getInternalItemName());
            ItemData itemData;
            if (existing.isPresent())
                itemData = existing.get();
            else
                itemData = framedGraph.addFramedVertex(ItemData.class);
            ItemData.copyItem(itemData, item);
            return itemData;
        }
    }

    @Override
    public Optional<ItemData> getItem(String internalName) {
        synchronized (interner.intern(internalName)) {
            return Optional.ofNullable(framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), ItemData.class).has("internalItemName", internalName)).nextOrDefault(ItemData.class, null));
        }
    }

    @Override
    public List<? extends ItemData> getAllItems() {
        return framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), ItemData.class)).toList(ItemData.class);
    }

    @Override
    public void removeItem(String internalName) {
        synchronized (interner.intern(internalName)) {
            // TODO : recursively remove all instances
            this.getItem(internalName).ifPresent((i) -> i.remove());
        }
    }

    public List<? extends NpcSpawn> getAllNpcs(GameManager gameManager) {
        List<? extends NpcData> npcDatas = this.getNpcDatas();
        return npcDatas.stream()
            .map(npcData -> new NpcBuilder(NpcData.copyNpc(npcData)).setGameManager(gameManager).createNpc())
            .collect(Collectors.toList());
    }

    public List<? extends NpcData> getNpcDatas() {
        return framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), NpcData.class)).toList(NpcData.class);
    }

    public NpcData newNpcData() {
        return framedGraph.addFramedVertex(NpcData.class);
    }
}
