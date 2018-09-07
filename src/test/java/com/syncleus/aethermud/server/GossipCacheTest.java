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
package com.syncleus.aethermud.server;

import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.server.communication.GossipCache;
import com.google.api.client.util.Maps;
import org.apache.commons.configuration.MapConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GossipCacheTest {

    GossipCache gossipCache;

    @Before
    public void setUp() throws Exception {
        GameManager mock = mock(GameManager.class);
        HashMap<String, Object> configuration = Maps.newHashMap();
        configuration.put("max.gossip.cache.size", 100);
        AetherMudConfiguration aetherMudConfiguration = new AetherMudConfiguration(new MapConfiguration(configuration));
        when(mock.getAetherMudConfiguration()).thenReturn(aetherMudConfiguration);
        this.gossipCache = new GossipCache(mock);
    }

    @Test
    public void testRecentGossip() throws Exception {
        for (int i = 0; i < 20; i++) {
            gossipCache.addGossipLine(String.valueOf(i));
        }
        List<String> recent = gossipCache.getRecent(20);
    }

}
