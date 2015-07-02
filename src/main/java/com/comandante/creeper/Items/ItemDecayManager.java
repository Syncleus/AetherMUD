package com.comandante.creeper.Items;

import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.SentryManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemDecayManager extends CreeperEntity {

    private final ConcurrentHashMap<String, DecayProgress> itemDecayTracker = new ConcurrentHashMap<String, DecayProgress>();
    private final EntityManager entityManager;
    private static final Logger log = Logger.getLogger(ItemDecayManager.class);
    private int tickBucket = 0;

    public ItemDecayManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addItem(Item item) {
        item.setWithPlayer(false);
        itemDecayTracker.put(item.getItemId(), new DecayProgress(item.getItemHalfLifeTicks()));
    }

    public void removeItem(String itemId) {
        itemDecayTracker.remove(itemId);
    }

    public ConcurrentHashMap<String, DecayProgress> getItemDecayTracker() {
        return itemDecayTracker;
    }

    @Override
    public void run() {
        try {
            if (tickBucket == 10) {
                List<String> itemsToRemoveFromDecay = new ArrayList<>();
                List<String> itemsToDestroy = new ArrayList<>();
                ConcurrentHashMap<String, DecayProgress> itemDecayTracker1 = getItemDecayTracker();
                for (Map.Entry<String, DecayProgress> next : itemDecayTracker1.entrySet()) {
                    DecayProgress decayProgress = next.getValue();
                    Item item = entityManager.getItemEntity(next.getKey());
                    if (item == null) {
                        removeItem(next.getKey());
                        continue;
                    }
                    if (item.isWithPlayer()) {
                        itemsToRemoveFromDecay.add(item.getItemId());
                        continue;
                    }
                    decayProgress.incTick();
                    if (decayProgress.getCurrentTicks() >= decayProgress.getNumberOfTicks()) {
                        itemsToDestroy.add(item.getItemId());
                    }
                }
                for (String itemId : itemsToRemoveFromDecay) {
                    removeItem(itemId);
                }
                for (String itemId : itemsToDestroy) {
                    removeItem(itemId);
                    entityManager.removeItem(itemId);
                }
                tickBucket = 0;
            } else {
                tickBucket = tickBucket + 1;
            }
        } catch (Exception e) {
            log.error("Problem with item decay manager!", e);
            SentryManager.logSentry(this.getClass(), e, "Exception caught in item decay manager!");
        }
    }
}

class DecayProgress {
    private final int numberOfTicks;
    private int currentTicks = 0;

    DecayProgress(int numberOfTicks) {
        this.numberOfTicks = numberOfTicks;
    }

    public void incTick() {
        this.currentTicks++;
    }

    public int getNumberOfTicks() {
        return numberOfTicks;
    }

    public int getCurrentTicks() {
        return currentTicks;
    }
}

