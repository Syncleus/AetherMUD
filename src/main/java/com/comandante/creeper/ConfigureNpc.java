package com.comandante.creeper;

import com.comandante.creeper.Items.*;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.*;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcExporter;
import com.comandante.creeper.spawner.ItemSpawner;
import com.comandante.creeper.spawner.NpcSpawner;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.spells.ClumsinessSpell;
import com.comandante.creeper.spells.LightningSpell;
import com.comandante.creeper.spells.SpellRegistry;
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

        ItemSpawner itemSpawner3 = new ItemSpawner(ItemType.PURPLE_DRANK, new SpawnRule(Area.FANCYHOUSE_ZONE, 10, 30, 5, 50), gameManager);
        ItemSpawner itemSpawner4 = new ItemSpawner(ItemType.PURPLE_DRANK, new SpawnRule(Area.HOUSE_ZONE, 10, 30, 5, 50), gameManager);

        entityManager.addEntity(itemSpawner);
        entityManager.addEntity(itemSpawner1);
        entityManager.addEntity(itemSpawner2);
        entityManager.addEntity(itemSpawner3);
        entityManager.addEntity(itemSpawner4);

        Map<Integer, MerchantItemForSale> itemsForSale = Maps.newLinkedHashMap();
        itemsForSale.put(1, new MerchantItemForSale(ItemType.BEER, 8));
        itemsForSale.put(2, new MerchantItemForSale(ItemType.BROAD_SWORD, 1000));
        itemsForSale.put(3, new MerchantItemForSale(ItemType.IRON_BOOTS, 800));
        itemsForSale.put(4, new MerchantItemForSale(ItemType.IRON_BRACERS, 400));
        itemsForSale.put(5, new MerchantItemForSale(ItemType.IRON_HELMET, 500));
        itemsForSale.put(6, new MerchantItemForSale(ItemType.IRON_CHEST_PLATE, 1500));
        itemsForSale.put(7, new MerchantItemForSale(ItemType.IRON_LEGGINGS, 1100));
        itemsForSale.put(8, new MerchantItemForSale(ItemType.PHANTOM_SWORD, 7000));
        itemsForSale.put(9, new MerchantItemForSale(ItemType.PHANTOM_HELMET, 3500));
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
        itemsForSale.put(27, new MerchantItemForSale(ItemType.VULCERIUM_SWORD, 160000));
        itemsForSale.put(28, new MerchantItemForSale(ItemType.VULCERIUM_HELMET, 37000));
        itemsForSale.put(29, new MerchantItemForSale(ItemType.VULCERIUM_CHESTPLATE, 52000));
        itemsForSale.put(30, new MerchantItemForSale(ItemType.VULCERIUM_BOOTS, 38000));
        itemsForSale.put(31, new MerchantItemForSale(ItemType.VULCERIUM_BRACERS, 29000));
        itemsForSale.put(32, new MerchantItemForSale(ItemType.VULCERIUM_LEGGINGS, 52000));
        itemsForSale.put(33, new MerchantItemForSale(ItemType.PURPLE_DRANK, 80));
        itemsForSale.put(33, new MerchantItemForSale(ItemType.LEATHER_SATCHEL, 25000));
        itemsForSale.put(34, new MerchantItemForSale(ItemType.BIGGERS_SKIN_SATCHEL, 250000));

        LloydBartender lloydBartender = new LloydBartender(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), itemsForSale);
        gameManager.getRoomManager().addMerchant(64, lloydBartender);

        Map<Integer, MerchantItemForSale> blacksmithItems = Maps.newHashMap();
        blacksmithItems.put(1, new MerchantItemForSale(ItemType.BROAD_SWORD, 1000));
        blacksmithItems.put(2, new MerchantItemForSale(ItemType.IRON_BOOTS, 800));
        blacksmithItems.put(3, new MerchantItemForSale(ItemType.IRON_BRACERS, 400));
        blacksmithItems.put(4, new MerchantItemForSale(ItemType.IRON_HELMET, 500));
        blacksmithItems.put(5, new MerchantItemForSale(ItemType.IRON_CHEST_PLATE, 1500));
        blacksmithItems.put(6, new MerchantItemForSale(ItemType.IRON_LEGGINGS, 1100));

        Blacksmith blacksmith = new Blacksmith(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), blacksmithItems);
        gameManager.getRoomManager().addMerchant(66, blacksmith);
        gameManager.getRoomManager().addMerchant(253, blacksmith);

        JimBanker jimBanker = new JimBanker(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(65, jimBanker);
        gameManager.getRoomManager().addMerchant(209, jimBanker);

        LockerRoomGuy lockerRoomGuy = new LockerRoomGuy(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(63, lockerRoomGuy);

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

        SpellRegistry.addSpell(new LightningSpell(gameManager));
        SpellRegistry.addSpell(new ClumsinessSpell(gameManager));

    }
}
