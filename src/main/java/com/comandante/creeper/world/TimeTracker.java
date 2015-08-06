package com.comandante.creeper.world;

import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;

import java.util.Iterator;
import java.util.Map;

public class TimeTracker extends CreeperEntity {

    private static final int FULL_DAYS_WORTH_OF_TICKS = 28800;
    private int currentTick = 0;
    private TimeOfDay currentTimeOfDay = TimeOfDay.MORNING;
    private final GameManager gameManager;

    public TimeTracker(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (currentTick >= FULL_DAYS_WORTH_OF_TICKS) {
            currentTick = 0;
        }
        incrementTick();
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

    private void incrementTick() {
        currentTick = currentTick + 1;
    }

    public TimeOfDay getTimeOfDay() {
        return currentTimeOfDay;
    }

    public TimeOfDay determineTimeOfDay() {
        if (currentTick <= (FULL_DAYS_WORTH_OF_TICKS * .25)) {
            return TimeOfDay.MORNING;
        } else if (currentTick <= (FULL_DAYS_WORTH_OF_TICKS * .50)) {
            return TimeOfDay.AFTERNOON;
        } else if (currentTick <= (FULL_DAYS_WORTH_OF_TICKS * .75)) {
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
}
