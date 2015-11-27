package com.comandante.creeper;

import com.comandante.creeper.Items.*;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.merchant.*;
import com.comandante.creeper.merchant.GrimulfWizard;
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
        ItemSpawner itemSpawner = new ItemSpawner(ItemType.BEER, new SpawnRuleBuilder().setArea(Area.NEWBIE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(100).setMaxPerRoom(5).setRandomPercent(40).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner1 = new ItemSpawner(ItemType.BEER, new SpawnRuleBuilder().setArea(Area.FANCYHOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner2 = new ItemSpawner(ItemType.BEER, new SpawnRuleBuilder().setArea(Area.HOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner3 = new ItemSpawner(ItemType.PURPLE_DRANK, new SpawnRuleBuilder().setArea(Area.FANCYHOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(30).setMaxPerRoom(5).setRandomPercent(50).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner4 = new ItemSpawner(ItemType.PURPLE_DRANK, new SpawnRuleBuilder().setArea(Area.HOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(30).setMaxPerRoom(5).setRandomPercent(50).createSpawnRule(), gameManager);
        ItemSpawner itemSpawner5 = new ItemSpawner(ItemType.KEY, new SpawnRuleBuilder().setArea(Area.LOBBY).setSpawnIntervalTicks(600).setMaxInstances(1).setMaxPerRoom(1).setRandomPercent(5).createSpawnRule(), gameManager);

        entityManager.addEntity(itemSpawner);
        entityManager.addEntity(itemSpawner1);
        entityManager.addEntity(itemSpawner2);
        entityManager.addEntity(itemSpawner3);
        entityManager.addEntity(itemSpawner4);
        entityManager.addEntity(itemSpawner5);

        Map<Integer, MerchantItemForSale> itemsForSale = Maps.newLinkedHashMap();
        itemsForSale.put(1, new MerchantItemForSale(ItemType.BEER, 8));
        itemsForSale.put(2, new MerchantItemForSale(ItemType.PURPLE_DRANK, 80));
        itemsForSale.put(3, new MerchantItemForSale(ItemType.LEATHER_SATCHEL, 25000));
        itemsForSale.put(4, new MerchantItemForSale(ItemType.BIGGERS_SKIN_SATCHEL, 250000));
        itemsForSale.put(5, new MerchantItemForSale(ItemType.STRENGTH_ELIXIR, 2000));
        itemsForSale.put(6, new MerchantItemForSale(ItemType.CHRONIC_JOOSE, 4000));
        itemsForSale.put(7, new MerchantItemForSale(ItemType.GUCCI_PANTS, 80000000));

        LloydBartender lloydBartender = new LloydBartender(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), itemsForSale);
        gameManager.getRoomManager().addMerchant(64, lloydBartender);
        
        Map<Integer, MerchantItemForSale> nigelForSale = Maps.newLinkedHashMap();
        nigelForSale.put(1, new MerchantItemForSale(ItemType.BEER, 6));
        
        NigelBartender nigelBartender = new NigelBartender(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), nigelForSale);
        gameManager.getRoomManager().addMerchant(377, nigelBartender);

        Map<Integer, MerchantItemForSale> blacksmithItems = Maps.newHashMap();
        blacksmithItems.put(1, new MerchantItemForSale(ItemType.BROAD_SWORD, 1000));
        blacksmithItems.put(2, new MerchantItemForSale(ItemType.IRON_BOOTS, 800));
        blacksmithItems.put(3, new MerchantItemForSale(ItemType.IRON_BRACERS, 400));
        blacksmithItems.put(4, new MerchantItemForSale(ItemType.IRON_HELMET, 500));
        blacksmithItems.put(5, new MerchantItemForSale(ItemType.IRON_CHEST_PLATE, 1500));
        blacksmithItems.put(6, new MerchantItemForSale(ItemType.IRON_LEGGINGS, 1100));
        blacksmithItems.put(7, new MerchantItemForSale(ItemType.PHANTOM_SWORD, 7000));
        blacksmithItems.put(8, new MerchantItemForSale(ItemType.PHANTOM_HELMET, 3500));
        blacksmithItems.put(9, new MerchantItemForSale(ItemType.PHANTOM_BOOTS, 3000));
        blacksmithItems.put(10, new MerchantItemForSale(ItemType.PHANTOM_BRACERS, 1500));
        blacksmithItems.put(11, new MerchantItemForSale(ItemType.PHANTOM_LEGGINGS, 4000));
        blacksmithItems.put(12, new MerchantItemForSale(ItemType.MITHRIL_SWORD, 14000));
        blacksmithItems.put(13, new MerchantItemForSale(ItemType.MITHRIL_HELMET, 7000));
        blacksmithItems.put(14, new MerchantItemForSale(ItemType.MITHRIL_CHESTPLATE, 10000));
        blacksmithItems.put(15, new MerchantItemForSale(ItemType.MITHRIL_BOOTS, 6000));
        blacksmithItems.put(16, new MerchantItemForSale(ItemType.MITHRIL_BRACERS, 4000));
        blacksmithItems.put(17, new MerchantItemForSale(ItemType.MITHRIL_LEGGINGS, 8000));
        blacksmithItems.put(18, new MerchantItemForSale(ItemType.PYAMITE_SWORD, 20000));
        blacksmithItems.put(19, new MerchantItemForSale(ItemType.PYAMITE_HELMET, 14000));
        blacksmithItems.put(20, new MerchantItemForSale(ItemType.PYAMITE_CHESTPLATE, 20000));
        blacksmithItems.put(21, new MerchantItemForSale(ItemType.PYAMITE_BOOTS, 12000));
        blacksmithItems.put(22, new MerchantItemForSale(ItemType.PYAMITE_BRACERS, 8000));
        blacksmithItems.put(23, new MerchantItemForSale(ItemType.PYAMITE_LEGGINGS, 16000));
        blacksmithItems.put(24, new MerchantItemForSale(ItemType.VULCERIUM_SWORD, 160000));
        blacksmithItems.put(25, new MerchantItemForSale(ItemType.VULCERIUM_HELMET, 37000));
        blacksmithItems.put(26, new MerchantItemForSale(ItemType.VULCERIUM_CHESTPLATE, 52000));
        blacksmithItems.put(27, new MerchantItemForSale(ItemType.VULCERIUM_BOOTS, 38000));
        blacksmithItems.put(28, new MerchantItemForSale(ItemType.VULCERIUM_BRACERS, 29000));
        blacksmithItems.put(29, new MerchantItemForSale(ItemType.VULCERIUM_LEGGINGS, 52000));
        blacksmithItems.put(30, new MerchantItemForSale(ItemType.BISMUTH_SWORD, 3000000));
        blacksmithItems.put(31, new MerchantItemForSale(ItemType.BISMUTH_HELMET, 2400000));
        blacksmithItems.put(32, new MerchantItemForSale(ItemType.LEATHER_SATCHEL, 25000));

        Map<Integer, MerchantItemForSale> grimulfItems = Maps.newHashMap();        
        grimulfItems.put(1, new MerchantItemForSale(ItemType.MARIJUANA, 100));
        grimulfItems.put(2, new MerchantItemForSale(ItemType.TAPPERHET_SWORD, 60000));
        grimulfItems.put(3, new MerchantItemForSale(ItemType.BIGGERS_SKIN_SATCHEL, 250000));
        grimulfItems.put(4, new MerchantItemForSale(ItemType.DWARF_BOOTS_OF_AGILITY, 10000));
       // grimulfItems.put(5, new MerchantItemForSale(ItemType.GOLDEN_WAND, 4000000000));
        grimulfItems.put(5, new MerchantItemForSale(ItemType.MITHAEM_LEAF, 1400000000));

        Map<Integer, MerchantItemForSale> ketilItems = Maps.newHashMap();          
        ketilItems.put(1, new MerchantItemForSale(ItemType.BEER, 12));
        ketilItems.put(2, new MerchantItemForSale(ItemType.PURPLE_DRANK, 120));
        ketilItems.put(3, new MerchantItemForSale(ItemType.MARIJUANA, 100));
        ketilItems.put(4, new MerchantItemForSale(ItemType.PYAMITE_ICEAXE, 10000000));
        ketilItems.put(5, new MerchantItemForSale(ItemType.STRENGTH_ELIXIR, 3000));
        ketilItems.put(6, new MerchantItemForSale(ItemType.CHRONIC_JOOSE, 5500));
        ketilItems.put(7, new MerchantItemForSale(ItemType.VIAGRA_SWORD, 900000000));
        
        Map<Integer, MerchantItemForSale> blackbeardItems = Maps.newHashMap();          
        blackbeardItems.put(1, new MerchantItemForSale(ItemType.IRON_LOCKPICKING_SET, 8000000));
        blackbeardItems.put(2, new MerchantItemForSale(ItemType.PYAMITE_LOCKPICKING_SET, 8000000));
        blackbeardItems.put(3, new MerchantItemForSale(ItemType.SPOOL_OF_CLIMBING_ROPE, 8000000));
        blackbeardItems.put(4, new MerchantItemForSale(ItemType.BLACK_CLOAK, 8000000));
        blackbeardItems.put(5, new MerchantItemForSale(ItemType.SMELTING_CRUCIBLE, 8000000));
        blackbeardItems.put(6, new MerchantItemForSale(ItemType.LEATHER_SHOES, 8000000));
        
        Map<Integer, MerchantItemForSale> wentworthItems = Maps.newHashMap();          
        wentworthItems.put(1, new MerchantItemForSale(ItemType.GENTLEMANS_TOP_HAT, 8000000));
        wentworthItems.put(2, new MerchantItemForSale(ItemType.LEATHER_GLOVES, 8000000));
        wentworthItems.put(3, new MerchantItemForSale(ItemType.WOOL_SCARF, 8000000));
        wentworthItems.put(4, new MerchantItemForSale(ItemType.LEATHER_BELT, 8000000));
        wentworthItems.put(5, new MerchantItemForSale(ItemType.SILK_SASH, 8000000));
        wentworthItems.put(6, new MerchantItemForSale(ItemType.LEATHER_SCABBARD, 8000000));
        wentworthItems.put(7, new MerchantItemForSale(ItemType.BYSENSKIN_SCABBARD, 19000000));
        wentworthItems.put(8, new MerchantItemForSale(ItemType.IRON_SPECTACLES, 8000000));
        wentworthItems.put(9, new MerchantItemForSale(ItemType.GOLDEN_SPECTACLES, 40000000));
        wentworthItems.put(10, new MerchantItemForSale(ItemType.NOBLEMANS_SHOULDER_CLASP, 200000000));
        wentworthItems.put(11, new MerchantItemForSale(ItemType.WOODSMANS_TUNIC, 8000000));
        wentworthItems.put(12, new MerchantItemForSale(ItemType.PLAIN_WHITE_ROBE, 8000000));
        wentworthItems.put(13, new MerchantItemForSale(ItemType.FINE_POWDERED_WIG, 30000000));
        wentworthItems.put(14, new MerchantItemForSale(ItemType.HABROK_FEATHER_HAT, 10000000));
        wentworthItems.put(15, new MerchantItemForSale(ItemType.POLISHED_IRON_CODPIECE, 30000000));
        wentworthItems.put(16, new MerchantItemForSale(ItemType.GOLDEN_CODPIECE, 950000000));

        BlackbeardRogue rogueshop = new BlackbeardRogue(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), blackbeardItems);
        gameManager.getRoomManager().addMerchant(864, rogueshop);
        
        Blacksmith blacksmith = new Blacksmith(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), blacksmithItems);
        gameManager.getRoomManager().addMerchant(66, blacksmith);
        gameManager.getRoomManager().addMerchant(253, blacksmith);

        JimBanker jimBanker = new JimBanker(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(65, jimBanker);
        gameManager.getRoomManager().addMerchant(209, jimBanker);

        LockerRoomGuy lockerRoomGuy = new LockerRoomGuy(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), null);
        gameManager.getRoomManager().addMerchant(63, lockerRoomGuy);

        GrimulfWizard grimulfWizard = new GrimulfWizard(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), grimulfItems);
        gameManager.getRoomManager().addMerchant(102, grimulfWizard);
        
        WentworthTailor wentworthTailor = new WentworthTailor(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), wentworthItems);
        gameManager.getRoomManager().addMerchant(865, wentworthTailor);
        
        KetilCommissary ketilCommissary = new KetilCommissary(gameManager, new Loot(18, 26, Sets.<ItemType>newHashSet()), ketilItems);
        gameManager.getRoomManager().addMerchant(420, ketilCommissary);

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

        ForageBuilder hazeForageBuilder = new ForageBuilder();
        hazeForageBuilder.setItemType(ItemType.HAZE);
        hazeForageBuilder.setMinAmt(1);
        hazeForageBuilder.setMaxAmt(3);
        hazeForageBuilder.setPctOfSuccess(5);
        hazeForageBuilder.setForageExperience(10);
        hazeForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE8_ZONE, hazeForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE9_ZONE, hazeForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE10_ZONE, hazeForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH12_ZONE, hazeForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH2_ZONE, hazeForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH3_ZONE, hazeForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH4_ZONE, hazeForageBuilder);

        ForageBuilder aexirianForageBuilder = new ForageBuilder();
        aexirianForageBuilder.setItemType(ItemType.AEXIRIAN_ROOT);
        aexirianForageBuilder.setMinAmt(1);
        aexirianForageBuilder.setMaxAmt(3);
        aexirianForageBuilder.setPctOfSuccess(5);
        aexirianForageBuilder.setForageExperience(10);
        aexirianForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, aexirianForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, aexirianForageBuilder);

        ForageBuilder mithaemForageBuilder = new ForageBuilder();
        mithaemForageBuilder.setItemType(ItemType.MITHAEM_LEAF);
        mithaemForageBuilder.setMinAmt(1);
        mithaemForageBuilder.setMaxAmt(3);
        mithaemForageBuilder.setPctOfSuccess(5);
        mithaemForageBuilder.setForageExperience(10);
        mithaemForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.TISLAND3_ZONE, mithaemForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.TISLAND4_ZONE, mithaemForageBuilder);

        ForageBuilder duriccaForageBuilder = new ForageBuilder();
        duriccaForageBuilder.setItemType(ItemType.DURICCA_ROOT);
        duriccaForageBuilder.setMinAmt(1);
        duriccaForageBuilder.setMaxAmt(3);
        duriccaForageBuilder.setPctOfSuccess(5);
        duriccaForageBuilder.setForageExperience(10);
        duriccaForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.TOFT1_ZONE, duriccaForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.TOFT2_ZONE, duriccaForageBuilder);

        ForageBuilder pondeselForageBuilder = new ForageBuilder();
        pondeselForageBuilder.setItemType(ItemType.PONDESEL_BERRY);
        pondeselForageBuilder.setMinAmt(1);
        pondeselForageBuilder.setMaxAmt(3);
        pondeselForageBuilder.setPctOfSuccess(5);
        pondeselForageBuilder.setForageExperience(10);
        pondeselForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.TISLAND6_ZONE, pondeselForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.TISLAND7_ZONE, pondeselForageBuilder);

        ForageBuilder vikalionusForageBuilder = new ForageBuilder();
        vikalionusForageBuilder.setItemType(ItemType.VIKALIONUS_CAP);
        vikalionusForageBuilder.setMinAmt(1);
        vikalionusForageBuilder.setMaxAmt(3);
        vikalionusForageBuilder.setPctOfSuccess(5);
        vikalionusForageBuilder.setForageExperience(10);
        vikalionusForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.NORTH12_ZONE, vikalionusForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH13_ZONE, vikalionusForageBuilder);

        ForageBuilder loornsForageBuilder = new ForageBuilder();
        loornsForageBuilder.setItemType(ItemType.LOORNS_LACE);
        loornsForageBuilder.setMinAmt(1);
        loornsForageBuilder.setMaxAmt(3);
        loornsForageBuilder.setPctOfSuccess(5);
        loornsForageBuilder.setForageExperience(10);
        loornsForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE4_ZONE, loornsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE5_ZONE, loornsForageBuilder);

        ForageBuilder tournearesForageBuilder = new ForageBuilder();
        tournearesForageBuilder.setItemType(ItemType.TOURNEARES_LEAF);
        tournearesForageBuilder.setMinAmt(1);
        tournearesForageBuilder.setMaxAmt(3);
        tournearesForageBuilder.setPctOfSuccess(5);
        tournearesForageBuilder.setForageExperience(10);
        tournearesForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, tournearesForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, tournearesForageBuilder);

        ForageBuilder haussianForageBuilder = new ForageBuilder();
        haussianForageBuilder.setItemType(ItemType.HAUSSIAN_BERRY);
        haussianForageBuilder.setMinAmt(1);
        haussianForageBuilder.setMaxAmt(3);
        haussianForageBuilder.setPctOfSuccess(5);
        haussianForageBuilder.setForageExperience(10);
        haussianForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, haussianForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, haussianForageBuilder);

        ForageBuilder pertilliumForageBuilder = new ForageBuilder();
        pertilliumForageBuilder.setItemType(ItemType.PERTILLIUM_ROOT);
        pertilliumForageBuilder.setMinAmt(1);
        pertilliumForageBuilder.setMaxAmt(3);
        pertilliumForageBuilder.setPctOfSuccess(5);
        pertilliumForageBuilder.setForageExperience(10);
        pertilliumForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, pertilliumForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, pertilliumForageBuilder);

        ForageBuilder hycianthisForageBuilder = new ForageBuilder();
        hycianthisForageBuilder.setItemType(ItemType.HYCIANTHIS_BARK);
        hycianthisForageBuilder.setMinAmt(1);
        hycianthisForageBuilder.setMaxAmt(3);
        hycianthisForageBuilder.setPctOfSuccess(5);
        hycianthisForageBuilder.setForageExperience(10);
        hycianthisForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, hycianthisForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, hycianthisForageBuilder);

        ForageBuilder punilareForageBuilder = new ForageBuilder();
        punilareForageBuilder.setItemType(ItemType.PUNILARE_FERN);
        punilareForageBuilder.setMinAmt(1);
        punilareForageBuilder.setMaxAmt(3);
        punilareForageBuilder.setPctOfSuccess(5);
        punilareForageBuilder.setForageExperience(10);
        punilareForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, punilareForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, punilareForageBuilder);

        ForageBuilder keakiarForageBuilder = new ForageBuilder();
        keakiarForageBuilder.setItemType(ItemType.KEAKIAR_CAP);
        keakiarForageBuilder.setMinAmt(1);
        keakiarForageBuilder.setMaxAmt(3);
        keakiarForageBuilder.setPctOfSuccess(5);
        keakiarForageBuilder.setForageExperience(10);
        keakiarForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, keakiarForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, keakiarForageBuilder);

        ForageBuilder dirtyBombForageBuilder = new ForageBuilder();
        dirtyBombForageBuilder.setItemType(ItemType.DIRTY_BOMB);
        dirtyBombForageBuilder.setMinAmt(1);
        dirtyBombForageBuilder.setMaxAmt(3);
        dirtyBombForageBuilder.setPctOfSuccess(2);
        dirtyBombForageBuilder.setForageExperience(100);
        dirtyBombForageBuilder.setCoolDownTicks(600);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, dirtyBombForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, dirtyBombForageBuilder);

        ForageBuilder radsuitHelmetForageBuilder = new ForageBuilder();
        radsuitHelmetForageBuilder.setItemType(ItemType.RADSUIT_HELMET);
        radsuitHelmetForageBuilder.setMinAmt(1);
        radsuitHelmetForageBuilder.setMaxAmt(1);
        radsuitHelmetForageBuilder.setPctOfSuccess(.5);
        radsuitHelmetForageBuilder.setForageExperience(500);
        radsuitHelmetForageBuilder.setCoolDownTicks(1000);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, radsuitHelmetForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, radsuitHelmetForageBuilder);

        ForageBuilder radsuitChestplateForageBuilder = new ForageBuilder();
        radsuitChestplateForageBuilder.setItemType(ItemType.RADSUIT_CHESTPLATE);
        radsuitChestplateForageBuilder.setMinAmt(1);
        radsuitChestplateForageBuilder.setMaxAmt(1);
        radsuitChestplateForageBuilder.setPctOfSuccess(.5);
        radsuitChestplateForageBuilder.setForageExperience(500);
        radsuitChestplateForageBuilder.setCoolDownTicks(1000);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, radsuitChestplateForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, radsuitChestplateForageBuilder);

        ForageBuilder radsuitBracersForageBuilder = new ForageBuilder();
        radsuitBracersForageBuilder.setItemType(ItemType.RADSUIT_BRACERS);
        radsuitBracersForageBuilder.setMinAmt(1);
        radsuitBracersForageBuilder.setMaxAmt(1);
        radsuitBracersForageBuilder.setPctOfSuccess(.5);
        radsuitBracersForageBuilder.setForageExperience(500);
        radsuitBracersForageBuilder.setCoolDownTicks(1000);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, radsuitBracersForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, radsuitBracersForageBuilder);

        ForageBuilder radsuitLeggingsForageBuilder = new ForageBuilder();
        radsuitLeggingsForageBuilder.setItemType(ItemType.RADSUIT_LEGGINGS);
        radsuitLeggingsForageBuilder.setMinAmt(1);
        radsuitLeggingsForageBuilder.setMaxAmt(1);
        radsuitLeggingsForageBuilder.setPctOfSuccess(.5);
        radsuitLeggingsForageBuilder.setForageExperience(500);
        radsuitLeggingsForageBuilder.setCoolDownTicks(1000);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, radsuitLeggingsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, radsuitLeggingsForageBuilder);

        ForageBuilder radsuitBootsForageBuilder = new ForageBuilder();
        radsuitBootsForageBuilder.setItemType(ItemType.RADSUIT_BOOTS);
        radsuitBootsForageBuilder.setMinAmt(1);
        radsuitBootsForageBuilder.setMaxAmt(1);
        radsuitBootsForageBuilder.setPctOfSuccess(.5);
        radsuitBootsForageBuilder.setForageExperience(500);
        radsuitBootsForageBuilder.setCoolDownTicks(1000);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE15_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH14_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.SOUTH1_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE14_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH10_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.NORTH11_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN4_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN5_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN2_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.WESTERN3_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE6_ZONE, radsuitBootsForageBuilder);
        gameManager.getForageManager().addForageToArea(Area.BLOODRIDGE7_ZONE, radsuitBootsForageBuilder);

        SpellRegistry.addSpell(new LightningSpell(gameManager));
        SpellRegistry.addSpell(new FreezeSpell(gameManager));
        SpellRegistry.addSpell(new ClumsinessSpell(gameManager));
        SpellRegistry.addSpell(new RestoreSpell(gameManager));
        SpellRegistry.addSpell(new AidsSpell(gameManager));
        SpellRegistry.addSpell(new BlackHoleSpell(gameManager));
    }
}
