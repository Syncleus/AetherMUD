package com.comandante.creeper;


import com.google.common.util.concurrent.AbstractScheduledService;
import org.mapdb.DB;

import java.util.concurrent.TimeUnit;

public class MapDbAutoCommitService extends AbstractScheduledService {

    private final DB db;
    private int bucket = 0;

    public MapDbAutoCommitService(DB db) {
        this.db = db;
    }

    @Override
    protected void runOneIteration() throws Exception {
        if (bucket == 960) {
            db.compact();
            bucket = 0;
        } else {
            bucket = bucket + 1;
        }
        db.commit();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(30, 30, TimeUnit.SECONDS);
    }
}
