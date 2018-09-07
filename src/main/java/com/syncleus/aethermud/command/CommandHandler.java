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
package com.syncleus.aethermud.command;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.command.commands.CommandAuditLog;
import com.syncleus.aethermud.command.commands.GossipCommand;
import com.syncleus.aethermud.command.commands.UnknownCommand;
import com.syncleus.aethermud.configuration.ConfigureCommands;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.merchant.MerchantCommandHandler;
import com.syncleus.aethermud.merchant.bank.commands.BankCommandHandler;
import com.syncleus.aethermud.merchant.lockers.LockerCommandHandler;
import com.syncleus.aethermud.merchant.playerclass_selector.PlayerClassCommandHandler;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.server.model.AetherMudSession;
import com.syncleus.aethermud.server.multiline.MultiLineInputHandler;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

public class CommandHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final Meter commandMeter = Main.metrics.meter(MetricRegistry.name(CommandHandler.class, "commands"));
    private static final Logger log = Logger.getLogger(CommandHandler.class);

    public CommandHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        AetherMudSession session = (AetherMudSession) e.getChannel().getAttachment();
        Player player = gameManager.getPlayerManager().getPlayerByUsername(session.getUsername().get());
        session.setLastActivity(System.currentTimeMillis());
        if (session.getGrabMultiLineInput().isPresent()) {
            addLastHandler(e, new MultiLineInputHandler(gameManager));
            super.messageReceived(ctx, e);
            return;
        }

        if (session.getGrabMerchant().isPresent()) {
            Merchant merchant = session.getGrabMerchant().get().getKey();
            if (merchant.getMerchantType() == Merchant.MerchantType.BANK) {
                addLastHandler(e, new BankCommandHandler(gameManager, merchant));
            } else if (merchant.getMerchantType() == Merchant.MerchantType.LOCKER) {
                addLastHandler(e, new LockerCommandHandler(gameManager, merchant));
            } else if (merchant.getMerchantType() == Merchant.MerchantType.PLAYERCLASS_SELECTOR) {
                addLastHandler(e, new PlayerClassCommandHandler(gameManager, merchant));
            } else {
                addLastHandler(e, new MerchantCommandHandler(gameManager, merchant));
            }
            super.messageReceived(ctx, e);
            return;
        }

        Command commandByTrigger = null;

        String rootCommand = getRootCommand(e);
        if (player.isChatModeOn()) {
            if (rootCommand.startsWith("/")) {
                commandByTrigger = ConfigureCommands.commandRegistry.getCommandByTrigger(rootCommand.substring(1));
            } else {
                commandByTrigger = new GossipCommand(gameManager);
            }
        } else {
            commandByTrigger = ConfigureCommands.commandRegistry.getCommandByTrigger(rootCommand);
        }

        if ((commandByTrigger.roles != null) && commandByTrigger.roles.size() > 0) {
            boolean roleMatch = gameManager.getPlayerManager().hasAnyOfRoles(player, commandByTrigger.roles);
            if (!roleMatch) {
                addLastHandler(e, new UnknownCommand(gameManager));
                super.messageReceived(ctx, e);
                return;
            }
        }

        if (commandByTrigger.getDescription() != null) {
            Main.metrics.counter(MetricRegistry.name(CommandHandler.class, rootCommand)).inc();
            CommandAuditLog.logCommand((String) e.getMessage(), session.getUsername().get());
        }

        commandMeter.mark();
        // Always create a copy of the command.
        addLastHandler(e, commandByTrigger.copy());
        super.messageReceived(ctx, e);
    }

    private void addLastHandler(MessageEvent e, ChannelHandler handler) {
        e.getChannel().getPipeline().addLast("executed_command", handler);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        SentryManager.logSentry(this.getClass(), e.getCause(), "Exception caught in command handler!");
        AetherMudSession aetherMudSession = (AetherMudSession) e.getChannel().getAttachment();
        log.error("Error in the Command Handler!, last message: \"" + aetherMudSession.getLastMessage() + "\" - from username:" + aetherMudSession.getUsername().get(), e.getCause());
        gameManager.getPlayerManager().removePlayer(aetherMudSession.getUsername().get());
        e.getChannel().close();
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
