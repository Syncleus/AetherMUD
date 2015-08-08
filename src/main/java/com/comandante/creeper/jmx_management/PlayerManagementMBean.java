package com.comandante.creeper.jmx_management;

import java.util.List;

public interface PlayerManagementMBean {

    public void toggleMarkForDelete();

    public boolean isMarkedForDelete();
}
