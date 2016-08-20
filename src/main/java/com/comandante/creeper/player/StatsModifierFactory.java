package com.comandante.creeper.player;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.stat.Stats;

public class StatsModifierFactory {

    private final GameManager gameManager;

    public StatsModifierFactory(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Stats getStatsModifier(Player player) {
        StatsModifier modifer = new BasicPlayerLevelStatsModifier(gameManager);
        switch (player.getPlayerClass()) {
            case WARRIOR:
                modifer = new WarriorStatsModifier(gameManager);
                break;
            case WIZARD:
                modifer = new WizardStatsModifier(gameManager);
                break;
            case RANGER:
                modifer = new RangerStatsModifier(gameManager);
                break;
            case SHAMAN:
                modifer = new ShamanStatsModifier(gameManager);
                break;
            default:
                modifer = new BasicPlayerLevelStatsModifier(gameManager);
                break;

        }
        return modifer.modify(player);
    }
}
