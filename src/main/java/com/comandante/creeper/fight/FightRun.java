package com.comandante.creeper.fight;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.stat.Stats;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;


public class FightRun implements Callable<FightResults> {

    private final Player player;
    private final Npc npc;
    private final GameManager gameManager;
    private static final Logger log = Logger.getLogger(FightRun.class);


    public FightRun(Player player, Npc npc, GameManager gameManager) {
        this.player = player;
        this.npc = npc;
        this.gameManager = gameManager;
    }

    @Override
    public FightResults call() throws Exception {
        FightResults fightResults = null;
        try {
            Stats npcStats = npc.getStats();
            Stats playerStats = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(player);
            boolean playerDied = false;
            while (npcStats.getCurrentHealth() > 0 && !playerDied) {
                if (getCurrentHealth() <= 0) {
                    playerDied = true;
                    continue;
                }
                gameManager.getFightManager().fightTurn(playerStats, npcStats, 3, player, npc);
                if (getCurrentHealth() > 0 && npcStats.getCurrentHealth() > 0) {
                    if (player.isValidPrimaryActiveFight(npc)) {
                        String prompt = gameManager.buildPrompt(player.getPlayerId());
                        gameManager.getChannelUtils().write(player.getPlayerId(), prompt, true);
                    }
                    gameManager.getPlayerManager().getSessionManager().getSession(player.getPlayerId()).setIsAbleToDoAbility(true);
                    try {
                        Thread.sleep(2200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gameManager.getPlayerManager().getSessionManager().getSession(player.getPlayerId()).setIsAbleToDoAbility(false);
                }
            }

            fightResults = new FightResultsBuilder().setNpcWon(false).setPlayerWon(false).createFightResults();
            if (playerDied) {
                player.killPlayer(npc);
                fightResults = new FightResultsBuilder().setNpcWon(true).setPlayerWon(false).createFightResults();
            }

            if (npcStats.getCurrentHealth() <= 0) {
                fightResults = new FightResultsBuilder().setNpcWon(false).setPlayerWon(true).createFightResults();
            }
        } catch (Exception e) {
            log.error("Fight Failure!", e);
        }
        return fightResults;
    }

    private int getCurrentHealth() {
        return gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId()).getStats().getCurrentHealth();
    }
}
