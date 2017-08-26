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
package com.syncleus.aethermud.server.communication;

import com.syncleus.aethermud.core.GameManager;
import com.google.api.client.util.Lists;
import com.google.common.collect.EvictingQueue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GossipCache {

    private final GameManager gameManager;
    private final EvictingQueue<String> evictingQueue;

    public GossipCache(GameManager gameManager) {
        this.gameManager = gameManager;
        this.evictingQueue = EvictingQueue.create(gameManager.getAetherMudConfiguration().maxGossipCacheSize);
    }

    public synchronized void addGossipLine(String line) {
        evictingQueue.add(line);
    }

    public List<String> getRecent(int size) {
        List<String> recent = Lists.newArrayList();
        List<String> currentEntries = evictingQueue.stream().collect(Collectors.toList());

        Collections.reverse(currentEntries);
        int i = 0;
        for (String s : currentEntries) {
            if (i < size) {
                recent.add(s);
                i++;
            } else {
                break;
            }
        }
        Collections.reverse(recent);
        return recent;
    }
}
