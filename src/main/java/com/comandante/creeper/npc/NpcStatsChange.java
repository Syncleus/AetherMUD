package com.comandante.creeper.npc;

import com.comandante.creeper.player.Player;
import com.comandante.creeper.stat.Stats;

import java.util.List;

public class NpcStatsChange {

    private final Stats stats;
    private final List<String> damageStrings;
    private final List<String> playerDamageStrings;
    private final Player player;
    private final Stats playerStatsChange;

    public NpcStatsChange(Stats stats, List<String> damageStrings, Player player, Stats playerStatsChange, List<String> playerDamageStrings) {
        this.stats = stats;
        this.damageStrings = damageStrings;
        this.player = player;
        this.playerStatsChange = playerStatsChange;
        this.playerDamageStrings = playerDamageStrings;
    }

    public Stats getStats() {
        return stats;
    }

    public List<String> getDamageStrings() {
        return damageStrings;
    }

    public Player getPlayer() {
        return player;
    }

    public Stats getPlayerStatsChange() {
        return playerStatsChange;
    }

    public List<String> getPlayerDamageStrings() {
        return playerDamageStrings;
    }
}
