package com.comandante.creeper.player;

import com.comandante.creeper.Main;
import com.comandante.creeper.configuration.ConfigureCommands;
import com.comandante.creeper.configuration.CreeperConfiguration;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.core_game.SessionManager;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemType;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcBuilder;
import com.comandante.creeper.server.model.CreeperSession;
import com.comandante.creeper.server.player_communication.ChannelCommunicationUtils;
import com.comandante.creeper.stats.DefaultStats;
import com.comandante.creeper.stats.Levels;
import com.comandante.creeper.stats.experience.Experience;
import com.comandante.creeper.storage.NpcStorage;
import com.comandante.creeper.storage.WorldStorage;
import com.comandante.creeper.world.MapsManager;
import com.comandante.creeper.world.RoomManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.configuration.MapConfiguration;
import org.jboss.netty.channel.Channel;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

public class NpcTestHarness {

    private GameManager gameManager;
    private EntityManager entityManager;
    private int totalFightRounds = 0;

    @Before
    public void setUp() throws Exception {
        ChannelCommunicationUtils channelUtils = new ChannelCommunicationUtils(){
            @Override
            public void write(String playerId, String message) {
                //System.out.println(message);
            }

            @Override
            public void write(String playerId, String message, boolean leadingBlankLine) {
                // System.out.println(message);
            }
        };
        CreeperConfiguration creeperConfiguration = new CreeperConfiguration(new MapConfiguration(Maps.newHashMap()));
        DB db = DBMaker.newMemoryDB().closeOnJvmShutdown().make();
        PlayerManager playerManager = new PlayerManager(db, new SessionManager());
        RoomManager roomManager = new RoomManager(playerManager);
        MapsManager mapsManager = new MapsManager(creeperConfiguration, roomManager);
        EntityManager entityManager = new EntityManager(roomManager, playerManager, db);
        GameManager gameManager = new GameManager(creeperConfiguration, roomManager, playerManager, entityManager, mapsManager, channelUtils);
        WorldStorage worldExporter = new WorldStorage(roomManager, mapsManager, gameManager.getFloorManager(), entityManager, gameManager);
        worldExporter.buildTestworld();
        ConfigureCommands.configure(gameManager);
        this.entityManager = entityManager;
        this.gameManager = gameManager;
    }

    @Test
    public void testAnotherCombat() throws Exception {
        Set<Item> equipment = Sets.newHashSet();
        equipment.add(ItemType.BERSEKER_BOOTS.create());
        //equipment.add(ItemType.BERSERKER_BATON.create());
        equipment.add(ItemType.BERSERKER_CHEST.create());
        equipment.add(ItemType.BERSEKER_SHORTS.create());
        String username = UUID.randomUUID().toString();
        Player player = createRandomPlayer(username, 0);
        equipArmor(player, equipment);
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc treeBerseker = npcsFromFile.stream().filter(npc -> npc.getName().equals("tree berserker")).collect(Collectors.toList()).get(0);
        Npc npc = new NpcBuilder(treeBerseker).createNpc();
        gameManager.getEntityManager().addEntity(npc);
        player.getCurrentRoom().addPresentNpc(npc.getEntityId());
        player.addActiveFight(npc);
        conductFight(player, npc);
    }

    @Test
    public void testCombat() throws Exception {
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc treeBerseker = npcsFromFile.stream().filter(npc -> npc.getName().equals("red-eyed bear")).collect(Collectors.toList()).get(0);
        int totalIterations = 100;
        Player player;
        Npc npc = null;
        Table t = new Table(8, BorderStyle.BLANKS, ShownBorders.NONE);
        t.setColumnWidth(0, 20, 20);
        t.setColumnWidth(1, 15, 20);
        t.setColumnWidth(2, 13, 16);
        t.setColumnWidth(3, 10, 16);
        t.setColumnWidth(4, 13, 16);
        t.setColumnWidth(5, 10, 16);
        t.setColumnWidth(6, 13, 16);
        //t.setColumnWidth(6, 16, 16);

        t.addCell("Npc");
        t.addCell("Player Level");
        t.addCell("Player Win");
        t.addCell("Npc Win");
        t.addCell("Avg Turns");
        t.addCell("Avg Gold");
        t.addCell("XP Earned");
        t.addCell("Drops");
        Set<Item> equipment = Sets.newHashSet();
       // equipment.add(ItemType.BERSEKER_BOOTS.create());
       // equipment.add(ItemType.BERSERKER_BATON.create());
       // equipment.add(ItemType.BERSERKER_CHEST.create());
       // equipment.add(ItemType.BERSEKER_SHORTS.create());
       // equipment.add(ItemType.BERSERKER_BRACERS.create());
       // equipment.add(ItemType.BERSEKER_HELM.create());

        for (int level = 20; level < 30; level++) {
            int playerWins = 0;
            int npcWins = 0;
            int totalGold = 0;
            totalFightRounds = 0;
            Map<String, AtomicInteger> drops = new HashMap<String, AtomicInteger>();
            for (int i = 0; i < 100; i++) {
                String username = UUID.randomUUID().toString();
                player = createRandomPlayer(username, level);
                equipArmor(player, equipment);
                npc = new NpcBuilder(treeBerseker).createNpc();
                npc.setCurrentRoom(player.getCurrentRoom());
                gameManager.getEntityManager().addEntity(npc);
                player.getCurrentRoom().addPresentNpc(npc.getEntityId());
                player.addActiveFight(npc);
                if (conductFight(player, npc)) {
                    playerWins++;
                    int gold = (int) gameManager.getLootManager().lootGoldAmountReturn(npc.getLoot());
                    totalGold += gold;
                    Set<Item> items = gameManager.getLootManager().lootItemsReturn(npc.getLoot());
                    items.forEach(item -> {
                        if (!drops.containsKey(item.getItemName())) {
                            drops.put(item.getItemName(), new AtomicInteger(1));
                        } else {
                            drops.get(item.getItemName()).incrementAndGet();
                        }
                    });
                } else {
                    npcWins++;
                }
                player.getCurrentRoom().removePresentNpc(npc.getEntityId());
                entityManager.deleteNpcEntity(npc.getEntityId());
                player.getCurrentRoom().removePresentPlayer(player.getPlayerId());
            }
            float playerWinPercent = (playerWins * 100.0f) / totalIterations;
            float npcWinPercent = (npcWins * 100.0f) / totalIterations;
            t.addCell(npc.getName());
            t.addCell(String.valueOf(level));
            t.addCell(String.valueOf(playerWinPercent) + "%");
            t.addCell(String.valueOf(npcWinPercent) + "%");
            t.addCell(String.valueOf(totalFightRounds / totalIterations));
            if (totalGold == 0 || playerWins == 0) {
                t.addCell("0");
            } else {
                t.addCell(String.valueOf(totalGold / playerWins));
            }
            t.addCell(String.valueOf(new Experience().calculateNpcXp(level, (int) Levels.getLevel(npc.getStats().getExperience()))));
            StringBuilder sb = new StringBuilder();
            drops.entrySet().stream().map(entry -> entry.getKey() + "(" + entry.getValue().get() + ")").forEach(s -> sb.append(s).append(","));
            t.addCell(sb.toString());
        }
        System.out.println("#### 100 round fight simulation results ####");
        System.out.println(t.render());

        // Stats difference = StatsHelper.getDifference(player.getPlayerStatsWithEquipmentAndLevel(), new DefaultStats().DEFAULT_PLAYER.createStats());
        // String player1 = gameManager.buildLookString("player", player.getPlayerStatsWithEquipmentAndLevel(),difference );
        // System.out.println(player1);
        // System.out.println("");

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

    private Player createRandomPlayer(String username, int level) throws FileNotFoundException {
        createUser(username, "3333333");
        Player player = new Player(username, gameManager);
        Channel mockChannel = mock(Channel.class);
        CreeperSession creeperSession = new CreeperSession();
        creeperSession.setUsername(Optional.of(username));
        player.setChannel(mockChannel);
        gameManager.getPlayerManager().addPlayer(player);
        gameManager.placePlayerInLobby(player);
        gameManager.getPlayerManager().getSessionManager().putSession(creeperSession);
        player.addExperience(Levels.getXp(level));
        player.setPlayerClass(PlayerClass.WARRIOR);
        return player;
    }

    private void equipArmor(Player player, Set<Item> equipment) {
        equipment.forEach(item -> {
            entityManager.saveItem(item);
            gameManager.acquireItem(player, item.getItemId());
            player.equip(item);
        });
    }

    private void createUser(String username, String password) {
        PlayerMetadata playerMetadata = new PlayerMetadata(username, password, Main.createPlayerId(username), DefaultStats.DEFAULT_PLAYER.createStats(), 0, Sets.newHashSet(PlayerRole.MORTAL), new String[0], 0, new String[0], Maps.newHashMap(), PlayerClass.BASIC, Sets.newHashSet(), null);
        gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
    }

    private int getLightningSpellDamage(int level, int intelligence, int npcIntelligence) {
        return (level * 1) + (3 * intelligence);

    }

    @Test
    public void screwingAround() throws Exception {

        int lightningSpellDamage = getLightningSpellDamage(4, 7, 4);
        System.out.println("Level 1 player with 7 intelligence vs 4 intelligence npc: " + lightningSpellDamage);

        lightningSpellDamage = getLightningSpellDamage(5, 11, 6);
        System.out.println("Level 1 player with 11 intelligence vs 6 intelligence npc: " + lightningSpellDamage);

        lightningSpellDamage = getLightningSpellDamage(5, 11, 6);
        System.out.println("Level 5 player with 11 intelligence vs 6 intelligence npc: " + lightningSpellDamage);
        lightningSpellDamage = getLightningSpellDamage(7, 14, 8);
        System.out.println("Level 7 player with 14 intelligence vs 8 intelligence npc: " + lightningSpellDamage);
        lightningSpellDamage = getLightningSpellDamage(9,16, 10);
        System.out.println("Level 9 player with 16 intelligence vs 10 intelligence npc: " + lightningSpellDamage);
        lightningSpellDamage = getLightningSpellDamage(12, 21, 13);
        System.out.println("Level 12 player with 21 intelligence vs 13 intelligence npc: " + lightningSpellDamage);

    }
}
