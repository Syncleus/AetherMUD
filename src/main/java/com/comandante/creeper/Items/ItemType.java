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

    WOMB_SHIFTER(4, Arrays.asList("womb", "womb shifter", "the womb shifter"),
            Color.YELLOW + "the womb shifter" + Color.RESET,
            "A sword big enough to shift wombs.",
            "A bloody womb shifter lays upon the ground.",
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
