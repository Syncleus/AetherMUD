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
package com.syncleus.aethermud.storage;


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.storage.graphdb.model.ItemData;
import com.syncleus.aethermud.storage.graphdb.model.ItemInstanceData;
import com.syncleus.aethermud.storage.graphdb.model.NpcData;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
}
