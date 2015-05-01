package com.comandante.creeper.player;


public enum EquipmentSlotType {

    HAND("hand"),
    HEAD("head"),
    FEET("feet"),
    LEGS("legs"),
    WRISTS("wrists"),
    CHEST("chest");

    private final String name;

    EquipmentSlotType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
