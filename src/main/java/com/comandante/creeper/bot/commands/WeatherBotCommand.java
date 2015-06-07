package com.comandante.creeper.bot.commands;

import com.comandante.creeper.bot.BotCommandManager;
import com.google.api.client.util.Lists;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WeatherBotCommand extends BotCommand {

    static Set<String> triggers = Sets.newHashSet("weather");
    static String helpUsage = "weather 97034";
    static String helpDescription = "Obtain weather using a zip code or city name.";

    public WeatherBotCommand(BotCommandManager botCommandManager) {
        super(botCommandManager, triggers, helpUsage, helpDescription);
    }

    @Override
    public List<String> process() {
        ArrayList<String> resp = Lists.newArrayList();
        String argumentString = joinArgs(args);
        if (isNumeric(argumentString)) {
            try {
                resp.addAll(botCommandManager.getWeatherManager().getWeather(argumentString));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] split = argumentString.split(",");
            try {
                resp.addAll(botCommandManager.getWeatherManager().getWeather(split[0], split[1]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }
}
