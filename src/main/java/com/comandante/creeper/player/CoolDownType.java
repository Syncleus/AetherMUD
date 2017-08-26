/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.comandante.creeper.player;


public enum CoolDownType {

    DEATH("death", 150),
    FORAGE_LONG("forage-long", 7),
    FORAGE_MEDIUM("forage-medium", 4),
    FORAGE_SHORT("forage-short", 3),
    FORAGE_SUPERSHORT("forage-supershort", 1),
    SPELL("",0),
    NPC_FIGHT("fight",30),
    NPC_ROAM("npc-roam", 1200),
    NPC_ALERTED("npc-alerted", 30),
    PLAYER_RECALL("recall", 600),
    DETAINMENT("detained", 32768),
    NEWBIE("newbie", 20000);
    private final String name;
    private final int ticks;

    CoolDownType(String name, int ticks) {
        this.name = name;
        this.ticks = ticks;
    }

    public String getName() {
        return name;
    }

    public int getTicks() {
        return ticks;
    }
}
