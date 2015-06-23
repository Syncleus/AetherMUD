package com.comandante.creeper.server;

import com.comandante.creeper.managers.GameManager;
import com.google.api.client.util.Lists;
import com.google.common.collect.EvictingQueue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GossipCache {

    private final GameManager gameManager;
    private final EvictingQueue<String> evictingQueue;

    public GossipCache(GameManager gameManager) {
        this.gameManager = gameManager;
        this.evictingQueue = EvictingQueue.create(gameManager.getCreeperConfiguration().maxGossipCacheSize);
    }

    public synchronized void addGossipLine(String line) {
        evictingQueue.add(line);
    }

    public List<String> getRecent(int size) {
        List<String> recent = Lists.newArrayList();
        Collections.reverse(Lists.newArrayList(recent));
        Iterator<String> iterator = evictingQueue.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i < size) {
                String next = iterator.next();
                recent.add(next);
                i++;
            } else {
                break;
            }
        }
        return recent;
    }
}
