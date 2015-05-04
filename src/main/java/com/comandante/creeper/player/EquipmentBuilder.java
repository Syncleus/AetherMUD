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
                case IRON_BOOTS:
                    return getIronBoots(item);
                case IRON_CHEST_PLATE:
                    return getIronChestPlate(item);
                case IRON_LEGGINGS:
                    return getIronLeggings(item);
                case PHANTOM_SWORD:
                    return getPhantomSword(item);
                case IRON_BRACERS:
                    return getIronBracers(item);
                case IRON_HELMET:
                    return getIronHelmet(item);
                case PHANTOM_HELMET:
                    return getPhantomHelmet(item);
                case PHANTOM_CHESTPLATE:
                    return getPhantomChestplate(item);
                case PHANTOM_BOOTS:
                    return getPhantomBoots(item);
                case PHANTOM_LEGGINGS:
                    return getPhantomLeggings(item);
                case PHANTOM_BRACERS:
                    return getPhantomBracers(item);
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

    public static Item getPhantomSword(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(5).setStrength(15).setWeaponRatingMax(5).setWeaponRatingMin(5).setNumberOfWeaponRolls(1).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPhantomHelmet(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(5).setStrength(5).setAgile(5).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPhantomChestplate(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(10).setStrength(5).setAgile(4).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }


    public static Item getPhantomBoots(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(6).setStrength(3).setAgile(1).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPhantomBracers(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(3).setStrength(2).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPhantomLeggings(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(6).setStrength(4).setAgile(3).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }
}
