package com.comandante.creeper.npc;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.room.Area;
import com.comandante.creeper.stat.Stats;
import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.comandante.creeper.server.Color.RED;
import static com.comandante.creeper.server.Color.RESET;


public abstract class Npc extends CreeperEntity {

    public void setLastPhraseTimestamp(long lastPhraseTimestamp) {
        this.lastPhraseTimestamp = lastPhraseTimestamp;
    }

    private long lastPhraseTimestamp;
    private final GameManager gameManager;
    private Integer roomId;
    private final String name;
    private final String colorName;
    private final Stats stats;
    private final String dieMessage;
    private final Optional<HashSet<Area>> roamAreas;
    private AtomicBoolean isInFight = new AtomicBoolean(false);
    Random random = new Random();

    public abstract Npc create(GameManager gameManager, Integer roomId);

    @Override
    public void run() {
        if (randInt(0, 100) < 2) {
            if (!isInFight.get() && roamAreas.isPresent()) {
                System.out.println("roam!" + getEntityId());
                NpcMover npcMover = new NpcMover();
                npcMover.roam(getGameManager(), getEntityId());
            }
        }
    }

    public String getColorName() {
        return colorName;
    }

    protected Npc(GameManager gameManager, Integer roomId, String name, String colorName, long lastPhraseTimestamp, Stats stats, String dieMessage, Optional<HashSet<Area>> roamAreas) {
        this.gameManager = gameManager;
        this.roomId = roomId;
        this.name = name;
        this.colorName = colorName;
        this.lastPhraseTimestamp = lastPhraseTimestamp;
        this.stats = stats;
        this.dieMessage = dieMessage;
        this.roamAreas = roamAreas;
    }

    public Optional<HashSet<Area>> getRoamAreas() {
        return roamAreas;
    }

    public boolean getIsInFight() {
        return this.isInFight.get();
    }

    public void setIsInFight(boolean isInFight) {
        this.isInFight.set(isInFight);
    }

    public Stats getStats() {
        return stats;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public long getLastPhraseTimestamp() {
        return lastPhraseTimestamp;
    }

    public String getName() {
        return name;
    }

    public String getDieMessage() {
        return dieMessage;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public void npcSay(Integer roomId, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(RED);
        sb.append(name).append(": ").append(message);
        sb.append(RESET);
    }

    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

}
