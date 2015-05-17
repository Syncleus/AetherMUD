package com.comandante.creeper.server;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.ConfigureCommands;
import com.comandante.creeper.Main;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.command.CommandAuditLog;
import com.comandante.creeper.command.UnknownCommand;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.merchant.MerchantCommandHandler;
import com.comandante.creeper.merchant.bank.commands.BankCommandHandler;
import com.comandante.creeper.player.Player;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

public class CreeperCommandHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final Meter commandMeter = Main.metrics.meter(MetricRegistry.name(CreeperCommandHandler.class, "commands"));
    private static final Logger log = Logger.getLogger(CreeperCommandHandler.class);

    public CreeperCommandHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String rootCommand = getRootCommand(e);
        CreeperSession session = (CreeperSession) e.getChannel().getAttachment();
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
            } else {
                addLastHandler(e, new MerchantCommandHandler(gameManager, merchant));
            }
            super.messageReceived(ctx, e);
            return;
        }
        Command commandByTrigger = ConfigureCommands.creeperCommandRegistry.getCommandByTrigger(rootCommand);

        Player player = gameManager.getPlayerManager().getPlayerByUsername(session.getUsername().get());
        if ((commandByTrigger.roles != null) && commandByTrigger.roles.size() > 0) {
            boolean roleMatch = gameManager.getPlayerManager().hasAnyOfRoles(player, commandByTrigger.roles);
            if (!roleMatch) {
                addLastHandler(e, new UnknownCommand(gameManager));
                super.messageReceived(ctx, e);
                return;
            }
        }
        if (commandByTrigger.getDescription() != null) {
            Main.metrics.counter(MetricRegistry.name(CreeperCommandHandler.class, rootCommand)).inc();
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
        CreeperSession creeperSession = (CreeperSession) e.getChannel().getAttachment();
        log.error("Error in the Command Handler!, last message:" + creeperSession.getLastMessage() + " from " + creeperSession.getUsername().get());
        gameManager.getPlayerManager().removePlayer(creeperSession.getUsername().get());
        e.getCause().printStackTrace();
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
