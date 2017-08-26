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
package com.syncleus.aethermud.storage;


import com.google.common.util.concurrent.AbstractScheduledService;
import org.mapdb.DB;

import java.util.concurrent.TimeUnit;

public class MapDbAutoCommitService extends AbstractScheduledService {

    private final DB db;

    public MapDbAutoCommitService(DB db) {
        this.db = db;
    }

    @Override
    protected void runOneIteration() throws Exception {
        db.commit();
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(30, 30, TimeUnit.SECONDS);
    }
}
