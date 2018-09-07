/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.player.combatsimuation;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.configuration.ConfigureCommands;
import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SessionManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.server.model.AetherMudSession;
import com.syncleus.aethermud.server.communication.ChannelCommunicationUtils;
import com.syncleus.aethermud.stats.DefaultStats;
import com.syncleus.aethermud.stats.Levels;
import com.syncleus.aethermud.stats.experience.Experience;
import com.syncleus.aethermud.storage.WorldStorage;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.world.MapsManager;
import com.syncleus.aethermud.world.RoomManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.http.impl.client.HttpClients;
import org.jboss.netty.channel.Channel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
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
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            List<? extends NpcSpawn> npcsFromFile = tx.getStorage().getAllNpcs(gameManager);
            NpcSpawn npcSpawnFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("demon cat")).collect(Collectors.toList()).get(0);
            processRunAndVerify(npcSpawnFromFile, 1, Sets.newHashSet(), 98f, 90f, 10, 5, 3, 0);
            processRunAndVerify(npcSpawnFromFile, 2, Sets.newHashSet(), 99.9f, 96f, 7, 4, 3, 0);
            processRunAndVerify(npcSpawnFromFile, 3, Sets.newHashSet(), 100f, 98f, 6, 3, 3, 0);
        }
    }

    // Levels 2-4
    @Test
    public void testSwampBerserker() throws Exception {
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            List<? extends NpcSpawn> npcsFromFile = tx.getStorage().getAllNpcs(gameManager);
            NpcSpawn npcSpawnFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("swamp berserker")).collect(Collectors.toList()).get(0);
            processRunAndVerify(npcSpawnFromFile, 1, Sets.newHashSet(), 15f, 0f, 10, 0, 10, 5);
            processRunAndVerify(npcSpawnFromFile, 2, Sets.newHashSet(), 25f, 0f, 10, 0, 10, 5);
            processRunAndVerify(npcSpawnFromFile, 3, Sets.newHashSet(), 55f, 0f, 10, 0, 10, 5);
            processRunAndVerify(npcSpawnFromFile, 4, Sets.newHashSet(), 90f, 0f, 10, 0, 10, 5);
        }
    }

    // Levels 2-4
    @Test
    public void testBloodWolf() throws Exception {
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            List<? extends NpcSpawn> npcsFromFile = tx.getStorage().getAllNpcs(gameManager);
            NpcSpawn npcSpawnFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("blood wolf")).collect(Collectors.toList()).get(0);
            processRunAndVerify(npcSpawnFromFile, 1, Sets.newHashSet(), 15f, 0f, 10, 0, 14, 5);
            processRunAndVerify(npcSpawnFromFile, 2, Sets.newHashSet(), 25f, 0f, 10, 0, 14, 5);
            processRunAndVerify(npcSpawnFromFile, 3, Sets.newHashSet(), 55f, 0f, 10, 0, 14, 5);
            processRunAndVerify(npcSpawnFromFile, 4, Sets.newHashSet(), 90f, 0f, 10, 0, 14, 5);
        }
    }

    // Levels 4-6
    @Test
    public void testTreeBerserker() throws Exception {
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            List<? extends NpcSpawn> npcsFromFile = tx.getStorage().getAllNpcs(gameManager);
            NpcSpawn npcSpawnFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("tree berserker")).collect(Collectors.toList()).get(0);
            processRunAndVerify(npcSpawnFromFile, 3, Sets.newHashSet(), 20f, 12f, 10, 0, 14, 8);
            processRunAndVerify(npcSpawnFromFile, 4, Sets.newHashSet(), 40f, 33f, 10, 0, 14, 8);
            processRunAndVerify(npcSpawnFromFile, 5, Sets.newHashSet(), 80f, 70f, 10, 0, 14, 8);
            processRunAndVerify(npcSpawnFromFile, 6, Sets.newHashSet(), 95f, 86f, 10, 0, 14, 8);
        }
    }

    // Levels 6-8
    @Test
    public void testSwampBear() throws Exception {
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            List<? extends NpcSpawn> npcsFromFile = tx.getStorage().getAllNpcs(gameManager);
            NpcSpawn npcSpawnFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("swamp bear")).collect(Collectors.toList()).get(0);
            processRunAndVerify(npcSpawnFromFile, 6, getEarlyLevelArmorSet(), 55, 40f, 10, 0, 18, 12);
            processRunAndVerify(npcSpawnFromFile, 7, getEarlyLevelArmorSet(), 85, 70f, 10, 0, 18, 12);
            processRunAndVerify(npcSpawnFromFile, 8, getEarlyLevelArmorSet(), 95f, 86f, 10, 0, 18, 12);
            processRunAndVerify(npcSpawnFromFile, 9, getEarlyLevelArmorSet(), 100f, 0f, 10, 0, 18, 12);
        }
    }

    // Levels 8-10
    @Test
    public void testRedEyeBear() throws Exception {
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            List<? extends NpcSpawn> npcsFromFile = tx.getStorage().getAllNpcs(gameManager);
            NpcSpawn npcSpawnFromFile = npcsFromFile.stream().filter(npc -> npc.getName().equals("red-eyed bear")).collect(Collectors.toList()).get(0);
            processRunAndVerify(npcSpawnFromFile, 8, getMidLevelArmorSet(), 55, 36f, 10, 0, 24, 18);
            processRunAndVerify(npcSpawnFromFile, 9, getMidLevelArmorSet(), 85, 70f, 10, 0, 24, 18);
            processRunAndVerify(npcSpawnFromFile, 10, getMidLevelArmorSet(), 100f, 0f, 10, 0, 24, 18);
        }
    }

    private Set<ItemInstance> getEarlyLevelArmorSet() {
        //  return Sets.newHashSet(ItemType.BERSERKER_BATON.create(), ItemType.BERSEKER_BOOTS.create(), ItemType.BERSEKER_SHORTS.create());
        return Sets.newConcurrentHashSet();
    }

    private Set<ItemInstance> getMidLevelArmorSet() {
        Set<ItemInstance> armorSet = getEarlyLevelArmorSet();
        //  armorSet.addAll(Sets.newHashSet(ItemType.BERSERKER_BRACERS.create(), ItemType.BERSERKER_CHEST.create()));
        return armorSet;
    }

    private void processRunAndVerify(NpcSpawn testNpcSpawn, int desiredLevel, Set<ItemInstance> equipment, float winPctMax, float winPctMin, int maxRounds, int minRounds, int maxAvgGold, int minAvgGold) throws Exception {
        CombatSimulationDetails combatSimulationDetailsLevel = new CombatSimulationDetails(desiredLevel, equipment, testNpcSpawn);
        CombatSimulationResult combatSimulationResultLevel = executeCombat(combatSimulationDetailsLevel);
        printCombatResults(combatSimulationDetailsLevel, combatSimulationResultLevel);
        Assert.assertTrue("player at level: " + desiredLevel + " does not win enough.", combatSimulationResultLevel.getPlayerWinPercent() >= winPctMin);
        Assert.assertTrue("player at level: " + desiredLevel + " wins too often.", combatSimulationResultLevel.getPlayerWinPercent() <= winPctMax);
        Assert.assertTrue("player at level: " + desiredLevel + " wins too quickly", combatSimulationResultLevel.getAverageRounds() >= minRounds);
        Assert.assertTrue("player at level: " + desiredLevel + " wins too slowly.", combatSimulationResultLevel.getAverageRounds() <= maxRounds);
        Assert.assertTrue("Not enough gold.", combatSimulationResultLevel.getAverageGoldPerWin() >= minAvgGold);
        Assert.assertTrue("Too much gold.", combatSimulationResultLevel.getAverageGoldPerWin() <= maxAvgGold);
    }

    public CombatSimulationResult executeCombat(CombatSimulationDetails combatSimulationDetails) throws Exception {
        Player player;
        NpcSpawn npcSpawn = null;
        int playerWins = 0;
        int npcWins = 0;
        int totalGold = 0;
        int totalFightRounds = 0;
        Map<String, AtomicInteger> drops = new HashMap<>();
        for (int i = 0; i < combatSimulationDetails.getTotalIterations(); i++) {
            player = createRandomPlayer(combatSimulationDetails.getLevel());
            equipArmor(player, combatSimulationDetails.getEquipmentSet());
            npcSpawn = new NpcBuilder(combatSimulationDetails.getNpcSpawn()).createNpc();
            npcSpawn.setCurrentRoom(player.getCurrentRoom());
            gameManager.getEntityManager().addEntity(npcSpawn);
            player.getCurrentRoom().addPresentNpc(npcSpawn.getEntityId());
            player.addActiveFight(npcSpawn);
            FightSimulationResult fightSimulationResult = conductFight(player, npcSpawn);
            totalFightRounds = totalFightRounds + (fightSimulationResult.getTotalFightRounds() / Player.FIGHT_TICK_BUCKET_SIZE);
            if (fightSimulationResult.isResult()) {
                playerWins++;
                int gold = (int) gameManager.getLootManager().lootGoldAmountReturn(npcSpawn.getLoot());
                totalGold += gold;
                Set<ItemInstance> items = gameManager.getLootManager().lootItemsReturn(npcSpawn.getLoot());
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
            player.getCurrentRoom().removePresentNpc(npcSpawn.getEntityId());
            entityManager.deleteNpcEntity(npcSpawn.getEntityId());
            player.getCurrentRoom().removePresentPlayer(player.getPlayerId());
        }

        float playerWinPercent = (playerWins * 100.0f) / combatSimulationDetails.getTotalIterations();
        float npcWinPercent = (npcWins * 100.0f) / combatSimulationDetails.getTotalIterations();
        int averageRounds = totalFightRounds / combatSimulationDetails.getTotalIterations();
        int npcExperience = Experience.calculateNpcXp(combatSimulationDetails.getLevel(), (int) Levels.getLevel(npcSpawn.getStats().getExperience()));
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

    private FightSimulationResult conductFight(Player player, NpcSpawn npcSpawn) {
        int i = 0;
        int totalFightRounds = 0;
        for (i = 0; i < 1000; i++) {
            player.run();
            npcSpawn.run();
            totalFightRounds = totalFightRounds + 1;
            if (!npcSpawn.getIsAlive().get()) {
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
        AetherMudSession aetherMudSession = new AetherMudSession();
        aetherMudSession.setUsername(Optional.of(username));
        player.setChannel(mockChannel);
        gameManager.getPlayerManager().addPlayer(player);
        gameManager.placePlayerInLobby(player);
        gameManager.getPlayerManager().getSessionManager().putSession(aetherMudSession);
        player.addExperience(Levels.getXp(level));
        player.setPlayerClass(PlayerClass.WARRIOR);
        return player;
    }

    private void equipArmor(Player player, Set<ItemInstance> equipment) {
        equipment.forEach(item -> {
            entityManager.saveItem(item);
            gameManager.acquireItem(player, item.getItemId());
            player.equip(item);
        });
    }

    public void printCombatResults(CombatSimulationDetails combatSimulationDetails, CombatSimulationResult combatSimulationResult) {
        Table t = configureTableOutput();
        t.addCell(combatSimulationDetails.getNpcSpawn().getName());
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
        try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
            PlayerData playerData = tx.getStorage().newPlayerData();
            playerData.setNpcKillLog(new HashMap<>());
            playerData.setCoolDowns(new HashMap<>());
            playerData.setEffects(new HashSet<>());
            playerData.setGold(0);
            playerData.setGoldInBank(0);
            playerData.setInventory(new ArrayList<>());
            playerData.setLearnedSpells(new ArrayList<>());
            playerData.setLockerInventory(new ArrayList<>());
            playerData.setIsMarkedForDelete(false);
            playerData.setPlayerName(username);
            playerData.setPassword(password);
            playerData.setPlayerClass(PlayerClass.BASIC);
            playerData.setPlayerEquipment(new ArrayList<>());
            playerData.setPlayerId(Main.createPlayerId(username));
            playerData.setPlayerRoles(Sets.newHashSet(PlayerRole.MORTAL));
            playerData.setPlayerSettings(new HashMap<>());
            try {
                PropertyUtils.copyProperties(playerData.createStatData(), DefaultStats.DEFAULT_PLAYER.createStats());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not create a stats object", e);
            }
        }
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
        AetherMudConfiguration aetherMudConfiguration = new AetherMudConfiguration(new MapConfiguration(Maps.newHashMap()));
        GraphStorageFactory storageFactory = new GraphStorageFactory();
        PlayerManager playerManager = new PlayerManager(storageFactory, new SessionManager());
        RoomManager roomManager = new RoomManager(playerManager);
        MapsManager mapsManager = new MapsManager(aetherMudConfiguration, roomManager);
        EntityManager entityManager = new EntityManager(storageFactory, roomManager, playerManager);
        GameManager gameManager = new GameManager(storageFactory, aetherMudConfiguration, roomManager, playerManager, entityManager, mapsManager, channelUtils, HttpClients.createDefault());
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
