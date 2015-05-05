package com.comandante.creeper.merchant.bank.commands;


import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.ConfigureCommands;
import com.comandante.creeper.Main;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.command.CommandAuditLog;
import com.comandante.creeper.command.UnknownCommand;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.merchant.bank.commands.AccountQueryCommand;
import com.comandante.creeper.merchant.bank.commands.BankCommand;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.CreeperCommandRegistry;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.server.MultiLineInputHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.UUID;

public class BankCommandHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final Merchant merchant;

    public BankCommandHandler(GameManager gameManager, Merchant merchant) {
        this.gameManager = gameManager;
        this.merchant = merchant;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String rootCommand = getRootCommand(e);
        CreeperSession session = (CreeperSession) e.getChannel().getAttachment();
        BankCommand commandByTrigger = ConfigureCommands.bankCommandRegistry.getCommandByTrigger(rootCommand);
        e.getChannel().getPipeline().addLast("executed_bank_command", commandByTrigger);
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