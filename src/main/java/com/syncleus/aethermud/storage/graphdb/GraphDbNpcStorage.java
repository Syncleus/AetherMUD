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

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.storage.NpcStorage;
import com.syncleus.ferma.WrappedFramedGraph;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GraphDbNpcStorage implements NpcStorage {
    private final GameManager gameManager;
    private static final Logger log = Logger.getLogger(GraphDbNpcStorage.class);
    private final WrappedFramedGraph<Graph> framedGraph;

    public GraphDbNpcStorage(GameManager gameManager, WrappedFramedGraph<Graph> framedGraph) {
        this.gameManager = gameManager;
        this.framedGraph = framedGraph;
    }

    public List<? extends NpcSpawn> getAllNpcs() {
        List<? extends NpcData> npcData = this.getNpcDatas();
        return npcData.stream()
            .map(metadata -> new NpcBuilder(metadata).setGameManager(gameManager).createNpc())
            .collect(Collectors.toList());
    }

    public List<? extends NpcData> getNpcDatas() {
        return framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), NpcData.class)).toList(NpcData.class);
    }

    public NpcData newNpcData() {
        return framedGraph.addFramedVertex(NpcData.class);
    }

    public void persist() {
        try {
            this.framedGraph.getBaseGraph().io(IoCore.graphson()).writeGraph(Main.DEFAULT_GRAPH_DB_FILE);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write to graph file.", e);
        }
    }
}

