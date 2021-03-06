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
package com.syncleus.aethermud.world;

import com.codahale.metrics.Timer;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.world.model.Coords;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static com.codahale.metrics.MetricRegistry.name;

public class MapsManager {

    private final RoomManager roomManager;
    private final Map<Integer, MapMatrix> floorMatrixMaps;
    private final AetherMudConfiguration aetherMudConfiguration;
    private final ExecutorService mapGeneratorService = Executors.newFixedThreadPool(1);
    private static final Logger log = Logger.getLogger(GameManager.class);
    private final Timer ticktime = Main.metrics.timer(name(MapsManager.class, "generate_all_maps_time"));

    public MapsManager(AetherMudConfiguration aetherMudConfiguration, RoomManager roomManager) {
        this.roomManager = roomManager;
        this.floorMatrixMaps = Maps.newHashMap();
        this.aetherMudConfiguration = aetherMudConfiguration;
    }

    public Optional<String> generateMap(Room room) {
        int maxRows = aetherMudConfiguration.defaultMapSize;
        int maxColumns = aetherMudConfiguration.defaultMapSize;

        Integer roomId = room.getRoomId();
        String s = drawMap(roomId, new Coords(maxRows, maxColumns));
        return Optional.of(s);
    }

    public String drawMap(Integer roomId, Coords max) {
        MapMatrix floorMatrix = floorMatrixMaps.get(roomManager.getRoom(roomId).getFloorId());
        MapMatrix mapMatrix = floorMatrix.extractMatrix(roomId, max);
        return mapMatrix.renderMap(roomId, roomManager);
    }

    public static Function<Integer, String> render(final Integer currentroomId, final RoomManager roomManager) {
        return roomId -> {
            if (roomId > 0) {
                Room room = roomManager.getRoom(roomId);
                boolean meHere = roomId.equals(currentroomId);
                boolean merchantsHere = room.getMerchants().size() > 0;
                boolean zoneChangeHere = room.getEnterExits().size() > 0;
                boolean upHere = room.getUpId().isPresent();
                boolean downHere = room.getDownId().isPresent();
                boolean mobsHere = room.getNpcIds().size() > 0;

                String leftChar = " ";
                if(zoneChangeHere)
                    leftChar = Color.BOLD_ON + Color.GREEN + "☼" + Color.RESET;
                else if(upHere)
                    leftChar = Color.BOLD_ON + Color.GREEN + "↑" + Color.RESET;
                else if(downHere)
                    leftChar = Color.BOLD_ON + Color.GREEN + "↓" + Color.RESET;

                String rightChar = " ";
                if(mobsHere)
                    rightChar = Color.BOLD_ON + Color.RED + "*" + Color.RESET;
                else if(merchantsHere)
                    rightChar = Color.BOLD_ON + Color.BLUE + "☻" + Color.RESET;

                String middleChar = " ";
                if(meHere)
                    middleChar = Color.BOLD_ON + Color.WHITE + "☺" + Color.RESET;

                if(leftChar.equals(" ") && !rightChar.equals(" ")) {
                    if(mobsHere && merchantsHere)
                        leftChar = Color.BOLD_ON + Color.BLUE + "☻" + Color.RESET;
                }
                else if(!leftChar.equals(" ") && rightChar.equals(" ")) {
                    if(zoneChangeHere && upHere)
                        rightChar = Color.BOLD_ON + Color.GREEN + "↑" + Color.RESET;
                    else if((zoneChangeHere || upHere) && downHere)
                        rightChar = Color.BOLD_ON + Color.GREEN + "↓" + Color.RESET;
                }

                return leftChar + middleChar + rightChar;
            } else {
                return "   ";
            }
        };
    }

    public void addFloorMatrix(Integer id, MapMatrix floorMatrix) {
        floorMatrixMaps.put(id, floorMatrix);
    }

    public Map<Integer, MapMatrix> getFloorMatrixMaps() {
        return floorMatrixMaps;
    }
}
