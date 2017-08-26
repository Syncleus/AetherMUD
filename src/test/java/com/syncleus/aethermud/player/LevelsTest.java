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
package com.syncleus.aethermud.player;

import com.syncleus.aethermud.stats.Levels;
import org.junit.Assert;
import org.junit.Test;

public class LevelsTest {

    @Test
    public void testLevels() throws Exception {
        for (int i = 0; i < 100; i++) {
            long xp = Levels.getXp(i);
            long level = Levels.getLevel(xp);
            Assert.assertEquals(level, i);
            System.out.println("Level: " + i + " | XP: " + xp);
        }
    }

    @Test
    public void printHowMuchXpIsNecessaryToLevelUp() throws Exception {
        for (int i = 0; i < 100; i++) {
            long xp = Levels.getXp(i);
            long level = Levels.getLevel(xp);
            long xpNext = Levels.getXp(i + 1);
            long l = xpNext - xp;
            System.out.println("Level " + i + " to " + (i + 1) + " takes " + l + " xp.");
        }
    }
}
