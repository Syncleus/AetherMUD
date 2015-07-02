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

public class WithdrawalCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("withdrawal", "get", "w");
    final static String description = "Withdrawal gold.";

    public WithdrawalCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            if (originalMessageParts.size() > 1 && Command.isInteger(originalMessageParts.get(1))) {
                int withdrawalAmount = Integer.parseInt(originalMessageParts.get(1));
                if (areBankFundsAvailable(withdrawalAmount)) {
                    player.transferBankGoldToPlayer(withdrawalAmount);
                    write("Your funds of " + NumberFormat.getNumberInstance(Locale.US).format(withdrawalAmount) + Color.YELLOW + " gold " + Color.RESET + "have been withdrawn from your bank account." + "\r\n");
                } else {
                    write("You don't have enough" + Color.YELLOW + " gold "+ Color.RESET + "to cover the withdrawal amount." + "\r\n");
                }
            }
        } finally {
            super.messageReceived(ctx, e);
        }
    }

    private boolean areBankFundsAvailable(int amt) {
        int bankGold = playerManager.getPlayerMetadata(playerId).getGoldInBank();
        return (bankGold >= amt);
    }

}