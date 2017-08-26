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
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class ToggleChatCommand extends Command {

    public final static String TOGGLE_CHAT_COMMAND_TRIGGER = "cm";
    final static List<String> validTriggers = Arrays.asList(TOGGLE_CHAT_COMMAND_TRIGGER);
    final static String description = "Configure chat mode.";
    final static String correctUsage = "cm on || /cm off";

    public ToggleChatCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            boolean chat = player.toggleChat();
            if (chat) {
                write("Chat mode enabled.");
            } else {
                write("Chat mode disabled.");
            }
        });
    }

}
