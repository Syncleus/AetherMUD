package com.comandante.creeper;


import com.comandante.creeper.managers.GameManager;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.AbstractIdleService;
import org.pircbotx.PircBotX;

public class IrcBotService extends AbstractExecutionThreadService {

    private final CreeperConfiguration creeperConfiguration;
    private final GameManager gameManager;
    private PircBotX bot;
    private org.pircbotx.Configuration configuration;
    public IrcBotService(CreeperConfiguration creeperConfiguration, GameManager gameManager) {
        this.creeperConfiguration = creeperConfiguration;
        this.gameManager = gameManager;
    }

    @Override
    protected void run() throws Exception {
        bot.startBot();
    }

    @Override
    protected void startUp() throws Exception {
        configuration = new org.pircbotx.Configuration.Builder()
                .setName(creeperConfiguration.ircNickname)
                .setLogin(creeperConfiguration.ircUsername)
                .setServerHostname(creeperConfiguration.ircServer)
                .addAutoJoinChannel(creeperConfiguration.ircChannel)
                .addListener(new MyListener(gameManager, 376))
                .buildConfiguration();

        bot = new PircBotX(configuration);
    }

    @Override
    protected void shutDown() throws Exception {
        bot.stopBotReconnect();
    }

    public PircBotX.State getState() {
        return bot.getState();
    }

    public PircBotX getBot() {
        return bot;
    }
}
