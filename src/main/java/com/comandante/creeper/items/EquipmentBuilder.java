package com.comandante.creeper.items;


import com.comandante.creeper.stats.Stats;
import com.comandante.creeper.stats.StatsBuilder;

public class EquipmentBuilder {

    public static Item getBerserkerBaton(Item item) {
        Stats stats = new StatsBuilder().setWeaponRatingMin(4).setWeaponRatingMax(6).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getBerserkerBoots(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(3).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getBerserkerChest(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(6).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getBerserkerShorts(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(4).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getBerserkerBracers(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(4).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getBerserkerHelm(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(3).createStats();
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

    public static Item getRedClawBeanie(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(8).setStrength(4).setMaxHealth(50).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getRedClawHoodie(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(15).setStrength(7).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getRedClawPants(Item item) {
        Stats stats = new StatsBuilder().setAgile(7).setForaging(6).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }
}