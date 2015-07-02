package com.comandante.creeper.merchant.bank.commands;


import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DepositCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("deposit", "d");
    final static String description = "Deposit gold.";

    public DepositCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() > 1 && Command.isInteger(originalMessageParts.get(1))) {
                int depositAmt = Integer.parseInt(originalMessageParts.get(1));
                if (areFundsAvailable(depositAmt)) {
                    player.transferGoldToBank(depositAmt);
                    write("Your funds of " + NumberFormat.getNumberInstance(Locale.US).format(depositAmt) + Color.YELLOW + " gold " + Color.RESET + "have been transferred to your bank account."+ "\r\n");
                } else {
                    write("You don't have enough" + Color.YELLOW + " gold "+ Color.RESET + "to cover the deposit amount."+ "\r\n");
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    private boolean areFundsAvailable(int amt) {
        int inventoryGold = playerManager.getPlayerMetadata(playerId).getGold();
        return (inventoryGold >= amt);
    }

}
