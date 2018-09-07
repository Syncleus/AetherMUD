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
package com.syncleus.aethermud.merchant.bank.commands;


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerUtil;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.server.communication.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AccountQueryCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("balance", "query", "q");
    final static String description = "Query account balance.";

    public AccountQueryCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            PlayerUtil.consume(gameManager, player.getPlayerId(), playerData -> {
                long goldInBank = playerData.getGoldInBank();
                long gold = playerData.getGold();
                write("You have " + NumberFormat.getNumberInstance(Locale.US).format(goldInBank) + Color.YELLOW + " gold" + Color.RESET + " in your bank account."+ "\r\n");
                write("You have " + NumberFormat.getNumberInstance(Locale.US).format(gold) + Color.YELLOW + " gold" + Color.RESET + " in your inventory."+ "\r\n");
            });

        } finally {
            super.messageReceived(ctx, e);
        }
    }

}
