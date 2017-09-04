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

import com.syncleus.aethermud.common.AetherMudEntry;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.merchant.lockers.LockerCommand;
import com.google.common.base.Joiner;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OpenCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("open", "o");
    final static String description = "Open a locker.";
    final static String correctUsage = "open lockers";

    public OpenCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        OpenCommand openCommand = this;
        execCommand(ctx, e, () -> {
            if (aetherMudSession.getGrabMerchant().isPresent()) {
                aetherMudSession.setGrabMerchant(Optional.empty());
                return;
            }
            originalMessageParts.remove(0);
            String desiredMerchantTalk = Joiner.on(" ").join(originalMessageParts);
            Set<Merchant> merchants = currentRoom.getMerchants();
            for (Merchant merchant : merchants) {
                if (merchant.getMerchantType() != Merchant.MerchantType.LOCKER) {
                    return;
                }
                if (merchant.getValidTriggers().contains(desiredMerchantTalk)) {
                    write(merchant.getWelcomeMessage() + "\r\n");
                    write(LockerCommand.getPrompt());
                    aetherMudSession.setGrabMerchant(Optional.of(new AetherMudEntry<>(merchant, openCommand)));
                }
            }
        });
    }
}
