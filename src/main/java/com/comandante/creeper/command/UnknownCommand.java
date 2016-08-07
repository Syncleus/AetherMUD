package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

public class UnknownCommand extends Command {

    public UnknownCommand(GameManager gameManager) {
        super(gameManager, null, null, null);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ;
        try {
            write(getPrompt(), false);
            e.getChannel().getPipeline().remove(ctx.getHandler());
        } finally {
        }
    }
}
