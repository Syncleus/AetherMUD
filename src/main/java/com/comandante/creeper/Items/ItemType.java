package com.comandante.creeper.Items;

import com.comandante.creeper.player.EquipmentBuilder;
import com.comandante.creeper.server.Color;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.comandante.creeper.server.Color.*;

public enum ItemType {

    UNKNOWN(0, Arrays.asList(""), "", "", "", false, 0, 0, false),
    KEY(1, Arrays.asList("key", "gold key", "shiny gold key"),
            YELLOW + "a shiny gold key" + RESET,
            YELLOW + "a shiny gold key" + RESET + " catches your eye.",
            "A basic key with nothing really remarkable other than its made of gold.",
            false,
            0,
            60,
            false),

    BEER(2, Arrays.asList("beer", "can of beer", "b"),
            "a dented can of " + CYAN + "beer" + RESET,
            "a " + CYAN + "beer" + RESET + " lies on the ground, unopened",
            "an ice cold " + CYAN + "beer" + RESET + " that restores 50 health" + RESET,
            true,
            2,
            60,
            false),

    BOOK(3, Arrays.asList("book", "used book"),
            MAGENTA + "a leather book" + RESET,
            MAGENTA + "a well used book" + RESET + " with what looks like a leather back rests here.",
            "A book written in a foreign language. Doesn't matter as you can't read.",
            false,
            0,
            60,
            false),

    BROAD_SWORD(4, Arrays.asList("sword", "broad", "a broad sword", "the broad sword"),
            Color.CYAN + "the broad sword" + Color.RESET,
            "an iron broad sword rests upon the ground.",
            "an iron broad sword",
            false,
            0,
            60,
            true),

    IRON_BOOTS(5, Arrays.asList("boots", "boot", "iron boots"),
    Color.CYAN + "iron boots" + Color.RESET,
            "a pair of iron boots are here on the ground.",
            "a pair of iron boots",
            false,
            0,
            60,
            true),

    IRON_CHEST_PLATE(6, Arrays.asList("chest", "iron chest plate", "plate"),
            "iron chest plate",
            "an iron ches tplate is on the ground.",
            "an iron chest plate",
            false,
            0,
            60,
            true),

    IRON_LEGGINGS(7, Arrays.asList("leggings", "iron leggings"),
            "iron leggings",
            "an a pair of iron leggings are here on the ground.",
            "an iron pair of leggings",
            false,
            0,
            60,
            true),

    PHANTOM_SWORD(8, Arrays.asList("phantom", "phantom sword", "the phantom sword"),
            Color.YELLOW + "the " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true),

    IRON_BRACERS(9, Arrays.asList("bracers", "iron bracers"),
            "iron bracers",
            "an a pair of iron bracers are here on the ground.",
            "an iron pair of bracers",
            false,
            0,
            60,
            true),

    PHANTOM_HELMET(10, Arrays.asList("helmet", "phantom helmet", "the phantom helmet"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true),

    PHANTOM_CHESTPLATE(11, Arrays.asList("chestplate", "chest", "phantom chest plate", "the phantom chest plate"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " chest plate" + Color.RESET,
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " chest plate" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " chest plate" + Color.RESET,
            false,
            0,
            60,
            true),

    PHANTOM_BOOTS(12, Arrays.asList("boots", "phantom boots", "the phantom boots"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true),

    PHANTOM_BRACERS(13, Arrays.asList("boots", "phantom bracers", "the phantom bracers"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true),

    PHANTOM_LEGGINGS(14, Arrays.asList("leggings", "phantom leggings", "the phantom leggings"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true),

    IRON_HELMET(15, Arrays.asList("helmet", "iron helmet"),
            "iron helmet",
            "an iron helmet is on the ground.",
            "an iron helmet",
            false,
            0,
            60,
            true),

    MITHRIL_SWORD(16, Arrays.asList("sword", "mithril sword", "mithril sword"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true),

    MITHRIL_CHESTPLATE(17, Arrays.asList("chestplate", "a mithril chestplate", "mithril chestplate"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET + " is on the ground.",
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            false,
            0,
            60,
            true),

    MITHRIL_HELMET(18, Arrays.asList("helmet", "a mithril helmet", "mithril helmet"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true),

    MITHRIL_BRACERS(19, Arrays.asList("helmet", "mithril bracers", "mithril bracers"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true),

    MITHRIL_LEGGINGS(20, Arrays.asList("helmet", "mithril leggings", "mithril leggings"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true),

    MITHRIL_BOOTS(21, Arrays.asList("helmet", "mithril boots"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true),

    PYAMITE_SWORD(22, Arrays.asList("sword", "pyamite sword", "pyamite sword"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true),

    PYAMITE_CHESTPLATE(23, Arrays.asList("chestplate", "a pyamite chestplate", "pyamite chestplate"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET + " is on the ground.",
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            false,
            0,
            60,
            true),

    PYAMITE_HELMET(24, Arrays.asList("helmet", "a pyamite helmet", "pyamite helmet"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true),

    PYAMITE_BRACERS(25, Arrays.asList("helmet", "pyamite bracers", "pyamite bracers"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true),

    PYAMITE_LEGGINGS(26, Arrays.asList("helmet", "pyamite leggings", "pyamite leggings"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true),

    PYAMITE_BOOTS(27, Arrays.asList("helmet", "pyamite boots"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true);

    private final Integer itemTypeCode;
    private final List<String> itemTriggers;
    private final String restingName;
    private final String itemName;
    private final String itemDescription;
    private final boolean isDisposable;
    private final int maxUses;
    private final int itemHalfLifeTicks;
    private final boolean isEquipment;

    ItemType(Integer itemTypeCode, List<String> itemTriggers, String itemName, String restingName, String itemDescription, boolean isDisposable, int maxUses, int itemHalfLifeTicks, boolean isEquipment) {
        this.itemTypeCode = itemTypeCode;
        this.itemTriggers = itemTriggers;
        this.itemName = itemName;
        this.restingName = restingName;
        this.itemDescription = itemDescription;
        this.maxUses = maxUses;
        if (maxUses > 0) {
            this.isDisposable = true;
        } else {
            this.isDisposable = isDisposable;
        }
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        this.isEquipment = isEquipment;
    }

    public Item create() {
        Item newItem = new Item(getItemName(), getItemDescription(), getItemTriggers(), getRestingName(), UUID.randomUUID().toString(), getItemTypeCode(), 0, false, itemHalfLifeTicks);
        if (isEquipment) {
            return EquipmentBuilder.Build(newItem);
        }
        return newItem;
    }


    public String getRestingName() {
        return restingName;
    }

    public Integer getItemTypeCode() {
        return itemTypeCode;
    }

    public List<String> getItemTriggers() {
        return itemTriggers;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public boolean isDisposable() {
        return isDisposable;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public static ItemType itemTypeFromCode(Integer code) {
        ItemType[] values = values();
        for (ItemType type : values) {
            if (type.getItemTypeCode().equals(code)) {
                return type;
            }
        }
        return ItemType.UNKNOWN;
    }
}
