/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.command.commands;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.stats.Levels;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            Set<Player> presentPlayers = currentRoom.getPresentPlayers();
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
                NpcSpawn currentNpcSpawn = gameManager.getEntityManager().getNpcEntity(npcId);
                if (currentNpcSpawn.getValidTriggers().contains(target)) {
                    write(gameManager.getLookString(currentNpcSpawn, Levels.getLevel(gameManager.getStatsModifierFactory().getStatsModifier(player).getExperience())) + "\r\n");
                }
            }
        });
    }
}
