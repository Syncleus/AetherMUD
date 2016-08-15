package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.TimeTracker;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class TimeCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("time");
    final static String description = "Display the time of day.";
    final static String correctUsage = "time";

    public TimeCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            TimeTracker.TimeOfDay timeOfDay = gameManager.getTimeTracker().getTimeOfDay();
            write(timeOfDay.color + timeOfDay + Color.RESET);
        });
    }
}