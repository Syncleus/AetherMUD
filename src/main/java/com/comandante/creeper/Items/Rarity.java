package com.comandante.creeper.Items;


public enum Rarity {

    BASIC("basic", 2.0),
    UNCOMMON("uncommon", 1.2),
    RARE("rare", .6),
    LEGENDARY("legendary", .2),
    EXOTIC("exotic", .01);

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
