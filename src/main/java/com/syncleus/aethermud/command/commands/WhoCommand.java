/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.stats.Levels;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.*;

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
                try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
                    Optional<PlayerData> playerMetadataOptional = tx.getStorage().getPlayerMetadata(allPlayer.getPlayerId());
                    if (!playerMetadataOptional.isPresent()){
                        continue;
                    }
                    PlayerData playerData = playerMetadataOptional.get();
                    t.addCell(Long.toString(Levels.getLevel(playerData.getStatData().getExperience())));
                    t.addCell(NumberFormat.getNumberInstance(Locale.US).format((playerData.getStatData().getExperience())));
                    t.addCell(roomManager.getPlayerCurrentRoom(allPlayer).get().getRoomTitle());
                }
            }
            output.append(t.render());
            write(output.toString());
        });
    }
}
