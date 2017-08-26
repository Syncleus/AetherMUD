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
package com.syncleus.aethermud.world.model;

import com.syncleus.aethermud.core.GameManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BasicRoomBuilder {
    private Integer roomId;
    private String roomTitle;
    private Integer floorId;
    private Optional<Integer> northId = Optional.empty();
    private Optional<Integer> southId = Optional.empty();
    private Optional<Integer> eastId = Optional.empty();
    private Optional<Integer> westId = Optional.empty();
    private Optional<Integer> upId = Optional.empty();
    private Optional<Integer> downId = Optional.empty();
    private List<RemoteExit> enterExits = Lists.newArrayList();
    private String roomDescription;
    private Set<String> roomTags = Sets.newConcurrentHashSet();
    private Set<Area> areas = Sets.newConcurrentHashSet();
    private Map<String, String> notables = Maps.newHashMap();

    private final GameManager gameManager;

    public BasicRoomBuilder(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public BasicRoomBuilder setRoomId(Integer roomId) {
        this.roomId = roomId;
        return this;
    }

    public BasicRoomBuilder setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
        return this;
    }

    public BasicRoomBuilder setNorthId(Optional<Integer> northId) {
        this.northId = northId;
        return this;
    }

    public BasicRoomBuilder setSouthId(Optional<Integer> southId) {
        this.southId = southId;
        return this;
    }

    public BasicRoomBuilder setEastId(Optional<Integer> eastId) {
        this.eastId = eastId;
        return this;
    }

    public BasicRoomBuilder setWestId(Optional<Integer> westId) {
        this.westId = westId;
        return this;
    }

    public BasicRoomBuilder setUpId(Optional<Integer> upId) {
        this.upId = upId;
        return this;
    }

    public BasicRoomBuilder setDownId(Optional<Integer> downId) {
        this.downId = downId;
        return this;
    }

    public BasicRoomBuilder setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
        return this;
    }

    public BasicRoomBuilder setFloorId(Integer floorId) {
        this.floorId = floorId;
        return this;
    }

    public BasicRoomBuilder addTag(String tag) {
        this.roomTags.add(tag);
        return this;
    }

    public BasicRoomBuilder addArea(Area area) {
        this.areas.add(area);
        return this;
    }

    public BasicRoomBuilder addEnterExit(RemoteExit remoteExit) {
        this.enterExits.add(remoteExit);
        return this;
    }
    
    public BasicRoomBuilder addNotable(String notableName, String description) {
        this.notables.put(notableName, description);
        return this;
    }

    public BasicRoom createBasicRoom() {
        return new BasicRoom(roomId, roomTitle, floorId, northId, southId, eastId, westId, upId, downId, enterExits, roomDescription, roomTags, areas, notables, gameManager);
    }
}
