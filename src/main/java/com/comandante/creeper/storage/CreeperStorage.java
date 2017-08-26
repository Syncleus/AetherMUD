/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.storage;


import com.comandante.creeper.items.Item;
import com.comandante.creeper.player.PlayerMetadata;

import java.util.Map;
import java.util.Optional;

public interface CreeperStorage {

    void savePlayerMetadata(PlayerMetadata playerMetadata);

    Optional<PlayerMetadata> getPlayerMetadata(String playerId);

    Map<String, PlayerMetadata> getAllPlayerMetadata();

    void removePlayerMetadata(String playerId);

    void saveItemEntity(Item item);

    Optional<Item> getItemEntity(String itemId);

    void removeItem(String itemId);
}
