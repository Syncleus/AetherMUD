package com.comandante.creeper.items;

public class ForageBuilder {
    private ItemType itemType;
    private int minLevel;
    private double pctOfSuccess;
    private int minAmt;
    private int maxAmt;
    private int forageExperience;
    private int coolDownTicks;

    public ForageBuilder setItemType(ItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    public ForageBuilder setMinLevel(int minLevel) {
        this.minLevel = minLevel;
        return this;
    }

    public ForageBuilder setPctOfSuccess(double pctOfSuccess) {
        this.pctOfSuccess = pctOfSuccess;
        return this;
    }

    public ForageBuilder setMinAmt(int minAmt) {
        this.minAmt = minAmt;
        return this;
    }

    public ForageBuilder setMaxAmt(int maxAmt) {
        this.maxAmt = maxAmt;
        return this;
    }

    public ForageBuilder setForageExperience(int forageExperience) {
        this.forageExperience = forageExperience;
        return this;
    }

    public ForageBuilder setCoolDownTicks(int coolDownTicks) {
        this.coolDownTicks = coolDownTicks;
        return this;
    }

    public Forage createForage() {
        return new Forage(itemType, minLevel, pctOfSuccess, minAmt, maxAmt, forageExperience, coolDownTicks);
    }
}