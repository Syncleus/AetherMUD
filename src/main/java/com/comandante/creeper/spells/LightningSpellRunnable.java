package com.comandante.creeper.spells;

import com.comandante.creeper.CreeperUtils;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.CoolDown;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.comandante.creeper.server.Color.BOLD_ON;
import static com.comandante.creeper.spells.Spells.getSpellAttack;

public class LightningSpellRunnable implements ExecuteSpellRunnable {

    private final GameManager gameManager;
    private final int manaCost = 60;
    public final static String name = BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + " bolt";
    private final static String description = "A powerful bolt of lightning.";

    public LightningSpellRunnable(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run(Player sourcePlayer, Optional<Npc> destinationNpc, Optional<Player> destinationPlayer, GameManager gameManager) {
        long availableMana = sourcePlayer.getPlayerStatsWithEquipmentAndLevel().getCurrentMana();
        if (availableMana < manaCost) {
            sourcePlayer.writeMessage("Not enough mana!" + "\r\n");
            return;
        }
        if (destinationNpc.isPresent()) {
            executeSpellAgainstNpc(sourcePlayer, destinationNpc.get());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private void executeSpellAgainstNpc(Player player, Npc npc) {
        long intelligence = player.getPlayerStatsWithEquipmentAndLevel().getIntelligence();
        long power = (player.getLevel() * 1) + (3 * intelligence);
        player.addActiveFight(npc);
        npc.doHealthDamage(player, Arrays.asList("LightingBoltAttack!! damage amount: " + power), -power);
    }

    private void executeSpellAgainstPlayer(Player player, Player destinationPlayer) {

    }
}
