package com.comandante.creeper.command.commands.admin;

import com.comandante.creeper.Main;
import com.comandante.creeper.command.commands.Command;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by chrisk on 5/13/17.
 */
public class RestartCommand extends Command {



    final static List<String> validTriggers = Arrays.asList("restart");
    final static String description = "restart server.";
    final static String correctUsage = "restart";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.GOD);

    public RestartCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommandThreadSafe(ctx, e, BounceIrcBotCommand.class, () -> {
            StringBuilder cmd = new StringBuilder();
            cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
            for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
                cmd.append(jvmArg + " ");
            }
            cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
            cmd.append(Main.class.getName()).append(" ");
//            for (String arg : args) {
//                cmd.append(arg).append(" ");
//            }
            Runtime.getRuntime().exec(cmd.toString() + " server.config");
            System.exit(0);
        });
    }
}