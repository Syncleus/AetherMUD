package com.comandante.creeper;

import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.npc.*;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.server.CreeperCommandRegistry;
import com.comandante.creeper.server.CreeperServer;
import com.comandante.creeper.server.command.*;
import com.comandante.creeper.server.command.admin.BuildCommand;
import com.comandante.creeper.server.command.admin.DescriptionCommand;
import com.comandante.creeper.server.command.admin.SaveWorldCommand;
import com.comandante.creeper.server.command.admin.TagRoomCommand;
import com.comandante.creeper.server.command.admin.TitleCommand;
import com.comandante.creeper.spawner.ItemSpawner;
import com.comandante.creeper.spawner.NpcSpawner;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Area;
import com.comandante.creeper.world.MapsManager;
import com.comandante.creeper.world.Room;
import com.comandante.creeper.world.RoomManager;
import com.comandante.creeper.world.WorldExporter;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Main {

    public static CreeperCommandRegistry creeperCommandRegistry;
    private static final Logger log = Logger.getLogger(Main.class);

    private static final int PORT = 8081;
    public static final String MUD_NAME = "creeper";

    public static void main(String[] args) throws Exception {

        checkAndCreateWorld();

        DB db = DBMaker.newFileDB(new File("world/creeper.mapdb")).closeOnJvmShutdown().encryptionEnable("creepandicrawl").make();

        RoomManager roomManager = new RoomManager();
        PlayerManager playerManager = new PlayerManager(db, new SessionManager());
        EntityManager entityManager = new EntityManager(roomManager, playerManager, db);
        Stats chrisBrianStats = new StatsBuilder().setStrength(7).setWillpower(8).setAim(6).setAgile(5).setArmorRating(4).setMeleSkill(10).setCurrentHealth(100).setMaxHealth(100).setWeaponRatingMin(10).setWeaponRatingMax(20).setNumberweaponOfRolls(1).createStats();
        startUpMessage("Configuring default admins.");
        if (playerManager.getPlayerMetadata(createPlayerId("chris")) == null) {
            startUpMessage("Creating Chris User.");
            playerManager.savePlayerMetadata(new PlayerMetadata("chris", "poop", new String(Base64.encodeBase64("chris".getBytes())), chrisBrianStats));
        }
        if (playerManager.getPlayerMetadata(createPlayerId("brian")) == null) {
            startUpMessage("Creating Brian User.");
            playerManager.savePlayerMetadata(new PlayerMetadata("brian", "poop", new String(Base64.encodeBase64("brian".getBytes())), chrisBrianStats));
        }
        MapsManager mapsManager = new MapsManager(roomManager);
        GameManager gameManager = new GameManager(roomManager, playerManager, entityManager, mapsManager);
        startUpMessage("Reading world from disk.");
        WorldExporter worldExporter = new WorldExporter(roomManager, mapsManager, gameManager.getFloorManager(), entityManager);
        worldExporter.readWorldFromDisk();
        startUpMessage("Generating maps");
        mapsManager.generateAllMaps(9, 9);

        startUpMessage("Configuring Creeper Commmands");
        creeperCommandRegistry = new CreeperCommandRegistry(new UnknownCommand(gameManager));
        creeperCommandRegistry.addCommand(new DropCommand(gameManager));
        creeperCommandRegistry.addCommand(new GossipCommand(gameManager));
        creeperCommandRegistry.addCommand(new InventoryCommand(gameManager));
        creeperCommandRegistry.addCommand(new FightKillCommand(gameManager));
        creeperCommandRegistry.addCommand(new LookCommand(gameManager));
        creeperCommandRegistry.addCommand(new MovementCommand(gameManager));
        creeperCommandRegistry.addCommand(new PickUpCommand(gameManager));
        creeperCommandRegistry.addCommand(new SayCommand(gameManager));
        creeperCommandRegistry.addCommand(new TellCommand(gameManager));
        creeperCommandRegistry.addCommand(new UseCommand(gameManager));
        creeperCommandRegistry.addCommand(new WhoamiCommand(gameManager));
        creeperCommandRegistry.addCommand(new WhoCommand(gameManager));
        creeperCommandRegistry.addCommand(new DescriptionCommand(gameManager));
        creeperCommandRegistry.addCommand(new TitleCommand(gameManager));
        creeperCommandRegistry.addCommand(new TagRoomCommand(gameManager));
        creeperCommandRegistry.addCommand(new SaveWorldCommand(gameManager));
        creeperCommandRegistry.addCommand(new BuildCommand(gameManager));
        creeperCommandRegistry.addCommand(new MapCommand(gameManager));
        creeperCommandRegistry.addCommand(new AreaCommand(gameManager));
        creeperCommandRegistry.addCommand(new HelpCommand(gameManager));



        CreeperServer creeperServer = new CreeperServer(PORT, db);
        startUpMessage("Creeper engine started");
        creeperServer.run(gameManager);
        startUpMessage("Creeper online");
    }

    private static void startUpMessage(String message) {
        System.out.println("[STARTUP] " + message);
        log.info(message);
    }

    private static void checkAndCreateWorld() throws IOException {
        if (!Files.isDirectory().apply(new File("world/"))) {
            Files.createParentDirs(new File("world/creeper_world_stuff"));
        }
    }

    public static String createPlayerId(String playerName) {
        return new String(Base64.encodeBase64(playerName.getBytes()));
    }

    private static void createNpcs(EntityManager entityManager, GameManager gameManager) {
        startUpMessage("Adding Street Hustlers");
        entityManager.addEntity(new NpcSpawner(new StreetHustler(gameManager), Sets.newHashSet(Area.NEWBIE_ZONE), gameManager, new SpawnRule(10, 5, 3, 1)));

        startUpMessage("Adding beer");
        ItemSpawner itemSpawner = new ItemSpawner(ItemType.BEER, Area.NEWBIE_ZONE, new SpawnRule(20, 20, 2, 25), gameManager);
        entityManager.addEntity(itemSpawner);

        startUpMessage("Adding Tree Berserkers");
        entityManager.addEntity(new NpcSpawner(new TreeBerserker(gameManager), Sets.newHashSet(Area.NEWBIE_ZONE, Area.NORTH1_ZONE), gameManager, new SpawnRule(10, 6, 2, 2)));

        startUpMessage("Adding Swamp Berserkers");
        entityManager.addEntity(new NpcSpawner(new SwampBerserker(gameManager), Sets.newHashSet(Area.NORTH2_ZONE), gameManager, new SpawnRule(10, 8, 2, 2)));

        startUpMessage("Adding Berg Orcs");
        entityManager.addEntity(new NpcSpawner(new BergOrc(gameManager), Sets.newHashSet(Area.BLOODRIDGE1_ZONE), gameManager, new SpawnRule(10, 8, 2, 2)));

        startUpMessage("Adding Red-Eyed Bears");
        entityManager.addEntity(new NpcSpawner(new RedEyedBear(gameManager), Sets.newHashSet(Area.TOFT1_ZONE, Area.TOFT2_ZONE), gameManager, new SpawnRule(10, 14, 2, 2)));

        startUpMessage("Adding Swamp Bears");

        entityManager.addEntity(new NpcSpawner(new RedEyedBear(gameManager), Sets.newHashSet(Area.NORTH3_ZONE, Area.NORTH4_ZONE), gameManager, new SpawnRule(10, 12, 2, 2)));
    }
}
