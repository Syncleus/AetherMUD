package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Levels;
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
        execCommand(ctx, e, () -> {
            StringBuilder output = new StringBuilder();
            // output.append(Color.MAGENTA + "Who--------------------------------" + Color.RESET).append("\r\n");
            Table t = new Table(4, BorderStyle.BLANKS,
                    ShownBorders.NONE);
            t.setColumnWidth(0, 14, 24);
            t.setColumnWidth(1, 7, 7);
            t.setColumnWidth(2, 16, 16);
            t.addCell("Player");
            t.addCell("Level");
            t.addCell("XP");
            t.addCell("Location");
            Set<Player> allPlayers = gameManager.getAllPlayers();
            for (Player allPlayer : allPlayers) {
                t.addCell(allPlayer.getPlayerName());
                t.addCell(Long.toString(Levels.getLevel(playerManager.getPlayerMetadata(allPlayer.getPlayerId()).getStats().getExperience())));
                t.addCell(NumberFormat.getNumberInstance(Locale.US).format((playerManager.getPlayerMetadata(allPlayer.getPlayerId()).getStats().getExperience())));
                t.addCell(roomManager.getPlayerCurrentRoom(allPlayer).get().getRoomTitle());
            }
            output.append(t.render());
            write(output.toString());
        });
    }
}
