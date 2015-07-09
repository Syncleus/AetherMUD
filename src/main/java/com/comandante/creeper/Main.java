package com.comandante.creeper;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.PickledGraphite;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.server.CreeperServer;
import com.comandante.creeper.world.MapsManager;
import com.comandante.creeper.world.RoomManager;
import com.comandante.creeper.world.WorldExporter;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.*;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    final public static MetricRegistry metrics = new MetricRegistry();

    final public static String CREEPER_VERSION = "0.5 ALPHA";

    final public static Set<Character> vowels = new HashSet<Character>(Arrays.asList('a', 'e', 'i', 'o', 'u'));

    public static void main(String[] args) throws Exception {

        CreeperConfiguration creeperConfiguration = new CreeperConfiguration(buildConfiguration(args));

        final JmxReporter jmxReporter = JmxReporter.forRegistry(metrics).build();
        jmxReporter.start();

        if (creeperConfiguration.isProduction) {
            final PickledGraphite pickledGraphite = new PickledGraphite(new InetSocketAddress(creeperConfiguration.graphiteHostname, creeperConfiguration.graphitePort));
            final GraphiteReporter reporter = GraphiteReporter.forRegistry(metrics)
                    .prefixedWith(creeperConfiguration.mudName)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(pickledGraphite);
            reporter.start(1, TimeUnit.MINUTES);
        }

        Files.isDirectory().apply(new File("world/"));

        DB db = DBMaker.newFileDB(new File("world/" + creeperConfiguration.databaseFileName)).closeOnJvmShutdown().make();

        PlayerManager playerManager = new PlayerManager(db, new SessionManager());
        playerManager.createAllGauges();

        RoomManager roomManager = new RoomManager(playerManager);

        startUpMessage("Configuring core systems.");
        MapsManager mapsManager = new MapsManager(creeperConfiguration, roomManager);
        ChannelUtils channelUtils = new ChannelUtils(playerManager, roomManager);
        EntityManager entityManager = new EntityManager(roomManager, playerManager, db, channelUtils);
        GameManager gameManager = new GameManager(creeperConfiguration, roomManager, playerManager, entityManager, mapsManager, channelUtils);

        startUpMessage("Reading world from disk.");
        WorldExporter worldExporter = new WorldExporter(roomManager, mapsManager, gameManager.getFloorManager(), entityManager);
        worldExporter.readWorldFromDisk();

        startUpMessage("Configuring commands");
        ConfigureCommands.configure(gameManager);

        startUpMessage("Configure Bank commands");
        ConfigureCommands.configureBankCommands(gameManager);

        startUpMessage("Configure Locker commands");
        ConfigureCommands.configureLockerCommands(gameManager);

        startUpMessage("Configuring npcs and merchants");
        ConfigureNpc.configure(entityManager, gameManager);
        CreeperServer creeperServer = new CreeperServer(creeperConfiguration.telnetPort);

        startUpMessage("Generating map data.");
        mapsManager.generateAllMaps();

        startUpMessage("Configuring default inventorySize limits");
        BackportCommands.configureDefaultInventorySize(entityManager, gameManager);

        startUpMessage("Configuring default maxEffects limits");
        BackportCommands.configureDefaultMaxEffectSize(entityManager, gameManager);

        BackportCommands.configureFibsHealth(entityManager, gameManager);

        startUpMessage("Creeper MUD engine started");

        creeperServer.run(gameManager);
        startUpMessage("Creeper MUD engine online");

        if (creeperConfiguration.isIrcEnabled) {
            startUpMessage("Starting irc server.");
            configureIrc(gameManager);
        }
    }

    public static void startUpMessage(String message) {
        log.info(message);
    }

    public static String createPlayerId(String playerName) {
        return new String(Base64.encodeBase64(playerName.getBytes()));
    }

    private static Configuration buildConfiguration(String[] args) throws ConfigurationException {
        CompositeConfiguration compositeConfiguration = new CompositeConfiguration();
        File propFile;
        if (args.length == 0) {
            propFile = new File("server.config");
        } else {
            propFile = new File(args[0]);
        }
        if (propFile.exists()) {
            compositeConfiguration.addConfiguration(new PropertiesConfiguration(propFile));
        }
        compositeConfiguration.addConfiguration(new SystemConfiguration());
        return compositeConfiguration;
    }

    public static void configureIrc(GameManager gameManager) throws Exception {
        gameManager.getIrcBotService().startAsync();
    }
}
