package com.comandante.creeper;

import com.comandante.creeper.Items.*;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.Blacksmith;
import com.comandante.creeper.merchant.JimBanker;
import com.comandante.creeper.merchant.LloydBartender;
import com.comandante.creeper.merchant.MerchantItemForSale;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcExporter;
import com.comandante.creeper.spawner.ItemSpawner;
import com.comandante.creeper.spawner.NpcSpawner;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.world.Area;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigureNpc {

    public static void configureAllNpcs(GameManager gameManager) throws FileNotFoundException {
        EntityManager entityManager = gameManager.getEntityManager();
        List<Npc> npcsFromFile = NpcExporter.getNpcsFromFile(gameManager);
        for (Npc npc: npcsFromFile) {
            Main.startUpMessage("Added " + npc.getName());
            entityManager.addEntity(npc);
            Set<SpawnRule> spawnRules = npc.getSpawnRules();
            for (SpawnRule spawnRule: spawnRules) {
                entityManager.addEntity(new NpcSpawner(npc, gameManager, spawnRule));
            }
        }
    }

    public static void configure(EntityManager entityManager, GameManager gameManager) throws FileNotFoundException {

        configureAllNpcs(gameManager);


        Main.startUpMessage("Adding beer");
        ItemSpawner itemSpawner = new ItemSpawner(ItemType.BEER, new SpawnRule(Area.NEWBIE_ZONE, 10, 100, 5, 40), gameManager);
        ItemSpawner itemSpawner1 = new ItemSpawner(ItemType.BEER, new SpawnRule(Area.FANCYHOUSE_ZONE, 10, 12, 2, 50), gameManager);
        ItemSpawner itemSpawner2 = new ItemSpawner(ItemType.BEER, new SpawnRule(Area.HOUSE_ZONE, 10, 12, 2, 50), gameManager);

        entityManager.addEntity(itemSpawner);
        entityManager.addEntity(itemSpawner1);
        entityManager.addEntity(itemSpawner2);

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
        itemsForSale.put(20, new MerchantItemForSale(ItemType.PYAMITE_SWORD, 20000));
        itemsForSale.put(21, new MerchantItemForSale(ItemType.PYAMITE_HELMET, 14000));
        itemsForSale.put(22, new MerchantItemForSale(ItemType.PYAMITE_CHESTPLATE, 20000));
        itemsForSale.put(23, new MerchantItemForSale(ItemType.PYAMITE_BOOTS, 12000));
        itemsForSale.put(24, new MerchantItemForSale(ItemType.PYAMITE_BRACERS, 8000));
        itemsForSale.put(25, new MerchantItemForSale(ItemType.PYAMITE_LEGGINGS, 16000));
        itemsForSale.put(26, new MerchantItemForSale(ItemType.TAPPERHET_SWORD, 60000));



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

        ForageBuilder forageBuilder = new ForageBuilder();
        forageBuilder.setItemType(ItemType.MARIJUANA);
        forageBuilder.setMinAmt(1);
        forageBuilder.setMaxAmt(3);
        forageBuilder.setPctOfSuccess(40);
        forageBuilder.setForageExperience(4);
        forageBuilder.setCoolDownTicks(30);
        gameManager.getForageManager().addForageToArea(Area.WESTERN9_ZONE, forageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH3_ZONE, forageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE2_ZONE, forageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE1_ZONE, forageBuilder);
    }
}
