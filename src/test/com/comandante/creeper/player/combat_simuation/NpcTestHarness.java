package com.comandante.creeper.player.combat_simuation;

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
import com.comandante.creeper.player.*;
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
import org.junit.Assert;
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

    // Levels 1-3
    @Test
    public void testDemonCat() throws Exception {
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc npcFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("demon cat")).collect(Collectors.toList()).get(0);
        processRunAndVerify(npcFromFile, 1, Sets.newHashSet(), 98f, 90f, 10, 5, 0, 0);
        processRunAndVerify(npcFromFile, 2, Sets.newHashSet(), 99.9f, 96f, 7, 4, 0, 0);
        processRunAndVerify(npcFromFile, 3, Sets.newHashSet(), 100f, 98f, 6, 3, 0, 0);
    }

    // Levels 2-4
    @Test
    public void testSwampBerserker() throws Exception {
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc npcFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("swamp berserker")).collect(Collectors.toList()).get(0);
        processRunAndVerify(npcFromFile, 1, Sets.newHashSet(), 15f, 0f, 10, 0, 10, 5);
        processRunAndVerify(npcFromFile, 2, Sets.newHashSet(), 25f, 0f, 10, 0, 10, 5);
        processRunAndVerify(npcFromFile, 3, Sets.newHashSet(), 55f, 0f, 10, 0,10, 5);
        processRunAndVerify(npcFromFile, 4, Sets.newHashSet(), 90f, 0f, 10, 0, 10, 5);
    }

    // Levels 4-6
    @Test
    public void testTreeBerserker() throws Exception {
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc npcFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("tree berserker")).collect(Collectors.toList()).get(0);
        processRunAndVerify(npcFromFile, 3, Sets.newHashSet(), 20f, 12f, 10, 0, 14, 8);
        processRunAndVerify(npcFromFile, 4, Sets.newHashSet(), 40f, 33f, 10, 0, 14, 8);
        processRunAndVerify(npcFromFile, 5, Sets.newHashSet(), 80f, 70f, 10, 0, 14, 8);
        processRunAndVerify(npcFromFile, 6, Sets.newHashSet(), 95f, 86f, 10, 0,14,8);
    }

    // Levels 6-8
    @Test
    public void testSwampBear() throws Exception {
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc npcFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("swamp bear")).collect(Collectors.toList()).get(0);
        processRunAndVerify(npcFromFile, 6, getEarlyLevelArmorSet(), 55, 40f, 10, 0, 18, 12);
        processRunAndVerify(npcFromFile, 7, getEarlyLevelArmorSet(), 85, 70f, 10, 0, 18, 12);
        processRunAndVerify(npcFromFile, 8, getEarlyLevelArmorSet(), 95f, 86f, 10, 0, 18, 12);
        processRunAndVerify(npcFromFile, 9, getEarlyLevelArmorSet(), 100f, 0f, 10, 0, 18, 12);
    }

    // Levels 8-10
    @Test
    public void testRedEyeBear() throws Exception {
        List<Npc> npcsFromFile = NpcStorage.getNpcsFromFile(gameManager);
        Npc npcFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("red-eyed bear")).collect(Collectors.toList()).get(0);
        processRunAndVerify(npcFromFile, 8, getMidLevelArmorSet(), 55, 40f, 10, 0, 24, 18);
        processRunAndVerify(npcFromFile, 9, getMidLevelArmorSet(), 85, 70f, 10, 0, 24, 18);
        processRunAndVerify(npcFromFile, 10, getMidLevelArmorSet(), 100f, 0f, 10, 0, 24, 18);
    }

    private Set<Item> getEarlyLevelArmorSet() {
        return Sets.newHashSet(ItemType.BERSERKER_BATON.create(), ItemType.BERSEKER_BOOTS.create(), ItemType.BERSEKER_SHORTS.create());
    }

    private Set<Item> getMidLevelArmorSet() {
        Set<Item> armorSet = getEarlyLevelArmorSet();
        armorSet.addAll(Sets.newHashSet(ItemType.BERSERKER_BRACERS.create(), ItemType.BERSERKER_CHEST.create()));
        return armorSet;
    }

    private void processRunAndVerify(Npc testNpc, int desiredLevel, Set<Item> equipment, float winPctMax, float winPctMin, int maxRounds, int minRounds, int maxAvgGold, int minAvgGold) throws Exception {
        CombatSimulationDetails combatSimulationDetailsLevel = new CombatSimulationDetails(desiredLevel, equipment, testNpc);
        CombatSimulationResult combatSimulationResultLevel = executeCombat(combatSimulationDetailsLevel);
        printCombatResults(combatSimulationDetailsLevel, combatSimulationResultLevel);
        Assert.assertTrue("player at level: " + desiredLevel + " does not win enough.", combatSimulationResultLevel.getPlayerWinPercent() >= winPctMin);
        Assert.assertTrue("player at level: " + desiredLevel + " wins too often.", combatSimulationResultLevel.getPlayerWinPercent() <= winPctMax);
        Assert.assertTrue("player at level: " + desiredLevel + " wins too quickly", combatSimulationResultLevel.getAverageRounds() >= minRounds);
        Assert.assertTrue("player at level: " + desiredLevel + " wins too slowly.", combatSimulationResultLevel.getAverageRounds() <= maxRounds);
        Assert.assertTrue("Too much gold.", combatSimulationResultLevel.getAverageGoldPerWin() >= minAvgGold);
        Assert.assertTrue("Not enough gold.",combatSimulationResultLevel.getAverageGoldPerWin() <= maxAvgGold);
    }

    public CombatSimulationResult executeCombat(CombatSimulationDetails combatSimulationDetails) throws Exception {
        Player player;
        Npc npc = null;
        int playerWins = 0;
        int npcWins = 0;
        int totalGold = 0;
        int totalFightRounds = 0;
        Map<String, AtomicInteger> drops = new HashMap<>();
        for (int i = 0; i < combatSimulationDetails.getTotalIterations(); i++) {
            player = createRandomPlayer(combatSimulationDetails.getLevel());
            equipArmor(player, combatSimulationDetails.getEquipmentSet());
            npc = new NpcBuilder(combatSimulationDetails.getNpc()).createNpc();
            npc.setCurrentRoom(player.getCurrentRoom());
            gameManager.getEntityManager().addEntity(npc);
            player.getCurrentRoom().addPresentNpc(npc.getEntityId());
            player.addActiveFight(npc);
            FightSimulationResult fightSimulationResult = conductFight(player, npc);
            totalFightRounds = totalFightRounds + (fightSimulationResult.getTotalFightRounds() / Player.FIGHT_TICK_BUCKET_SIZE);
            if (fightSimulationResult.isResult()) {
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

        float playerWinPercent = (playerWins * 100.0f) / combatSimulationDetails.getTotalIterations();
        float npcWinPercent = (npcWins * 100.0f) / combatSimulationDetails.getTotalIterations();
        int averageRounds = totalFightRounds / combatSimulationDetails.getTotalIterations();
        int npcExperience = Experience.calculateNpcXp(combatSimulationDetails.getLevel(), (int) Levels.getLevel(npc.getStats().getExperience()));
        int averageGoldPerWin;
        if (totalGold == 0 || playerWins == 0) {
            averageGoldPerWin = 0;
        } else {
            averageGoldPerWin = totalGold / playerWins;
        }

        return new CombatSimulationResult(playerWinPercent, npcWinPercent, averageRounds, npcExperience, averageGoldPerWin, drops);


        // Stats difference = StatsHelper.getDifference(player.getPlayerStatsWithEquipmentAndLevel(), new DefaultStats().DEFAULT_PLAYER.createStats());
        // String player1 = gameManager.buildLookString("player", player.getPlayerStatsWithEquipmentAndLevel(),difference );
        // System.out.println(player1);
        // System.out.println("");

    }

    private FightSimulationResult conductFight(Player player, Npc npc) {
        int i = 0;
        int totalFightRounds = 0;
        for (i = 0; i < 1000; i++) {
            player.run();
            npc.run();
            totalFightRounds = totalFightRounds + 1;
            if (!npc.getIsAlive().get()) {
                return new FightSimulationResult(true, totalFightRounds);
            }

            if (player.isActive(CoolDownType.DEATH)) {
                return new FightSimulationResult(false, totalFightRounds);
            }

        }
        return new FightSimulationResult(false, totalFightRounds);
    }

    private Player createRandomPlayer(int level) throws FileNotFoundException {
        String username = UUID.randomUUID().toString();
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

    public void printCombatResults(CombatSimulationDetails combatSimulationDetails, CombatSimulationResult combatSimulationResult) {
        Table t = configureTableOutput();
        t.addCell(combatSimulationDetails.getNpc().getName());
        t.addCell(String.valueOf(combatSimulationDetails.getLevel()));
        t.addCell(String.valueOf(combatSimulationResult.getPlayerWinPercent()) + "%");
        t.addCell(String.valueOf(combatSimulationResult.getNpcWinPercent()) + "%");
        t.addCell(String.valueOf(combatSimulationResult.getAverageRounds()));
        t.addCell(String.valueOf(combatSimulationResult.getAverageGoldPerWin()));
        t.addCell(String.valueOf(combatSimulationResult.getNpcExperience()));
        StringBuilder sb = new StringBuilder();
        combatSimulationResult.getDrops().entrySet().stream().map(entry -> entry.getKey() + "(" + entry.getValue().get() + ")").forEach(s -> sb.append(s).append(","));
        t.addCell(sb.toString());
        System.out.println(t.render());
    }

    private Table configureTableOutput() {
        Table t = new Table(8, BorderStyle.BLANKS, ShownBorders.NONE);
        t.setColumnWidth(0, 20, 20);
        t.setColumnWidth(1, 15, 20);
        t.setColumnWidth(2, 13, 16);
        t.setColumnWidth(3, 10, 16);
        t.setColumnWidth(4, 13, 16);
        t.setColumnWidth(5, 10, 16);
        t.setColumnWidth(6, 13, 16);

        t.addCell("Npc");
        t.addCell("Player Level");
        t.addCell("Player Win");
        t.addCell("Npc Win");
        t.addCell("Avg Turns");
        t.addCell("Avg Gold");
        t.addCell("XP Earned");
        t.addCell("Drops");
        return t;
    }

    private void createUser(String username, String password) {
        PlayerMetadata playerMetadata = new PlayerMetadata(username, password, Main.createPlayerId(username), DefaultStats.DEFAULT_PLAYER.createStats(), 0, Sets.newHashSet(PlayerRole.MORTAL), new String[0], 0, new String[0], Maps.newHashMap(), PlayerClass.BASIC, Sets.newHashSet(), null);
        gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
    }

    @Before
    public void setUp() throws Exception {
        ChannelCommunicationUtils channelUtils = new ChannelCommunicationUtils() {
            @Override
            public void write(String playerId, String message) {
               //System.out.println(message);
            }

            @Override
            public void write(String playerId, String message, boolean leadingBlankLine) {
               //System.out.println(message);
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
        lightningSpellDamage = getLightningSpellDamage(9, 16, 10);
        System.out.println("Level 9 player with 16 intelligence vs 10 intelligence npc: " + lightningSpellDamage);
        lightningSpellDamage = getLightningSpellDamage(12, 21, 13);
        System.out.println("Level 12 player with 21 intelligence vs 13 intelligence npc: " + lightningSpellDamage);

    }
}
