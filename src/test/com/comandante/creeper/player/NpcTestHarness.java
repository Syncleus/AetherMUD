package com.comandante.creeper.player;

import com.comandante.creeper.ConfigureCommands;
import com.comandante.creeper.CreeperConfiguration;
import com.comandante.creeper.Items.ItemUseRegistry;
import com.comandante.creeper.Main;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcBuilder;
import com.comandante.creeper.npc.NpcExporter;
import com.comandante.creeper.server.ChannelCommunicationUtils;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.world.MapsManager;
import com.comandante.creeper.world.RoomManager;
import com.comandante.creeper.world.WorldExporter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.configuration.MapConfiguration;
import org.jboss.netty.channel.Channel;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

public class NpcTestHarness {

    private GameManager gameManager;
    private EntityManager entityManager;
    private int totalFightRounds = 0;

    @Before
    public void setUp() throws Exception {
        ChannelCommunicationUtils channelUtils = new ChannelCommunicationUtils() {
            @Override
            public void write(String playerId, String message) {}

            @Override
            public void write(String playerId, String message, boolean leadingBlankLine) {}
        };
        CreeperConfiguration creeperConfiguration = new CreeperConfiguration(new MapConfiguration(Maps.newHashMap()));
        DB db = DBMaker.newMemoryDB().closeOnJvmShutdown().make();
        PlayerManager playerManager = new PlayerManager(db, new SessionManager());
        RoomManager roomManager = new RoomManager(playerManager);
        MapsManager mapsManager = new MapsManager(creeperConfiguration, roomManager);
        EntityManager entityManager = new EntityManager(roomManager, playerManager, db);
        GameManager gameManager = new GameManager(creeperConfiguration, roomManager, playerManager, entityManager, mapsManager, channelUtils);
        WorldExporter worldExporter = new WorldExporter(roomManager, mapsManager, gameManager.getFloorManager(), entityManager, gameManager);
        worldExporter.buildTestworld();
        ConfigureCommands.configure(gameManager);
        ItemUseRegistry.configure();
        this.entityManager = entityManager;
        this.gameManager = gameManager;
    }

    @Test
    public void testCombat() throws Exception {
        List<Npc> npcsFromFile = NpcExporter.getNpcsFromFile(gameManager);
        Npc treeBerseker = npcsFromFile.stream().filter(npc -> npc.getName().equals("tree berserker")).collect(Collectors.toList()).get(0);

        int playerWins = 0;
        int npcWins = 0;

        totalFightRounds = 0;
        int totalIterations = 1000;
        for (int i = 0; i < totalIterations; i++) {
            String username = UUID.randomUUID().toString();
            Player player = createRandomPlayer(username);
            Npc npc = new NpcBuilder(treeBerseker).createNpc();
            gameManager.getEntityManager().addEntity(npc);
            player.getCurrentRoom().addPresentNpc(npc.getEntityId());
            player.addActiveFight(npc);
            if (conductFight(player, npc)) {
                playerWins++;
            } else {
                npcWins++;
            }
            player.getCurrentRoom().removePresentNpc(npc.getEntityId());
            entityManager.deleteNpcEntity(npc.getEntityId());
            player.getCurrentRoom().removePresentPlayer(player.getPlayerId());
            if (i%100==0) {
                System.out.println("Fight iterations: " + i);
            }
        }
        System.out.println("Player Wins: " + playerWins);
        System.out.println("Npc Wins: " + npcWins);
        System.out.println("Average rounds: " + totalFightRounds / totalIterations);
    }

    private boolean conductFight(Player player, Npc npc) {
        int i = 0;
        try {
            for (i = 0; i < 1000; i++) {
                player.run();
                npc.run();
                if (!npc.getIsAlive().get()) {
                    return true;
                }
                if (player.isActive(CoolDownType.DEATH)) {
                    return false;
                }
            }
            return false;
        } finally {
            totalFightRounds += i;
        }
    }

    private Player createRandomPlayer(String username) throws FileNotFoundException {
        createUser(username, "3333333");
        Player player = new Player(username, gameManager);
        Channel mockChannel = mock(Channel.class);
        CreeperSession creeperSession = new CreeperSession();
        creeperSession.setUsername(Optional.of(username));
        player.setChannel(mockChannel);
        gameManager.getPlayerManager().addPlayer(player);
        gameManager.placePlayerInLobby(player);
        gameManager.getPlayerManager().getSessionManager().putSession(creeperSession);
        return player;
    }

    private void createUser(String username, String password) {
        PlayerMetadata playerMetadata = new PlayerMetadata(username, password, Main.createPlayerId(username), PlayerStats.DEFAULT_PLAYER.createStats(), 0, Sets.newHashSet(PlayerRole.MORTAL), new String[0], 0, new String[0]);
        gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
    }

}
