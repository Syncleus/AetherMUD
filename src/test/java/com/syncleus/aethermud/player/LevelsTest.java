/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.player;

import com.syncleus.aethermud.stats.Levels;
import org.junit.Assert;
import org.junit.Test;

public class LevelsTest {
    private static final int[] TOTAL_EXPERIENCE_PER_LEVEL = new int[]{0, 2500, 10000, 22500, 40000, 62500, 90000,
        122500, 160000, 202500, 250000, 302500, 360000, 422500, 490000, 562500, 640000, 722500, 810000, 902500, 1000000,
        1102500, 1210000, 1322500, 1440000, 1562500, 1690000, 1822500, 1960000, 2102500, 2250000, 2402500, 2560000,
        2722500, 2890000, 3062500, 3240000, 3422500, 3610000, 3802500, 4000000, 4202500, 4410000, 4622500, 4840000,
        5062500, 5290000, 5522500, 5760000, 6002500, 6250000, 6502500, 6760000, 7022500, 7290000, 7562500, 7840000,
        8122500, 8410000, 8702500, 9000000, 9302500, 9610000, 9922500, 10240000, 10562500, 10890000, 11222500, 11560000,
        11902500, 12250000, 12602500, 12960000, 13322500, 13690000, 14062500, 14440000, 14822500, 15210000, 15602500,
        16000000, 16402500, 16810000, 17222500, 17640000, 18062500, 18490000, 18922500, 19360000, 19802500, 20250000,
        20702500, 21160000, 21622500, 22090000, 22562500, 23040000, 23522500, 24010000, 24502500};

    @Test
    public void testTotalExperienceToLevel() throws Exception {
        for (int i = 0; i < 100; i++) {
            long xp = Levels.getXp(i);
            long level = Levels.getLevel(xp);
            Assert.assertEquals(level, i);
            Assert.assertEquals(xp, TOTAL_EXPERIENCE_PER_LEVEL[(int) level]);
        }
    }

    @Test
    public void testIncrementalExperienceToLevel() throws Exception {
        for (int i = 0; i < 99; i++) {
            long caclculatedExperience = Levels.getXp(i, i + 1);
            long expectedExperience = TOTAL_EXPERIENCE_PER_LEVEL[i + 1] - TOTAL_EXPERIENCE_PER_LEVEL[i];
            Assert.assertEquals(caclculatedExperience, expectedExperience);
        }
    }
}
