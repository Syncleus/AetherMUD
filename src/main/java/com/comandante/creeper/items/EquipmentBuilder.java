package com.comandante.creeper.items;


public class EquipmentBuilder {

//    public static Item build(Item item) {
//        ItemType itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
//        if (itemType != null) {
//            switch (itemType) {
//                case BERSERKER_BATON:
//                    return getBerserkerBaton(item);
//                case BERSEKER_BOOTS:
//                    return getBerserkerBoots(item);
//                case BERSERKER_CHEST:
//                    return getBerserkerChest(item);
//                case BERSEKER_SHORTS:
//                    return getBerserkerShorts(item);
//                case BERSERKER_BRACERS:
//                    return getBerserkerBracers(item);
//                case BERSEKER_HELM:
//                    return getBerserkerHelm(item);
//                case LEATHER_SATCHEL:
//                    return getLeatherSatchel(item);
//                case BIGGERS_SKIN_SATCHEL:
//                    return getBiggersSkinSatchel(item);
//                case RED_CLAW_BEANIE:
//                    return getRedClawBeanie(item);
//                case RED_CLAW_HOODIE:
//                    return getRedClawHoodie(item);
//                case RED_CLAW_PANTS:
//                    return getRedClawPants(item);
//            }
//        }
//        return null;
//    }
//
//    public static Item getBerserkerBaton(Item item) {
//        Stats stats = new StatsBuilder().setWeaponRatingMin(4).setWeaponRatingMax(6).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getBerserkerBoots(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(3).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getBerserkerChest(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(6).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getBerserkerShorts(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(4).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getBerserkerBracers(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(4).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getBerserkerHelm(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(3).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getLeatherSatchel(Item item) {
//        Stats stats = new StatsBuilder().setInventorySize(15).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getBiggersSkinSatchel(Item item) {
//        Stats stats = new StatsBuilder().setInventorySize(100).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getRedClawBeanie(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(8).setStrength(4).setMaxHealth(50).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getRedClawHoodie(Item item) {
//        Stats stats = new StatsBuilder().setArmorRating(15).setStrength(7).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
//
//    public static Item getRedClawPants(Item item) {
//        Stats stats = new StatsBuilder().setAgile(7).setForaging(6).createStats();
//        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
//        item.setEquipment(equipment);
//        return item;
//    }
}