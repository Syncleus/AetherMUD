package com.comandante.creeper.Items;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.stats.Stats;

import java.util.List;

public class Effect extends CreeperEntity {

    private final String effectName;
    private final String effectDescription;
    private final List<String> effectApplyMessages;
    private final Stats applyStatsOnTick;
    private final Stats durationStats;
    private final int maxEffectApplications;
    private final boolean frozenMovement;
    private int effectApplications;
    private String playerId;

    public Effect(String effectName, String effectDescription, List<String> effectApplyMessages, Stats applyStatsOnTick, Stats durationStats, int maxEffectApplications, boolean frozenMovement) {
        this.effectName = effectName;
        this.effectDescription = effectDescription;
        this.effectApplyMessages = effectApplyMessages;
        this.applyStatsOnTick = applyStatsOnTick;
        this.durationStats = durationStats;
        this.maxEffectApplications = maxEffectApplications;
        this.frozenMovement = frozenMovement;
        this.effectApplications = 0;
    }

        public Effect(Effect effect) {
            this.setEntityId(effect.getEntityId());
            this.effectName = effect.effectName;
            this.effectDescription = effect.effectDescription;
            this.effectApplyMessages = effect.effectApplyMessages;
        this.applyStatsOnTick = effect.applyStatsOnTick;
        this.durationStats = effect.durationStats;
        this.maxEffectApplications = effect.maxEffectApplications;
        this.frozenMovement = effect.frozenMovement;
        this.effectApplications = effect.effectApplications;
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

    public int getMaxEffectApplications() {
        return maxEffectApplications;
    }

    public boolean isFrozenMovement() {
        return frozenMovement;
    }

    public int getEffectApplications() {
        return effectApplications;
    }

    public void setEffectApplications(int effectApplications) {
        this.effectApplications = effectApplications;
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
