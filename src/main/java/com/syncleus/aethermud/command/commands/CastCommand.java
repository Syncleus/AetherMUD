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
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.spells.SpellRunnable;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CastCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("cast", "c");
    final static String description = "Cast a spell.";
    final static String correctUsage = "cast lightning";

    public CastCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (player.getCurrentHealth() <= 0) {
                write("You have no health and as such you can not attack.");
                return;
            }
            if (player.isActive(CoolDownType.DEATH)) {
                write("You are dead and can not attack.");
                return;
            }
            if (originalMessageParts.size() == 1) {
                write("Need to specify a spell or optionally, a target player/npc.\r\n");
                return;
            }
            String desiredSpellName = originalMessageParts.get(1);
            Optional<SpellRunnable> spellRunnable = gameManager.getSpells().getSpellRunnable(desiredSpellName);
            if (!spellRunnable.isPresent() || !player.doesHaveSpellLearned(spellRunnable.get().getName())) {
                write("No spell found with the name: " + desiredSpellName + "\r\n");
                return;
            }
            if (player.isActiveSpellCoolDown(spellRunnable.get().getName())) {
                write("That spell is still in cooldown.\r\n");
                write(gameManager.renderCoolDownString(player.getCoolDowns()));
                return;
            }
            if (originalMessageParts.size() == 2) {
                gameManager.getSpells().executeSpell(player, Optional.empty(), Optional.empty(), spellRunnable.get());
                return;
            }
            originalMessageParts.remove(0);
            originalMessageParts.remove(0);
            String target = Joiner.on(" ").join(originalMessageParts);
            if (player.getPlayerName().equals(target)) {
                gameManager.getSpells().executeSpell(player, Optional.empty(), Optional.ofNullable(player), spellRunnable.get());
                return;
            }
            for (Player destinationPlayer : currentRoom.getPresentPlayers()) {
                if (destinationPlayer.getPlayerName().equalsIgnoreCase(target)) {
                    gameManager.getSpells().executeSpell(player, Optional.empty(), Optional.of(destinationPlayer), spellRunnable.get());
                    return;
                }
            }
            for (String npcId : currentRoom.getNpcIds()) {
                NpcSpawn npcSpawnEntity = entityManager.getNpcEntity(npcId);
                if (npcSpawnEntity.getValidTriggers().contains(target)) {
                    gameManager.getSpells().executeSpell(player, Optional.of(npcSpawnEntity), Optional.empty(), spellRunnable.get());
                    return;
                }
            }
        });
    }
}
