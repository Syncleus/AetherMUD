package com.comandante.creeper.merchant.playerclass_selector;

import com.comandante.creeper.CreeperUtils;
import com.comandante.creeper.classes.PlayerClass;
import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;


public class ChooseClassCommand extends PlayerClassCommand {

    private final PlayerClass playerClass;

    public ChooseClassCommand(PlayerClass playerClass, GameManager gameManager) {
        super(gameManager);
        this.playerClass = playerClass;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            configure(e);
            player.setPlayerClass(playerClass);
            write("You are now and forever, a " + CreeperUtils.capitalize(playerClass.getIdentifier()) + "\r\n");
            e.getChannel().getPipeline().remove("executed_command");
            e.getChannel().getPipeline().remove("executed_playerclass_command");
            String s = gameManager.buildPrompt(playerId);
            write(s);
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}