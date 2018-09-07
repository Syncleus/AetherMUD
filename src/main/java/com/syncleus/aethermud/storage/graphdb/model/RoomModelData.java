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
package com.syncleus.aethermud.storage.graphdb.model;

import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import com.syncleus.aethermud.world.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;


@GraphElement
public abstract class RoomModelData extends AbstractInterceptingVertexFrame {

    @Property("areaNames")
    public abstract Set<String> getAreaNames();

    @Property("areaNames")
    public abstract void setAreaNames(Set<String> areaNames);

    @Property("floorId")
    public abstract int getFloorId();

    @Property("floorId")
    public abstract void setFloorId(int floorId);

    @Property("roomTags")
    public abstract Set<String> getRoomTags();

    @Property("roomTags")
    public abstract void setRoomTags(Set<String> roomTags);

    @Property("roomId")
    public abstract int getRoomId();

    @Property("roomId")
    public abstract void setRoomId(int roomId);

    @Property("roomDescription")
    public abstract String getRoomDescription();

    @Property("roomDescription")
    public abstract void setRoomDescription(String roomDescription);

    @Property("roomTitle")
    public abstract String getRoomTitle();

    @Property("roomTitle")
    public abstract void setRoomTitle(String roomTitle);

    @Property("enterExitNames")
    public abstract Map<String, String> getEnterExitNames();

    @Property("enterExitNames")
    public abstract void setEnterExitNames(Map<String, String> enterExitNames);

    @Property("notables")
    public abstract Map<String, String> getNotables();
    
    @Property("notables")
    public abstract void setNotables(Map<String, String> notables);
    
        
    public static void copyRoomModel(RoomModelData dest, RoomModel src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static RoomModel copyRoomModel(RoomModelData src) {
        RoomModel retVal = new RoomModel();
        try {
            PropertyUtils.copyProperties(retVal, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
