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


import com.comandante.creeper.common.ColorizedTextTemplate;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NpcStorage {

    private final GameManager gameManager;
    private final static String LOCAL_NPC_DIRECTORY = "world/npcs/";
    private static final Logger log = Logger.getLogger(NpcStorage.class);
    private final FilebasedJsonStorage filebasedJsonStorage;
    private final List<NpcMetadata> npcMetadatas;

    public NpcStorage(GameManager gameManager, FilebasedJsonStorage filebasedJsonStorage) {
        this.gameManager = gameManager;
        this.filebasedJsonStorage = filebasedJsonStorage;
        this.npcMetadatas = readAllNpcs();
    }

    public List<Npc> getAllNpcs() {
        List<NpcMetadata> npcMetadata = readAllNpcs();
        return npcMetadata.stream()
                .map(metadata -> new NpcBuilder(metadata).setGameManager(gameManager).createNpc())
                .collect(Collectors.toList());
    }

    public void saveNpcMetadata(NpcMetadata npcMetadata) throws IOException {
        npcMetadata.setDieMessage(ColorizedTextTemplate.renderToTemplateLanguage(npcMetadata.getDieMessage()));
        npcMetadata.setColorName(ColorizedTextTemplate.renderToTemplateLanguage(npcMetadata.getColorName()));
        filebasedJsonStorage.saveMetadata(npcMetadata.getName(), LOCAL_NPC_DIRECTORY, npcMetadata);
    }

    public List<NpcMetadata> getNpcMetadatas() {
        return npcMetadatas;
    }

    private List<NpcMetadata> readAllNpcs() {
        List<NpcMetadata> npcMetadatas = filebasedJsonStorage.readAllMetadatas(LOCAL_NPC_DIRECTORY, true, new NpcMetadata());

        for (NpcMetadata npcMetadata: npcMetadatas) {
            npcMetadata.setColorName(ColorizedTextTemplate.renderFromTemplateLanguage(npcMetadata.getColorName()));
            npcMetadata.setDieMessage(ColorizedTextTemplate.renderFromTemplateLanguage(npcMetadata.getDieMessage()));
        }
        return npcMetadatas;
    }
}
