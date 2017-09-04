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
package com.syncleus.aethermud;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.PickledGraphite;
import com.syncleus.aethermud.configuration.ConfigureCommands;
import com.syncleus.aethermud.configuration.ConfigureNpc;
import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SessionManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.player.PlayerManagementManager;
import com.syncleus.aethermud.player.PlayerManager;
import com.syncleus.aethermud.server.communication.ChannelUtils;
import com.syncleus.aethermud.server.telnet.AetherMudServer;
import com.syncleus.aethermud.storage.WorldStorage;
import com.syncleus.aethermud.storage.graphdb.*;
import com.syncleus.aethermud.world.MapsManager;
import com.syncleus.aethermud.world.RoomManager;
import com.google.common.io.Files;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.WrappedFramedGraph;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.*;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    final public static MetricRegistry metrics = new MetricRegistry();

    final public static Set<Character> vowels = new HashSet<Character>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
    public static final Set<Class<?>> FRAMED_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(new Class<?>[]{
        PlayerData.class,
        NpcData.class,
        StatsData.class,
        ItemData.class,
        LootData.class,
        SpawnRuleData.class,
        CoolDownData.class,
        EffectData.class
    })));

    public static String getAetherMudVersion() {

        String buildVersion = Main.class.getPackage().getSpecificationVersion();
        if (buildVersion == null || buildVersion.isEmpty()) {
            return "aethermud-local-dev";
        }

        return buildVersion ;
    }

    public static void main(String[] args) throws Exception {

        AetherMudConfiguration aetherMudConfiguration = new AetherMudConfiguration(buildConfiguration(args));

        final JmxReporter jmxReporter = JmxReporter.forRegistry(metrics).build();
        jmxReporter.start();

        if (aetherMudConfiguration.isProduction) {
            final PickledGraphite pickledGraphite = new PickledGraphite(new InetSocketAddress(aetherMudConfiguration.graphiteHostname, aetherMudConfiguration.graphitePort));
            final GraphiteReporter reporter = GraphiteReporter.forRegistry(metrics)
                    .prefixedWith(aetherMudConfiguration.mudName)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .filter(MetricFilter.ALL)
                    .build(pickledGraphite);
            reporter.start(1, TimeUnit.MINUTES);
        }

        Files.isDirectory().apply(new File("world/"));

        Graph graph = TinkerGraph.open();
        File f = new File(aetherMudConfiguration.databaseFileName);
        if(f.exists() && !f.isDirectory()) {
            try {
                graph.io(IoCore.graphson()).readGraph(aetherMudConfiguration.databaseFileName);
            } catch (IOException e) {
                throw new IllegalStateException("Could not read from graph file despite being present.", e);
            }
        }
        WrappedFramedGraph<Graph> framedGraph = new DelegatingFramedGraph(graph, FRAMED_TYPES);

        GraphDbAetherMudStorage graphStorage = new GraphDbAetherMudStorage(framedGraph);
        graphStorage.startAsync();
        graphStorage.awaitRunning();

        PlayerManager playerManager = new PlayerManager(graphStorage, new SessionManager());
        playerManager.createAllGauges();

        RoomManager roomManager = new RoomManager(playerManager);

        startUpMessage("Configuring core systems.");
        MapsManager mapsManager = new MapsManager(aetherMudConfiguration, roomManager);
        ChannelUtils channelUtils = new ChannelUtils(playerManager, roomManager);
        EntityManager entityManager = new EntityManager(graphStorage, roomManager, playerManager);
        GameManager gameManager = new GameManager(graphStorage, framedGraph, aetherMudConfiguration, roomManager, playerManager, entityManager, mapsManager, channelUtils, HttpClients.createDefault());

        startUpMessage("Reading world from disk.");
        WorldStorage worldExporter = new WorldStorage(roomManager, mapsManager, gameManager.getFloorManager(), entityManager, gameManager);
        worldExporter.readWorldFromDisk();

        startUpMessage("Creating and registering Player Management MBeans.");
        PlayerManagementManager playerManagementManager = new PlayerManagementManager(gameManager);
        playerManagementManager.processPlayersMarkedForDeletion();
        playerManagementManager.createAndRegisterAllPlayerManagementMBeans();

        startUpMessage("Configuring commands");
        ConfigureCommands.configure(gameManager);

        startUpMessage("Configure Bank commands");
        ConfigureCommands.configureBankCommands(gameManager);

        startUpMessage("Configure Locker commands");
        ConfigureCommands.configureLockerCommands(gameManager);

        startUpMessage("Configure Player Class Selection commands");
        ConfigureCommands.configurePlayerClassSelector(gameManager);

        startUpMessage("Configuring npcs and merchants");
        ConfigureNpc.configure(entityManager, gameManager);
        AetherMudServer aetherMudServer = new AetherMudServer(aetherMudConfiguration.telnetPort);

        startUpMessage("Aether MUD engine started");

        aetherMudServer.run(gameManager);
        startUpMessage("Aether MUD engine online");

        if (aetherMudConfiguration.isIrcEnabled) {
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
