package com.comandante.creeper.Items;

import com.comandante.creeper.player.EquipmentBuilder;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.TimeTracker;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.comandante.creeper.server.Color.*;

public enum ItemType {

    UNKNOWN(0, Arrays.asList(""), "", "", "", false, 0, 0, false, Rarity.RARE, 0, Sets.<TimeTracker.TimeOfDay>newHashSet()),
    KEY(1, Arrays.asList("key", "gold key", "shiny gold key"),
            "a shiny " + YELLOW + "gold key" + RESET,
            "a shiny " + YELLOW + "gold key" + RESET + " catches your eye.",
            "A basic key with nothing really remarkable other than its made of gold.",
            false,
            0,
            60,
            false,
            Rarity.BASIC,
            10, Sets.<TimeTracker.TimeOfDay>newHashSet(TimeTracker.TimeOfDay.NIGHT)),

    SMALL_HEALTH_POTION(2, Arrays.asList("potion", "health potion", "vial", "small vial of health potion", "p"),
            "a small vial of " + RED + "health potion" + RESET,
            "a small vial of " + RED + "health potion" + RESET + " rests on the ground.",
            "a small vial of " + RED + "health potion" + RESET + " that restores 50 health" + RESET,
            true,
            0,
            60,
            false,
            Rarity.BASIC,
            1, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BROAD_SWORD(3, Arrays.asList("sword", "broad", "a broad sword", "the broad sword"),
            Color.CYAN + "the broad sword" + Color.RESET,
            "an iron broad sword rests upon the ground.",
            "an iron broad sword",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            100, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    IRON_BOOTS(4, Arrays.asList("boots", "boot", "iron boots"),
            Color.CYAN + "iron boots" + Color.RESET,
            "a pair of iron boots are here on the ground.",
            "a pair of iron boots",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            50, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    IRON_CHEST_PLATE(5, Arrays.asList("chest", "iron chest plate", "plate"),
            "iron chest plate",
            "an iron ches tplate is on the ground.",
            "an iron chest plate",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            70, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    IRON_LEGGINGS(6, Arrays.asList("leggings", "iron leggings"),
            "iron leggings",
            "an a pair of iron leggings are here on the ground.",
            "an iron pair of leggings",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            80, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    IRON_BRACERS(7, Arrays.asList("bracers", "iron bracers"),
            "iron bracers",
            "an a pair of iron bracers are here on the ground.",
            "an iron pair of bracers",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            40, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    IRON_HELMET(8, Arrays.asList("helmet", "iron helmet"),
            "iron helmet",
            "an iron helmet is on the ground.",
            "an iron helmet",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            80, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    MARIJUANA(9, Arrays.asList("marijuana", "weed", "m", "w", "f", "flowers"),
            Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET,
            "some " + Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET + " are here on the ground.",
            "some " + Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET,
            true,
            0,
            60,
            false,
            Rarity.BASIC,
            80, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    PURPLE_DRANK(10, Arrays.asList("drank", "purple drank", "p", "purple", "lean", "sizzurp"),
            "a double cup of " + MAGENTA + "purple" + RESET + " drank",
            "a double cup of " + MAGENTA + "purple" + RESET + " drank rests on the ground.",
            "a tonic called " + MAGENTA + "purple" + RESET + " drank that restores 500 health" + RESET,
            true,
            0,
            60,
            false,
            Rarity.BASIC,
            8, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    LEATHER_SATCHEL(11, Arrays.asList("leather satchel", "satchel"),
            "a " + Color.GREEN + "leather satchel" + Color.RESET,
            "a " + Color.GREEN + "leather satchel" + Color.RESET,
            "a " + Color.GREEN + "leather satchel" + Color.RESET + " with room to store an extra `15 items.",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            800, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BIGGERS_SKIN_SATCHEL(12, Arrays.asList("biggers skin satchel", "skin satchel"),
            "a " + Color.GREEN + "biggers skin satchel" + Color.RESET,
            "a " + Color.GREEN + "biggers skin satchel" + Color.RESET,
            "a " + Color.GREEN + "biggers skin satchel" + Color.RESET + " with room to store 100 items.",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            3000, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    LIGHTNING_SPELLBOOKNG(13, Arrays.asList("lightning book", "lightning spell book", "book", "spell book"),
            "a " + Color.YELLOW + "lightning" + Color.RESET + " spell book." + Color.RESET,
            "a " + Color.YELLOW + "lightning" + Color.RESET + " spell book." + Color.RESET,
            "a " + Color.YELLOW + "lightning" + Color.RESET + " spell book." + Color.RESET,
            true,
            0,
            60,
            false,
            Rarity.RARE,
            3000, Sets.<TimeTracker.TimeOfDay>newHashSet()),;

            
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
    private final Set<TimeTracker.TimeOfDay> validTimeOfDays;

    ItemType(Integer itemTypeCode, List<String> itemTriggers, String itemName, String restingName, String itemDescription, boolean isDisposable, int maxUses, int itemHalfLifeTicks, boolean isEquipment, Rarity rarity, int valueInGold, Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        this.itemTypeCode = itemTypeCode;
        this.itemTriggers = itemTriggers;
        this.itemName = itemName;
        this.restingName = restingName;
        this.itemDescription = itemDescription;
        this.maxUses = maxUses;
        this.isDisposable = maxUses > 0 || isDisposable;
        this.itemHalfLifeTicks = itemHalfLifeTicks;
        this.isEquipment = isEquipment;
        this.rarity = rarity;
        this.valueInGold = valueInGold;
        this.validTimeOfDays = validTimeOfDays;
    }

    public Item create() {
        Item newItem = new Item(getItemName(), getItemDescription(), getItemTriggers(), getRestingName(), UUID.randomUUID().toString(), getItemTypeCode(), 0, false, itemHalfLifeTicks, getRarity(), getValueInGold());
        if (isEquipment) {
            return EquipmentBuilder.build(newItem);
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

    public Set<TimeTracker.TimeOfDay> getValidTimeOfDays() {
        return validTimeOfDays;
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
