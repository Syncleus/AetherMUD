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
package com.syncleus.aethermud.npc;

public enum Temperament {
    AGGRESSIVE("Aggressive"),
    PASSIVE("Passive");

    private final String friendlyFormat;

    Temperament(String friendlyFormat) {
        this.friendlyFormat = friendlyFormat;
    }

    public String getFriendlyFormat() {
        return friendlyFormat;
    }

    public static Temperament get(String s) {
        for (Temperament t : Temperament.values()) {
            if (t.name().equalsIgnoreCase(s)) {
                return t;
            }
        }
        return null;
    }
}