package com.comandante.creeper.merchant.bank.commands;


import com.comandante.creeper.ConfigureCommands;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.server.CreeperSession;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AccountQueryCommand extends BankCommand {

    final static List<String> validTriggers = Arrays.asList("balance", "query", "q");
    final static String description = "Query account balance.";

    public AccountQueryCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            int goldInBank = playerManager.getPlayerMetadata(playerId).getGoldInBank();
            int gold = playerManager.getPlayerMetadata(playerId).getGold();
            write("You have " + goldInBank + Color.YELLOW + " gold" + Color.RESET + " in your bank account."+ "\r\n");
            write("You have " + gold + Color.YELLOW + " gold" + Color.RESET + " in your inventory."+ "\r\n");
        } finally {
            super.messageReceived(ctx, e);
        }
    }

}
