package com.comandante.creeper.items;

public class ForageBuilder {
    private String internalItemName;
    private int minLevel;
    private double pctOfSuccess;
    private int minAmt;
    private int maxAmt;
    private int forageExperience;
    private int coolDownTicks;

    public ForageBuilder setInternalItemName(String internalItemName) {
        this.internalItemName = internalItemName;
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
        return new Forage(internalItemName, minLevel, pctOfSuccess, minAmt, maxAmt, forageExperience, coolDownTicks);
    }
}