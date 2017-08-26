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
import com.google.api.client.util.Lists;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ForecastCommand extends BotCommand {

    static Set<String> triggers = Sets.newHashSet("forecast");
    static String helpUsage = "forecast 97034";
    static String helpDescription = "Obtain weather forecast using a zip code or city name.";

    public ForecastCommand(BotCommandManager botCommandManager) {
        super(botCommandManager, triggers, helpUsage, helpDescription);
    }

    @Override
    public List<String> process() {
        ArrayList<String> resp = Lists.newArrayList();
        String argumentString = joinArgs(args);
        if (isNumeric(argumentString)) {
            try {
                resp.addAll(botCommandManager.getWeatherManager().getWeatherForecast(argumentString));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] split = argumentString.split(",");
            try {
                resp.addAll(botCommandManager.getWeatherManager().getWeatherForecast(split[0], split[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }
}
