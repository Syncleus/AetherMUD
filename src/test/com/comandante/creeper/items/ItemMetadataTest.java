package com.comandante.creeper.items;

import com.comandante.creeper.server.player_communication.Color;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.spawner.SpawnRuleBuilder;
import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.stats.StatsBuilder;
import com.comandante.creeper.storage.ItemStorage;
import com.comandante.creeper.world.model.Area;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;


public class ItemMetadataTest {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testSerialization() throws Exception {

//        private String itemName;
//        private String itemDescription;
//        private String restingName;
//        private int numberOfUses;
//        private int valueInGold;
//        private int itemHalfLifeTicks;
//        private Rarity rarity;
//        private Equipment equipment;
//        private Set<Effect> effects;
//        private List<String> itemTriggers;


        ItemMetadata itemMetadata = new ItemMetadata();
        itemMetadata.setInternalItemName("little potion");
        itemMetadata.setItemName("a " + Color.RED + "little potion" + Color.RESET);
        itemMetadata.setItemDescription("a " + Color.RED + "little potion" + Color.RESET);
        itemMetadata.setRestingName("a " + Color.GREEN + "little potion" + Color.RESET);
        itemMetadata.setMaxUses(1);
        itemMetadata.setDisposable(true);
        itemMetadata.setValueInGold(30);
        itemMetadata.setItemHalfLifeTicks(60);
        itemMetadata.setRarity(Rarity.BASIC);
       // itemMetadata.setValidTimeOfDays(Sets.newHashSet(TimeTracker.TimeOfDay.MORNING, TimeTracker.TimeOfDay.NIGHT));

        SpawnRule spawnRule1 = new SpawnRuleBuilder().setArea(Area.NEWBIE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(100).setMaxPerRoom(5).setRandomPercent(40).createSpawnRule();
        SpawnRule spawnRule2 = new SpawnRuleBuilder().setArea(Area.FANCYHOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule();
        SpawnRule spawnRule3 = new SpawnRuleBuilder().setArea(Area.HOUSE_ZONE).setSpawnIntervalTicks(600).setMaxInstances(12).setMaxPerRoom(2).setRandomPercent(50).createSpawnRule();
        itemMetadata.setSpawnRules(Sets.newHashSet(spawnRule1, spawnRule2, spawnRule3));

        Stats stats = new StatsBuilder().setInventorySize(100).createStats();
      //  final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);

     //   itemMetadata.setEquipment(equipment);
        itemMetadata.setItemTriggers(Lists.newArrayList("p", "little potion", "potion"));


        EffectBuilder effectBuilder  = new EffectBuilder()
                .setDurationStats(new StatsBuilder().setCurrentHealth(25).createStats())
                .setEffectApplyMessages(Lists.newArrayList("You start to feel a healing effect."))
                .setEffectDescription("Little healing.")
                .setEffectName("Little Potion Heal")
                .setFrozenMovement(false)
                .setLifeSpanTicks(0);


        itemMetadata.setEffects(Sets.newHashSet(effectBuilder.createEffect()));


        ItemStorage itemStorage = new ItemStorage(gson);
        itemStorage.saveItemMetaData(itemMetadata);


        String s = gson.toJson(itemMetadata);
        System.out.println(s);

    }



}