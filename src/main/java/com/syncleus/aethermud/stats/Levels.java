/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.stats;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class Levels {

    private static double CONSTANT_MODIFIER = 0.02;

    public static int getLevel(long experience) {
        double v = CONSTANT_MODIFIER * sqrt(experience);
        return (int) Math.floor(v);
    }

    public static int getXp(int level) {
        double v = pow(level, 2) / pow(CONSTANT_MODIFIER, 2);
        return (int) Math.ceil(v);
    }

    public static int getXp(int fromLevel, int toLevel) {
        return getXp(toLevel) - getXp(fromLevel);
    }

}
