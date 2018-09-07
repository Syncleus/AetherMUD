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
package com.syncleus.aethermud.storage;

import java.util.List;

public class GraphInfo {
    private final int numberOfItems;
    private final int numberOfItemInstances;
    private final List<String> itemInternalNames;
    private final int numberOfNodes;

    public GraphInfo(int numberOfItems, int numberOfItemInstances, List<String> itemInternalNames, int numberOfNodes) {
        this.numberOfItems = numberOfItems;
        this.numberOfItemInstances = numberOfItemInstances;
        this.itemInternalNames = itemInternalNames;
        this.numberOfNodes = numberOfNodes;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public int getNumberOfItemInstances() {
        return numberOfItemInstances;
    }

    public List<String> getItemInternalNames() {
        return itemInternalNames;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }
}
