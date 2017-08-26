/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.command.commands;

import com.codahale.metrics.Meter;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.PlayerMetadata;
import com.syncleus.aethermud.stats.Levels;
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
import java.util.Optional;


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
            Optional<PlayerMetadata> playerMetadataOptional = playerManager.getPlayerMetadata(player.getPlayerId());
            if (!playerMetadataOptional.isPresent()) {
                return;
            }
            PlayerMetadata playerMetadata = playerMetadataOptional.get();
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
