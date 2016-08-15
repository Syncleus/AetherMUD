
package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Levels;
import com.comandante.creeper.player.Player;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class LookCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("look", "l");
    final static String description = "look at the room, another player, or yourself.";
    final static String correctUsage = "look <playerName>";

    public LookCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() == 1) {
                printCurrentRoomInformation();
                return;
            }
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            if (target.equalsIgnoreCase("self")) {
                write(player.getLookString() + "\r\n");
            }
            //Notables
            for (Map.Entry<String, String> notable : currentRoom.getNotables().entrySet()) {
                if (notable.getKey().equalsIgnoreCase(target)) {
                    write(notable.getValue() + "\r\n");
                }
            }
            //Players
            Set<Player> presentPlayers = roomManager.getPresentPlayers(currentRoom);
            for (Player presentPlayer : presentPlayers) {
                if (presentPlayer != null && presentPlayer.getPlayerName().equals(target)) {
                    write(presentPlayer.getLookString() + "\r\n");
                    if (!presentPlayer.getPlayerId().equals(playerId)) {
                        channelUtils.write(presentPlayer.getPlayerId(), player.getPlayerName() + " looks at you.", true);
                    }
                }
            }
            Set<String> npcIds = currentRoom.getNpcIds();
            for (String npcId : npcIds) {
                Npc currentNpc = gameManager.getEntityManager().getNpcEntity(npcId);
                if (currentNpc.getValidTriggers().contains(target)) {
                    write(gameManager.getLookString(currentNpc, Levels.getLevel(gameManager.getStatsModifierFactory().getStatsModifier(player).getExperience())) + "\r\n");
                }
            }
        });
    }
}
