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
package com.syncleus.aethermud.storage;


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.storage.graphdb.model.*;
import com.syncleus.aethermud.world.FloorManager;
import com.syncleus.aethermud.world.MapsManager;
import com.syncleus.aethermud.world.RoomManager;
import com.syncleus.aethermud.world.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface AetherMudStorage {

    PlayerData newPlayerData();

    Optional<PlayerData> getPlayerMetadata(String playerId);

    Map<String, PlayerData> getAllPlayerMetadata();

    ItemInstanceData saveItemEntity(ItemInstance item);

    Optional<ItemInstanceData> getItemEntity(String itemId);

    void removeItemEntity(String itemId);

    ItemData saveItem(Item item);

    Optional<ItemData> getItem(String internalName);

    void removeItem(String internalName);

    public List<? extends ItemData> getAllItems();

    List<? extends NpcSpawn> getAllNpcs(GameManager gameManager);

    List<? extends NpcData> getNpcDatas();

    NpcData newNpcData();

    GraphInfo getGraphInfo();

    List<? extends MerchantData> getMerchantDatas();

    List<Merchant> getAllMerchants(GameManager gameManager);

    Merchant createMerchant(GameManager gameManager, MerchantData merchantData);

    Optional<MerchantData> getMerchantData(String internalName);

    MerchantData newMerchantData();
    
    void saveWorld(RoomManager roomManager, MapsManager mapsManager, FloorManager floorManager);
    
    void loadWorld(MapsManager mapsManager, EntityManager entityManager, GameManager gameManager);
    
    void loadWorld(RoomManager roomManager, MapsManager mapsManager, EntityManager entityManager, GameManager gameManager, WorldModel worldModel);

    static Function<Room, RoomModel> buildRoomModelsFromRooms() {
        return room -> {
            RoomModelBuilder roomModelBuilder = new RoomModelBuilder();
            for (RemoteExit remoteExit : room.getEnterExits()) {
                roomModelBuilder.addEnterExitName(remoteExit.getRoomId(), remoteExit.getExitDetail());
            }
            roomModelBuilder.setRoomDescription(room.getRoomDescription());
            roomModelBuilder.setRoomTitle(room.getRoomTitle());
            roomModelBuilder.setRoomId(room.getRoomId());
            roomModelBuilder.setRoomTags(room.getRoomTags());
            roomModelBuilder.setFloorId(room.getFloorId());
            for (Area area : room.getAreas()) {
                roomModelBuilder.addAreaName(area.getName());
            }
            for (Map.Entry<String, String> notable : room.getNotables().entrySet()) {
                roomModelBuilder.addNotable(notable.getKey(), notable.getValue());
            }
            return roomModelBuilder.build();
        };
    }
}
