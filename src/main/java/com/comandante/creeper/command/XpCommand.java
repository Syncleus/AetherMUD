package com.comandante.creeper.command;

import com.codahale.metrics.Meter;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Levels;
import com.comandante.creeper.player.PlayerMetadata;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class XpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("xp");
    final static String description = "Display experience to next level.";
    final static String correctUsage = "xp";

    public XpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
            long nextLevel = Levels.getLevel(playerMetadata.getStats().getExperience()) + 1;
            long expToNextLevel = Levels.getXp(nextLevel) - playerMetadata.getStats().getExperience();
            Meter meter = Main.metrics.meter("experience-" + player.getPlayerName());

            Table table = new Table(2, BorderStyle.CLASSIC_COMPATIBLE, ShownBorders.NONE);

            table.setColumnWidth(0, 8, 20);
            table.setColumnWidth(1, 10, 20);
            table.addCell("Window");
            table.addCell("XP/sec");
            table.addCell(" 1 min");
            table.addCell(String.valueOf(round(meter.getOneMinuteRate())));
            table.addCell(" 5 min");
            table.addCell(String.valueOf(round(meter.getFiveMinuteRate())));
            table.addCell("15 min");
            table.addCell(String.valueOf(round(meter.getFifteenMinuteRate())));

            write(NumberFormat.getNumberInstance(Locale.US).format(expToNextLevel) + " experience to level " + nextLevel + ".\r\n" + table.render());
        });
    }

    public static double round(double value) {
        int places = 2;
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}