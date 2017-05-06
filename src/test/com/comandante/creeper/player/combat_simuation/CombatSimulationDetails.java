package com.comandante.creeper.player.combat_simuation;

import com.comandante.creeper.items.Equipment;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.npc.Npc;

import java.util.Set;

public class CombatSimulationDetails {

    private final int level;
    private final int totalIterations;
    private final Set<Item> equipmentSet;
    private final Npc npc;

    public CombatSimulationDetails(int level, int totalIterations, Set<Item> equipmentSet, Npc npc) {
        this.level = level;
        this.totalIterations = totalIterations;
        this.equipmentSet = equipmentSet;
        this.npc = npc;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalIterations() {
        return totalIterations;
    }

    public Npc getNpc() {
        return npc;
    }

    public Set<Item> getEquipmentSet() {
        return equipmentSet;
    }
}
