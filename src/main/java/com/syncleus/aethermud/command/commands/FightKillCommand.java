/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
import com.syncleus.aethermud.player.CoolDownType;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FightKillCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("kill", "k", "fight");
    final static String description = "Initiate a fight with a mob.";
    final static String correctUsage = "kill <mob name>";

    public FightKillCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (player.getCurrentHealth() <= 0) {
                write("You have no health and as such you can not attack.");
                return;
            }
            if (player.getActiveFights().size() > 0) {
                write("You are already in a fight!");
                return;
            }
            if (player.isActive(CoolDownType.DEATH)) {
                write("You are dead and can not attack.");
                return;
            }
            if (originalMessageParts.size() == 1) {
                write("You need to specify who you want to fight.");
                return;
            }
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            Set<String> npcIds = currentRoom.getNpcIds();
            for (String npcId : npcIds) {
                NpcSpawn npcSpawnEntity = entityManager.getNpcEntity(npcId);
                if (npcSpawnEntity.getValidTriggers().contains(target)) {
                    if (player.addActiveFight(npcSpawnEntity)) {
                        writeToRoom(player.getPlayerName() + " has attacked a " + npcSpawnEntity.getColorName());
                       // player.addActiveFight(npcEntity);
                        return;
                    } else {
                        return;
                    }
                }
            }
            write("There's no NPC here to fight by that name.");
        });
    }
}
