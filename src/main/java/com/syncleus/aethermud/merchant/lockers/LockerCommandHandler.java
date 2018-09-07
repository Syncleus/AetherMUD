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
package com.syncleus.aethermud.merchant.lockers;

import com.syncleus.aethermud.command.commands.CommandAuditLog;
import com.syncleus.aethermud.configuration.ConfigureCommands;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.server.model.AetherMudSession;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * Created by kearney on 6/12/15.
 */
public class LockerCommandHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final Merchant merchant;

    public LockerCommandHandler(GameManager gameManager, Merchant merchant) {
        this.gameManager = gameManager;
        this.merchant = merchant;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String rootCommand = getRootCommand(e);
        AetherMudSession session = (AetherMudSession) e.getChannel().getAttachment();
        CommandAuditLog.logCommand(rootCommand, session.getUsername().get());
        LockerCommand commandByTrigger = ConfigureCommands.lockerCommandRegistry.getCommandByTrigger(rootCommand);
        LockerCommand cmd = commandByTrigger.createObj(commandByTrigger.getClass().getName());
        e.getChannel().getPipeline().addLast("executed_locker_command", cmd);
        super.messageReceived(ctx, e);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
        AetherMudSession aetherMudSession = (AetherMudSession) e.getChannel().getAttachment();
        gameManager.getPlayerManager().removePlayer(aetherMudSession.getUsername().get());
    }

    private String getRootCommand(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        String[] split = origMessage.split(" ");
        if (split.length > 0) {
            return split[0].toLowerCase();
        } else {
            return origMessage;
        }
    }
}
