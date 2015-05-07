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
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class CreeperCommandHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final Meter commandMeter = Main.metrics.meter(MetricRegistry.name(CreeperCommandHandler.class, "commands"));

    public CreeperCommandHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String rootCommand = getRootCommand(e);
        CreeperSession session = (CreeperSession) e.getChannel().getAttachment();
        if (session.getGrabMultiLineInput().isPresent()) {
            e.getChannel().getPipeline().addLast("executed_command", new MultiLineInputHandler(gameManager));
            super.messageReceived(ctx, e);
            return;
        }
        if (session.getGrabMerchant().isPresent()) {
            Merchant merchant = session.getGrabMerchant().get().getKey();
            if (merchant.getMerchantType() == Merchant.MerchantType.BANK) {
                e.getChannel().getPipeline().addLast("executed_command", new BankCommandHandler(gameManager, merchant));
            } else {
                e.getChannel().getPipeline().addLast("executed_command", new MerchantCommandHandler(gameManager, merchant));
            }
            super.messageReceived(ctx, e);
            return;
        }
        Command commandByTrigger = ConfigureCommands.creeperCommandRegistry.getCommandByTrigger(rootCommand);
        Player player = gameManager.getPlayerManager().getPlayerByUsername(session.getUsername().get());
        if ((commandByTrigger.roles != null) && commandByTrigger.roles.size() > 0) {
            boolean roleMatch = gameManager.getPlayerManager().hasAnyOfRoles(player, commandByTrigger.roles);
            if (!roleMatch) {
                e.getChannel().getPipeline().addLast("executed_command", new UnknownCommand(gameManager));
                super.messageReceived(ctx, e);
                return;
            }
        }
        if (commandByTrigger.getDescription() != null) {
            Main.metrics.counter(MetricRegistry.name(CreeperCommandHandler.class, rootCommand)).inc();
            CommandAuditLog.logCommand((String) e.getMessage(), session.getUsername().get());
        }
        commandMeter.mark();
        e.getChannel().getPipeline().addLast("executed_command", commandByTrigger);
        super.messageReceived(ctx, e);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
        CreeperSession creeperSession = (CreeperSession) e.getChannel().getAttachment();
        gameManager.setPlayerAfk(creeperSession.getUsername().get());
        gameManager.getPlayerManager().removePlayer(creeperSession.getUsername().get());
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
