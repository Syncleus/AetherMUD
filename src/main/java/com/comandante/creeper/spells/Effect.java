package com.comandante.creeper.spells;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.stat.Stats;

import java.util.List;

public class Effect extends CreeperEntity {

    private final String effectName;
    private final String effectDescription;
    private final List<String> effectApplyMessages;
    private final Stats applyStatsOnTick;
    private final Stats durationStats;
    private final int lifeSpanTicks;
    private final boolean frozenMovement;
    private int ticks;
    private String playerId;

    public Effect(String effectName, String effectDescription, List<String> effectApplyMessages, Stats applyStatsOnTick, Stats durationStats, int lifeSpanTicks, boolean frozenMovement) {
        this.effectName = effectName;
        this.effectDescription = effectDescription;
        this.effectApplyMessages = effectApplyMessages;
        this.applyStatsOnTick = applyStatsOnTick;
        this.durationStats = durationStats;
        this.lifeSpanTicks = lifeSpanTicks;
        this.frozenMovement = frozenMovement;
        this.ticks = 0;
    }

    public Effect(Effect effect) {
        this.setEntityId(effect.getEntityId());
        this.effectName = effect.effectName;
        this.effectDescription = effect.effectDescription;
        this.effectApplyMessages = effect.effectApplyMessages;
        this.applyStatsOnTick = effect.applyStatsOnTick;
        this.durationStats = effect.durationStats;
        this.lifeSpanTicks = effect.lifeSpanTicks;
        this.frozenMovement = effect.frozenMovement;
        this.ticks = effect.ticks;
    }

    @Override
    public void run() {

    }

    public String getEffectName() {
        return effectName;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    public List<String> getEffectApplyMessages() {
        return effectApplyMessages;
    }

    public Stats getApplyStatsOnTick() {
        return applyStatsOnTick;
    }

    public int getLifeSpanTicks() {
        return lifeSpanTicks;
    }

    public boolean isFrozenMovement() {
        return frozenMovement;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public Stats getDurationStats() {
        return durationStats;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
