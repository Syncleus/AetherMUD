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
package com.syncleus.aethermud.bot.command.commands;

import com.syncleus.aethermud.bot.command.BotCommandManager;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.stats.Levels;
import com.google.api.client.util.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WhoBotCommand extends BotCommand {

    static Set<String> triggers = Sets.newHashSet("who");
    static String helpUsage = "who";
    static String helpDescription = "Who is connected to the mud?";

    public WhoBotCommand(BotCommandManager botCommandManager) {
        super(botCommandManager, triggers, helpUsage, helpDescription);
    }

    @Override
    public List<String> process() {
        ArrayList<String> resp = Lists.newArrayList();
        Set<Player> allPlayers = botCommandManager.getGameManager().getAllPlayers();
        for (Player player: allPlayers) {
            String line = player.getPlayerName() + " (level " + Levels.getLevel(player.getStats().getExperience()) + ") - " + player.getCurrentRoom().getRoomTitle();
            resp.add(line);
        }
        return resp;
    }
}
