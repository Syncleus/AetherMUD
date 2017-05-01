package com.comandante.creeper.core_game.service;

import com.google.api.client.util.Lists;
import com.google.common.util.concurrent.AbstractScheduledService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedEventProcessor extends AbstractScheduledService {

    private final ArrayBlockingQueue<CreeperEvent> creeperEventQueue;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static final Logger log = Logger.getLogger(MultiThreadedEventProcessor.class);

    public MultiThreadedEventProcessor(ArrayBlockingQueue<CreeperEvent> creeperEventQueue) {
        this.creeperEventQueue = creeperEventQueue;
    }

    @Override
    protected void runOneIteration() throws Exception {
        ArrayList<CreeperEvent> events = Lists.newArrayList();
        creeperEventQueue.drainTo(events);
        for (CreeperEvent event: events) {
            executorService.submit(() -> safeRun(event));
        }
    }

    public void addEvent(CreeperEvent event) {
        try {
            creeperEventQueue.put(event);
        } catch (InterruptedException ex) {
            log.error("Problem adding event.", ex);
        }
    }

    private void safeRun(final CreeperEvent e) {
        try {
            e.run();
        } catch (Exception ex) {
            log.error("Problem executing event.", ex);
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 10, TimeUnit.MILLISECONDS);
    }
}
