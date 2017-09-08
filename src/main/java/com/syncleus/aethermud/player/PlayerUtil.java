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


import com.google.common.base.Function;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;

import java.util.Optional;
import java.util.function.Consumer;

public final class PlayerUtil {
    private PlayerUtil() {
    }

    public static <T> T transact(GameManager manager, String playerId, Function<PlayerData, T> func) {
        try( GraphStorageFactory.AetherMudTx tx = manager.getGraphStorageFactory().beginTransaction() ) {
            Optional<PlayerData> playerDataOptional = tx.getStorage().getPlayerMetadata(playerId);
            if (!playerDataOptional.isPresent())
                throw new IllegalStateException("Player exists in memory but not in the database");
            PlayerData playerData = playerDataOptional.get();

            T retVal = func.apply(playerData);
            tx.success();
            return retVal;
        }
    }

    public static void consume(GameManager manager, String playerId, Consumer<PlayerData> func) {
        try( GraphStorageFactory.AetherMudTx tx = manager.getGraphStorageFactory().beginTransaction() ) {
            Optional<PlayerData> playerDataOptional = tx.getStorage().getPlayerMetadata(playerId);
            if (!playerDataOptional.isPresent())
                throw new IllegalStateException("Player exists in memory but not in the database");
            PlayerData playerData = playerDataOptional.get();

            func.accept(playerData);
            tx.success();
        }
    }

    public static <T> T transactRead(GameManager manager, String playerId, Function<PlayerData, T> func) {
        try( GraphStorageFactory.AetherMudTx tx = manager.getGraphStorageFactory().beginTransaction() ) {
            Optional<PlayerData> playerDataOptional = tx.getStorage().getPlayerMetadata(playerId);
            if (!playerDataOptional.isPresent())
                throw new IllegalStateException("Player exists in memory but not in the database");
            PlayerData playerData = playerDataOptional.get();

            return func.apply(playerData);
        }
    }

    public static void consumeRead(GameManager manager, String playerId, Consumer<PlayerData> func) {
        try( GraphStorageFactory.AetherMudTx tx = manager.getGraphStorageFactory().beginTransaction() ) {
            Optional<PlayerData> playerDataOptional = tx.getStorage().getPlayerMetadata(playerId);
            if (!playerDataOptional.isPresent())
                throw new IllegalStateException("Player exists in memory but not in the database");
            PlayerData playerData = playerDataOptional.get();

            func.accept(playerData);
        }
    }
}
