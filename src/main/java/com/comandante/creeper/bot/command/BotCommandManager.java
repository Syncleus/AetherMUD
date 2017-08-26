/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.bot.command;


import com.comandante.creeper.core_game.GameManager;

public class BotCommandManager {

    private final GameManager gameManager;
    private final WeatherManager weatherManager;
    private final ChuckNorrisManager chuckNorrisManager;
    private final DictionaryManager dictionaryManager;
    private final OmdbManager omdbManager;

    public BotCommandManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.weatherManager = new WeatherManager(gameManager.getCreeperConfiguration());
        this.chuckNorrisManager = new ChuckNorrisManager(gameManager.getCreeperConfiguration());
        this.dictionaryManager = new DictionaryManager(gameManager.getCreeperConfiguration());
        this.omdbManager = new OmdbManager();
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

    public DictionaryManager getDictionaryManager() {
        return dictionaryManager;
    }

    public OmdbManager getOmdbManager() {
        return omdbManager;
    }
}
