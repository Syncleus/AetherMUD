package com.comandante.creeper.command.commands;

import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.CreeperSession;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final List<String> validTriggers;
    private final String description;

    protected Command(GameManager gameManager, List<String> validTriggers, String description) {
        this.gameManager = gameManager;
        this.validTriggers = validTriggers;
        this.description = description;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        e.getChannel().getPipeline().remove(ctx.getHandler());
        super.messageReceived(ctx, e);
    }

    public CreeperSession getCreeperSession(Channel channel) {
        return (CreeperSession) channel.getAttachment();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public List<String> getValidTriggers() {
        return validTriggers;
    }

    public String getDescription() {
        return description;
    }

    public String getPlayerId(CreeperSession creeperSession) {
        return Main.createPlayerId(creeperSession.getUsername().get());
    }

    public String getRootCommand(MessageEvent e){
        String origMessage = (String) e.getMessage();
        return origMessage.split(" ")[0].toLowerCase();
    }

    public List<String> getOriginalMessageParts(MessageEvent e) {
        String origMessage = (String) e.getMessage();
        return new ArrayList<>(Arrays.asList(origMessage.split(" ")));
    }
}