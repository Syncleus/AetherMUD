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
                case MITHRIL_SWORD:
                    return getMithrilSword(item);
                case MITHRIL_CHESTPLATE:
                    return getMithrilChestplate(item);
                case MITHRIL_HELMET:
                    return getMithrilHelmet(item);
                case MITHRIL_BRACERS:
                    return getMithrilBracers(item);
                case MITHRIL_LEGGINGS:
                    return getMithrilLeggings(item);
                case MITHRIL_BOOTS:
                    return getMithrilBoots(item);
                case PYAMITE_SWORD:
                    return getPyamiteSword(item);
                case PYAMITE_CHESTPLATE:
                    return getPyamiteChestplate(item);
                case PYAMITE_HELMET:
                    return getPyamiteHelmet(item);
                case PYAMITE_BRACERS:
                    return getPyamiteBracers(item);
                case PYAMITE_LEGGINGS:
                    return getPyamiteLeggings(item);
                case PYAMITE_BOOTS:
                    return getPyamiteBoots(item);
                case TAPPERHET_SWORD:
                    return getTapperhetSword(item);
                case VULCERIUM_SWORD:
                    return getVulceriumSword(item);
                case VULCERIUM_CHESTPLATE:
                    return getVulceriumChestplate(item);
                case VULCERIUM_HELMET:
                    return getVulceriumHelmet(item);
                case VULCERIUM_BRACERS:
                    return getVulceriumBracers(item);
                case VULCERIUM_LEGGINGS:
                    return getVulceriumLeggings(item);
                case VULCERIUM_BOOTS:
                    return getVulceriumBoots(item);
                case DWARF_BOOTS_OF_AGILITY:
                    return getDwarfBootsOfAgility(item);
                case LEATHER_SATCHEL:
                    return getLeatherSatchel(item);
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

    public static Item getMithrilSword(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(10).setStrength(30).setWeaponRatingMax(10).setWeaponRatingMin(10).setNumberOfWeaponRolls(2).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getMithrilChestplate(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(18).setStrength(10).setAgile(7).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }


    public static Item getMithrilHelmet(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(9).setStrength(9).setAgile(7).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getMithrilBracers(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(6).setStrength(5).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getMithrilLeggings(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(12).setStrength(8).setAgile(6).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getMithrilBoots(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(12).setStrength(6).setAgile(2).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPyamiteSword(Item item){
        Stats stats = new StatsBuilder().setArmorRating(19).setStrength(44).setWeaponRatingMax(28).setWeaponRatingMin(17).setNumberOfWeaponRolls(2).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPyamiteChestplate(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(36).setStrength(20).setAgile(14).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }


    public static Item getPyamiteHelmet(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(18).setStrength(18).setAgile(14).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPyamiteBracers(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(12).setStrength(10).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPyamiteLeggings(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(24).setStrength(16).setAgile(12).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getPyamiteBoots(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(24).setStrength(12).setAgile(24).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getTapperhetSword(Item item){
        Stats stats = new StatsBuilder().setAgile(40).setArmorRating(27).setStrength(64).setWeaponRatingMax(37).setWeaponRatingMin(28).setNumberOfWeaponRolls(2).setForaging(20000).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getVulceriumSword(Item item){
        Stats stats = new StatsBuilder().setArmorRating(60).setStrength(120).setWeaponRatingMax(60).setWeaponRatingMin(50).setNumberOfWeaponRolls(3).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HAND, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getVulceriumChestplate(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(100).setStrength(60).setAgile(45).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.CHEST, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getVulceriumHelmet(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(55).setStrength(55).setAgile(39).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.HEAD, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getVulceriumBracers(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(38).setStrength(30).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.WRISTS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getVulceriumLeggings(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(72).setStrength(48).setAgile(36).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.LEGS, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getVulceriumBoots(Item item) {
        Stats stats = new StatsBuilder().setArmorRating(74).setStrength(37).setAgile(60).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getDwarfBootsOfAgility(Item item) {
        Stats stats = new StatsBuilder().setAgile(400).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.FEET, stats);
        item.setEquipment(equipment);
        return item;
    }

    public static Item getLeatherSatchel(Item item) {
        Stats stats = new StatsBuilder().setInventorySize(10).createStats();
        final Equipment equipment = new Equipment(EquipmentSlotType.BAG, stats);
        item.setEquipment(equipment);
        return item;
    }

}
