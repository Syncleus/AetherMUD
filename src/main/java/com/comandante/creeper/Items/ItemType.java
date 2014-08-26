package com.comandante.creeper.Items;

import java.util.UUID;

import static com.comandante.creeper.model.Color.*;

public enum ItemType {
    UNKNOWN(0, "", "", "", "", false, 0, 0),
    KEY(1, "key",
            YELLOW + "a shiny gold key" + RESET,
            YELLOW + "a shiny gold key" + RESET + " catches your eye.",
            "A basic key with nothing really remarkable other than its made of gold.",
            false,
            0,
            60),
    BEER(2, "beer",
            CYAN + "a dented can of beer" + RESET,
            CYAN + "a beer" + RESET + " lies on the ground, unopened",
            "This beer looks sketch but you'll probably drink it anyways.",
            false,
            0,
            60),
    BOOK(3, "beer",
            MAGENTA + "a leather book" + RESET,
            MAGENTA + "a well used book" + RESET + " with what looks like a leather back rests here.",
            "A book written in a foreign language. Doesn't matter as you can't read.",
            false,
            0,
            60);

    private final Integer itemTypeCode;
    private final String itemShortName;
    private final String restingName;
    private final String itemName;
    private final String itemDescription;
    private final boolean isDisposable;
    private final int maxUses;
    private final int itemHalfLifeTicks;

    ItemType(Integer itemTypeCode, String itemShortName, String itemName, String restingName, String itemDescription, boolean isDisposable, int maxUses, int itemHalfLifeTicks) {
        this.itemTypeCode = itemTypeCode;
        this.itemShortName = itemShortName;
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
    }

    public Item create() {
        return new Item(getItemName(), getItemDescription(), getItemShortName(), getRestingName(), UUID.randomUUID().toString(), getItemTypeCode(), 0, false);
    }


    public String getRestingName() {
        return restingName;
    }

    public Integer getItemTypeCode() {
        return itemTypeCode;
    }

    public String getItemShortName() {
        return itemShortName;
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

    public int getItemHalfLifeTicks() {
        return itemHalfLifeTicks;
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
