package com.comandante.creeper.items;

import com.comandante.creeper.core_game.service.TimeTracker;
import com.comandante.creeper.server.player_communication.Color;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.comandante.creeper.server.player_communication.Color.*;

public enum ItemType {

    UNKNOWN(0, Arrays.asList(""), "", "", "", false, 0, 0, false, com.comandante.creeper.items.Rarity.RARE, 0, Sets.<TimeTracker.TimeOfDay>newHashSet()),
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
            Rarity.OFTEN,
            1, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BERSERKER_BATON(3, Arrays.asList("baton", "a berserker baton", "b"),
            Color.CYAN + "a berserker baton" + Color.RESET,
            "a berserker baton rests upon the ground.",
            "a berserker baton",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            100, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BERSEKER_BOOTS(4, Arrays.asList("boots", "boot", "berserker boots", "b"),
            Color.CYAN + "berserker boots" + Color.RESET,
            "a pair of berserker boots are here on the ground.",
            "a pair of berserker boots",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            50, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BERSERKER_CHEST(5, Arrays.asList("chest", "berserker chest", "c"),
            Color.CYAN + "berserker chest" + Color.RESET,
            "a berserker chest is on the ground.",
            "a berserker chest",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            70, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BERSEKER_SHORTS(6, Arrays.asList("shorts", "berserker shorts", "s"),
            Color.CYAN + "berserker shorts" + Color.RESET,
            "a pair of berserker shorts are here on the ground.",
            "a pair of berserker shorts",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            80, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BERSERKER_BRACERS(7, Arrays.asList("bracers", "berserker bracers", "b"),
            Color.CYAN + "berserker bracers" + Color.RESET,
            "a pair of berserker bracers are here on the ground.",
            "a pair of berserker bracers",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            40, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    BERSEKER_HELM(8, Arrays.asList("helm", "berserker helm", "h"),
            Color.CYAN + "berserker helm" + Color.RESET,
            "a berserker helm is on the ground.",
            "a berserker helm",
            false,
            0,
            60,
            true,
            Rarity.BASIC,
            40, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    MARIJUANA(9, Arrays.asList("marijuana", "weed", "m", "w", "f", "flowers"),
            Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET,
            "some " + Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET + " are here on the ground.",
            "some " + Color.GREEN + "marijuana" + Color.RESET + " flowers" + Color.RESET,
            true,
            0,
            60,
            false,
            Rarity.BASIC,
            10, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    PURPLE_DRANK(10, Arrays.asList("drank", "purple drank", "p", "purple", "lean", "sizzurp"),
            "a double cup of " + MAGENTA + "purple" + RESET + " drank",
            "a double cup of " + MAGENTA + "purple" + RESET + " drank rests on the ground.",
            "a tonic called " + MAGENTA + "purple" + RESET + " drank that restores 500 health" + RESET,
            true,
            0,
            60,
            false,
            Rarity.BASIC,
            30, Sets.<TimeTracker.TimeOfDay>newHashSet()),

    LEATHER_SATCHEL(11, Arrays.asList("leather satchel", "satchel"),
            "a " + Color.GREEN + "leather satchel" + Color.RESET,
            "a " + Color.GREEN + "leather satchel" + Color.RESET,
            "a " + Color.GREEN + "leather satchel" + Color.RESET + " (15 items)",
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

    LIGHTNING_SPELLBOOKNG(
            // itemTypeCode
            13,
            // triggers
            Arrays.asList("lightning book", "lightning spell book", "book", "spell book"),
            // name
            "a " + Color.YELLOW + "lightning" + Color.RESET + " spell book." + Color.RESET,
            // resting name
            "a " + Color.YELLOW + "lightning" + Color.RESET + " spell book." + Color.RESET,
            // description
            "a " + Color.YELLOW + "lightning" + Color.RESET + " spell book." + Color.RESET,
            // disposable?
            true,
            // max uses
            0,
            // half life ticks
            60,
            // equipment/can you wear it?
            false,
            // controls how often it will spawn (if configured in a spawn)
            Rarity.RARE,
            // default sell value (gold)
            3000,
            // Some items have time of days where they are "valid" and times when they are not.  If they are not valid for MORNING (for example) they will turn to dust as soon as the world switches to morning.
            Sets.<TimeTracker.TimeOfDay>newHashSet()),
    STICK_OF_JUSTICE(
            // itemTypeCode
            14,
            // triggers
            Arrays.asList("stick of justice", "justice", "stick"),
            // name
            "a " + Color.BOLD_ON + Color.MAGENTA + "stick" + Color.BOLD_OFF + Color.GREEN + " of " + Color.BOLD_ON + Color.BLUE + "justice" + Color.RESET,
            // resting name
            "a " + Color.BOLD_ON + Color.MAGENTA + "stick" + Color.BOLD_OFF + Color.GREEN + " of " + Color.BOLD_ON + Color.BLUE + "justice" + Color.RESET,
            // description
            "a " + Color.BOLD_ON + Color.MAGENTA + "stick" + Color.BOLD_OFF + Color.GREEN + " of " + Color.BOLD_ON + Color.BLUE + "justice" + Color.RESET,
            // disposable?
            true,
            // max uses
            0,
            // half life ticks
            60,
            // equipment/can you wear it?
            false,
            // controls how often it will spawn (if configured in a spawn)
            Rarity.RARE,
            // default sell value (gold)
            3000,
            // Some items have time of days where they are "valid" and times when they are not.  If they are not valid for MORNING (for example) they will turn to dust as soon as the world switches to morning.
            Sets.<TimeTracker.TimeOfDay>newHashSet()),

    RED_CLAW_BEANIE(
            // itemTypeCode
            15,
            // triggers
            Arrays.asList("beanie", "red-claw beanie", "b"),
            // name
            Color.RED + "red-claw " + Color.RESET + "beanie" + Color.RESET,
            // resting name
            "a " + Color.RED + "red-claw " + Color.RESET + "beanie is on the ground.",
            // description
            "a red-claw beanie",
            // disposable?
            false,
            // max uses
            0,
            // half life ticks
            60,
            // equipment?
            true,
            Rarity.LEGENDARY,
            // default sell value (gold)
            3500,
            Sets.<TimeTracker.TimeOfDay>newHashSet()),

    RED_CLAW_HOODIE(
            // itemTypeCode
            16,
            // triggers
            Arrays.asList("hoodie", "red-claw hoodie", "h"),
            // name
            Color.RED + "red-claw " + Color.RESET + "hoodie" + Color.RESET,
            // resting name
            "a " + Color.RED + "red-claw " + Color.RESET + "hoodie is on the ground.",
            // description
            "a red-claw hoodie",
            // disposable?
            false,
            // max uses
            0,
            // half life ticks
            60,
            // equipment?
            true,
            Rarity.LEGENDARY,
            // default sell value (gold)
            3500,
            Sets.<TimeTracker.TimeOfDay>newHashSet()),

    RED_CLAW_PANTS(
            // itemTypeCode
            17,
            // triggers
            Arrays.asList("pants", "red-claw pants", "p"),
            // name
            Color.RED + "red-claw " + Color.RESET + "pants" + Color.RESET,
            // resting name
            "a " + Color.RED + "red-claw " + Color.RESET + "pants is on the ground.",
            // description
            "a red-claw pants",
            // disposable?
            false,
            // max uses
            0,
            // half life ticks
            60,
            // equipment?
            true,
            Rarity.LEGENDARY,
            // default sell value (gold)
            3500,
            Sets.<TimeTracker.TimeOfDay>newHashSet()),;

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
