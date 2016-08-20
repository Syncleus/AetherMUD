package com.comandante.creeper.classes;

public enum PlayerClass {

    WARRIOR(1, "warrior"),
    WIZARD(2,"wizard"),
    RANGER(3, "ranger"),
    SHAMAN(4, "shaman"),
    NOCLASS(0, "noclass");

    private final int id;
    private final String identifier;

    PlayerClass(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }
}
