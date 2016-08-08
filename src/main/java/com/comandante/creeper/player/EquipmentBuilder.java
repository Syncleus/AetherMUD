package com.comandante.creeper.player;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.spells.EffectBuilder;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Sets;

import java.util.List;
import java.util.Set;

public class EquipmentBuilder {

    public static Item build(Item item) {
        ItemType itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
        if (itemType != null) {
            switch (itemType) {
                case BROAD_SWORD:
                    return getBroadSword(item);
                case IRON_BOOTS:
                    return getIronBoots(item);
                case IRON_CHEST_PLATE:
                    return getIronChestPlate(item);
                case IRON_LEGGINGS:
                    return getIronLeggings(item);
                case IRON_BRACERS:
                    return getIronBracers(item);
                case IRON_HELMET:
                    return getIronHelmet(item);
                case LEATHER_SATCHEL:
                    return getLeatherSatchel(item);
                case BIGGERS_SKIN_SATCHEL:
                    return getBiggersSkinSatchel(item);
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

    public static Item getIronBoots(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(3).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getIronChestPlate(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(7).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getIronLeggings(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(5).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getIronBracers(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(2).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getIronHelmet(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(4).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getLeatherSatchel(Item item) {
        Stats stats = new StatsBuilder().setInventorySize(15).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getBiggersSkinSatchel(Item item) {
        Stats stats = new StatsBuilder().setInventorySize(100).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);
        item.setEquipment(equipment);
        return item;
    }
}