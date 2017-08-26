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
import com.syncleus.aethermud.player.PlayerMetadata;
import com.syncleus.aethermud.server.communication.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GoldCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("gold");
    final static String description = "Displays how much gold is in your posession.";
    final static String correctUsage = "gold";

    public GoldCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Optional<PlayerMetadata> playerMetadataOptional = playerManager.getPlayerMetadata(playerId);
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerMetadata playerMetadata = playerMetadataOptional.get();
            write("You have " + NumberFormat.getNumberInstance(Locale.US).format(playerMetadata.getGold()) + Color.YELLOW + " gold." + Color.RESET);
        });
    }
}
