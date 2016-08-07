package com.comandante.creeper.command;


import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.merchant.MerchantCommandHandler;
import com.comandante.creeper.merchant.bank.commands.BankCommand;
import com.comandante.creeper.merchant.lockers.LockerCommand;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TalkCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("talk");
    final static String description = "Talk to a merchant.";
    final static String correctUsage = "talk <merchant name>";

    public TalkCommand(GameManager gameManager) {
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
                if (merchant.getValidTriggers().contains(desiredMerchantTalk)) {
                    write(merchant.getWelcomeMessage() + "\r\n");
                    if (merchant.getMerchantType() == Merchant.MerchantType.BASIC) {
                        write(merchant.getMenu() + "\r\n");
                        gameManager.getChannelUtils().write(playerId, "\r\n" + MerchantCommandHandler.buildPrompt());
                    } else if (merchant.getMerchantType() == Merchant.MerchantType.BANK) {
                        write(BankCommand.getPrompt());
                    } else if (merchant.getMerchantType() == Merchant.MerchantType.LOCKER) {
                        write(LockerCommand.getPrompt());
                    }
                    creeperSession.setGrabMerchant(Optional.of(
                            new CreeperEntry<Merchant, SimpleChannelUpstreamHandler>(merchant, this)));
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}