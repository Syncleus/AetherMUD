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
package com.syncleus.aethermud.spells;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.EffectBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.CoolDownPojo;
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.stats.StatsBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Optional;

import static com.syncleus.aethermud.server.communication.Color.BOLD_ON;

public class LightningSpell implements SpellRunnable {

    public final static String name = BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + " bolt";
    public final static String description = "A powerful bolt of lightning that leaves its victim with a burn effect.";

    private final static int manaCost = 60;

    private final GameManager gameManager;

    public LightningSpell(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run(Player sourcePlayer, Optional<NpcSpawn> destinationNpc, Optional<Player> destinationPlayer, GameManager gameManager) {
        long availableMana = sourcePlayer.getPlayerStatsWithEquipmentAndLevel().getCurrentMana();
        if (availableMana < manaCost) {
            sourcePlayer.writeMessage("Not enough mana!" + "\r\n");
            return;
        }
        if (destinationNpc.isPresent()) {
            executeSpellAgainstNpc(sourcePlayer, destinationNpc.get());
            sourcePlayer.updatePlayerMana(-manaCost);
            sourcePlayer.addCoolDown(new CoolDownPojo(getName(), 5, CoolDownType.SPELL));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private void executeSpellAgainstNpc(Player player, NpcSpawn npcSpawn) {
        announceSpellCastToCurrentRoom(player, npcSpawn.getColorName());
        Stats stats = player.getPlayerStatsWithEquipmentAndLevel();
        int power = (player.getLevel() * 1) + (3 * stats.getIntelligence());
        player.addActiveFight(npcSpawn);
        gameManager.getEffectsManager().applyEffectsToNpcs(player, Sets.newHashSet(npcSpawn), Sets.newHashSet(selectEffect(stats).createEffect()));
        npcSpawn.doHealthDamage(player, Arrays.asList(getDamageMessage(power, npcSpawn.getColorName())), -power);
    }

    private EffectBuilder selectEffect(Stats stats) {
        if (Math.random() < 0.1) {
            int electrofiedPower = (int) ((stats.getLevel() * .3) + (5 * stats.getIntelligence()));
            return getElectrofried(electrofiedPower, 4);
          }
        int burnEffectPower = (int) ((stats.getLevel() * .05) + (1 * stats.getIntelligence()));
        return getBurnEffect(burnEffectPower, 2);
    }

    private void executeSpellAgainstPlayer(Player player, Player destinationPlayer) {

    }

    private String getAttackMessage() {
        return "a broad stroke of " + BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + " bolts across the sky";
    }

    private String getDamageMessage(long amt, String name) {
        return Color.BOLD_ON + Color.YELLOW + "[spell] " + Color.RESET + Color.YELLOW + "+" + amt + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE " + Color.RESET + getAttackMessage() + Color.BOLD_ON + Color.RED + " >>>> " + Color.RESET + name;
    }

    private void announceSpellCastToCurrentRoom(Player player, String name) {
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + Color.CYAN + " casts " + Color.RESET + "a " + Color.BOLD_ON + Color.WHITE + "[" + Color.RESET + getName() + Color.BOLD_ON + Color.WHITE + "]" + Color.RESET + " on " + name + "! \r\n");
    }

    private EffectBuilder getBurnEffect(int amt, int ticksDuration) {
        return new EffectBuilder()
                .setEffectApplyMessages(Lists.newArrayList("You are " + Color.BOLD_ON + Color.RED + "burning" + Color.RESET + " from the lightning strike!"))
                .setEffectDescription(Color.BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + Color.BOLD_ON + Color.RED + " BURN" + Color.RESET)
                .setEffectName(Color.BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + Color.BOLD_ON + Color.RED + " BURN" + Color.RESET)
                .setDurationStats(new StatsBuilder().createStats())
                .setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-amt).createStats())
                .setFrozenMovement(false)
                .setLifeSpanTicks(ticksDuration);
    }

    private EffectBuilder getElectrofried(int amt, int ticksDuration) {
        return new EffectBuilder()
                .setEffectApplyMessages(Lists.newArrayList("You are " + Color.BOLD_ON + Color.YELLOW + "ELECTROFIED" + Color.RESET + " from the lightning strike!"))
                .setEffectDescription(Color.BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + Color.BOLD_ON + Color.YELLOW + " ELECTROFIED" + Color.RESET)
                .setEffectName(Color.BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + Color.BOLD_ON + Color.YELLOW + " ELECTROFIED" + Color.RESET)
                .setDurationStats(new StatsBuilder().createStats())
                .setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-amt).createStats())
                .setFrozenMovement(false)
                .setLifeSpanTicks(ticksDuration);
    }
}
