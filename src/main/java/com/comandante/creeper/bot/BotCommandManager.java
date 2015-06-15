package com.comandante.creeper.bot;


import com.comandante.creeper.managers.GameManager;

public class BotCommandManager {

    private final GameManager gameManager;
    private final WeatherManager weatherManager;
    private final ChuckNorrisManager chuckNorrisManager;

    public BotCommandManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.weatherManager = new WeatherManager(gameManager.getCreeperConfiguration());
        this.chuckNorrisManager = new ChuckNorrisManager(gameManager.getCreeperConfiguration());
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public WeatherManager getWeatherManager() {
        return weatherManager;
    }

    public ChuckNorrisManager getChuckNorrisManager() {
        return chuckNorrisManager;
    }
}
