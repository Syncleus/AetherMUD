//package com.comandante.creeper.items;
//
//import com.comandante.creeper.server.player_communication.Color;
//import com.comandante.creeper.spawner.SpawnRule;
//import com.comandante.creeper.spawner.SpawnRuleBuilder;
//import com.comandante.creeper.stats.Stats;
//import com.comandante.creeper.stats.StatsBuilder;
//import com.comandante.creeper.storage.ItemStorage;
//import com.comandante.creeper.world.model.Area;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.junit.Test;
//
//import java.util.Set;
//
//
//public class ItemMetadataTest {
//
//    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//    @Test
//    public void testSerialization() throws Exception {
//
////        private String itemName;
////        private String itemDescription;
////        private String restingName;
////        private int numberOfUses;
////        private int valueInGold;
////        private int itemHalfLifeTicks;
////        private Rarity rarity;
////        private Equipment equipment;
////        private Set<Effect> effects;
////        private List<String> itemTriggers;
//
//
//        ItemMetadata itemMetadata = new ItemMetadata();
//        itemMetadata.setInternalItemName("little potion");
//        itemMetadata.setItemName("a " + Color.RED + "little potion" + Color.RESET);
//        itemMetadata.setItemDescription("a " + Color.RED + "little potion" + Color.RESET);
//        itemMetadata.setRestingName("a " + Color.GREEN + "little potion" + Color.RESET);
//        itemMetadata.setMaxUses(1);
//        itemMetadata.setDisposable(true);
//        itemMetadata.setValueInGold(30);
//        itemMetadata.setItemHalfLifeTicks(60);
//        itemMetadata.setRarity(Rarity.BASIC);
//        // itemMetadata.setValidTimeOfDays(Sets.newHashSet(TimeTracker.TimeOfDay.MORNING, TimeTracker.TimeOfDay.NIGHT));
//
//        SpawnRule spawnRule1 = new SpawnRuleBuilder().setArea(Area.NEWBIE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(100).setMaxPerRoom(5).setRandomPercent(40).createSpawnRule();
//        SpawnRule spawnRule2 = new SpawnRuleBuilder().setArea(Area.FANCYHOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule();
//        SpawnRule spawnRule3 = new SpawnRuleBuilder().setArea(Area.HOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule();
//        itemMetadata.setSpawnRules(Sets.newHashSet(spawnRule1, spawnRule2, spawnRule3));
//
//        Stats stats = new StatsBuilder().setInventorySize(100).createStats();
//        //  final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);
//
//        //   itemMetadata.setEquipment(equipment);
//        itemMetadata.setItemTriggers(Lists.newArrayList("p", "little potion", "potion"));
//
//
//        EffectBuilder effectBuilder = new EffectBuilder()
//                .setDurationStats(new StatsBuilder().setCurrentHealth(25).createStats())
//                .setEffectApplyMessages(Lists.newArrayList("You start to feel a healing effect."))
//                .setEffectDescription("Little healing.")
//                .setEffectName("Little Potion Heal")
//                .setFrozenMovement(false)
//                .setLifeSpanTicks(0);
//
//
//        itemMetadata.setEffects(Sets.newHashSet(effectBuilder.createEffect()));
//
//
//        ItemStorage itemStorage = new ItemStorage(gson);
//        itemStorage.saveItemMetaData(itemMetadata);
//
//
//        String s = gson.toJson(itemMetadata);
//        System.out.println(s);
//
//    }
//
//    @Test
//    public void generator() throws Exception {
//
//        ItemStorage itemStorage = new ItemStorage(gson);
//
//        // BERSERKER BOOTS
//        ItemMetadata itemMetadata = metadataFrom(ItemType.BERSEKER_BOOTS);
//        Stats stats = new StatsBuilder().setArmorRating(3).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
//        itemMetadata.setEquipment(equipment);
//        itemMetadata.setInternalItemName("beserker boots");
//        itemStorage.saveItemMetaData(itemMetadata);
//
//        // BERSERKER HELM
//        ItemMetadata itemMetadataHelm = metadataFrom(ItemType.BERSEKER_HELM);
//        Stats statsHelm = new StatsBuilder().setArmorRating(3).createStats();
//        final Equipment equipmentHeml = new Equipment(EquipmentSlotType.HEAD, statsHelm);
//        itemMetadataHelm.setEquipment(equipmentHeml);
//        itemMetadataHelm.setInternalItemName("beserker helm");
//        itemStorage.saveItemMetaData(itemMetadataHelm);
//
//
//        // BERSERKER SHORTS
//        ItemMetadata itemMetadataShorts = metadataFrom(ItemType.BERSEKER_SHORTS);
//        Stats statsShorts = new StatsBuilder().setArmorRating(4).createStats();
//        final Equipment equipmentShorts = new Equipment(EquipmentSlotType.LEGS, statsShorts);
//        itemMetadataShorts.setEquipment(equipmentShorts);
//        itemMetadataShorts.setInternalItemName("beserker shorts");
//        itemStorage.saveItemMetaData(itemMetadataShorts);
//
//
//        // BERSERKER BATON
//        ItemMetadata itemMetadataBaton = metadataFrom(ItemType.BERSERKER_BATON);
//        Stats statsBaton = new StatsBuilder().setWeaponRatingMin(4).setWeaponRatingMax(6).createStats();
//        final Equipment equipmentBaton = new Equipment(EquipmentSlotType.HAND, statsBaton);
//        itemMetadataBaton.setEquipment(equipmentBaton);
//        itemMetadataBaton.setInternalItemName("beserker baton");
//        itemStorage.saveItemMetaData(itemMetadataBaton);
//
//        // BERSERKER BATON
//        ItemMetadata itemMetadataBracers = metadataFrom(ItemType.BERSERKER_BRACERS);
//        Stats statsBracers = new StatsBuilder().setArmorRating(4).createStats();
//        final Equipment equipmentBracers = new Equipment(EquipmentSlotType.WRISTS, statsBracers);
//        itemMetadataBracers.setEquipment(equipmentBracers);
//        itemMetadataBracers.setInternalItemName("beserker bracers");
//        itemStorage.saveItemMetaData(itemMetadataBracers);
//
//
//        // BERSERKER BATON
//        ItemMetadata itemMetadataChest = metadataFrom(ItemType.BERSERKER_CHEST);
//        Stats statsChest = new StatsBuilder().setArmorRating(6).createStats();
//        final Equipment equipmentChest = new Equipment(EquipmentSlotType.CHEST, statsChest);
//        itemMetadataChest.setEquipment(equipmentChest);
//        itemMetadataChest.setInternalItemName("beserker chest");
//        itemStorage.saveItemMetaData(itemMetadataChest);
//
//        // BIGGERS SKIN SATCHEL
//        ItemMetadata itemMetadataBiggersSkinSatchel = metadataFrom(ItemType.BIGGERS_SKIN_SATCHEL);
//        Stats statsBiggersSkinSatchel = new StatsBuilder().setInventorySize(100).createStats();
//        final Equipment equipmentBiggersSkinSatchel= new Equipment(EquipmentSlotType.BAG, statsBiggersSkinSatchel);
//        itemMetadataBiggersSkinSatchel.setEquipment(equipmentBiggersSkinSatchel);
//        itemMetadataBiggersSkinSatchel.setInternalItemName("biggers skin satchel");
//        itemStorage.saveItemMetaData(itemMetadataBiggersSkinSatchel);
//
//
//        // Key
//        ItemMetadata itemMetadataKey = metadataFrom(ItemType.KEY);
//        itemMetadataKey.setInternalItemName("basic key");
//        SpawnRule spawnRule = new SpawnRuleBuilder().setArea(Area.LOBBY).setSpawnIntervalTicks(600).setMaxInstances(1).setMaxPerRoom(1).setRandomPercent(5).createSpawnRule();
//        itemMetadataKey.setSpawnRules(Sets.newHashSet(spawnRule));
//        itemStorage.saveItemMetaData(itemMetadataKey);
//
//
//        // Marijuana
//        ItemMetadata itemMetadataMarijuana = metadataFrom(ItemType.MARIJUANA);
//        itemMetadataMarijuana.setInternalItemName("marijuana");
//        ForageBuilder marijuanaForageBuilder = new ForageBuilder();
//        marijuanaForageBuilder.setMinAmt(1);
//        marijuanaForageBuilder.setMaxAmt(3);
//        marijuanaForageBuilder.setPctOfSuccess(40);
//        marijuanaForageBuilder.setForageExperience(4);
//        marijuanaForageBuilder.setCoolDownTicks(600);
//        marijuanaForageBuilder.setAreas(Sets.newHashSet(Area.WESTERN9_ZONE, Area.NORTH3_ZONE, Area.BLOODRIDGE2_ZONE, Area.BLOODRIDGE1_ZONE));
//        itemMetadataMarijuana.setForages(Sets.newHashSet(marijuanaForageBuilder.createForage()));
//        itemStorage.saveItemMetaData(itemMetadataMarijuana);
//
//        // Drank
//        ItemMetadata itemMetadataDrank = metadataFrom(ItemType.PURPLE_DRANK);
//        itemMetadataDrank.setInternalItemName("purple drank");
//        Stats statsDrank = new StatsBuilder().setCurrentHealth(50).createStats();
//        itemMetadataDrank.setItemApplyStats(statsDrank);
//        itemStorage.saveItemMetaData(itemMetadataDrank);
//
//        // Smell Health Potion
//        Set<SpawnRule> spawnRulesHealthPotion = Sets.newHashSet(
//                new SpawnRuleBuilder().setArea(Area.NEWBIE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(100).setMaxPerRoom(5).setRandomPercent(40).createSpawnRule(),
//                new SpawnRuleBuilder().setArea(Area.FANCYHOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule(),
//                new SpawnRuleBuilder().setArea(Area.HOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule()
//        );
//        ItemMetadata itemMetadataHealthPotion = metadataFrom(ItemType.SMALL_HEALTH_POTION);
//        itemMetadataHealthPotion.setInternalItemName("small health potion");
//        Stats statsHealthPotion = new StatsBuilder().setCurrentHealth(20).createStats();
//        itemMetadataHealthPotion.setSpawnRules(spawnRulesHealthPotion);
//        itemMetadataHealthPotion.setItemApplyStats(statsHealthPotion);
//        itemStorage.saveItemMetaData(itemMetadataHealthPotion);
//
//
//        // BIGGERS SKIN SATCHEL
//        ItemMetadata itemMetadataSpellbook = metadataFrom(ItemType.LIGHTNING_SPELLBOOKNG);
//        itemMetadataSpellbook.setInternalItemName("lightning spellbook");
//        itemStorage.saveItemMetaData(itemMetadataSpellbook);
//
//        // Stick OF Justice
//        ItemMetadata itemMetadataStickOfJustice = metadataFrom(ItemType.STICK_OF_JUSTICE);
//        itemMetadataStickOfJustice.setInternalItemName("stick of justice");
//        itemStorage.saveItemMetaData(itemMetadataStickOfJustice);
//
//        // RED CLAW BEANIE
//        ItemMetadata itemMetadataBeanie = metadataFrom(ItemType.RED_CLAW_BEANIE);
//        Stats statsBeanie = new StatsBuilder().setArmorRating(8).setStrength(4).setMaxHealth(50).createStats();
//        final Equipment equipmentBeanie = new Equipment(EquipmentSlotType.HEAD, statsBeanie);
//        itemMetadataBeanie.setEquipment(equipmentBeanie);
//        itemMetadataBeanie.setInternalItemName("red claw beanie");
//        itemStorage.saveItemMetaData(itemMetadataBeanie);
//
//
//        // RED CLAW Hoodie
//        ItemMetadata itemMetadataHoodie = metadataFrom(ItemType.RED_CLAW_HOODIE);
//        Stats statsHoodie = new StatsBuilder().setArmorRating(15).setStrength(7).createStats();
//        final Equipment equipmentHoodie = new Equipment(EquipmentSlotType.CHEST, statsHoodie);
//        itemMetadataHoodie.setEquipment(equipmentHoodie);
//        itemMetadataHoodie.setInternalItemName("rad claw hoodie");
//        itemStorage.saveItemMetaData(itemMetadataHoodie);
//
//
//        // RED CLAW PANTS
//        ItemMetadata itemMetadataPants = metadataFrom(ItemType.RED_CLAW_PANTS);
//        Stats statsPants = new StatsBuilder().setArmorRating(15).setStrength(7).createStats();
//        final Equipment equipmentPants = new Equipment(EquipmentSlotType.LEGS, statsPants);
//        itemMetadataPants.setEquipment(equipmentPants);
//        itemMetadataPants.setInternalItemName("rad claw pants");
//        itemStorage.saveItemMetaData(itemMetadataPants);
//    }
//
//
//    private ItemMetadata metadataFrom(ItemType itemType) {
//        ItemMetadata itemMetadata = new ItemMetadata();
//        itemMetadata.setInternalItemName(null);
//        itemMetadata.setItemName(itemType.getItemName());
//        itemMetadata.setRestingName(itemType.getRestingName());
//        itemMetadata.setItemDescription(itemType.getItemDescription());
//        itemMetadata.setItemHalfLifeTicks(itemType.getItemHalfLifeTicks());
//        itemMetadata.setItemTriggers(itemType.getItemTriggers());
//        itemMetadata.setMaxUses(itemType.getMaxUses());
//        itemMetadata.setRarity(itemType.getRarity());
//        itemMetadata.setSpawnRules(null);
//        itemMetadata.setValidTimeOfDays(itemType.getValidTimeOfDays());
//        itemMetadata.setValueInGold(itemType.getValueInGold());
//        itemMetadata.setItemApplyStats(null);
//        itemMetadata.setEquipment(null);
//        itemMetadata.setEffects(null);
//        itemMetadata.setDisposable(itemType.isDisposable());
//        return itemMetadata;
//    }
//
//
//}