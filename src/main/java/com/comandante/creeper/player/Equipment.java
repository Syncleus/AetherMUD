package com.comandante.creeper.player;


import com.comandante.creeper.stat.Stats;

public class Equipment {

    private final EquipmentSlotType equipmentSlotType;
    private final Stats stats;

    Equipment(EquipmentSlotType equipmentSlotType, Stats stats) {
        this.equipmentSlotType = equipmentSlotType;
        this.stats = stats;
    }

    public EquipmentSlotType getEquipmentSlotType() {
        return equipmentSlotType;
    }

    public Stats getStats() {
        return stats;
    }
}
