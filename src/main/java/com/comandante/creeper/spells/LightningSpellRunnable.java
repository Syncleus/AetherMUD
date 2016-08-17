package com.comandante.creeper.spells;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.CoolDown;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.SpellRunnable;

import java.util.Arrays;
import java.util.Optional;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class LightningSpellRunnable implements SpellRunnable {

    public final static String name = BOLD_ON + Color.YELLOW + "lightning" + Color.RESET + " bolt";
    public final static String description = "A powerful bolt of lightning.";

    private final static int manaCost = 60;

    private final GameManager gameManager;

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
            sourcePlayer.updatePlayerMana(-manaCost);
            sourcePlayer.addCoolDown(new CoolDown(getName(), 5, CoolDownType.SPELL));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    private void executeSpellAgainstNpc(Player player, Npc npc) {
        announceSpellCastToCurrentRoom(player, npc.getColorName());
        long intelligence = player.getPlayerStatsWithEquipmentAndLevel().getIntelligence();
        long power = (player.getLevel() * 1) + (3 * intelligence);
        player.addActiveFight(npc);
        npc.doHealthDamage(player, Arrays.asList(getDamageMessage(power, npc.getColorName())), -power);
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
}
