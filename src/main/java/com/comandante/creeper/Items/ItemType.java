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
    Color.CYAN + "iron chest plate" + Color.RESET,
            "an iron chest place is on the ground.",
            "an iron chest place",
            false,
            0,
            60,
            true),

    IRON_LEGGINGS(7, Arrays.asList("leggings", "iron leggings", "legs"),
    Color.CYAN + "iron leggings" + Color.RESET,
            "an a pair of iron leggings are here on the ground",
            "an iron pair of leggings",
            false,
            0,
            60,
            true),

    BALLERS_SWORD(8, Arrays.asList("ballers", "ballers sword", "sword"),
            Color.CYAN + "b" + Color.GREEN + "a" + Color.RED + "l" + Color.BLUE + "l" + Color.YELLOW + "e" + Color.MAGENTA + "r" + Color.YELLOW + "s" + " sword" + Color.RESET,
            "a " + Color.CYAN + "b" + Color.GREEN + "a" + Color.RED + "l" + Color.BLUE + "l" + Color.YELLOW + "e" + Color.MAGENTA + "r" + Color.YELLOW + "s" + " sword" + Color.RESET + " is on the ground.",
            "a " + Color.CYAN + "b" + Color.GREEN + "a" + Color.RED + "l" + Color.BLUE + "l" + Color.YELLOW + "e" + Color.MAGENTA + "r" + Color.YELLOW + "s" + " sword" + Color.RESET,
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
