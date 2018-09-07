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

import com.syncleus.aethermud.world.model.Coords;
import com.syncleus.aethermud.world.model.RemoteExit;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.syncleus.aethermud.world.model.Room;

import java.util.*;

public class MapMatrix {

    private final List<List<Integer>> matrix;
    private final Map<Integer, Set<RemoteExit>> remotes;
    private Coords max;

    public MapMatrix(List<List<Integer>> matrix, Map<Integer, Set<RemoteExit>> remotes) {
        this.matrix = matrix;
        this.remotes = remotes;
        this.max = new Coords(matrix.size(), getMaxColumn());
    }

    public Map<Integer, Set<RemoteExit>> getRemotes() {
        return remotes;
    }

    public void addRemote(Integer roomId, RemoteExit exit) {
        if (remotes.get(roomId) == null) {
            remotes.put(roomId, Sets.newHashSet(exit));
        } else {
            remotes.get(roomId).add(exit);
        }
    }

    private static void addRemote(Integer roomId, RemoteExit exit, Map<Integer, Set<RemoteExit>> remotes) {
        if (remotes.get(roomId) == null) {
            remotes.put(roomId, Sets.newHashSet(exit));
        } else {
            remotes.get(roomId).add(exit);
        }
    }

    public int getMaxRow() {
        return max.row;
    }

    public int getMaxCol() {
        return max.column;
    }

    public void setMax(Coords max) {
        this.max = max;
    }

    public java.util.Iterator<List<Integer>> getRows() {
        return matrix.iterator();
    }

    public Coords getCoords(Integer roomId) {
        int row = 0;
        int column = 0;
        for (List<Integer> r : matrix) {
            for (Integer id : r) {
                if (id.equals(roomId)) {
                    return new Coords(row, column);
                } else {
                    column++;
                }
            }
            row++;
            column = 0;
        }
        return null;
    }

    public void setCoordsValue(Coords coords, Integer roomId) {
        if (coords.row < 0 || coords.column < 0 || coords.row >= max.row || coords.column >= max.column) {
            return;
        }
        matrix.get(coords.row).set(coords.column, roomId);
    }


    public Integer getNorthernExit(Integer sourceId) {
        Coords coords = getCoords(sourceId);
        int rowNorth = coords.row - 1;
        int columnNorth = coords.column;
        return getId(rowNorth, columnNorth);
    }

    public Integer getSouthernExit(Integer sourceId) {
        Coords coords = getCoords(sourceId);
        int rowSouth = coords.row + 1;
        int columnSouth = coords.column;
        return getId(rowSouth, columnSouth);
    }

    public Integer getEasternExit(Integer sourceId) {
        Coords coords = getCoords(sourceId);
        int rowEast = coords.row;
        int columnEast = coords.column + 1;
        return getId(rowEast, columnEast);
    }

    public Integer getWesternExit(Integer sourceId) {
        Coords coords = getCoords(sourceId);
        int rowWest = coords.row;
        int columnWest = coords.column - 1;
        return getId(rowWest, columnWest);
    }

    public boolean isNorthernMapSpaceEmpty(Integer sourceId) {
        return (getNorthernExit(sourceId) == 0);
    }

    public boolean isSouthernMapSpaceEmpty(Integer sourceId) {
        return (getSouthernExit(sourceId) == 0);
    }

    public boolean isEasternMapSpaceEmpty(Integer sourceId) {
        return (getEasternExit(sourceId) == 0);
    }

    public boolean isWesternMapSpaceEmpty(Integer sourceId) {
        return (getWesternExit(sourceId) == 0);
    }

    private Integer getId(int row, int column) {
        if (row < 0 || column < 0 || row >= max.row || column >= max.column) {
            return 0;
        }
        List<Integer> integers = matrix.get(row);
        return integers.get(column);
    }

    private int getMaxColumn() {
        int max = 0;
        for (List<Integer> list : matrix) {
            if (list.size() > max) {
                max = list.size();
            }
        }
        return max;
    }

    public MapMatrix extractMatrix(Integer roomId, Coords newMax) {
        MapMatrix destinationMatrix = getBlankMatrix(newMax.row, newMax.column);
        Coords coords = getCoords(roomId);
        int rowDifference = destinationMatrix.getMaxRow() / 2 - coords.row;
        int columnDifference = destinationMatrix.getMaxCol() / 2 - coords.column;
        Iterator<List<Integer>> rows = getRows();
        while (rows.hasNext()) {
            UnmodifiableIterator<Integer> ids = Iterators.filter(rows.next().iterator(), removeZeros());
            while (ids.hasNext()) {
                Integer id = ids.next();
                Coords currentMatrixCoords = getCoords(id);
                Coords destinationMatrixCoords = new Coords(currentMatrixCoords.row + rowDifference,
                        currentMatrixCoords.column + columnDifference);
                destinationMatrix.setCoordsValue(destinationMatrixCoords, id);
            }
        }
        return destinationMatrix;
    }

    private Predicate<Integer> removeZeros() {
        return integer -> {
            if (integer > 0) {
                return true;
            }
            return false;
        };
    }

    public String getCsv() {
        StringBuilder sb = new StringBuilder();
        for (List<Integer> list : matrix) {
            for (Integer roomId : list) {
                if (!roomId.equals(0)) {
                    sb.append(roomId);
                    if (remotes.containsKey(roomId)) {
                        for (RemoteExit exit : remotes.get(roomId)) {
                            if (exit.getDirection().equals(RemoteExit.Direction.UP)) {
                                sb.append("u|").append(exit.getRoomId());
                            } else if (exit.getDirection().equals(RemoteExit.Direction.DOWN)) {
                                sb.append("d|").append(exit.getRoomId());
                            }
                        }
                    }
                }
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static Integer getUp(String csvInputCell) {
        String[] us = csvInputCell.split("u\\|");
        if (us[1].matches(".*[a-zA-Z]+.*")) {
            return Integer.valueOf(us[1].split("[a-zA-Z]")[0]);
        }
        return Integer.valueOf(us[1]);
    }

    private static Integer getDown(String csvInputCell) {
        String[] us = csvInputCell.split("d\\|");
        if (us[1].matches(".*[a-zA-Z]+.*")) {
            return Integer.valueOf(us[1].split("[a-zA-Z]")[0]);
        }
        return Integer.valueOf(us[1]);
    }

    public static MapMatrix createMatrixFromCsv(String mapCSV) {
        List<String> rows = Arrays.asList(mapCSV.split("\\r?\\n"));
        ArrayList<List<Integer>> rowsList = Lists.newArrayList();
        Map<Integer, Set<RemoteExit>> remotes = Maps.newHashMap();
        for (String row : rows) {
            List<String> strings = Arrays.asList(row.split(",", -1));
            List<Integer> data = Lists.newArrayList();
            for (String string : strings) {
                if (!string.isEmpty()) {
                    Integer roomId = Integer.parseInt(string.split("[a-zA-Z]")[0]);
                    if (string.contains("u|")) {
                        Integer up = getUp(string);
                        addRemote(roomId, new RemoteExit(RemoteExit.Direction.UP, up, ""), remotes);
                    }
                    if (string.contains("d|")) {
                        Integer down = getDown(string);
                        addRemote(roomId, new RemoteExit(RemoteExit.Direction.DOWN, down, ""), remotes);
                    }
                    data.add(roomId);
                } else {
                    data.add(0);
                }
            }
            rowsList.add(data);
        }
        return new MapMatrix(rowsList, remotes);
    }

    private static MapMatrix getBlankMatrix(int maxRows, int maxColumns) {
        List<List<Integer>> lists = Lists.newArrayList();
        for (int i = 0; i <= maxRows; i++) {
            lists.add(Lists.<Integer>newArrayList());
        }
        for (List<Integer> roomOpts : lists) {
            for (int i = 0; i <= maxColumns; i++) {
                roomOpts.add(0);
            }
        }
        return new MapMatrix(lists, Maps.newHashMap());
    }

    public String renderMap(Integer roomId, RoomManager roomManager) {
        StringBuilder sb = new StringBuilder();
        int width = (max.getRow()-1) * 4 + 1;
        int height = (max.getColumn()-1) * 2 + 1;
        for(int row = 0; row < height; row++) {
            boolean borderRow = (row % 2 == 0);
            int roomRow = row / 2;
            for(int column = 0; column < width; column++) {
                boolean borderColumn = (column % 4 == 0);
                int roomColumn = column / 4;
                Integer renderRoomId = matrix.get(roomRow).get(roomColumn);
                boolean hereRoom = (renderRoomId != 0 && row < height - 1 && column < width - 1);
                boolean westRoom = (roomColumn == 0 || row >= height - 1 ? false : matrix.get(roomRow).get(roomColumn - 1) != 0);
                boolean northRoom = (roomRow == 0 || column >= width - 1 ? false : matrix.get(roomRow - 1).get(roomColumn) != 0);
                boolean northWestRoom = (roomRow == 0 || roomColumn == 0 ? false : matrix.get(roomRow - 1).get(roomColumn - 1) != 0);
                if(borderRow) {
                    if(borderColumn) {
                        //is an intersection between the four rooms
                        if( (hereRoom && northWestRoom) || (westRoom && northRoom) )
                            sb.append("┼");
                        else if(hereRoom && northRoom)
                            sb.append("├");
                        else if(hereRoom && westRoom)
                            sb.append("┬");
                        else if(northWestRoom && westRoom)
                            sb.append("┤");
                        else if(northWestRoom && northRoom)
                            sb.append("┴");
                        else if(hereRoom)
                            sb.append("┌");
                        else if(westRoom)
                            sb.append("┐");
                        else if(northWestRoom)
                            sb.append("┘");
                        else if(northRoom)
                            sb.append("└");
                        else
                            sb.append(" ");
                    }
                    else if((column >= 2) && ((column - 2) % 4 == 0)) {
                        // is a row exit
                        if( hereRoom || northRoom ) {
                            if(hereRoom && northRoom)
                                sb.append("↕");
                            else
                                sb.append("─");
                        }
                        else
                            sb.append(" ");
                    }
                    else {
                        //is a row wall
                        if(hereRoom || northRoom)
                            sb.append("─");
                        else
                            sb.append(" ");
                    }
                }
                else {
                    if(borderColumn) {
                        //is an intersection between the four rooms
                        if( hereRoom || westRoom ) {
                            if(hereRoom && westRoom)
                                sb.append("↔");
                            else
                                sb.append("│");
                        }
                        else
                            sb.append(" ");
                    }
                    else {
                        //a room space
                        sb.append(MapsManager.render(roomId, roomManager).apply(renderRoomId));
                        column += 2;
                        //if unexplored
                        //sb.append("░");
                    }
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public void addRow(boolean startOfArray) {
        ArrayList<Integer> newRow = Lists.newArrayList();
        for (int i = 0; i < matrix.get(0).size(); i++) {
            newRow.add(0);
        }
        if (startOfArray) {
            matrix.add(0, newRow);
        } else {
            matrix.add(newRow);
        }
        setMax(new Coords(matrix.size(), matrix.get(0).size()));
    }

    public void addColumn(boolean startOfArray) {
        Iterator<List<Integer>> rows = getRows();
        while (rows.hasNext()) {
            List<Integer> next = rows.next();
            if (startOfArray) {
                next.add(0, 0);
            } else {
                next.add(0);
            }
        }
        setMax(new Coords(matrix.size(), matrix.get(0).size()));
    }

    public Coords getNorthCords(Coords coords) {
        Coords ret = new Coords(coords.row - 1, coords.column);
        if (ret.getRow() < 0) {
            addRow(true);
            ret = new Coords(0, coords.column);
        }
        return ret;
    }

    public Coords getSouthCoords(Coords coords) {
        Coords ret = new Coords(coords.row + 1, coords.column);
        if (ret.getRow() >= getMaxRow()) {
            addRow(false);
        }
        return ret;
    }

    public Coords getEastCoords(Coords coords) {
        Coords ret = new Coords(coords.row, coords.column + 1);
        if (ret.getColumn() >= getMaxCol()) {
            addColumn(false);
        }
        return ret;
    }

    public Coords getWestCoords(Coords coords) {
        Coords ret = new Coords(coords.row, coords.column - 1);
        if (ret.getColumn() < 0) {
            addColumn(true);
            ret = new Coords(coords.row, 0);
        }
        return ret;
    }
}
