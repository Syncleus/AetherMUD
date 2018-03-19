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
import com.syncleus.aethermud.world.model.Room;
import com.google.api.client.util.Lists;
import com.google.common.collect.Sets;

import java.util.*;

public class RandomRoomDescriptionCommand extends BotCommand {

    static Set<String> triggers = Sets.newHashSet("blazecraft");
    static String helpUsage = "blazecraft";
    static String helpDescription = "A random room description, courtesy of BLAZECRAFT";
    private final Random random = new Random();

    public RandomRoomDescriptionCommand(BotCommandManager botCommandManager) {
        super(botCommandManager, triggers, helpUsage, helpDescription);
    }

    @Override
    public List<String> process() {
        Map<Integer, Room> getrooms = botCommandManager.getGameManager().getRoomManager().getrooms();
        int size = getrooms.size();
        int randomRoomNumber = randInt(1, size);
        Room randomRoom = getrooms.get(randomRoomNumber);
        ArrayList<String> output = Lists.newArrayList();
        output.add(randomRoom.getRoomTitle());
        output.add(" ");
        output.add(randomRoom.getRoomDescription());
        /*String mapString = botCommandManager.getGameManager().getMapsManager().drawMap(randomRoom.getRoomIds(), new Coords(5, 5));
        String[] split = mapString.split("\\r?\\n");
        for (String s: split) {
            output.add(s);
        }*/
        return output;
    }

    private int randInt(int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
