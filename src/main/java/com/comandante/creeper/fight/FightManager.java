package com.comandante.creeper.fight;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FightManager {

    private final GameManager gameManager;
    private final ChannelUtils channelUtils;
    private final ExecutorService fightService;
    private static final Random random = new Random();

    private static final Logger log = Logger.getLogger(FightManager.class);

    public FightManager(GameManager gameManager) {
        this.channelUtils = gameManager.getChannelUtils();
        this.gameManager = gameManager;
        this.fightService = Executors.newFixedThreadPool(100, new ThreadFactoryBuilder().setNameFormat("creeper-fight-thread-%d").build());
    }

    public Future<FightResults> fight(FightRun fightRun) {
        return fightService.submit(fightRun);
    }

    public void fightTurn(Stats challenger, Stats victim, int numRoundsPerTurns, Player player, Npc npc) throws PlayerDeathException {
        for (int i = 0; i < numRoundsPerTurns; i++) {
            if (challenger.getCurrentHealth() <= 0 || victim.getCurrentHealth() <= 0) {
                return;
            }
            fightRound(challenger, victim, player, npc);
        }
    }

    public void fightRound(Stats challenger, Stats victim, Player player, Npc npc) throws PlayerDeathException {
        try {
            if (player.isActive(CoolDownType.DEATH)) {
                throw new PlayerDeathException("Player died! Noticed he has a death cooldown.");
            }
            int chanceToHit = getChanceToHit(challenger, victim);
            if (player.isValidPrimaryActiveFight(npc)) {
                int damageToVictim = 0;
                if (randInt(0, 100) < chanceToHit) {
                    damageToVictim = getAttackAmt(challenger, victim);
                }
                if (damageToVictim > 0) {
                    doNpcDamage(npc, damageToVictim, player);
                } else {
                    final String fightMsg = "You MISS " + npc.getName() + "!";
                    channelUtils.write(player.getPlayerId(), fightMsg, true);
                }
                Thread.sleep(600);
                if (victim.getCurrentHealth() <= 0) {
                    return;
                }
            } else {
                Thread.sleep(600);
            }
            if (player.getCurrentRoom().getNpcIds().contains(npc.getEntityId())) {
                if (!player.doesActiveFightExist(npc) && !player.isActive(CoolDownType.DEATH)) {
                    player.addActiveFight(npc);
                }
            } else {
                Thread.sleep(600);
            }
        } catch (InterruptedException e) {
            log.info("Fight was interrupted! (" + player.getPlayerName() + ") vs. (" + npc.getName() + ")", e);
        }
    }

    private boolean doPlayerDamage(Player player, int damageAmount, Npc npc) {
        return player.updatePlayerHealth(-damageAmount, npc);
    }

    private void doNpcDamage(Npc npc, int damageAmount, Player player) {
        final String fightMsg = Color.YELLOW + "+" + damageAmount + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npc.getColorName();
        npc.doHealthDamage(player, Arrays.asList(fightMsg), damageAmount);
    }

    private static int getChanceToHit(Stats challenger, Stats victim) {
        return (challenger.getStrength() + challenger.getMeleSkill()) * 5 - victim.getAgile() * 5;
    }

    private static int getAttackAmt(Stats challenger, Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= challenger.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + randInt(challenger.getWeaponRatingMin(), challenger.getWeaponRatingMax());
        }
        int i = challenger.getStrength() + totDamage - victim.getArmorRating();
        if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }

    private static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public class PlayerDeathException extends Exception {
        public PlayerDeathException() {
            super();
        }

        public PlayerDeathException(String message) {
            super(message);
        }

        public PlayerDeathException(String message, Throwable cause) {
            super(message, cause);
        }

        public PlayerDeathException(Throwable cause) {
            super(cause);
        }
    }
}

