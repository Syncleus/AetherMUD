package com.comandante.creeper.player;


public enum CoolDownType {

    DEATH("death", 30),
    FORAGE_LONG("forage-long", 7),
    FORAGE_MEDIUM("forage-medium", 4),
    FORAGE_SHORT("forage-short", 3),
    FORAGE_SUPERSHORT("forage-supershort", 1),
    SPELL("",0);

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
