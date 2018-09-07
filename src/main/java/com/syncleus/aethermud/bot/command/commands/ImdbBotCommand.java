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
package com.syncleus.aethermud.bot.command.commands;

import com.syncleus.aethermud.bot.command.BotCommandManager;
import com.google.api.client.util.Lists;
import com.google.common.collect.Sets;
import com.omertron.omdbapi.OMDBException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ImdbBotCommand extends BotCommand {

    static Set<String> triggers = Sets.newHashSet("mov", "movie", "imdb");
    static String helpUsage = "imdb raging bull";
    static String helpDescription = "Get movie information from OMDB.";
    private static final Logger log = Logger.getLogger(ImdbBotCommand.class);

    public ImdbBotCommand(BotCommandManager botCommandManager) {
        super(botCommandManager, triggers, helpUsage, helpDescription);
    }

    @Override
    public List<String> process() {
        ArrayList<String> resp = Lists.newArrayList();
        String argumentString = joinArgs(args);
        try {
            resp.addAll(botCommandManager.getOmdbManager().getMovieInfo(argumentString));
        } catch (OMDBException e) {
            log.error(e);
        }
        return resp;
    }
}
