package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
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

public class WhoCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("who");
    final static String description = "Display who is currently logged into the mud.";
    final static String correctUsage = "who";

    public WhoCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            Table t = new Table(4, BorderStyle.CLASSIC_COMPATIBLE,
                    ShownBorders.HEADER_AND_COLUMNS);
            t.setColumnWidth(0, 8, 14);
            t.setColumnWidth(1, 14, 16);
            t.addCell("player");
            t.addCell("ip address");
            t.addCell("XP");
            t.addCell("Location");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());
                t.addCell(allPlayer.getChannel().getRemoteAddress().toString().substring(1).split(":")[0]);
                t.addCell(NumberFormat.getNumberInstance(Locale.US).format((playerManager.getPlayerMetadata(playerId).getStats().getExperience())));
                t.addCell(roomManager.getPlayerCurrentRoom(player).get().getRoomTitle());
            }
            write(t.render());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
