package com.comandante.creeper.command.commands;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class UsersCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("users");
    final static String description = "Display extended inforation about who is logged in.";
    final static String correctUsage = "users";

    public UsersCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, null);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Table t = new Table(4, BorderStyle.BLANKS,
                    ShownBorders.NONE);
            t.setColumnWidth(0, 14, 24);
            t.setColumnWidth(1, 18, 18);
            t.setColumnWidth(2, 21, 21);
            t.addCell("Player");
            t.addCell("IP");
            t.addCell("Logged in since");
            t.addCell("Idle");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());

                InetSocketAddress remoteAddress = (InetSocketAddress) allPlayer.getChannel().getRemoteAddress();
                String remoteUsersHost = remoteAddress.getHostString();
                t.addCell(remoteUsersHost);

                SimpleDateFormat loggedInFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                String loginTime = loggedInFormat.format(new Date(playerManager.getSessionManager().getSession(allPlayer.getPlayerId()).getInitialLoginTime()));
                t.addCell(loginTime);

                long lastActivity = playerManager.getSessionManager().getSession(allPlayer.getPlayerId()).getLastActivity();
                String idleTime = getFriendlyTime(new Date(lastActivity));
                t.addCell(idleTime);
            }
            write(t.render());
        });
    }

    public static String getFriendlyTime(Date dateTime) {
        StringBuffer sb = new StringBuffer();
        Date current = Calendar.getInstance().getTime();
        long diffInSeconds = (current.getTime() - dateTime.getTime()) / 1000;

        long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
        long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
        long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
        long years = (diffInSeconds = (diffInSeconds / 12));

        if (years > 0) {
            if (years == 1) {
                sb.append("a year");
            } else {
                sb.append(years + " years");
            }
            if (years <= 6 && months > 0) {
                if (months == 1) {
                    sb.append(" and a month");
                } else {
                    sb.append(" and " + months + " months");
                }
            }
        } else if (months > 0) {
            if (months == 1) {
                sb.append("a month");
            } else {
                sb.append(months + " months");
            }
            if (months <= 6 && days > 0) {
                if (days == 1) {
                    sb.append(" and a day");
                } else {
                    sb.append(" and " + days + " days");
                }
            }
        } else if (days > 0) {
            if (days == 1) {
                sb.append("a day");
            } else {
                sb.append(days + " days");
            }
            if (days <= 3 && hrs > 0) {
                if (hrs == 1) {
                    sb.append(" and an hour");
                } else {
                    sb.append(" and " + hrs + " hours");
                }
            }
        } else if (hrs > 0) {
            if (hrs == 1) {
                sb.append("an hour");
            } else {
                sb.append(hrs + " hours");
            }
            if (min > 1) {
                sb.append(" and " + min + " minutes");
            }
        } else if (min > 0) {
            if (min == 1) {
                sb.append("a minute");
            } else {
                sb.append(min + " minutes");
            }
            if (sec > 1) {
                sb.append(" and " + sec + " seconds");
            }
        } else {
            if (sec <= 1) {
                sb.append("about a second");
            } else {
                sb.append("about " + sec + " seconds");
            }
        }
        return sb.toString();
    }
}
