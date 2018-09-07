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

import com.google.common.collect.Sets;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;
import com.syncleus.aethermud.world.model.RoomModelData;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GraphElement
public abstract class FloorModelData extends AbstractInterceptingVertexFrame {
    
    @Adjacency(label = "roomModel", direction = Direction.OUT)
    public abstract RoomModelData addRoomModelData(RoomModelData roomModels);

    @Adjacency(label = "roomModel", direction = Direction.OUT)
    public abstract void removeRoomModelData(RoomModelData stats);

    @Adjacency(label = "roomModel", direction = Direction.OUT)
    public abstract <N extends RoomModelData> Iterator<? extends N> getRoomModelDatasIterator(Class<? extends N> type);

    public Set<RoomModelData> getRoomModelDatas() {
        return Collections.unmodifiableSet(Sets.newHashSet(this.getRoomModelDatasIterator(RoomModelData.class)));
    }

    public void setRoomModelDatas(Set<RoomModelData> roomModels) {
        DataUtils.setAllElements(roomModels, () -> this.getRoomModelDatasIterator(RoomModelData.class), roomModelData -> this.addRoomModelData(roomModelData), () -> {} );
    }

    public RoomModelData createRoomModelData() {
        final RoomModelData roomModel = this.getGraph().addFramedVertex(RoomModelData.class);
        this.addEffectData(roomModel);
        return roomModel;
    }

    @Property("rawMatrixCsv")
    public abstract String getRawMatrixCsv();

    @Property("rawMatrixCsv")
    public abstract void setRawMatrixCsv(String rawMatrixCsv);

    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

    @Property("id")
    public abstract Integer getId();

    @Property("id")
    public abstract void setId(Integer id);
    
    public static void copyFloorModel(FloorModelData dest, FloorModel src) {
        try {
            PropertyUtils.copyProperties(dest, src);

            for(RoomModelData data : dest.getRoomModelDatas())
                data.remove();
            if( src.getRoomModelDatas() != null )
                for(RoomModelData roomModelData : src.getRoomModelDatas())
                    RoomModelData.copyFloorModel(dest.createRoomModelData(), roomModelData);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static FloorModel copyFloorModel(FloorModelData src) {
        FloorModel retVal = new FloorModel();
        try {
            PropertyUtils.copyProperties(retVal, src);

            Set<RoomModel> roomModels = new HashSet<>();
            if( src.getRoomModelDatas() != null )
                for(RoomModelData roomModelData : src.getRoomModelDatas())
                    roomModels.add(RoomModelData.copyRoomModel(roomModelData));
            retVal.setRoomModels(Collections.unmodifiableSet(roomModels));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
