/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.items;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcStatsChange;
import com.syncleus.aethermud.npc.NpcStatsChangeBuilder;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.stats.StatsPojo;
import com.syncleus.aethermud.storage.graphdb.PlayerData;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.stats.StatsBuilder;
import com.syncleus.aethermud.stats.StatsHelper;
import org.apache.log4j.Logger;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class EffectsManager {

    private final GameManager gameManager;

    private static final Logger log = Logger.getLogger(EffectsManager.class);

    public EffectsManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void applyEffectsToNpcs(Player player, Set<NpcSpawn> npcSpawns, Set<Effect> effects) {
        effects.forEach(effect ->
                npcSpawns.forEach(npc -> {
                    Effect nEffect = new Effect(effect);
                    nEffect.setPlayerId(player.getPlayerId());
                    if (effect.getDurationStats().getCurrentHealth() < 0) {
                        log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                        return;
                    }
                    StatsHelper.combineStats(npc.getStats(), effect.getDurationStats());
                    npc.addEffect(nEffect);
                }));
    }

    public void applyEffectsToPlayer(Player destinationPlayer, Player player, Set<Effect> effects) {
        for (Effect effect : effects) {
            Effect nEffect = new Effect(effect);
            nEffect.setPlayerId(player.getPlayerId());
            if (effect.getDurationStats().getCurrentHealth() < 0) {
                log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                continue;
            }
            String effectApplyMessage;
            if (destinationPlayer.addEffect(effect)) {
                effectApplyMessage = Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + nEffect.getEffectName() + " applied!" + "\r\n";
                gameManager.getChannelUtils().write(destinationPlayer.getPlayerId(), effectApplyMessage);
            } else {
                effectApplyMessage = Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + Color.RED + "Unable to apply " + nEffect.getEffectName() + "!" + "\r\n";
                gameManager.getChannelUtils().write(player.getPlayerId(), effectApplyMessage);
            }
        }
    }

    public void application(Effect effect, Player player) {
        // if there are effecst that modify player health, deal with it here, you can't rely on combine stats.
        Stats applyStatsOnTick = effect.getApplyStatsOnTick();
        if (effect.getApplyStatsOnTick() != null) {
            if (effect.getApplyStatsOnTick().getCurrentHealth() != 0) {
                gameManager.getPlayerManager().getPlayer(player.getPlayerId()).updatePlayerHealth(effect.getApplyStatsOnTick().getCurrentHealth(), null);
                for (String message : effect.getEffectApplyMessages()) {
                    if (effect.getApplyStatsOnTick().getCurrentHealth() > 0) {
                        gameManager.getChannelUtils().write(player.getPlayerId(), Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + message + " +" + Color.GREEN + NumberFormat.getNumberInstance(Locale.US).format(effect.getApplyStatsOnTick().getCurrentHealth()) + Color.RESET + "\r\n", true);
                    } else {
                        gameManager.getChannelUtils().write(player.getPlayerId(), Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + message + " -" + Color.RED + NumberFormat.getNumberInstance(Locale.US).format(effect.getApplyStatsOnTick().getCurrentHealth()) + Color.RESET + "\r\n", true);
                    }
                }
                //applyStatsOnTick = new StatsBuilder(applyStatsOnTick).setCurrentHealth(0).createStats();
            }
            //  StatsHelper.combineStats(playerMetadata.getStats(), applyStatsOnTick);
        }
    }

    public void application(Effect effect, NpcSpawn npcSpawn) {
        Player player = gameManager.getPlayerManager().getPlayer(effect.getPlayerId());
        Stats applyStats = new StatsPojo(effect.getApplyStatsOnTick());
        // if there are effecst that modify npc health, deal with it here, you can't rely on combine stats.
        if (effect.getApplyStatsOnTick().getCurrentHealth() < 0) {
            String s = Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + npcSpawn.getColorName() + " is affected by " + effect.getEffectDescription() + " " + Color.RED + applyStats.getCurrentHealth() + Color.RESET + Color.CYAN + Color.RESET;
            NpcStatsChange npcStatsChange = new NpcStatsChangeBuilder()
                    .setStats(applyStats)
                    .setDamageStrings(Arrays.asList(s))
                    .setPlayer(player)
                    .createNpcStatsChange();
            npcSpawn.addNpcDamage(npcStatsChange);
        }
        // Remove any health mods, as it will screw things up.
        Stats finalCombineWorthyStats = new StatsBuilder(applyStats).setCurrentHealth(0).createStats();
        StatsHelper.combineStats(npcSpawn.getStats(), finalCombineWorthyStats);
    }

    public void removeDurationStats(Effect effect, NpcSpawn npcSpawn) {
        Stats newStats = new StatsPojo(effect.getDurationStats());
        StatsHelper.inverseStats(newStats);
        StatsHelper.combineStats(npcSpawn.getStats(), newStats);
    }

    public void removeDurationStats(Effect effect, PlayerData playerData) {
        Stats newStats = new StatsPojo(effect.getDurationStats());
        StatsHelper.inverseStats(newStats);
        StatsHelper.combineStats(playerData.getStats(), newStats);
    }
}
