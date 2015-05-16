package com.comandante.creeper.command.admin;

import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Levels;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UsersCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("users");
    final static String description = "Display extended inforation about who is logged in.";
    final static String correctUsage = "users";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public UsersCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            Table t = new Table(4, BorderStyle.CLASSIC_COMPATIBLE,
                    ShownBorders.HEADER_AND_COLUMNS);
            t.setColumnWidth(0, 8, 14);
            t.addCell("Player");
            t.addCell("IP");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());
                t.addCell(allPlayer.getChannel().getRemoteAddress().toString());
            }
            write(t.render());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
