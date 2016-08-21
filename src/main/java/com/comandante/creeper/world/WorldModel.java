package com.comandante.creeper.world;

import com.comandante.creeper.world.model.FloorModel;

import java.util.Set;

public class WorldModel {

    Set<FloorModel> floorModelList;

    public Set<FloorModel> getFloorModelList() {
        return floorModelList;
    }

    public void setFloorModelList(Set<FloorModel> floorModelList) {
        this.floorModelList = floorModelList;
    }
}
