package com.comandante.creeper.command;

import com.comandante.creeper.fight.FightManager;
import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class QuitCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("quit");
    final static String description = "Quit the game";
    final static String correctUsage = "quit";

    public QuitCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        if (FightManager.isActiveFight(creeperSession)) {
            write("You can't quit in the middle of a fight!");
        } else {
            gameManager.getPlayerManager().removePlayer(creeperSession.getUsername().get());
        }
    }
}
