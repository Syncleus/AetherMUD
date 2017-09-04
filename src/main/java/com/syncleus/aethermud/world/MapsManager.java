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

    private void generate() {
        Timer.Context time = ticktime.time();
        int maxRows = aetherMudConfiguration.defaultMapSize;
        int maxColumns = aetherMudConfiguration.defaultMapSize;
        Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRoomsIterator();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            Integer roomId = next.getValue().getRoomId();
            try {
                String s = drawMap(roomId, new Coords(maxRows, maxColumns));
                next.getValue().setMapData(Optional.of(s));
            } catch (Exception e) {
                log.error("Unable to generate room map for roomId: " + roomId);
            }
        }
        time.stop();
    }

    public void generateAllMaps() {
        mapGeneratorService.submit(new MapGeneration());
    }

    public String drawMap(Integer roomId, Coords max) {
        MapMatrix floorMatrix = floorMatrixMaps.get(roomManager.getRoom(roomId).getFloorId());
        MapMatrix mapMatrix = floorMatrix.extractMatrix(roomId, max);
        return mapMatrix.renderMap(roomId, roomManager);
    }

    public static Function<Integer, String> render(final Integer currentroomId, final RoomManager roomManager) {
        return roomId -> {
            Room room = roomManager.getRoom(roomId);
            if (roomId > 0) {
                if (roomId.equals(currentroomId)) {
                    return " " + Color.BOLD_ON + Color.WHITE + "☺" + Color.RESET + " ";
                } else if (roomId.equals(1)) {
                    return " " + Color.BOLD_ON + Color.BLUE + "L" + Color.RESET + " ";
                } else if (room.getMerchants().size() > 0) {
                    return " " + Color.YELLOW + "m" + Color.RESET + " ";
                }  else if (room.getEnterExits().size() > 0) {
                    return " " + Color.CYAN + "e" + Color.RESET + " ";
                } else if (room.getUpId().isPresent()) {
                    return " " + Color.GREEN + "^" + Color.RESET + " ";
                } else if (room.getDownId().isPresent()) {
                    return " " + Color.GREEN + "v" + Color.RESET + " ";
                } else {
                    return "   ";
                }
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

    class MapGeneration implements Runnable {
        @Override
        public void run() {
            try {
                generate();
            } catch (Exception e) {
                log.error("Unable to generate map data!", e);
            }
        }
    }
}
