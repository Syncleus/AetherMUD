package com.comandante.creeper.bot;


import com.comandante.creeper.managers.GameManager;

public class BotCommandManager {

    private final GameManager gameManager;
    private final WeatherManager weatherManager;

    public BotCommandManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.weatherManager = new WeatherManager(gameManager.getCreeperConfiguration());
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WeatherManager getWeatherManager() {
        return weatherManager;
    }
}
