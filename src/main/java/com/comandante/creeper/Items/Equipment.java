package com.comandante.creeper.items;


import com.comandante.creeper.stats.Stats;

public class Equipment {

    private final EquipmentSlotType equipmentSlotType;
    private final Stats stats;

    public Equipment(EquipmentSlotType equipmentSlotType, Stats stats) {
        this.equipmentSlotType = equipmentSlotType;
        this.stats = stats;
    }

    public Equipment(Equipment equipment) {
        this.equipmentSlotType = equipment.equipmentSlotType;
        this.stats = equipment.stats;
    }

    public EquipmentSlotType getEquipmentSlotType() {
        return equipmentSlotType;
    }

    public Stats getStats() {
        return stats;
    }
}
