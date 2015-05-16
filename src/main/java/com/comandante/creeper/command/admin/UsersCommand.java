package com.comandante.creeper.command.admin;

import com.comandante.creeper.command.Command;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerRole;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
            t.addCell("Logged in since");
            t.addCell("Last activity");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());

                InetSocketAddress remoteAddress = (InetSocketAddress) allPlayer.getChannel().getRemoteAddress();
                String remoteUsersHost = remoteAddress.getHostString();
                t.addCell(remoteUsersHost);

                DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String loginTime = format.format(new Date(playerManager.getSessionManager().getSession(allPlayer.getPlayerId()).getInitialLoginTime()));
                t.addCell(loginTime);

                String lastActivity = format.format(new Date(playerManager.getSessionManager().getSession(allPlayer.getPlayerId()).getLastActivity()));
                t.addCell(lastActivity);
            }
            write(t.render());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
