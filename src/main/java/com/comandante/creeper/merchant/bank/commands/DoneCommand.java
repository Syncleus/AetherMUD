package com.comandante.creeper.merchant.bank.commands;


import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.server.Color;
import com.google.common.base.Optional;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DoneCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("done");
    final static String description = "Complete transaction.";

    public DoneCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        gameManager.getChannelUtils().write(playerId, "Thanks, COME AGAIN." + "\r\n" + "\r\n" + "\r\n", true);
        creeperSession.setGrabMerchant(Optional.<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>>absent());
        e.getChannel().getPipeline().remove("executed_command");
        e.getChannel().getPipeline().remove("executed_bank_command");
    }
}
