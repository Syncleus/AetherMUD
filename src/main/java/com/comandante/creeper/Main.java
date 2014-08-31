package com.comandante.creeper;

import com.comandante.creeper.command.CreeperCommandRegistry;
import com.comandante.creeper.command.commands.DropCommand;
import com.comandante.creeper.command.commands.GossipCommand;
import com.comandante.creeper.command.commands.InventoryCommand;
import com.comandante.creeper.command.commands.KillCommand;
import com.comandante.creeper.command.commands.LookCommand;
import com.comandante.creeper.command.commands.MovementCommand;
import com.comandante.creeper.command.commands.PickUpCommand;
import com.comandante.creeper.command.commands.SayCommand;
import com.comandante.creeper.command.commands.TellCommand;
import com.comandante.creeper.command.commands.UnknownCommand;
import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.command.commands.WhoCommand;
import com.comandante.creeper.command.commands.WhoamiCommand;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.managers.SessionManager;
import com.comandante.creeper.npc.Derper;
import com.comandante.creeper.player.PlayerManager;
import com.comandante.creeper.player.PlayerMetadata;
import com.comandante.creeper.room.RoomBuilders;
import com.comandante.creeper.room.RoomManager;
import com.comandante.creeper.server.CreeperServer;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import org.apache.commons.codec.binary.Base64;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;

public class Main {

    public static CreeperCommandRegistry creeperCommandRegistry;

    public static void main(String[] args) throws Exception {

        DB db = DBMaker.newFileDB(new File("creeperDb"))
                .closeOnJvmShutdown()
                .encryptionEnable("creepandicrawl")
                .make();

        RoomManager roomManager = new RoomManager();
        PlayerManager playerManager = new PlayerManager(db, new SessionManager());

        EntityManager entityManager = new EntityManager(roomManager, playerManager, db);

        Stats chrisBrianStats = new StatsBuilder().setStrength(7).setWillpower(8).setAim(6).setAgile(5).setArmorRating(4).setMeleSkill(10).setCurrentHealth(100).setMaxHealth(100).setWeaponRatingMin(10).setWeaponRatingMax(20).setNumberweaponOfRolls(1).createStats();
        if (playerManager.getPlayerMetadata(createPlayerId("chris")) == null) {
            System.out.println("Creating Chris User.");
            playerManager.savePlayerMetadata(new PlayerMetadata("chris", "poop", new String(Base64.encodeBase64("chris".getBytes())), chrisBrianStats));
        }

        if (playerManager.getPlayerMetadata(createPlayerId("brian")) == null) {
            System.out.println("Creating Brian User.");
            playerManager.savePlayerMetadata(new PlayerMetadata("brian", "poop", new String(Base64.encodeBase64("brian".getBytes())), chrisBrianStats));
        }

        GameManager gameManager = new GameManager(roomManager, playerManager, entityManager);

        // build zones

        RoomBuilders.buildFedTraining(gameManager);

        RoomBuilders.buildSpacePort(entityManager);

        RoomBuilders.buildNeoPortland(entityManager);

        RoomBuilders.buildOldTown(entityManager);

        // zones end

        entityManager.addEntity(new Derper(gameManager, 1));

        creeperCommandRegistry = new CreeperCommandRegistry(new UnknownCommand(gameManager));
        creeperCommandRegistry.addCommand(new DropCommand(gameManager));
        creeperCommandRegistry.addCommand(new GossipCommand(gameManager));
        creeperCommandRegistry.addCommand(new InventoryCommand(gameManager));
        creeperCommandRegistry.addCommand(new KillCommand(gameManager));
        creeperCommandRegistry.addCommand(new LookCommand(gameManager));
        creeperCommandRegistry.addCommand(new MovementCommand(gameManager));
        creeperCommandRegistry.addCommand(new PickUpCommand(gameManager));
        creeperCommandRegistry.addCommand(new SayCommand(gameManager));
        creeperCommandRegistry.addCommand(new TellCommand(gameManager));
        creeperCommandRegistry.addCommand(new UseCommand(gameManager));
        creeperCommandRegistry.addCommand(new WhoamiCommand(gameManager));
        creeperCommandRegistry.addCommand(new WhoCommand(gameManager));

        CreeperServer creeperServer = new CreeperServer(8080, db);
        creeperServer.run(gameManager);

        System.out.println("Creeper started.");
    }

    public static String createPlayerId(String playerName) {
        return new String(Base64.encodeBase64(playerName.getBytes()));
    }
}
