package com.comandante.creeper.Items;

import com.comandante.creeper.player.EquipmentBuilder;
import com.comandante.creeper.server.Color;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.comandante.creeper.server.Color.*;

public enum ItemType {

    UNKNOWN(0, Arrays.asList(""), "", "", "", false, 0, 0, false, Rarity.RARE, 0),
    KEY(1, Arrays.asList("key", "gold key", "shiny gold key"),
            YELLOW + "a shiny gold key" + RESET,
            YELLOW + "a shiny gold key" + RESET + " catches your eye.",
            "A basic key with nothing really remarkable other than its made of gold.",
            false,
            0,
            60,
            false,
            Rarity.BASIC,
            10),

    BEER(2, Arrays.asList("beer", "can of beer", "b"),
            "a dented can of " + CYAN + "beer" + RESET,
            "a " + CYAN + "beer" + RESET + " lies on the ground, unopened",
            "an ice cold " + CYAN + "beer" + RESET + " that restores 50 health" + RESET,
            true,
            2,
            60,
            false,
            Rarity.BASIC,
            1),

    BOOK(3, Arrays.asList("book", "used book"),
            MAGENTA + "a leather book" + RESET,
            MAGENTA + "a well used book" + RESET + " with what looks like a leather back rests here.",
            "A book written in a foreign language. Doesn't matter as you can't read.",
            false,
            0,
            60,
            false,
            Rarity.BASIC,
            1),

    BROAD_SWORD(4, Arrays.asList("sword", "broad", "a broad sword", "the broad sword"),
            Color.CYAN + "the broad sword" + Color.RESET,
            "an iron broad sword rests upon the ground.",
            "an iron broad sword",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            100),

    IRON_BOOTS(5, Arrays.asList("boots", "boot", "iron boots"),
    Color.CYAN + "iron boots" + Color.RESET,
            "a pair of iron boots are here on the ground.",
            "a pair of iron boots",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            50),

    IRON_CHEST_PLATE(6, Arrays.asList("chest", "iron chest plate", "plate"),
            "iron chest plate",
            "an iron ches tplate is on the ground.",
            "an iron chest plate",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            70),

    IRON_LEGGINGS(7, Arrays.asList("leggings", "iron leggings"),
            "iron leggings",
            "an a pair of iron leggings are here on the ground.",
            "an iron pair of leggings",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            80),

    PHANTOM_SWORD(8, Arrays.asList("phantom", "phantom sword", "the phantom sword"),
            Color.YELLOW + "the " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            500),

    IRON_BRACERS(9, Arrays.asList("bracers", "iron bracers"),
            "iron bracers",
            "an a pair of iron bracers are here on the ground.",
            "an iron pair of bracers",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            40),

    PHANTOM_HELMET(10, Arrays.asList("helmet", "phantom helmet", "the phantom helmet"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            250),

    PHANTOM_CHESTPLATE(11, Arrays.asList("chestplate", "chest", "phantom chest plate", "the phantom chest plate"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " chest plate" + Color.RESET,
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " chest plate" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " chest plate" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            350),

    PHANTOM_BOOTS(12, Arrays.asList("boots", "phantom boots", "the phantom boots"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            280),

    PHANTOM_BRACERS(13, Arrays.asList("boots", "phantom bracers", "the phantom bracers"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            250),

    PHANTOM_LEGGINGS(14, Arrays.asList("leggings", "phantom leggings", "the phantom leggings"),
            Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.CYAN + "phantom" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            290),

    IRON_HELMET(15, Arrays.asList("helmet", "iron helmet"),
            "iron helmet",
            "an iron helmet is on the ground.",
            "an iron helmet",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            80),

    MITHRIL_SWORD(16, Arrays.asList("sword", "mithril sword", "mithril sword"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            500),

    MITHRIL_CHESTPLATE(17, Arrays.asList("chestplate", "a mithril chestplate", "mithril chestplate"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET + " is on the ground.",
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            400),

    MITHRIL_HELMET(18, Arrays.asList("helmet", "a mithril helmet", "mithril helmet"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            280),

    MITHRIL_BRACERS(19, Arrays.asList("helmet", "mithril bracers", "mithril bracers"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            300),

    MITHRIL_LEGGINGS(20, Arrays.asList("helmet", "mithril leggings", "mithril leggings"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            350),

    MITHRIL_BOOTS(21, Arrays.asList("helmet", "mithril boots"),
            Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.MAGENTA + "mithril" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            190),

    PYAMITE_SWORD(22, Arrays.asList("sword", "pyamite sword", "pyamite sword"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            3000),

    PYAMITE_CHESTPLATE(23, Arrays.asList("chestplate", "a pyamite chestplate", "pyamite chestplate"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET + " is on the ground.",
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            2700),

    PYAMITE_HELMET(24, Arrays.asList("helmet", "a pyamite helmet", "pyamite helmet"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            2000),

    PYAMITE_BRACERS(25, Arrays.asList("bracers", "pyamite bracers", "pyamite bracers"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            2100),

    PYAMITE_LEGGINGS(26, Arrays.asList("leggings", "pyamite leggings", "pyamite leggings"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            2900),

    PYAMITE_BOOTS(27, Arrays.asList("helmet", "pyamite boots"),
            Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.GREEN + "pyamite" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            2000),

    MARIJUANA(28, Arrays.asList("marijuana", "weed", "m", "w", "f", "flowers"),
            Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET,
            "some " + Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET + " are here on the ground.",
            "some " + Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET,
            true,
            0,
            60,
            false,
            Rarity.BASIC,
            80),

    TAPPERHET_SWORD(29, Arrays.asList("sword", "tapperhet sword"),
            Color.BOLD_ON + Color.GREEN + "tapperhet" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.BOLD_ON + Color.GREEN + "tapperhet" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.BOLD_ON + Color.GREEN + "tapperhet" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            7000),

    VULCERIUM_SWORD(30, Arrays.asList("sword", "vulcerium sword", "vulcerium sword"),
            Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            "a " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " sword" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            10000),

    VULCERIUM_CHESTPLATE(31, Arrays.asList("chestplate", "a vulcerium chestplate", "vulcerium chestplate"),
            Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            "a " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET + " is on the ground.",
            "a " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " chestplate" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            9000),

    VULCERIUM_HELMET(32, Arrays.asList("helmet", "a vulcerium helmet", "vulcerium helmet"),
            Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            "a " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET + " is on the ground.",
            "a " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " helmet" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            6000),

    VULCERIUM_BRACERS(33, Arrays.asList("bracers", "vulcerium bracers", "vulcerium bracers"),
            Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            "a pair of " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET + " are on the ground.",
            "a pair of " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " bracers" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            5900),

    VULCERIUM_LEGGINGS(34, Arrays.asList("leggings", "vulcerium leggings", "vulcerium leggings"),
            Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            "a pair of " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET + " are on the ground.",
            "a pair of " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " leggings" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            7500),

    VULCERIUM_BOOTS(35, Arrays.asList("boots", "vulcerium boots"),
            Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            "a pair of " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.RED + "vulcerium" + Color.RESET + Color.YELLOW + " boots" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            7100),

    DWARF_BOOTS_OF_AGILITY(36, Arrays.asList("dwarf boots", "boots"),
            Color.BLUE + "dwarf" + Color.RESET + Color.RED + " boots" + Color.RESET,
            "a pair of " + Color.BLUE + "dwarf" + Color.RESET + Color.RED + " boots" + Color.RESET + " are on the ground.",
            "a pair of " + Color.BLUE + "dwarf" + Color.RESET + Color.RED + " boots" + Color.RESET,
            false,
            0,
            60,
            true,
            Rarity.UNCOMMON,
            2500),

    DEATHCRAWLER_SCALES(37, Arrays.asList("deathcrawler scales", "scales"),
            Color.BOLD_ON + Color.MAGENTA + "deathcrawler" + Color.RESET + Color.RED + " scales" + Color.RESET,
            "some " + Color.BOLD_ON + Color.MAGENTA + "deathcrawler" + Color.RESET + Color.RED + " scales" + Color.RESET + " are on the ground.",
            "some " + Color.BOLD_ON + Color.MAGENTA + "deathcrawler" + Color.RESET + Color.RED + " scales" + Color.RESET,
            false,
            0,
            60,
            false,
            Rarity.BASIC,
            700),


    DWARVEN_PENDANT(38, Arrays.asList("dwarven pendant", "pendant"),
            Color.BOLD_ON + Color.MAGENTA + "dwarven" + Color.RESET + Color.RED + " pendant" + Color.RESET,
            "some " + Color.BOLD_ON + Color.MAGENTA + "dwarven" + Color.RESET + Color.RED + " pendant" + Color.RESET + " are on the ground.",
            "some " + Color.BOLD_ON + Color.MAGENTA + "dwarven" + Color.RESET + Color.RED + " pendant" + Color.RESET,
            false,
            0,
            60,
            false,
            Rarity.BASIC,
            450),

    BYSEN_BALLS(38, Arrays.asList("bysen balls", "balls"),
            Color.BOLD_ON + Color.MAGENTA + "bysen" + Color.RESET + Color.RED + " balls" + Color.RESET,
            "some " + Color.BOLD_ON + Color.MAGENTA + "bysen" + Color.RESET + Color.RED + " balls" + Color.RESET + " are on the ground.",
            "some " + Color.BOLD_ON + Color.MAGENTA + "bysen" + Color.RESET + Color.RED + " balls" + Color.RESET,
            false,
            0,
            60,
            false,
            Rarity.BASIC,
            2000);

    private final Integer itemTypeCode;
    private final List<String> itemTriggers;
    private final String restingName;
    private final String itemName;
    private final String itemDescription;
    private final boolean isDisposable;
    private final int maxUses;
    private final int itemHalfLifeTicks;
    private final boolean isEquipment;
    private final Rarity rarity;
    private final int valueInGold;

    ItemType(Integer itemTypeCode, List<String> itemTriggers, String itemName, String restingName, String itemDescription, boolean isDisposable, int maxUses, int itemHalfLifeTicks, boolean isEquipment, Rarity rarity, int valueInGold) {
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
        this.rarity = rarity;
        this.valueInGold = valueInGold;
    }

    public Item create() {
        Item newItem = new Item(getItemName(), getItemDescription(), getItemTriggers(), getRestingName(), UUID.randomUUID().toString(), getItemTypeCode(), 0, false, itemHalfLifeTicks, getRarity(), getValueInGold());
        if (isEquipment) {
            return EquipmentBuilder.Build(newItem);
        }
        return newItem;
    }

    public Rarity getRarity() {
        return rarity;
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

    public int getValueInGold() {
        return valueInGold;
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
