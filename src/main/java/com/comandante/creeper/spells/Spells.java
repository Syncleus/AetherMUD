package com.comandante.creeper.spells;

import com.comandante.creeper.CreeperUtils;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsHelper;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.Set;

public class Spells {

    private GameManager gameManager;

    private static final Logger log = Logger.getLogger(Spell.class);

    public Spells(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void executeSpell(Player sourcePlayer, Optional<Npc> destinationNpc, Optional<Player> destinationPlayer, ExecuteSpellRunnable executeSpellRunnable) {
        try {
            executeSpellRunnable.run(sourcePlayer, destinationNpc, destinationPlayer, gameManager);
        } catch (Exception e) {
            log.error("Problem executing spell.", e);
        }
    }

    public Optional<ExecuteSpellRunnable> getSpellRunnable(String triggerName) {
        if (Sets.newHashSet("lightning", "l").contains(triggerName)) {
            return Optional.of(new LightningSpellRunnable(gameManager));
        }
        return Optional.empty();
    }

    public void applyEffectsToNpcs(Player player, Set<Npc> npcs, Set<Effect> effects) {
        effects.forEach(effect ->
                npcs.forEach(npc -> {
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
                effectApplyMessage = Color.BOLD_ON + Color.GREEN + "[effect] " + Color.RESET + Color.RED + "Unable to apply " + nEffect.getEffectName() + "!" + "\r\n";
                gameManager.getChannelUtils().write(player.getPlayerId(), effectApplyMessage);
            }
        }
    }

    public static long getSpellAttack(Stats player, Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= player.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + CreeperUtils.randInt((int) player.getWeaponRatingMin(), (int) player.getWeaponRatingMax());
        }
        long i = player.getStrength() + totDamage - victim.getArmorRating();
        if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }
}


