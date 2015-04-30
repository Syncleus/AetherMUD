package com.comandante.creeper.player;


public enum EquipmentSlotType {

    HAND("hand"),
    HEAD("head"),
    FOOT("foot"),
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
