package com.comandante.creeper.player;


import com.google.common.collect.Lists;

import java.util.List;

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

    public static EquipmentSlotType getByName(String name) {
        EquipmentSlotType[] values = EquipmentSlotType.values();
        for (EquipmentSlotType e: values) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static List<EquipmentSlotType> getAll() {
        List<EquipmentSlotType> theName = Lists.newArrayList();
        theName.add(EquipmentSlotType.HEAD);
        theName.add(EquipmentSlotType.CHEST);
        theName.add(EquipmentSlotType.WRISTS);
        theName.add(EquipmentSlotType.HAND);
        theName.add(EquipmentSlotType.LEGS);
        theName.add(EquipmentSlotType.FEET);
        return theName;
    }
}
