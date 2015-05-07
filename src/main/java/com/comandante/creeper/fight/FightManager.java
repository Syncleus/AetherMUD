package com.comandante.creeper.fight;

import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.stat.Stats;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FightManager {

    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private final ChannelUtils channelUtils;
    private static final Random random = new Random();

    private final ExecutorService fightService;

    public FightManager(GameManager gameManager) {
        this.channelUtils = gameManager.getChannelUtils();
        this.playerManager = gameManager.getPlayerManager();
        this.gameManager = gameManager;
        this.fightService = Executors.newFixedThreadPool(10);
    }


    public Future<FightResults> fight(FightRun fightRun) {
        return fightService.submit(fightRun);
    }

    public void fightTurn(Stats challenger, Stats victim, int numRoundsPerTurns, Player player, Npc npc) {
        for (int i = 0; i < numRoundsPerTurns; i++) {
            if (challenger.getCurrentHealth() <= 0 || victim.getCurrentHealth() <= 0) {
                return;
            }
             fightRound(challenger, victim, player, npc);
        }
    }

    public void fightRound(Stats challenger, Stats victim, Player player, Npc npc) {
        int chanceToHit = getChanceToHit(challenger, victim);
        int damageToVictim = 0;
        if (randInt(0, 100) < chanceToHit) {
            damageToVictim = getAttack(challenger, victim);
        }
        if (damageToVictim > 0) {
            channelUtils.write(player.getPlayerId(), Color.YELLOW + "+" + damageToVictim + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npc.getColorName(), true);
            doNpcDamage(npc.getEntityId(), damageToVictim, player.getPlayerId());
        } else {
            channelUtils.write(player.getPlayerId(), "You MISS " + npc.getName() + "!", true);
        }
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (victim.getCurrentHealth() <= 0) {
            return;
        }
        int chanceToHitBack = getChanceToHit(victim, challenger);
        int damageBack = getAttack(victim, challenger);
        if (randInt(0, 100) < chanceToHitBack) {
            doPlayerDamage(player, damageBack);
            channelUtils.write(player.getPlayerId(), npc.getColorName() + Color.BOLD_ON + Color.RED + " DAMAGES" + Color.RESET + " you for " + Color.RED + "-" + damageBack + Color.RESET, true);
        } else {
            channelUtils.write(player.getPlayerId(), npc.getColorName() + Color.BOLD_ON + Color.CYAN + " MISSES"+ Color.RESET + " you!", true);
        }
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doPlayerDamage(Player player, int damageAmount) {
        playerManager.updatePlayerHealth(player, -damageAmount);
    }

    private void doNpcDamage(String npcId, int damageAmount, String playerId) {
        gameManager.updateNpcHealth(npcId, -damageAmount, playerId);
    }

    private static int getChanceToHit(Stats challenger, Stats victim) {
        return (challenger.getStrength() + challenger.getMeleSkill()) * 5 - victim.getAgile() * 5;
    }

    private static int getAttack(Stats challenger, Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= challenger.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + randInt(challenger.getWeaponRatingMin(), challenger.getWeaponRatingMax());
        }
        return challenger.getStrength() + totDamage - victim.getArmorRating();
    }

    private static int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static boolean isActiveFight(CreeperSession session) {
        if (session == null) return false;
        return (session.getActiveFight().isPresent() && !session.getActiveFight().get().isDone());
    }

}

