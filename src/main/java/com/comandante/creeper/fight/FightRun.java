package com.comandante.creeper.fight;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.stat.Stats;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;


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
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(player.getPlayerId());
            Stats playerStats = gameManager.getEquipmentManager().getPlayerStatsWithEquipment(playerMetadata);

            while (npcStats.getCurrentHealth() > 0) {
                if (playerStats.getCurrentHealth() <= 0) {
                    break;
                }
                gameManager.getFightManager().fightTurn(playerStats, npcStats, 3, player, npc);
                if (FightManager.isActiveFight(gameManager.getPlayerManager().getSessionManager().getSession(player.getPlayerId()))) {
                    gameManager.getChannelUtils().write(player.getPlayerId(), "Use an ability!", true);
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

            if (playerStats.getCurrentHealth() <= 0) {
                gameManager.getChannelUtils().writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " is now dead." + "\r\n");
                PlayerMovement playerMovement = new PlayerMovement(player, gameManager.getRoomManager().getPlayerCurrentRoom(player).get().getRoomId(), GameManager.LOBBY_ID, null, "vanished into the ether.", "");
                gameManager.movePlayer(playerMovement);
                gameManager.currentRoomLogic(player.getPlayerId());
                String prompt = gameManager.getPlayerManager().buildPrompt(player.getPlayerId());
                gameManager.getChannelUtils().write(player.getPlayerId(), prompt, true);
                npc.setIsInFight(false);
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
}
