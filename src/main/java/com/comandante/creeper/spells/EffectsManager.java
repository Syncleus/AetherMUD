package com.comandante.creeper.spells;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsHelper;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;

public class EffectsManager {

    private final GameManager gameManager;

    public EffectsManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void applyEffectStatsOnTick(Effect effect, PlayerMetadata playerMetadata) {
        StatsHelper.combineStats(playerMetadata.getStats(), effect.getApplyStatsOnTick());
    }

    public void applyEffectStatsOnTick(Effect effect, Npc npc) {
        Player player = gameManager.getPlayerManager().getPlayer(effect.getPlayerId());
        Stats applyStats = new Stats(effect.getApplyStatsOnTick());
        if (effect.getApplyStatsOnTick().getCurrentHealth() < 0) {
            Optional<Room> npcCurrentRoom = gameManager.getRoomManager().getNpcCurrentRoom(npc);
            if (npcCurrentRoom.isPresent()) {
                Room room = npcCurrentRoom.get();
                gameManager.writeToRoom(room.getRoomId(), npc.getColorName() + " is affected by " + effect.getEffectDescription() + " " + Color.RED + applyStats.getCurrentHealth() + Color.RESET + Color.CYAN + " <" + Color.RESET + player.getPlayerName() + Color.CYAN + ">" + Color.RESET);
            }
            gameManager.updateNpcHealth(npc.getEntityId(), applyStats.getCurrentHealth(), effect.getPlayerId());
            // removing this because all health damage to an npc needs to flow through one method, i knwo its ghetto
            applyStats.setCurrentHealth(0);
        }
        StatsHelper.combineStats(npc.getStats(), applyStats);
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
