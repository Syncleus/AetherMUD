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

import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.common.AetherMudEntry;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.server.multiline.MultiLineInputManager;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class DescriptionCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("desc", "description");
    final static String description = "Edit the description for the current room.";
    final static String correctUsage = "description";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);


    public DescriptionCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        DescriptionCommand descriptionCommand = this;
        execCommand(ctx, e, () -> {
            if (aetherMudSession.getGrabMultiLineInput().isPresent()) {
                MultiLineInputManager multiLineInputManager = gameManager.getMultiLineInputManager();
                UUID uuid = aetherMudSession.getGrabMultiLineInput().get().getKey();
                String multiLineInput = multiLineInputManager.retrieveMultiLineInput(uuid);
                currentRoom.setRoomDescription(multiLineInput);
                aetherMudSession.setGrabMultiLineInput(Optional.empty());
                return;
            }
            if (originalMessageParts.size() > 1) {
                String possibleNotable = originalMessageParts.get(1);
                for (Map.Entry<String, String> notable : currentRoom.getNotables().entrySet()) {
                    if (notable.getKey().equalsIgnoreCase(possibleNotable)) {
                        if (originalMessageParts.size() > 2) {
                            originalMessageParts.remove(0);
                            originalMessageParts.remove(0);
                            String newDesc = Joiner.on(" ").join(originalMessageParts);
                            notable.setValue(newDesc);
                            write("Notable: "  + notable.getKey() + " description is set to: " + newDesc);
                            return;
                        }
                    }
                }
            }
            write("You are now in multi-line mode.  Type \"done\" on an empty line to exit and save.\r\n");
            aetherMudSession.setGrabMultiLineInput(Optional.of(
                    new AetherMudEntry<>(gameManager.getMultiLineInputManager().createNewMultiLineInput(), descriptionCommand)));
        });
    }
}
