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
package com.syncleus.aethermud.stats.experience;

import com.syncleus.aethermud.npc.NpcSpawn;

public class Experience {

    public static int calculateNpcXp(int playerLevel, int npcLevel) {
        float xp = 0;
        if (npcLevel == playerLevel) {
            xp = (playerLevel * 5 + 45);
        }
        if (npcLevel > playerLevel) {
            float i = playerLevel * 5 + 45;
            float diff = npcLevel - playerLevel;
            float modifier = 1 + .2f * (diff);
            xp = i * modifier;
        }
        if (npcLevel < playerLevel) {
            if (getLevelColor(playerLevel, npcLevel).equals(NpcSpawn.NpcLevelColor.WHITE)) {
                xp = 0;
            } else {
                float levelDif = playerLevel - npcLevel;
                xp = (playerLevel * 5 + 45) * (1 - (levelDif / getZD(playerLevel)));
            }
        }
        if (xp == 0) {
            return 0;
        }
        return (int) Math.floor(xp + 0.5);
    }


    public static NpcSpawn.NpcLevelColor getLevelColor(int playerLevel, int npcLevel) {
        if (playerLevel + 5 <= npcLevel) {
            return NpcSpawn.NpcLevelColor.RED;
        } else {
            switch (npcLevel - playerLevel) {
                case 4:
                case 3:
                    return NpcSpawn.NpcLevelColor.ORANGE;
                case 2:
                case 1:
                case 0:
                case -1:
                case -2:
                    return NpcSpawn.NpcLevelColor.YELLOW;
                default:
                    if (playerLevel <= 5) {
                        return NpcSpawn.NpcLevelColor.GREEN;
                    } else {
                        if (playerLevel <= 50) {
                            if (npcLevel <= (playerLevel - 5 - Math.floor(playerLevel / 10))) {
                                return NpcSpawn.NpcLevelColor.WHITE;
                            } else {
                                return NpcSpawn.NpcLevelColor.GREEN;
                            }
                        } else {
                            // Player is over level 50
                            if (npcLevel <= (playerLevel - 1 - Math.floor(playerLevel / 5))) {
                                return NpcSpawn.NpcLevelColor.WHITE;
                            } else {
                                return NpcSpawn.NpcLevelColor.GREEN;
                            }
                        }
                    }
            }
        }
    }

    private static int getZD(int lvl) {
        if (lvl <= 7) {
            return 5;
        }
        if (lvl <= 9) {
            return 6;
        }
        if (lvl <= 11) {
            return 7;
        }
        if (lvl <= 15) {
            return 8;
        }
        if (lvl <= 19) {
            return 9;
        }
        if (lvl <= 29) {
            return 11;
        }
        if (lvl <= 39) {
            return 12;
        }
        if (lvl <= 49) {
            return 13;
        }
        if (lvl <= 59) {
            return 14;
        }
        if (lvl <= 69) {
            return 15;
        }
        if (lvl <= 79) {
            return 16;
        }
        if (lvl <= 89) {
            return 17;
        }
        if (lvl <= 99) {
            return 18;
        } else {
            return 19;
        }
    }

}
