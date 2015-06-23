package com.comandante.creeper.player;


public class CoolDown {

    private int numberOfTicks;
    private final String name;
    private final CoolDownType coolDownType;

    CoolDown(CoolDownType coolDownType) {
        this.name = coolDownType.getName();
        this.numberOfTicks = coolDownType.getTicks();
        this.coolDownType = coolDownType;
    }

    public void decrementTick() {
        if (numberOfTicks > 0) {
            this.numberOfTicks = numberOfTicks - 1;
        }
    }

    public boolean isActive() {
        return numberOfTicks > 0;
    }

    public CoolDownType getCoolDownType() {
        return coolDownType;
    }
}
