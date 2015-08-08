package com.comandante.creeper.Items;


public enum Rarity {

    BASIC("basic", 100.0),
    UNCOMMON("uncommon", 17.0),
    RARE("rare", 9.0),
    LEGENDARY("legendary", 2.0),
    EXOTIC("exotic", .05);

    private final String rarityTypeName;
    private final double percentToLoot;

    Rarity(String rarityTypeName, double percentToLoot) {
        this.rarityTypeName = rarityTypeName;
        this.percentToLoot = percentToLoot;
    }

    public String getRarityTypeName() {
        return rarityTypeName;
    }

    public double getPercentToLoot() {
        return percentToLoot;
    }
}
