package com.comandante.creeper.spells;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.CoolDown;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsHelper;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.apache.log4j.Logger;

import java.util.Arrays;
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
    private Set<Effect> effects;
    private final boolean isAreaSpell;
    private final SpellExecute spellExecute;
    private final int coolDownTicks;

    private static final Logger log = Logger.getLogger(Spell.class);


    public Spell(GameManager gameManager,
                 Set<String> validTriggers,
                 int manaCost,
                 Stats attackStats,
                 List<String> attackMessages,
                 String spellDescription,
                 String spellName,
                 Set<Effect> effects,
                 boolean isAreaSpell,
                 SpellExecute spellExecute,
                 int coolDownTicks) {
        this.gameManager = gameManager;
        this.validTriggers = validTriggers;
        this.manaCost = manaCost;
        this.attackStats = attackStats;
        this.attackMessages = attackMessages;
        this.spellDescription = spellDescription;
        this.spellName = spellName;
        this.effects = effects;
        this.isAreaSpell = isAreaSpell;
        this.spellExecute = spellExecute;
        this.coolDownTicks = coolDownTicks;
    }

    private long getSpellAttack(Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= attackStats.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + randInt((int)attackStats.getWeaponRatingMin(), (int)attackStats.getWeaponRatingMax());
        }
        long i = attackStats.getStrength() + totDamage - victim.getArmorRating();
        if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }

    public String getAttackMessage(long amt, Npc npc) {
        int i = random.nextInt(attackMessages.size());
        String s = attackMessages.get(i);
        if (amt == 0) {
            return s;
        } else {
            return Color.BOLD_ON + Color.YELLOW + "[spell] " + Color.RESET + Color.YELLOW + "+" + amt + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE " + Color.RESET + s + Color.BOLD_ON + Color.RED + " >>>> " + Color.RESET + npc.getColorName();
        }
    }

    private void applySpell(Set<String> npcIds, Player player) {
        if (spellExecute != null) {
            for (String npcId : npcIds) {
                Npc npc = gameManager.getEntityManager().getNpcEntity(npcId);
                spellExecute.executeNpc(gameManager, npc, player);
            }
        }
    }

    public void attackSpell(Set<String> npcIds, Player player) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            long availableMana = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getStats().getCurrentMana();
            if (availableMana < manaCost) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Not enough mana!" + "\r\n");
            } else {
                applySpell(npcIds, player);
                for (String npcId : npcIds) {
                    Npc npc = gameManager.getEntityManager().getNpcEntity(npcId);
                    gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + Color.CYAN + " casts " + Color.RESET + "a " + Color.BOLD_ON + Color.WHITE + "[" + Color.RESET + spellName + Color.BOLD_ON + Color.WHITE + "]" + Color.RESET + " on " + npc.getColorName() + "! \r\n");
                    long spellAttack = getSpellAttack(npc.getStats());
                    final String spellAttackStr = getAttackMessage(spellAttack, npc);
                    player.addActiveFight(npc);
                    npc.doHealthDamage(player, Arrays.asList(spellAttackStr), -spellAttack);
                }
                if (npcIds.size() > 0) {
                    applyEffectsToNpcs(npcIds, player);
                    player.updatePlayerMana(-manaCost);
                    player.addCoolDown(new CoolDown(spellName, coolDownTicks, CoolDownType.SPELL));
                }
            }
        }
    }

    public void attackSpell(Player destinationPlayer, Player sourcePlayer) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(sourcePlayer.getPlayerId())) {
            long availableMana = gameManager.getPlayerManager().getPlayerMetadata(sourcePlayer.getPlayerId()).getStats().getCurrentMana();
            if (availableMana < manaCost) {
                gameManager.getChannelUtils().write(sourcePlayer.getPlayerId(), "Not enough mana!" + "\r\n");
            } else {
                gameManager.writeToPlayerCurrentRoom(sourcePlayer.getPlayerId(), sourcePlayer.getPlayerName() + Color.CYAN + " casts " + Color.RESET + "a " + Color.BOLD_ON + Color.WHITE + "[" + Color.RESET + spellName + Color.BOLD_ON + Color.WHITE + "]" + Color.RESET + " on " + destinationPlayer.getPlayerName() + "! \r\n");
                applyEffectsToPlayer(destinationPlayer, sourcePlayer);
                sourcePlayer.updatePlayerMana(-manaCost);
                sourcePlayer.addCoolDown(new CoolDown(spellName, coolDownTicks, CoolDownType.SPELL));
            }
        }
    }

    public void applyEffectsToPlayer(Player destinationPlayer, Player player) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            long availableMana = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getStats().getCurrentMana();
            if (availableMana < manaCost) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Not enough mana!" + "\r\n");
            } else {
                for (Effect effect : effects) {
                    Effect nEffect = new Effect(effect);
                    nEffect.setPlayerId(player.getPlayerId());
                    gameManager.getEntityManager().saveEffect(nEffect);
                    if (effect.getDurationStats().getCurrentHealth() < 0) {
                        log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                        continue;
                    }
                    String effectApplyMessage;
                    if (destinationPlayer.addEffect(effect.getEntityId())) {
                        effectApplyMessage = Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + nEffect.getEffectName() + " applied!" + "\r\n";
                        gameManager.getChannelUtils().write(destinationPlayer.getPlayerId(), effectApplyMessage);
                    } else {
                        effectApplyMessage = Color.BOLD_ON + Color.GREEN + "[effect] "+ Color.RESET + Color.RED + "Unable to apply "  + nEffect.getEffectName() + "!" + "\r\n";
                        gameManager.getChannelUtils().write(player.getPlayerId(), effectApplyMessage);
                    }
                }
            }
        }
    }

    public void applyEffectsToNpcs(Set<String> npcIds, Player player) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            long availableMana = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getStats().getCurrentMana();
            if (availableMana < manaCost) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Not enough mana!" + "\r\n");
            } else {
                for (Effect effect : effects) {
                    for (String npcId : npcIds) {
                        Npc npc = gameManager.getEntityManager().getNpcEntity(npcId);
                        if (npc == null) {
                            // assume this npc died
                            continue;
                        }
                        Effect nEffect = new Effect(effect);
                        nEffect.setPlayerId(player.getPlayerId());
                        if (effect.getDurationStats().getCurrentHealth() < 0) {
                            log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                            continue;
                        }
                        StatsHelper.combineStats(npc.getStats(), effect.getDurationStats());
                        // gameManager.getEffectsManager().application(nEffect, npc);
                        npc.addEffect(nEffect);
                    }
                }
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

    public Set<Effect> getEffects() {
        return effects;
    }

    public void setEffects(Set<Effect> effects) {
        this.effects = effects;
    }

    public boolean isAreaSpell() {
        return isAreaSpell;
    }

    public SpellExecute getSpellExecute() {
        return spellExecute;
    }

    interface SpellExecute {
        public void executeNpc(GameManager gameManager, Npc npc, Player player);
    }
}
