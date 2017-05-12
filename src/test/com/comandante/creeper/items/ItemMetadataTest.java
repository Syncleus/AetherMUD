package com.comandante.creeper.items;

import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.stats.StatsBuilder;
import com.comandante.creeper.storage.NpcMetadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.junit.Assert.*;


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


        ItemType biggersSkinSatchel = ItemType.BIGGERS_SKIN_SATCHEL;

        ItemMetadata itemMetadata = new ItemMetadata();
        itemMetadata.setBasicItemName("biggers skin satchel ");
        itemMetadata.setItemName(biggersSkinSatchel.getItemName());
        itemMetadata.setItemDescription(biggersSkinSatchel.getItemDescription());
        itemMetadata.setRestingName(biggersSkinSatchel.getRestingName());
        itemMetadata.setNumberOfUses(biggersSkinSatchel.getMaxUses());
        itemMetadata.setValueInGold(biggersSkinSatchel.getValueInGold());
        itemMetadata.setItemHalfLifeTicks(biggersSkinSatchel.getItemHalfLifeTicks());
        itemMetadata.setRarity(biggersSkinSatchel.getRarity());

        Stats stats = new StatsBuilder().setInventorySize(100).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);

        itemMetadata.setEquipment(equipment);
        itemMetadata.setItemTriggers(itemMetadata.getItemTriggers());

        itemMetadata.setItemTriggers(biggersSkinSatchel.getItemTriggers());


        String s = gson.toJson(itemMetadata);
        System.out.println(s);

    }



}