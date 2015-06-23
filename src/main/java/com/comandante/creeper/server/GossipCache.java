package com.comandante.creeper.server;

import com.comandante.creeper.managers.GameManager;
import com.google.api.client.util.Lists;
import com.google.common.collect.EvictingQueue;

import java.util.*;

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
        List<String> currentEntries = Lists.newArrayList();
        Iterator<String> iterator = evictingQueue.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            currentEntries.add(next);
        }
       Collections.reverse(currentEntries);
        int i = 0;
        for (String s: currentEntries) {
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
