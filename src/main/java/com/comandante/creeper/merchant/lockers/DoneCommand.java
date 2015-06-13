package com.comandante.creeper.merchant.lockers;

import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.google.common.base.Optional;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.Arrays;
import java.util.List;

public class DoneCommand extends LockerCommand {

    final static List<String> validTriggers = Arrays.asList("done");
    final static String description = "Complete transaction.";

    public DoneCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        creeperSession.setGrabMerchant(Optional.<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>>absent());
        e.getChannel().getPipeline().remove("executed_command");
        e.getChannel().getPipeline().remove("executed_locker_command");
        String s = gameManager.buildPrompt(playerId);
        write(s);
    }
}
