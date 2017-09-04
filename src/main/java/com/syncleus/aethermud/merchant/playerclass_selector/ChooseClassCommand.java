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
package com.syncleus.aethermud.merchant.playerclass_selector;

import com.syncleus.aethermud.common.AetherMudEntry;
import com.syncleus.aethermud.common.AetherMudUtils;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.player.PlayerClass;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.Optional;


public class ChooseClassCommand extends PlayerClassCommand {

    private final PlayerClass playerClass;

    public ChooseClassCommand(PlayerClass playerClass, GameManager gameManager) {
        super(gameManager);
        this.playerClass = playerClass;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        aetherMudSession.setGrabMerchant(Optional.<AetherMudEntry<Merchant, SimpleChannelUpstreamHandler>>empty());
        player.setPlayerClass(playerClass);
        write("You are now and forever, a " + AetherMudUtils.capitalize(playerClass.getIdentifier()) + "\r\n");
        e.getChannel().getPipeline().remove("executed_command");
        e.getChannel().getPipeline().remove("executed_playerclass_command");
        String s = gameManager.buildPrompt(playerId);
        write(s);
    }
}
