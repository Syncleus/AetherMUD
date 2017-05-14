package com.comandante.creeper.items;


import com.comandante.creeper.stats.Stats;

public class Equipment {

    private final EquipmentSlotType equipmentSlotType;
    private final Stats statsIncreaseWhileEquipped;

    public Equipment(EquipmentSlotType equipmentSlotType, Stats statsIncreaseWhileEquipped) {
        this.equipmentSlotType = equipmentSlotType;
        this.statsIncreaseWhileEquipped = statsIncreaseWhileEquipped;
    }

    public Equipment(Equipment equipment) {
        this.equipmentSlotType = equipment.equipmentSlotType;
        this.statsIncreaseWhileEquipped = equipment.statsIncreaseWhileEquipped;
    }

    public EquipmentSlotType getEquipmentSlotType() {
        return equipmentSlotType;
    }

    public Stats getStats() {
        return statsIncreaseWhileEquipped;
    }
}
