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

import java.lang.reflect.Type;

public enum PlayerSettings {
    AUTO_MAP("auto_map", "Automatically draws the creeper map on the CurrentRoomLogic function.", Integer.TYPE);

    private final String settingName;
    private final String settingDescription;
    private final Type type;

    PlayerSettings(String settingName, String settingDescription, Type type) {
        this.settingName = settingName;
        this.settingDescription = settingDescription;
        this.type = type;
    }

    public static PlayerSettings getByKey(String key) {
        PlayerSettings[] values = values();
        for (PlayerSettings playerSettings: values) {
            if (playerSettings.settingName.equals(key)) {
                return playerSettings;
            }
        }
        return null;
    }

    public String getSettingName() {
        return settingName;
    }

    public String getSettingDescription() {
        return settingDescription;
    }

    public Type getType() {
        return type;
    }
}
