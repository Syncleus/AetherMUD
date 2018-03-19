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
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.GraphInfo;
import com.syncleus.aethermud.storage.graphdb.model.*;
import com.syncleus.ferma.VertexFrame;
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
            ItemInstanceData.copyItem(itemInstanceData, itemInstance, this.saveItem(itemInstance.getItem()));
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

    @Override
    public GraphInfo getGraphInfo() {
        final List<? extends ItemData> allItems = this.getAllItems();
        final int numberOfItems = allItems.size();
        final List<String> internalNames = new ArrayList<>();
        for(ItemData itemData : allItems) {
            internalNames.add(itemData.getInternalItemName());
        }
        final int numberOfItemInstances = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), ItemInstanceData.class)).toList(ItemInstanceData.class).size();
        final int numberOfNodes = framedGraph.traverse((g) -> g.V()).toList(VertexFrame.class).size();
        return new GraphInfo(numberOfItems, numberOfItemInstances, internalNames, numberOfNodes);
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

    public List<? extends MerchantData> getMerchantDatas() {
        return framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), MerchantData.class)).toList(MerchantData.class);
    }

    public List<Merchant> getAllMerchants(GameManager gameManager) {
        return this.getMerchantDatas().stream().map(m -> this.createMerchant(gameManager, m)).collect(Collectors.toList());
    }

    public Merchant createMerchant(GameManager gameManager, MerchantData merchantData) {
        if (merchantData.getMerchantType() != null) {
            return new Merchant(gameManager,
                merchantData.getInternalName(),
                merchantData.getName(),
                merchantData.getColorName(),
                merchantData.getValidTriggers(),
                merchantData.getMerchantItemForSaleDatas().stream().map(m -> MerchantItemForSaleData.copyMerchantItemForSale(m)).collect(Collectors.toList()),
                merchantData.getWelcomeMessage(),
                merchantData.getRoomIds(),
                merchantData.getMerchantType());
        }

        return new Merchant(gameManager,
            merchantData.getInternalName(),
            merchantData.getName(),
            merchantData.getColorName(),
            merchantData.getValidTriggers(),
            merchantData.getMerchantItemForSaleDatas().stream().map(m -> MerchantItemForSaleData.copyMerchantItemForSale(m)).collect(Collectors.toList()),
            merchantData.getWelcomeMessage(),
            merchantData.getRoomIds());
    }

    public Optional<MerchantData> getMerchantData(String internalName) {
        MerchantData data = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), MerchantData.class).property("internalName", internalName)).nextOrDefault(MerchantData.class,null);
        return Optional.ofNullable(data);
    }

    public MerchantData newMerchantData() {
        return framedGraph.addFramedVertex(MerchantData.class);
    }
}
