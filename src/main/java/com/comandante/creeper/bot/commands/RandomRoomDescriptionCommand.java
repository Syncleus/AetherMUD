package com.comandante.creeper.bot.commands;


import com.comandante.creeper.bot.BotCommandManager;
import com.comandante.creeper.world.Coords;
import com.comandante.creeper.world.Room;
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
        String mapString = botCommandManager.getGameManager().getMapsManager().drawMap(randomRoom.getRoomId(), new Coords(5, 5));
        String[] split = mapString.split("\\r?\\n");
        for (String s: split) {
            output.add(s);
        }
        return output;
    }

    private int randInt(int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
