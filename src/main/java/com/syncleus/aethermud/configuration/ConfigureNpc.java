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
package com.syncleus.aethermud.configuration;


import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.Forage;
import com.syncleus.aethermud.items.ItemMetadata;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.spawner.ItemSpawner;
import com.syncleus.aethermud.spawner.NpcSpawner;
import com.syncleus.aethermud.spawner.SpawnRule;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ConfigureNpc {

    public static void configureAllNpcs(GameManager gameManager) throws IOException {
        EntityManager entityManager = gameManager.getEntityManager();
        List<? extends NpcSpawn> npcsFromFile = gameManager.getNpcStorage().getAllNpcs();
        for (NpcSpawn npcSpawn : npcsFromFile) {
            Main.startUpMessage("Adding npc spawn: " + npcSpawn.getName());
            entityManager.addEntity(npcSpawn);
            Set<SpawnRule> spawnRules = npcSpawn.getSpawnRules();
            for (SpawnRule spawnRule : spawnRules) {
                entityManager.addEntity(new NpcSpawner(npcSpawn, gameManager, spawnRule));
            }
        }
    }

    public static void configure(EntityManager entityManager, GameManager gameManager) throws IOException {

        configureAllNpcs(gameManager);

        List<ItemMetadata> allItemMetadata = gameManager.getItemStorage().getItemMetadatas();

        for (ItemMetadata itemMetadata : allItemMetadata) {
            for (SpawnRule spawnRule : itemMetadata.getSpawnRules()) {
                Main.startUpMessage("Adding item spawn: " + itemMetadata.getInternalItemName());
                ItemSpawner itemSpawner = new ItemSpawner(itemMetadata, spawnRule, gameManager);
                entityManager.addEntity(itemSpawner);
            }
        }

        for (ItemMetadata itemMetadata : allItemMetadata) {
            for (Forage forage : itemMetadata.getForages()) {
                Main.startUpMessage("Adding forage: " + itemMetadata.getInternalItemName());
                gameManager.getForageManager().addForage(itemMetadata.getInternalItemName(), forage);
            }
        }

        List<Merchant> allMerchantMetadatas = gameManager.getMerchantStorage().getAllMerchants();
        for (Merchant merchant : allMerchantMetadatas) {
            Main.startUpMessage("Adding merchant: " + merchant.getInternalName());
            gameManager.getRoomManager().addMerchant(merchant);
        }

    }
}
