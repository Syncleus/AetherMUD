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

public enum PlayerRole {

    ADMIN("administrator", 1),
    MORTAL("mortal", 2),
    TELEPORTER("teleporter", 3),
    GOD("god", 3);
    
    private final String roleType;
    private final int roleId;

    PlayerRole(String roleType, int roleId) {
        this.roleType = roleType;
        this.roleId = roleId;
    }

    public String getRoleType() {
        return roleType;
    }

    public int getRoleId() {
        return roleId;
    }

    public static PlayerRole getByType(String type) {
        PlayerRole[] values = PlayerRole.values();
        for (PlayerRole playerRole : values) {
            if(playerRole.getRoleType().equals(type)) {
                return playerRole;
            }
        }
        return null;
    }
}
