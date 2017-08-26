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

import com.syncleus.aethermud.items.Effect;
import com.syncleus.aethermud.items.Item;
import com.syncleus.aethermud.player.PlayerMetadata;
import com.syncleus.aethermud.player.PlayerMetadataSerializer;
import com.google.common.util.concurrent.AbstractIdleService;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Map;
import java.util.Optional;

public class MapDBAetherMudStorage extends AbstractIdleService implements AetherMudStorage {

    private final DB db;
    private final MapDbAutoCommitService mapDbAutoCommitService;

    private final HTreeMap<String, Item> items;
    private final HTreeMap<String, Effect> effects;
    private final HTreeMap<String, PlayerMetadata> playerMetadataStore;

    private final static String ITEM_MAP = "itemMap";
    private final static String EFFECTS_MAP = "effectsMap";
    private final static String PLAYER_METADATA_MAP = "playerMetadata";

    public MapDBAetherMudStorage(DB db) {
        this.db = db;
        this.items = db.hashMap(ITEM_MAP)
                .keySerializer(Serializer.STRING)
                .valueSerializer(new ItemSerializer())
                .createOrOpen();

        this.effects = db.hashMap(EFFECTS_MAP)
                .keySerializer(Serializer.STRING)
                .valueSerializer(new EffectSerializer())
                .createOrOpen();

        this.playerMetadataStore = db.hashMap(PLAYER_METADATA_MAP)
                .keySerializer(Serializer.STRING)
                .valueSerializer(new PlayerMetadataSerializer())
                .createOrOpen();

        this.mapDbAutoCommitService = new MapDbAutoCommitService(db);

    }

    @Override
    public void savePlayerMetadata(PlayerMetadata playerMetadata) {
        this.playerMetadataStore.put(playerMetadata.getPlayerId(), playerMetadata);
    }

    @Override
    public Optional<PlayerMetadata> getPlayerMetadata(String playerId) {
        return Optional.ofNullable(this.playerMetadataStore.get(playerId));
    }

    @Override
    public Map<String, PlayerMetadata> getAllPlayerMetadata() {
        return playerMetadataStore;
    }

    @Override
    public void removePlayerMetadata(String playerId) {
        this.playerMetadataStore.remove(playerId);
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
