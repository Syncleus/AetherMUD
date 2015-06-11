package com.comandante.creeper.player;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class Levels {

    private static double CONSTANT_MODIFIER = 0.005;

    public static int getLevel(int experience) {
        double v = CONSTANT_MODIFIER * sqrt(experience);
        return (int) Math.floor(v);
    }

    public static int getXp(int level) {
        double v = pow(level, 2) / pow(CONSTANT_MODIFIER, 2);
        return (int) Math.ceil(v);
    }

}
