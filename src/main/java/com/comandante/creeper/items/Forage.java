package com.comandante.creeper.items;


import com.comandante.creeper.entity.CreeperEntity;

public class Forage extends CreeperEntity {

    private final ItemType itemType;
    private final int minLevel;
    private final double pctOfSuccess;
    private final int minAmt;
    private final int maxAmt;
    private final int forageExperience;
    private final int coolDownTicks;
    private int coolDownTicksLeft;

    public Forage(ItemType itemType, int minLevel, double pctOfSuccess, int minAmt, int maxAmt, int forageExperience, int coolDownTicks) {
        this.itemType = itemType;
        this.minLevel = minLevel;
        this.pctOfSuccess = pctOfSuccess;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
        this.coolDownTicksLeft = 0;
        this.forageExperience = forageExperience;
        this.coolDownTicks = coolDownTicks;
    }

    public Forage(Forage forage) {
        this.itemType = forage.itemType;
        this.minLevel = new Integer(forage.getMinLevel());
        this.pctOfSuccess = new Double(forage.getPctOfSuccess());
        this.minAmt = new Integer(forage.getMinAmt());
        this.maxAmt = new Integer(forage.getMaxAmt());
        this.coolDownTicks = new Integer(forage.getCoolDownTicks());
        this.coolDownTicksLeft = new Integer(0);
        this.forageExperience = new Integer(forage.getForageExperience());
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public double getPctOfSuccess() {
        return pctOfSuccess;
    }

    public int getMinAmt() {
        return minAmt;
    }

    public int getMaxAmt() {
        return maxAmt;
    }

    public int getCoolDownTicks() {
        return coolDownTicks;
    }

    public int getForageExperience() {
        return forageExperience;
    }

    public int getCoolDownTicksLeft() {
        return coolDownTicksLeft;
    }

    public void setCoolDownTicksLeft(int coolDownTicksLeft) {
        this.coolDownTicksLeft = coolDownTicksLeft;
    }

    @Override
    public void run() {
        if (coolDownTicksLeft > 0) {
            coolDownTicksLeft--;
        }
    }
}
