package com.comandante.creeper.player;

import java.text.DecimalFormat;

import static java.lang.StrictMath.sqrt;

public class Levels {

    private static double CONSTANT_MODIFIER = 0.005;

    public static void main(String[] args) {
        int i = 0;
        while (i < 1000000) {
            int level = getLevel(i);
            System.out.println("xp is: " + i + " level is: " + level + " double checking math: " + getXp(level));
            i = i + 1000;
        }

        int level = 0;
        while (level < 60) {
            level++;
            int xp = getXp(level);
            System.out.println("level: " + level + " is " + xp + "exp.");
        }
    }

    public static int getLevel(int experience) {
        double v = CONSTANT_MODIFIER * sqrt(experience);
        return (int) Math.floor(v);
    }

    public static int getXp(int level) {
        double v = Math.pow(level, 2) / Math.pow(CONSTANT_MODIFIER, 2);
        return (int) Math.ceil(v);
    }

}
