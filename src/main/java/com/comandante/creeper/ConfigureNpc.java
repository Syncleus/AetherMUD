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
import com.comandante.creeper.spawner.SpawnRuleBuilder;
import com.comandante.creeper.spells.*;
import com.comandante.creeper.world.Area;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigureNpc {

    public static void configureAllNpcs(GameManager gameManager) throws IOException {
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

    public static void configure(EntityManager entityManager, GameManager gameManager) throws IOException {

        configureAllNpcs(gameManager);

        Main.startUpMessage("Adding beer");
        ItemSpawner itemSpawner = new ItemSpawner(ItemType.SMALL_HEALTH_POTION, new SpawnRuleBuilder().setArea(Area.NEWBIE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(100).setMaxPerRoom(5).setRandomPercent(40).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner1 = new ItemSpawner(ItemType.SMALL_HEALTH_POTION, new SpawnRuleBuilder().setArea(Area.FANCYHOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner2 = new ItemSpawner(ItemType.SMALL_HEALTH_POTION, new SpawnRuleBuilder().setArea(Area.HOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule(), gameManager);
         ItemSpawner itemSpawner5 = new ItemSpawner(ItemType.KEY, new SpawnRuleBuilder().setArea(Area.LOBBY).setSpawnIntervalTicks(600).setMaxInstances(1).setMaxPerRoom(1).setRandomPercent(5).createSpawnRule(), gameManager);

        entityManager.addEntity(itemSpawner);
        entityManager.addEntity(itemSpawner1);
        entityManager.addEntity(itemSpawner2);
        entityManager.addEntity(itemSpawner5);

        Map<Integer, MerchantItemForSale> itemsForSale = Maps.newLinkedHashMap();
        itemsForSale.put(1, new MerchantItemForSale(ItemType.SMALL_HEALTH_POTION, 8));
        itemsForSale.put(2, new MerchantItemForSale(ItemType.PURPLE_DRANK, 80));
        itemsForSale.put(3, new MerchantItemForSale(ItemType.LEATHER_SATCHEL, 25000));

        LloydBartender lloydBartender = new LloydBartender(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), itemsForSale);
        gameManager.getRoomManager().addMerchant(64, lloydBartender);
        
        Map<Integer, MerchantItemForSale> nigelForSale = Maps.newLinkedHashMap();
        nigelForSale.put(1, new MerchantItemForSale(ItemType.SMALL_HEALTH_POTION, 6));
        
        NigelBartender nigelBartender = new NigelBartender(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), nigelForSale);
        gameManager.getRoomManager().addMerchant(377, nigelBartender);

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

        Map<Integer, MerchantItemForSale> wizarditems = Maps.newHashMap();
        wizarditems.put(1, new MerchantItemForSale(ItemType.LIGHTNING_SPELLBOOKNG, 5000));

        Wizard wizard = new Wizard(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), wizarditems);
        gameManager.getRoomManager().addMerchant(98, wizard);

        JimBanker jimBanker = new JimBanker(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(65, jimBanker);
        gameManager.getRoomManager().addMerchant(209, jimBanker);

        LockerRoomGuy lockerRoomGuy = new LockerRoomGuy(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(63, lockerRoomGuy);

        ForageBuilder marijuanaForageBuilder = new ForageBuilder();
        marijuanaForageBuilder.setItemType(ItemType.MARIJUANA);
        marijuanaForageBuilder.setMinAmt(1);
        marijuanaForageBuilder.setMaxAmt(3);
        marijuanaForageBuilder.setPctOfSuccess(40);
        marijuanaForageBuilder.setForageExperience(4);
        marijuanaForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.WESTERN9_ZONE, marijuanaForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH3_ZONE, marijuanaForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE2_ZONE, marijuanaForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE1_ZONE, marijuanaForageBuilder);

        SpellTriggerRegistry.addSpell(new LightningSpell(gameManager));
    }
}
