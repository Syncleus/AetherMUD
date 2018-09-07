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


import com.syncleus.aethermud.common.AetherMudEntry;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.merchant.MerchantCommandHandler;
import com.syncleus.aethermud.merchant.bank.commands.BankCommand;
import com.syncleus.aethermud.merchant.lockers.LockerCommand;
import com.syncleus.aethermud.merchant.playerclass_selector.PlayerClassCommand;
import com.syncleus.aethermud.player.PlayerClass;
import com.syncleus.aethermud.server.communication.Color;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TalkCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("talk");
    final static String description = "Talk to a merchant.";
    final static String correctUsage = "talk <merchant name>";

    public TalkCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        TalkCommand talkCommand = this;
        execCommand(ctx, e, () -> {
            if (aetherMudSession.getGrabMerchant().isPresent()) {
                aetherMudSession.setGrabMerchant(Optional.empty());
                return;
            }
            originalMessageParts.remove(0);
            String desiredMerchantTalk = Joiner.on(" ").join(originalMessageParts);
            Set<Merchant> merchants = currentRoom.getMerchants();
            for (Merchant merchant : merchants) {
                if (merchant.getValidTriggers().contains(desiredMerchantTalk)) {
                    write(merchant.getWelcomeMessage() + "\r\n");
                    if (merchant.getMerchantType() == Merchant.MerchantType.BASIC) {
                        write(merchant.getMenu() + "\r\n");
                        gameManager.getChannelUtils().write(playerId, "\r\n" + MerchantCommandHandler.buildPrompt());
                    } else if (merchant.getMerchantType() == Merchant.MerchantType.BANK) {
                        write(BankCommand.getPrompt());
                    } else if (merchant.getMerchantType() == Merchant.MerchantType.LOCKER) {
                        write(LockerCommand.getPrompt());
                    } else if (merchant.getMerchantType() == Merchant.MerchantType.PLAYERCLASS_SELECTOR) {
                        if (player.getLevel() < 2) {
                            write("Before you can pick a character class, you must be at least level 2.");
                            return;
                        }
                        if (!player.getPlayerClass().equals(PlayerClass.BASIC)) {
                            write("You've already selected a character class. " +
                                    "\r\nIf you'd like to re-select you must present the old wise man with a " +
                                    "" + Color.YELLOW + "golden" + Color.MAGENTA + " fortune cookie" + Color.RESET);
                            return;
                        }
                        write(PlayerClassCommand.getPrompt());
                    }
                    aetherMudSession.setGrabMerchant(Optional.of(
                            new AetherMudEntry<>(merchant, talkCommand)));
                }
            }
        });
    }
}
