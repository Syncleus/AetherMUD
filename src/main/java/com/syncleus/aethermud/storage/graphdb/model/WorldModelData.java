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
import com.syncleus.aethermud.world.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GraphElement
public abstract class WorldModelData extends AbstractInterceptingVertexFrame {
    @Adjacency(label = "floorModel", direction = Direction.OUT)
    public abstract FloorModelData addFloorModelData(FloorModelData floorModels);

    @Adjacency(label = "floorModel", direction = Direction.OUT)
    public abstract void removeFloorModelData(FloorModelData stats);

    @Adjacency(label = "floorModel", direction = Direction.OUT)
    public abstract <N extends FloorModelData> Iterator<? extends N> getFloorModelDatasIterator(Class<? extends N> type);

    public Set<FloorModelData> getFloorModelDatas() {
        return Collections.unmodifiableSet(Sets.newHashSet(this.getFloorModelDatasIterator(FloorModelData.class)));
    }

    public void setFloorModelDatas(Set<FloorModelData> floorModels) {
        DataUtils.setAllElements(floorModels, () -> this.getFloorModelDatasIterator(FloorModelData.class), floorModelData -> this.addFloorModelData(floorModelData), () -> {} );
    }

    public FloorModelData createFloorModelData() {
        final FloorModelData floorModel = this.getGraph().addFramedVertex(FloorModelData.class);
        this.addFloorModelData(floorModel);
        return floorModel;
    }

    public static void copyWorldModel(WorldModelData dest, WorldModel src) {
        try {
            PropertyUtils.copyProperties(dest, src);

            for(FloorModelData data : dest.getFloorModelDatas())
                data.remove();
            if( src.getFloorModelList() != null )
                for(FloorModel floorModel : src.getFloorModelList())
                    FloorModelData.copyFloorModel(dest.createFloorModelData(), floorModel);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static WorldModel copyWorldModel(WorldModelData src) {
        WorldModel retVal = new WorldModel();
        try {
            PropertyUtils.copyProperties(retVal, src);

            Set<FloorModel> floorModels = new HashSet<>();
            if( src.getFloorModelDatas() != null )
                for(FloorModelData floorModelData : src.getFloorModelDatas())
                    floorModels.add(FloorModelData.copyFloorModel(floorModelData));
            retVal.setFloorModelList(Collections.unmodifiableSet(floorModels));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
        return retVal;
    }
}
