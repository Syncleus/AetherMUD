package com.comandante.creeper.spells;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.List;
import java.util.Random;
import java.util.Set;


public abstract class Spell {

    private final String spellName;
    private final String spellDescription;
    private final List<String> attackMessages;
    private final Stats attackStats;
    private final int manaCost;
    private final Set<String> validTriggers;
    private final static Random random = new Random();
    private final GameManager gameManager;

    public Spell(GameManager gameManager, Set<String> validTriggers, int manaCost, Stats attackStats, List<String> attackMessages, String spellDescription, String spellName) {
        this.gameManager = gameManager;
        this.validTriggers = validTriggers;
        this.manaCost = manaCost;
        this.attackStats = attackStats;
        this.attackMessages = attackMessages;
        this.spellDescription = spellDescription;
        this.spellName = spellName;
    }

    private int getSpellAttack(Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= attackStats.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + randInt(attackStats.getWeaponRatingMin(), attackStats.getWeaponRatingMax());
        }
        int i = attackStats.getStrength() + totDamage - victim.getArmorRating();
        if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }

    public String getAttackMessage(int amt) {
        int i = random.nextInt(attackMessages.size());
        String s = attackMessages.get(i);
        return Color.YELLOW + "+" + amt + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE " + Color.RESET + s;
    }

    public void attackSpell(Set<String> npcIds, Player player) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            int availableMana = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getStats().getCurrentMana();
            if (availableMana < manaCost) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Not enough mana!" + "\r\n");
            } else {
                for (String npcId: npcIds) {
                    Npc npc = gameManager.getEntityManager().getNpcEntity(npcId);
                    gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + Color.CYAN + " casts " + Color.RESET + "a " + Color.BOLD_ON + Color.WHITE + "[" + Color.RESET + spellName + Color.BOLD_ON + Color.WHITE + "]" + Color.RESET + " on " + npc.getColorName() + "! \r\n");
                    int spellAttack = getSpellAttack(npc.getStats());
                    gameManager.getChannelUtils().write(player.getPlayerId(), getAttackMessage(spellAttack));
                    gameManager.updateNpcHealth(npc.getEntityId(), -spellAttack, player.getPlayerId());
                }
                gameManager.getPlayerManager().updatePlayerMana(player, -manaCost);
            }
        }
    }

    private static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public String getSpellName() {
        return spellName;
    }

    public String getSpellDescription() {
        return spellDescription;
    }

    public List<String> getAttackMessages() {
        return attackMessages;
    }

    public Stats getAttackStats() {
        return attackStats;
    }

    public int getManaCost() {
        return manaCost;
    }

    public Set<String> getValidTriggers() {
        return validTriggers;
    }

    public static Random getRandom() {
        return random;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
