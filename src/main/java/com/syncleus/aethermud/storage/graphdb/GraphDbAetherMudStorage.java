/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.GraphInfo;
import com.syncleus.aethermud.storage.graphdb.model.*;
import com.syncleus.aethermud.world.FloorManager;
import com.syncleus.aethermud.world.MapMatrix;
import com.syncleus.aethermud.world.MapsManager;
import com.syncleus.aethermud.world.RoomManager;
import com.syncleus.aethermud.world.model.*;
import com.syncleus.ferma.VertexFrame;
import com.syncleus.ferma.WrappedFramedGraph;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;

import java.util.*;
import java.util.function.Function;
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
    
    @Override
    public void loadWorld(RoomManager roomManager, MapsManager mapsManager, EntityManager entityManager, GameManager gameManager, WorldModel worldModel) {
        for( FloorModel floorModel : worldModel.getFloorModelList())
            this.buildFloor(mapsManager, entityManager, gameManager, floorModel);
        this.saveWorld(roomManager, mapsManager, gameManager.getFloorManager());
    }
    
    @Override
    public void loadWorld( MapsManager mapsManager, EntityManager entityManager, GameManager gameManager) {
        WorldModelData data = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), WorldModelData.class)).nextOrDefault(WorldModelData.class,null);
        if( data != null) {
            WorldModel worldModel = WorldModelData.copyWorldModel(data);
            for( FloorModel floorModel : worldModel.getFloorModelList())
                this.buildFloor(mapsManager, entityManager, gameManager, floorModel);
        } else {
            this.buildEmptyWorld(mapsManager, entityManager, gameManager);
        }
    }
    
    @Override
    public void saveWorld(RoomManager roomManager, MapsManager mapsManager, FloorManager floorManager) {
        WorldModel worldModel = new WorldModel();
        Set<FloorModel> floors = Sets.newHashSet();
        Set<Integer> floorIds = floorManager.getFloorIds();
        for (Integer floorId : floorIds) {
            floors.add(generateFloorModel(roomManager, floorManager, floorId, mapsManager.getFloorMatrixMaps().get(floorId)));
        }
        worldModel.setFloorModelList(floors);
        
        WorldModelData data = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), WorldModelData.class)).nextOrDefault(WorldModelData.class,null);
        if( data == null )
            data = framedGraph.addFramedVertex(WorldModelData.class);
        WorldModelData.copyWorldModel(data, worldModel);
    }
    
    private FloorModel generateFloorModel(RoomManager roomManager, FloorManager floorManager, Integer floorId, MapMatrix mapMatrix) {
        Set<Room> rooms = roomManager.getRoomsByFloorId(floorId);
        FloorModel floorModel = new FloorModel();
        floorModel.setId(floorId);
        floorModel.setRawMatrixCsv(mapMatrix.getCsv());
        floorModel.setRoomModels((new HashSet<RoomModel>()));
        floorModel.setName(floorManager.getName(floorId));
        rooms.stream()
                .map(AetherMudStorage.buildRoomModelsFromRooms())
                .forEach(roomModel -> floorModel.getRoomModels()
                        .add(roomModel));
        return floorModel;
    }

    public Function<RoomModel, BasicRoom> getBasicRoom(GameManager gameManager, final MapMatrix mapMatrix) {
        return roomModel -> {
            BasicRoomBuilder basicRoomBuilder = new BasicRoomBuilder(gameManager)
                    .setRoomId(roomModel.getRoomId())
                    .setFloorId(roomModel.getFloorId())
                    .setRoomDescription(roomModel.getRoomDescription())
                    .setRoomTitle(roomModel.getRoomTitle());

            for (String tag : roomModel.getRoomTags()) {
                basicRoomBuilder.addTag(tag);
            }
            for (String areaName : roomModel.getAreaNames()) {
                Area byName = Area.getByName(areaName);
                if (byName != null) {
                    basicRoomBuilder.addArea(byName);
                }
            }
            Map<String, String> enterExitNames = roomModel.getEnterExitNames();
            if (enterExitNames != null) {
                for (Map.Entry<String, String> next : enterExitNames.entrySet()) {
                    RemoteExit remoteExit = new RemoteExit(RemoteExit.Direction.ENTER, Integer.parseInt(next.getKey()), next.getValue());
                    basicRoomBuilder.addEnterExit(remoteExit);
                    mapMatrix.addRemote(roomModel.getRoomId(), remoteExit);
                }
            }
            Map<String, String> notables = roomModel.getNotables();
            if (notables != null) {
                for (Map.Entry<String, String> next : notables.entrySet()) {
                    basicRoomBuilder.addNotable(next.getKey(), next.getValue());
                }
            }
            configureExits(basicRoomBuilder, mapMatrix, roomModel.getRoomId());
            return basicRoomBuilder.createBasicRoom();
        };
    }

    private void configureExits(BasicRoomBuilder basicRoomBuilder, MapMatrix mapMatrix, int roomId) {
        Integer north = mapMatrix.getNorthernExit(roomId);
        if (north > 0) {
            basicRoomBuilder.setNorthId(Optional.of(north));
        }
        Integer east = mapMatrix.getEasternExit(roomId);
        if (east > 0) {
            basicRoomBuilder.setEastId(Optional.of(east));
        }
        Integer south = mapMatrix.getSouthernExit(roomId);
        if (south > 0) {
            basicRoomBuilder.setSouthId(Optional.of(south));
        }
        Integer west = mapMatrix.getWesternExit(roomId);
        if (west > 0) {
            basicRoomBuilder.setWestId(Optional.of(west));
        }
        if (mapMatrix.getRemotes().containsKey(roomId)) {
            for (RemoteExit exit : mapMatrix.getRemotes().get(roomId)) {
                if (exit.getDirection().equals(RemoteExit.Direction.UP)) {
                    basicRoomBuilder.setUpId(Optional.of(exit.getRoomId()));
                } else if (exit.getDirection().equals(RemoteExit.Direction.DOWN)) {
                    basicRoomBuilder.setDownId(Optional.of(exit.getRoomId()));
                }
            }
        }
    }

    private void buildFloor(MapsManager mapsManager, EntityManager entityManager, GameManager gameManager, FloorModel floorModel) {
        MapMatrix matrixFromCsv = MapMatrix.createMatrixFromCsv(floorModel.getRawMatrixCsv());
        Set<Room> rooms = Sets.newHashSet();
        if (floorModel.getRoomModels() == null || floorModel.getRoomModels().size() == 0) {
            Iterator<List<Integer>> rows = matrixFromCsv.getRows();
            while (rows.hasNext()) {
                List<Integer> row = rows.next();
                for (Integer roomId : row) {
                    if (roomId.equals(0)) {
                        continue;
                    }
                    BasicRoomBuilder basicRoomBuilder = new BasicRoomBuilder(gameManager);
                    basicRoomBuilder.setFloorId(floorModel.getId());
                    basicRoomBuilder.setRoomId(roomId);
                    basicRoomBuilder.setRoomTitle("This is a blank title.");
                    basicRoomBuilder.setRoomDescription("This is a blank Description.\nWords should go here, ideally.");
                    configureExits(basicRoomBuilder, matrixFromCsv, roomId);
                    rooms.add(basicRoomBuilder.createBasicRoom());
                }
            }
            for (Room r : rooms) {
                entityManager.addEntity(r);
            }
            gameManager.getFloorManager().addFloor(floorModel.getId(), floorModel.getName());
            mapsManager.addFloorMatrix(floorModel.getId(), matrixFromCsv);
            return;
        }
        floorModel.getRoomModels().stream().map(getBasicRoom(gameManager, matrixFromCsv)).forEach(entityManager::addEntity);
        gameManager.getFloorManager().addFloor(floorModel.getId(), floorModel.getName());
        mapsManager.addFloorMatrix(floorModel.getId(), matrixFromCsv);
    }
    
    private void buildEmptyWorld(MapsManager mapsManager, EntityManager entityManager, GameManager gameManager) {
        WorldModel worldModel = new GsonBuilder().create().fromJson("{\n" +
                "  \"floorModelList\": [\n" +
                "    {\n" +
                "      \"name\": \"main\",\n" +
                "      \"id\": 0,\n" +
                "      \"rawMatrixCsv\": \"0,1\"\n" +
                "}]\n" +
                "}", WorldModel.class);
        for (FloorModel next : worldModel.getFloorModelList()) {
            buildFloor(mapsManager, entityManager, gameManager, next);
        }
    }
}
