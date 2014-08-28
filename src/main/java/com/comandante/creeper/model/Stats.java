package com.comandante.creeper.model;

import java.io.Serializable;
import java.util.Random;

public class Stats implements Serializable {
    int strength;
    int willpower;
    int aim;
    int agile;
    int armorRating;
    int meleSkill;
    int health;
    int weaponRatingMax;
    int weaponRatingMin;
    int numberweaponOfRolls;

    public static int NO_TURNS = 0;
    public static int NO_HITS_CHALLENGER = 0;
    public static int NO_HITS_VICTIM = 0;
    public static int NO_MISSES_CHALLENGER = 0;
    public static int NO_MISSES_VICTIM = 0;
    public static int TOTAL_DAMAGE_CHALLENGER = 0;
    public static int TOTAL_DAMAGE_VICTIM = 0;

    public Stats(int strength, int willpower, int aim, int agile, int armorRating, int meleSkill, int health, int weaponRatingMin, int weaponRatingMax, int numberweaponOfRolls) {
        this.strength = strength;
        this.willpower = willpower;
        this.aim = aim;
        this.agile = agile;
        this.armorRating = armorRating;
        this.meleSkill = meleSkill;
        this.health = health;
        this.weaponRatingMax = weaponRatingMax;
        this.weaponRatingMin = weaponRatingMin;
        this.numberweaponOfRolls = numberweaponOfRolls;
    }

    public Stats() {
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public int getAim() {
        return aim;
    }

    public void setAim(int aim) {
        this.aim = aim;
    }

    public int getAgile() {
        return agile;
    }

    public void setAgile(int agile) {
        this.agile = agile;
    }

    public int getArmorRating() {
        return armorRating;
    }

    public void setArmorRating(int armorRating) {
        this.armorRating = armorRating;
    }

    public int getMeleSkill() {
        return meleSkill;
    }

    public void setMeleSkill(int meleSkill) {
        this.meleSkill = meleSkill;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getWeaponRatingMax() {
        return weaponRatingMax;
    }

    public void setWeaponRatingMax(int weaponRatingMax) {
        this.weaponRatingMax = weaponRatingMax;
    }

    public int getWeaponRatingMin() {
        return weaponRatingMin;
    }

    public void setWeaponRatingMin(int weaponRatingMin) {
        this.weaponRatingMin = weaponRatingMin;
    }

    public int getNumberweaponOfRolls() {
        return numberweaponOfRolls;
    }

    public void setNumberweaponOfRolls(int numberweaponOfRolls) {
        this.numberweaponOfRolls = numberweaponOfRolls;
    }

    public static void main(String[] args) throws InterruptedException {
        int totalChallengerWin = 0;
        int totalVictimWin = 0;
        int NUM_EXECUTION = 30000;
        for (int i = 0; i < NUM_EXECUTION; i++) {
            boolean results = fight(
                    new StatsBuilder()
                            .setStrength(10)
                            .setWillpower(1)
                            .setAim(1)
                            .setAgile(1)
                            .setArmorRating(2)
                            .setMeleSkill(10)
                            .setHealth(100)
                            .setWeaponRatingMin(10)
                            .setWeaponRatingMax(20)
                            .setNumberweaponOfRolls(1).createStats(),
                    new StatsBuilder()
                            .setStrength(5)
                            .setWillpower(1)
                            .setAim(1)
                            .setAgile(1)
                            .setArmorRating(5)
                            .setMeleSkill(5)
                            .setHealth(100)
                            .setWeaponRatingMin(5)
                            .setWeaponRatingMax(10)
                            .setNumberweaponOfRolls(1).createStats());
            //strength, willpower, aim, agile, armorRating, meleSkill, health, weaponRatingMin, weaponRatingMax, numberweaponOfRolls
            if (results) {
                totalChallengerWin++;
            } else {
                totalVictimWin++;
            }
        }
        System.out.println("Challenger: " + totalChallengerWin + " wins.");
        System.out.println("Victim: " + totalVictimWin + " wins.");
        System.out.println("AVERAGE TURNS: " + NO_TURNS / NUM_EXECUTION);
        System.out.println("AVERAGE HITS CHALLENGER: " + NO_HITS_CHALLENGER / NUM_EXECUTION);
        System.out.println("AVERAGE MISSES CHALLENGER: " + NO_MISSES_CHALLENGER / NUM_EXECUTION);
        System.out.println("AVERAGE DAMAGE CHALLENGER: " + TOTAL_DAMAGE_CHALLENGER / NUM_EXECUTION);
        System.out.println("AVERAGE HITS VICTIM: " + NO_HITS_VICTIM / NUM_EXECUTION);
        System.out.println("AVERAGE MISSES VICTIM: " + NO_MISSES_VICTIM / NUM_EXECUTION);
        System.out.println("AVERAGE DAMAGE VICTIM: " + TOTAL_DAMAGE_VICTIM / NUM_EXECUTION);

    }

    public static boolean fight(Stats challenger, Stats victim) {
        int damageToVictim = 0;
        int chanceToHitVictim = 0;


        int damageToChallenger = 0;
        int chanceToHitChallenger = 0;


        int turns = 0;
        while (true) {
            if (challenger.getHealth() <= 0 || victim.getHealth() <= 0) {
                break;
            }
            NO_TURNS++;
            damageToVictim = getAttack(challenger, victim);
            chanceToHitVictim = (challenger.getStrength() + challenger.getMeleSkill()) * 5 - victim.getAgile() * 5;
            if (randInt(0, 100) < chanceToHitVictim) {
                victim.setHealth(victim.getHealth() - damageToVictim);
                TOTAL_DAMAGE_CHALLENGER = TOTAL_DAMAGE_CHALLENGER + damageToVictim;
                NO_HITS_CHALLENGER++;
            } else {
                NO_MISSES_CHALLENGER++;
            }
            damageToChallenger = getAttack(victim, challenger);
            chanceToHitChallenger = (victim.getStrength() + victim.getMeleSkill()) * 5 - (challenger.getAgile() * 5);

            if (randInt(0, 100) < chanceToHitChallenger) {
                challenger.setHealth(challenger.getHealth() - damageToChallenger);
                TOTAL_DAMAGE_VICTIM = TOTAL_DAMAGE_VICTIM + damageToVictim;
                NO_HITS_VICTIM++;
            } else {
                NO_MISSES_VICTIM++;
            }
        }

        if (challenger.getHealth() > victim.getHealth()) {
            return true;
        } else {
            return false;

        }
    }

    private static int getAttack(Stats challenger, Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= challenger.getNumberweaponOfRolls()) {
            rolls++;
            totDamage = totDamage + randInt(challenger.getWeaponRatingMin(), challenger.getWeaponRatingMax());
        }
        return challenger.getStrength() + totDamage - victim.getArmorRating();
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


}
