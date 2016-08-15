package com.comandante.creeper.player;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.Room;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

public class NpcAggroCountDown extends AbstractExecutionThreadService {

    private final int delaySeconds;
    private final GameManager gameManager;
    private final Player player;
    private final Npc npc;
    private final Room originalRoom;

    public NpcAggroCountDown(int delaySeconds, GameManager gameManager, Player player, Npc npc, Room originalRoom) {
        this.delaySeconds = delaySeconds;
        this.gameManager = gameManager;
        this.player = player;
        this.npc = npc;
        this.originalRoom = originalRoom;
    }

    @Override
    protected void run() throws Exception {
        try {
            Thread.sleep(delaySeconds * 1000);
            if (!player.getCurrentRoom().getRoomId().equals(originalRoom.getRoomId())) {
                return;
            }
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " has " + Color.BOLD_ON + Color.RED + "angered" + Color.RESET + " a " + npc.getColorName() + "\r\n");
            player.addActiveFight(npc);
        } finally {
            this.shutDown();
        }
    }
}
