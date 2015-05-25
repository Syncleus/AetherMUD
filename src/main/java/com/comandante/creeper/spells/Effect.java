package com.comandante.creeper.spells;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.stat.Stats;

import java.util.List;

public abstract class Effect extends CreeperEntity {

    private final String effectName;
    private final String effectDescription;
    private final List<String> effectApplyMessages;
    private final Stats applyStats;
    private final int lifeSpanTicks;

    public Effect(String effectName, String effectDescription, List<String> effectApplyMessages, Stats applyStats, int lifeSpanTicks) {
        this.effectName = effectName;
        this.effectDescription = effectDescription;
        this.effectApplyMessages = effectApplyMessages;
        this.applyStats = applyStats;
        this.lifeSpanTicks = lifeSpanTicks;
    }

    @Override
    public void run() {

    }
}
