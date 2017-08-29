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

import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class PlayerDataTest {
    private static final Set<Class<?>> TEST_TYPES = new HashSet<>(Arrays.asList(new Class<?>[]{PlayerData.class}));

    @Test
    public void testPersist() {

        Graph graph = TinkerGraph.open();

        final FramedGraph fg = new DelegatingFramedGraph(graph, TEST_TYPES);

        Map<String, Long> testMap = new HashMap<>();
        testMap.put("foo", 77L);
        testMap.put("bar", 23L);

        PlayerData p1 = fg.addFramedVertex(PlayerData.class);
        p1.setName("Jeff");
        p1.setNpcKillLog(testMap);

        PlayerData jeff = fg.traverse((g) -> g.V().has("name", "Jeff")).next(PlayerData.class);

        Assert.assertTrue(PlayerData.class.isAssignableFrom(jeff.getClass()));
        Assert.assertEquals(testMap, jeff.getNpcKillLog());
        Assert.assertEquals(2, jeff.getNpcKillLog().size());
        Assert.assertEquals(2, testMap.size());
    }
}
