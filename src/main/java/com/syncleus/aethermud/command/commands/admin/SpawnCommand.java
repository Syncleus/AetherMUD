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
package com.syncleus.aethermud.command.commands.admin;

import com.google.common.collect.Lists;
import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.server.communication.Color;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SpawnCommand  extends Command {

    final static List<String> validTriggers = Arrays.asList("spawn");
    final static String description = "Spawn a NPC.";
    final static String correctUsage = "spawn <npc name> | spawn";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public SpawnCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            List<? extends NpcSpawn> npcsFromFile = gameManager.getNpcStorage().getAllNpcs();
            if (originalMessageParts.size() == 1) {
                write(getHeader());
                for (NpcSpawn npcSpawn : npcsFromFile) {
                    write(npcSpawn.getName() + "\r\n");
                }
            } else {
                originalMessageParts.remove(0);
                String targetNpc = Joiner.on(" ").join(originalMessageParts);
                for (NpcSpawn npcSpawn : npcsFromFile) {
                    if (targetNpc.equals(npcSpawn.getName())) {
                        Loot loot = new Loot(0,0, Lists.newArrayList());
                        NpcSpawn modifiedNpcSpawn = new NpcBuilder(npcSpawn).setSpawnRules(null).setLoot(loot).createNpc();
                        modifiedNpcSpawn.getStats().setExperience(0);
                        modifiedNpcSpawn.setCurrentRoom(currentRoom);
                        gameManager.getEntityManager().addEntity(modifiedNpcSpawn);
                        currentRoom.addPresentNpc(modifiedNpcSpawn.getEntityId());
                        writeToRoom("A " + modifiedNpcSpawn.getColorName() + " appears." + "\r\n");
                        return;
                    }
                }
                write("No npc found with name: " + targetNpc + "\r\n");
            }
        });
    }

    public String getHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(Color.MAGENTA + "-+=[ " + Color.RESET).append("Spawn").append(Color.MAGENTA + " ]=+- " + Color.RESET).append("\r\n");
        sb.append(Color.MAGENTA + "AvailableNpcs-----------------------" + Color.RESET).append("\r\n");
        return sb.toString();
    }
}
