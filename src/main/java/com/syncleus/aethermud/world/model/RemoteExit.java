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
package com.syncleus.aethermud.world.model;

public class RemoteExit {

    public enum Direction {
       UP, DOWN, ENTER
    }

    private final Direction direction;
    private final Integer roomId;
    private final String exitDetail;

    public RemoteExit(Direction direction, Integer roomId, String exitDetail) {
        this.direction = direction;
        this.roomId = roomId;
        this.exitDetail = exitDetail;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getExitDetail() {
        return exitDetail;
    }
}
