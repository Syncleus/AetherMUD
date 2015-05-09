package com.comandante.creeper.command;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Levels;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMetadata;
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


public class XpCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("xp");
    final static String description = "Display experience to next level.";
    final static String correctUsage = "xp";

    public XpCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        configure(e);
        try {
            PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
            int nextLevel = Levels.getLevel(playerMetadata.getStats().getExperience()) + 1;
            int expToNextLevel = Levels.getXp(nextLevel) - playerMetadata.getStats().getExperience();
            write(expToNextLevel + " experience to level " + nextLevel + ".\r\n");
        } finally {
            super.messageReceived(ctx, e);
        }
    }
}