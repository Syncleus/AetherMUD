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
package com.comandante.creeper.world.model;

import com.comandante.creeper.core_game.GameManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BasicRoom extends Room {
    @Override
    public void run() {
        super.run();
        // System.out.println(getRoomTitle() + " tick.");
    }

    public BasicRoom(Integer roomId,
                     String roomTitle,
                     Integer floorId,
                     Optional<Integer> northId,
                     Optional<Integer> southId,
                     Optional<Integer> eastId,
                     Optional<Integer> westId,
                     Optional<Integer> upId,
                     Optional<Integer> downId,
                     List<RemoteExit> enterExits,
                     String roomDescription,
                     Set<String> roomTags,
                     Set<Area> areas,
                     Map<String, String> notables,
                     GameManager gameManager) {
        super(roomId, roomTitle, floorId, northId, southId, eastId, westId, upId, downId, enterExits, roomDescription, roomTags, areas, notables, gameManager);
    }
}
