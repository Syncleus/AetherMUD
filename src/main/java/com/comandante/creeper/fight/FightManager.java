package com.comandante.creeper.fight;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.stat.Stats;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

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
        this.fightService = Executors.newFixedThreadPool(100, new ThreadFactoryBuilder().setNameFormat("creeper-fight-thread-%d").build());
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
        Integer roomId = gameManager.getRoomManager().getPlayerCurrentRoom(player).get().getRoomId();
        int chanceToHit = getChanceToHit(challenger, victim);
        if (isValidActiveFightForThisNpc(player, npc)) {
            int damageToVictim = 0;
            if (randInt(0, 100) < chanceToHit) {
                damageToVictim = getAttackAmt(challenger, victim);
            }
            if (damageToVictim > 0) {
                final String fightMsg = Color.YELLOW + "+" + damageToVictim + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npc.getColorName();
                channelUtils.write(player.getPlayerId(), fightMsg, true);
                doNpcDamage(npc.getEntityId(), damageToVictim, player.getPlayerId());
            } else {
                final String fightMsg = "You MISS " + npc.getName() + "!";
                channelUtils.write(player.getPlayerId(), fightMsg, true);
            }
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (victim.getCurrentHealth() <= 0) {
                return;
            }
        }
        int chanceToHitBack = getChanceToHit(victim, challenger);
        int damageBack = getAttackAmt(victim, challenger);
        if (randInt(0, 100) < chanceToHitBack) {
            doPlayerDamage(player, damageBack);
            final String fightMsg = npc.getColorName() + Color.BOLD_ON + Color.RED + " DAMAGES" + Color.RESET + " you for " + Color.RED + "-" + damageBack + Color.RESET;
            channelUtils.write(player.getPlayerId(), fightMsg, true);
        } else {
            final String fightMsg = npc.getColorName() + Color.BOLD_ON + Color.CYAN + " MISSES" + Color.RESET + " you!";
            channelUtils.write(player.getPlayerId(), fightMsg, true);
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

    public static boolean isActiveFight(CreeperSession session) {
        if (session == null) return false;
        return (session.getActiveFight().isPresent() && !session.getActiveFight().get().isDone());
    }

    private boolean isValidActiveFightForThisNpc(Player player, Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(player.getPlayerId())) {
            if (player.getNpcEntityCurrentlyInFightWith() == null) {
                player.setNpcEntityCurrentlyInFightWith(npc.getEntityId());
                return true;
            }
            if (gameManager.getEntityManager().getNpcEntity(player.getNpcEntityCurrentlyInFightWith()) != null) {
                return npc.getEntityId().equals(player.getNpcEntityCurrentlyInFightWith());
            } else {
                player.setNpcEntityCurrentlyInFightWith(npc.getEntityId());
                return true;
            }
        }
    }
}

