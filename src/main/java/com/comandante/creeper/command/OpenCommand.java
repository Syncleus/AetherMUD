package com.comandante.creeper.command;

import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.merchant.lockers.LockerCommand;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by kearney on 6/12/15.
 */
public class OpenCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("open", "o");
    final static String description = "Open a locker.";
    final static String correctUsage = "open lockers";

    public OpenCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ;
        try {
            if (creeperSession.getGrabMerchant().isPresent()) {
                creeperSession.setGrabMerchant(Optional.<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>>absent());
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
                    creeperSession.setGrabMerchant(Optional.of(
                            new CreeperEntry<Merchant, SimpleChannelUpstreamHandler>(merchant, this)));
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}