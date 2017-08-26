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
package com.syncleus.aethermud.core.service;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.server.communication.Color;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeTracker extends AetherMudEntity {

    private static final int NUMBER_OF_MILLISECONDS_IN_A_DAY = 86400000;
    private int currentTick = 0;
    private TimeOfDay currentTimeOfDay = TimeOfDay.MORNING;
    private final GameManager gameManager;

    public TimeTracker(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
             TimeOfDay timeOfDay = determineTimeOfDay();
             if (timeOfDay != currentTimeOfDay) {
                 announceChange(timeOfDay);
             }
             currentTimeOfDay = timeOfDay;
    }

    private void announceChange(TimeOfDay timeOfDay) {
        Iterator<Map.Entry<String, Player>> players = gameManager.getPlayerManager().getPlayers();
        while (players.hasNext()) {
            Map.Entry<String, Player> next = players.next();
            gameManager.getChannelUtils().write(next.getValue().getPlayerId(), "It is now " + timeOfDay.color + timeOfDay + Color.RESET + ".\r\n", true);
        }
    }

    public TimeOfDay getTimeOfDay() {
        return currentTimeOfDay;
    }

    private TimeOfDay determineTimeOfDay() {
        long milliSecondsSinceMidnight = milliSecondsSinceMidnight();
        if (milliSecondsSinceMidnight <= TimeUnit.HOURS.toMillis(5)) {
            return TimeOfDay.NIGHT;
        } else if (milliSecondsSinceMidnight <= TimeUnit.HOURS.toMillis(12)) {
            return TimeOfDay.MORNING;
        } else if (milliSecondsSinceMidnight <= TimeUnit.HOURS.toMillis(16)) {
            return TimeOfDay.AFTERNOON;
        } else if (milliSecondsSinceMidnight <= TimeUnit.HOURS.toMillis(20)) {
            return TimeOfDay.EVENING;
        } else {
            return TimeOfDay.NIGHT;
        }
    }

    public enum TimeOfDay {
        MORNING(Color.YELLOW),
        AFTERNOON(Color.GREEN),
        EVENING(Color.RED),
        NIGHT(Color.CYAN);

        public String color;

        TimeOfDay(String color) {
            this.color = color;
        }
    }

    private long milliSecondsSinceMidnight() {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long passed = now - c.getTimeInMillis();
        return passed;
    }
}
