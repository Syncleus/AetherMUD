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
package com.syncleus.aethermud.merchant.bank.commands;


import com.syncleus.aethermud.common.AetherMudEntry;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.Merchant;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DoneCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("done");
    final static String description = "Complete transaction.";

    public DoneCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        gameManager.getChannelUtils().write(playerId, "Thanks, COME AGAIN." + "\r\n", true);
        creeperSession.setGrabMerchant(Optional.<AetherMudEntry<Merchant, SimpleChannelUpstreamHandler>>empty());
        e.getChannel().getPipeline().remove("executed_command");
        e.getChannel().getPipeline().remove("executed_bank_command");
        String s = gameManager.buildPrompt(playerId);
        write(s);
    }
}
