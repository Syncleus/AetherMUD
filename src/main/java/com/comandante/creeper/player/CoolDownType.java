package com.comandante.creeper.player;


public enum CoolDownType {

    DEATH("death", 3);

    private final String name;
    private final int ticks;

    CoolDownType(String name, int ticks) {
        this.name = name;
        this.ticks = ticks;
    }

    public String getName() {
        return name;
    }

    public int getTicks() {
        return ticks;
    }
}
