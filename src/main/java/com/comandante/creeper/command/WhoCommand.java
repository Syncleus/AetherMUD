package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class WhoCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("who");
    final static String description = "Drop an item";

    public WhoCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            Table t = new Table(3, BorderStyle.CLASSIC_COMPATIBLE,
                    ShownBorders.HEADER_AND_FIRST_COLLUMN);
            t.setColumnWidth(0, 8, 14);
            t.setColumnWidth(1, 14, 16);
            t.setColumnWidth(2, 26, 26);
            t.addCell("player");
            t.addCell("ip address");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());
                t.addCell(allPlayer.getChannel().getRemoteAddress().toString().substring(1).split(":")[0]);
            }
            write(t.render());
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}
