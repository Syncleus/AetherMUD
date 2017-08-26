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
package com.syncleus.aethermud.bot.command;

import com.google.common.collect.Maps;
import com.syncleus.aethermud.bot.command.commands.*;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BotCommandFactory {

    private final Map<String, BotCommand> botCommandRegistry = Maps.newHashMap();

    public BotCommandFactory(BotCommandManager botCommandManager) {
        addCommand(new WeatherBotCommand(botCommandManager));
        addCommand(new WhoBotCommand(botCommandManager));
        addCommand(new CheckNorrisBotCommand(botCommandManager));
        addCommand(new DictionaryBotCommand(botCommandManager));
        addCommand(new ImdbBotCommand(botCommandManager));
        addCommand(new ForecastCommand(botCommandManager));
        addCommand(new RandomRoomDescriptionCommand(botCommandManager));
        addCommand(new CardsCommand(botCommandManager));
    }

    public BotCommand getCommand(MessageEvent event, String originalFullCmd) {
        List<String> originalMessageParts = new ArrayList<>(Arrays.asList(originalFullCmd.split(" ")));
        if (originalMessageParts.size() == 0) {
            return null;
        }
        String rootCommand = originalMessageParts.get(0);
        BotCommand botCommand = botCommandRegistry.get(rootCommand);
        if (botCommand != null) {
            if (event != null) {
                botCommand.setMessageEvent(event);
            }
            botCommand.setOriginalFullCommand(originalFullCmd);
        }
        return botCommand;
    }

    private void addCommand(BotCommand botCommand) {
        for (String trigger: botCommand.getTriggers()) {
            botCommandRegistry.put(trigger, botCommand);
        }
    }
}
