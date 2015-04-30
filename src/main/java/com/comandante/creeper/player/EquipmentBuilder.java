package com.comandante.creeper.player;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;

public class EquipmentBuilder {

    public static Item Build(Item item) {
        ItemType itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
        if (itemType != null) {
            switch (itemType) {
                case BROAD_SWORD:
                    return getBroadSword(item);
            }
        }
        return null;
    }

    public static Item getBroadSword(Item item) {
        Stats stats = new StatsBuilder().setStrength(10).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }
}
