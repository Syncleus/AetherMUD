package com.comandante.creeper.player;


public enum CoolDownType {

    DEATH("death", 30),
    FORAGE_LONG("forage-long", 15),
    FORAGE_MEDIUM("forage-medium", 10),
    FORAGE_SHORT("forage-short", 16),
    FORAGE_SUPERSHORT("forage-supershort", 3);

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
