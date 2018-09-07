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
package com.syncleus.aethermud.world.model;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class RoomModelBuilder {
    private int roomId;
    private int floorId;
    private String roomDescription;
    private String roomTitle;
    private Set<String> roomTags = Sets.newHashSet();
    private Set<String> areaNames = Sets.newHashSet();
    private Map<String, String> enterExitNames = Maps.newHashMap();
    private Map<String, String> notables = Maps.newHashMap();

    public RoomModelBuilder setRoomId(int roomId) {
        this.roomId = roomId;
        return this;
    }

    public RoomModelBuilder setFloorId(int floorId) {
        this.floorId = floorId;
        return this;
    }

    public RoomModelBuilder setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
        return this;
    }

    public RoomModelBuilder setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
        return this;
    }

    public void setRoomTags(Set<String> roomTags) {
        this.roomTags = roomTags;
    }

    public RoomModelBuilder addRoomTag(String roomTag) {
        roomTags.add(roomTag);
        return this;
    }

    public RoomModelBuilder addAreaName(String areaName) {
        areaNames.add(areaName);
        return this;
    }

    public RoomModelBuilder addEnterExitName(Integer roomId, String enterExitName) {
        enterExitNames.put(String.valueOf(roomId), enterExitName);
        return this;
    }

    public RoomModelBuilder addNotable(String notableName, String description) {
        notables.put(notableName, description);
        return this;
    }


    public RoomModel build() {
        return new RoomModel(roomId, floorId, roomDescription, roomTitle, notables, roomTags, areaNames, enterExitNames);
    }
}
