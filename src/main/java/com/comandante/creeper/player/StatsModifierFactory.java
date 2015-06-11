package com.comandante.creeper.player;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.stat.Stats;

public class StatsModifierFactory {

    private final GameManager gameManager;

    public StatsModifierFactory(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Stats getStatsModifier(Player player) {
        BasicPlayerLevelStatsModifier basicPlayerLevelStatsModifier = new BasicPlayerLevelStatsModifier(gameManager);
        return basicPlayerLevelStatsModifier.modify(player);
    }
}
