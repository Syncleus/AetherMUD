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
package com.syncleus.aethermud.bot;


import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.util.concurrent.AbstractIdleService;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;

public class IrcBotService extends AbstractIdleService {

    private final AetherMudConfiguration aetherMudConfiguration;
    private final GameManager gameManager;
    private PircBotX bot;
    private org.pircbotx.Configuration configuration;
    MultiBotManager manager;

    public IrcBotService(AetherMudConfiguration aetherMudConfiguration, GameManager gameManager) {
        this.aetherMudConfiguration = aetherMudConfiguration;
        this.gameManager = gameManager;
    }

    @Override
    protected void startUp() throws Exception {
        manager = newBot();
        manager.start();
    }

    public MultiBotManager newBot() {
        manager = new MultiBotManager();
        configuration = new org.pircbotx.Configuration.Builder()
                .setName(aetherMudConfiguration.ircNickname)
                .setLogin(aetherMudConfiguration.ircUsername)
                .setServerHostname(aetherMudConfiguration.ircServer)
                .addAutoJoinChannel(aetherMudConfiguration.ircChannel)
                .addListener(new MyListener(gameManager, 376))
                .setVersion("Aether MUD IRC But.")
                .setAutoReconnect(true)
                .buildConfiguration();
        bot = new PircBotX(configuration);
        // bot.startBot();
        manager.addBot(bot);
        return manager;
    }

    @Override
    protected void shutDown() throws Exception {
        bot.stopBotReconnect();
        manager.stopAndWait();
    }

    public PircBotX.State getState() {
        return bot.getState();
    }

    public PircBotX getBot() {
        return bot;
    }

    public MultiBotManager getManager() {
        return manager;
    }

    public void setManager(MultiBotManager manager) {
        this.manager = manager;
    }
}
