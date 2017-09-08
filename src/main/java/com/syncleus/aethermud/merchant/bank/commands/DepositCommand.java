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


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerUtil;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.server.communication.Color;
import org.apache.commons.lang.math.NumberUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class DepositCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("deposit", "d");
    final static String description = "Deposit gold.";

    public DepositCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() > 1 && NumberUtils.isNumber(originalMessageParts.get(1))) {
                int depositAmt = Integer.parseInt(originalMessageParts.get(1));
                if (areFundsAvailable(depositAmt)) {
                    player.transferGoldToBank(depositAmt);
                    write("Your funds of " + NumberFormat.getNumberInstance(Locale.US).format(depositAmt) + Color.YELLOW + " gold " + Color.RESET + "have been transferred to your bank account."+ "\r\n");
                } else {
                    write("You don't have enough" + Color.YELLOW + " gold "+ Color.RESET + "to cover the deposit amount."+ "\r\n");
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    private boolean areFundsAvailable(long amt) {
        return PlayerUtil.transactRead(gameManager, player.getPlayerId(), playerData -> playerData.getGold() >= amt);
    }

}
