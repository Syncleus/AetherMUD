package com.comandante.creeper;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Blacksmith;
import com.comandante.creeper.merchant.JimBanker;
import com.comandante.creeper.merchant.LloydBartender;
import com.comandante.creeper.merchant.MerchantItemForSale;
import com.comandante.creeper.npc.*;
import com.comandante.creeper.spawner.ItemSpawner;
import com.comandante.creeper.spawner.NpcSpawner;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.world.Area;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;

public class ConfigureNpc {

    public static void configure(EntityManager entityManager, GameManager gameManager) {
        Main.startUpMessage("Adding Street Hustlers");
        entityManager.addEntity(new NpcSpawner(new StreetHustler(gameManager, new Loot(1, 3, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NEWBIE_ZONE), gameManager, new SpawnRule(10, 5, 3, 100)));

        Main.startUpMessage("Adding beer");
        ItemSpawner itemSpawner = new ItemSpawner(ItemType.BEER, Area.NEWBIE_ZONE, new SpawnRule(10, 100, 5, 40), gameManager);
        ItemSpawner itemSpawner1 = new ItemSpawner(ItemType.BEER, Area.FANCYHOUSE_ZONE, new SpawnRule(10, 12, 2, 50), gameManager);
        ItemSpawner itemSpawner2 = new ItemSpawner(ItemType.BEER, Area.HOUSE_ZONE, new SpawnRule(10, 12, 2, 50), gameManager);

        entityManager.addEntity(itemSpawner);
        entityManager.addEntity(itemSpawner1);
        entityManager.addEntity(itemSpawner2);


        Main.startUpMessage("Adding Tree Berserkers");
        entityManager.addEntity(new NpcSpawner(new TreeBerserker(gameManager, new Loot(2, 5, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NEWBIE_ZONE, Area.NORTH1_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));

        Main.startUpMessage("Adding Swamp Berserkers");
        entityManager.addEntity(new NpcSpawner(new SwampBerserker(gameManager, new Loot(4, 10, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH2_ZONE), gameManager, new SpawnRule(10, 8, 2, 100)));

        Main.startUpMessage("Adding Berg Orcs");
        entityManager.addEntity(new NpcSpawner(new BergOrc(gameManager, new Loot(4, 10, Sets.<Item>newHashSet())), Sets.newHashSet(Area.BLOODRIDGE1_ZONE), gameManager, new SpawnRule(10, 8, 2, 100)));

        Main.startUpMessage("Adding Red-Eyed Bears");
        entityManager.addEntity(new NpcSpawner(new RedEyedBear(gameManager, new Loot(8, 13, Sets.<Item>newHashSet())), Sets.newHashSet(Area.TOFT1_ZONE), gameManager, new SpawnRule(10, 14, 3, 100)));
        entityManager.addEntity(new NpcSpawner(new RedEyedBear(gameManager, new Loot(8, 13, Sets.<Item>newHashSet())), Sets.newHashSet(Area.TOFT2_ZONE), gameManager, new SpawnRule(10, 14, 3, 100)));
        entityManager.addEntity(new NpcSpawner(new RedEyedBear(gameManager, new Loot(8, 13, Sets.<Item>newHashSet())), Sets.newHashSet(Area.RADIO_ROOM), gameManager, new SpawnRule(10, 1, 1, 10)));


        Main.startUpMessage("Adding Swamp Bears");
        entityManager.addEntity(new NpcSpawner(new SwampBear(gameManager, new Loot(9, 14, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH3_ZONE), gameManager, new SpawnRule(10, 12, 3, 100)));
        entityManager.addEntity(new NpcSpawner(new SwampBear(gameManager, new Loot(9, 14, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH4_ZONE), gameManager, new SpawnRule(10, 12, 3, 100)));

        Main.startUpMessage("Adding Gray Ekimmus");
        entityManager.addEntity(new NpcSpawner(new GrayEkimmu(gameManager, new Loot(14, 17, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH4_ZONE), gameManager, new SpawnRule(10, 12, 3, 100)));
        entityManager.addEntity(new NpcSpawner(new GrayEkimmu(gameManager, new Loot(14, 17, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH5_ZONE), gameManager, new SpawnRule(10, 12, 3, 100)));

        Main.startUpMessage("Adding Stealth Panthers");
        entityManager.addEntity(new NpcSpawner(new StealthPanther(gameManager, new Loot(14, 22, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH5_ZONE), gameManager, new SpawnRule(10, 12, 3, 100)));
        entityManager.addEntity(new NpcSpawner(new StealthPanther(gameManager, new Loot(14, 22, Sets.<Item>newHashSet())), Sets.newHashSet(Area.NORTH6_ZONE), gameManager, new SpawnRule(10, 12, 3, 100)));

        Main.startUpMessage("Adding Phantom Knights");

        PhantomKnight phantomKnight = new PhantomKnight(gameManager, new Loot(18, 26, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(phantomKnight, Sets.newHashSet(Area.BLOODRIDGE5_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(phantomKnight, Sets.newHashSet(Area.BLOODRIDGE6_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));
        entityManager.addEntity(new NpcSpawner(phantomKnight, Sets.newHashSet(Area.BLOODRIDGE7_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));


        PhantomOrc phantomOrc = new PhantomOrc(gameManager, new Loot(16, 24, Sets.<Item>newHashSet()));
        Main.startUpMessage("Adding Phantom Orcs");
        entityManager.addEntity(new NpcSpawner(phantomOrc, Sets.newHashSet(Area.BLOODRIDGE4_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(phantomOrc, Sets.newHashSet(Area.BLOODRIDGE4_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        PhantomWizard phantomWizard = new PhantomWizard(gameManager, new Loot(16, 24, Sets.<Item>newHashSet()));
        Main.startUpMessage("Adding Phantom Wizards");
        entityManager.addEntity(new NpcSpawner(phantomWizard, Sets.newHashSet(Area.BLOODRIDGE4_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(phantomWizard, Sets.newHashSet(Area.BLOODRIDGE5_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Main.startUpMessage("Adding Demon Succubi");
        DemonSuccubus demonSuccubus = new DemonSuccubus(gameManager, new Loot(80, 100, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(demonSuccubus, Sets.newHashSet(Area.WESTERN9_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(demonSuccubus, Sets.newHashSet(Area.WESTERN10_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Main.startUpMessage("Adding Death Griffins");
        DeathGriffin deathGriffin = new DeathGriffin(gameManager, new Loot(60, 80, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(deathGriffin, Sets.newHashSet(Area.WESTERN8_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(deathGriffin, Sets.newHashSet(Area.WESTERN9_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Main.startUpMessage("Adding Nightmare Trolls");
        NightmareTroll nightmareTroll = new NightmareTroll(gameManager, new Loot(18, 26, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(nightmareTroll, Sets.newHashSet(Area.NORTH7_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(nightmareTroll, Sets.newHashSet(Area.NORTH8_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Main.startUpMessage("Adding Tiger Monoceruses");
        TigerMonocerus tigerMonocerus = new TigerMonocerus(gameManager, new Loot(20, 29, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(tigerMonocerus, Sets.newHashSet(Area.NORTH8_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(tigerMonocerus, Sets.newHashSet(Area.NORTH9_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Main.startUpMessage("Adding Razor-claw Wolves");
        RazorClawWolf razorClawWolf = new RazorClawWolf(gameManager, new Loot(18, 26, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(razorClawWolf, Sets.newHashSet(Area.TOFT2_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(razorClawWolf, Sets.newHashSet(Area.TOFT3_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));


        Main.startUpMessage("Adding Black-hooded Wizards");
        BlackHoodedWizard blackHoodedWizard = new BlackHoodedWizard(gameManager, new Loot(55, 74, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(blackHoodedWizard, Sets.newHashSet(Area.TISLAND4_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(blackHoodedWizard, Sets.newHashSet(Area.TISLAND3_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));


        Main.startUpMessage("Adding Tunnel Cobras");
        TunnelCobra tunnelCobra = new TunnelCobra(gameManager, new Loot(40, 52, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(tunnelCobra, Sets.newHashSet(Area.TISLAND3_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(tunnelCobra, Sets.newHashSet(Area.TISLAND4_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Main.startUpMessage("Adding Stone Giants");
        StoneGiant stoneGiant = new StoneGiant(gameManager, new Loot(55, 69, Sets.<Item>newHashSet()));
        entityManager.addEntity(new NpcSpawner(stoneGiant, Sets.newHashSet(Area.TISLAND4_ZONE), gameManager, new SpawnRule(10, 6, 2, 100)));
        entityManager.addEntity(new NpcSpawner(stoneGiant, Sets.newHashSet(Area.TISLAND5_ZONE), gameManager, new SpawnRule(10, 14, 2, 100)));

        Map<Integer, MerchantItemForSale> itemsForSale = Maps.newLinkedHashMap();
        itemsForSale.put(1, new MerchantItemForSale(ItemType.BEER, 1));
        itemsForSale.put(2, new MerchantItemForSale(ItemType.BROAD_SWORD, 1000));
        itemsForSale.put(3, new MerchantItemForSale(ItemType.IRON_BOOTS, 800));
        itemsForSale.put(4, new MerchantItemForSale(ItemType.IRON_BRACERS, 400));
        itemsForSale.put(5, new MerchantItemForSale(ItemType.IRON_HELMET, 500));
        itemsForSale.put(6, new MerchantItemForSale(ItemType.IRON_CHEST_PLATE, 1500));
        itemsForSale.put(7, new MerchantItemForSale(ItemType.IRON_LEGGINGS, 1100));
        itemsForSale.put(8, new MerchantItemForSale(ItemType.PHANTOM_SWORD, 7000));
        itemsForSale.put(9, new MerchantItemForSale(ItemType.PHANTOM_HELMET, 3500));
        itemsForSale.put(10, new MerchantItemForSale(ItemType.PHANTOM_CHESTPLATE, 5000));
        itemsForSale.put(11, new MerchantItemForSale(ItemType.PHANTOM_BOOTS, 3000));
        itemsForSale.put(12, new MerchantItemForSale(ItemType.PHANTOM_BRACERS, 1500));
        itemsForSale.put(13, new MerchantItemForSale(ItemType.PHANTOM_LEGGINGS, 4000));
        itemsForSale.put(14, new MerchantItemForSale(ItemType.MITHRIL_SWORD, 14000));
        itemsForSale.put(15, new MerchantItemForSale(ItemType.MITHRIL_HELMET, 7000));
        itemsForSale.put(16, new MerchantItemForSale(ItemType.MITHRIL_CHESTPLATE, 10000));
        itemsForSale.put(17, new MerchantItemForSale(ItemType.MITHRIL_BOOTS, 6000));
        itemsForSale.put(18, new MerchantItemForSale(ItemType.MITHRIL_BRACERS, 4000));
        itemsForSale.put(19, new MerchantItemForSale(ItemType.MITHRIL_LEGGINGS, 8000));


        LloydBartender lloydBartender = new LloydBartender(gameManager, new Loot(18, 26, Sets.<Item>newHashSet()), itemsForSale);
        gameManager.getRoomManager().addMerchant(64, lloydBartender);

        Map<Integer, MerchantItemForSale> blacksmithItems = Maps.newHashMap();
        blacksmithItems.put(1, new MerchantItemForSale(ItemType.BROAD_SWORD, 1000));
        blacksmithItems.put(2, new MerchantItemForSale(ItemType.IRON_BOOTS, 800));
        blacksmithItems.put(3, new MerchantItemForSale(ItemType.IRON_BRACERS, 400));
        blacksmithItems.put(4, new MerchantItemForSale(ItemType.IRON_HELMET, 500));
        blacksmithItems.put(5, new MerchantItemForSale(ItemType.IRON_CHEST_PLATE, 1500));
        blacksmithItems.put(6, new MerchantItemForSale(ItemType.IRON_LEGGINGS, 1100));

        Blacksmith blacksmith = new Blacksmith(gameManager, new Loot(18, 26, Sets.<Item>newHashSet()), blacksmithItems);
        gameManager.getRoomManager().addMerchant(66, blacksmith);
        gameManager.getRoomManager().addMerchant(253, blacksmith);

        JimBanker jimBanker = new JimBanker(gameManager, new Loot(18, 26, Sets.<Item>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(65, jimBanker);
        gameManager.getRoomManager().addMerchant(209, jimBanker);


    }
}
