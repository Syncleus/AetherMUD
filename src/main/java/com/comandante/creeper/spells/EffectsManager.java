package com.comandante.creeper.spells;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcStatsChange;
import com.comandante.creeper.npc.NpcStatsChangeBuilder;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.stat.StatsHelper;

import java.util.Arrays;

public class EffectsManager {

    private final GameManager gameManager;

    public EffectsManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void application(Effect effect, PlayerMetadata playerMetadata) {
        // if there are effecst that modify player health, deal with it here, you can't rely on combine stats.
        if (effect.getApplyStatsOnTick() != null) {
            StatsHelper.combineStats(playerMetadata.getStats(), effect.getApplyStatsOnTick());
        }
    }

    public void application(Effect effect, Npc npc) {
        Player player = gameManager.getPlayerManager().getPlayer(effect.getPlayerId());
        Stats applyStats = new Stats(effect.getApplyStatsOnTick());
        // if there are effecst that modify npc health, deal with it here, you can't rely on combine stats.
        if (effect.getApplyStatsOnTick().getCurrentHealth() < 0) {
            String s = Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET +  npc.getColorName() + " is affected by " + effect.getEffectDescription() + " " + Color.RED + applyStats.getCurrentHealth() + Color.RESET + Color.CYAN + Color.RESET;
            NpcStatsChange npcStatsChange = new NpcStatsChangeBuilder()
                    .setStats(applyStats)
                    .setDamageStrings(Arrays.asList(s))
                    .setPlayer(player)
                    .createNpcStatsChange();
            npc.addNpcDamage(npcStatsChange);
        }
        // Remove any health mods, as it will screw things up.
        Stats finalCombineWorthyStats = new StatsBuilder(applyStats).setCurrentHealth(0).createStats();
        StatsHelper.combineStats(npc.getStats(), finalCombineWorthyStats);
    }

    public void removeDurationStats(Effect effect, Npc npc) {
        Stats newStats = new Stats(effect.getDurationStats());
        StatsHelper.inverseStats(newStats);
        StatsHelper.combineStats(npc.getStats(), newStats);
    }

    public void removeDurationStats(Effect effect, PlayerMetadata playerMetadata) {
        Stats newStats = new Stats(effect.getDurationStats());
        StatsHelper.inverseStats(newStats);
        StatsHelper.combineStats(playerMetadata.getStats(), newStats);
    }
}
