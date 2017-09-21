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
package com.syncleus.aethermud.storage.graphdb;

import com.google.common.collect.Sets;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.storage.graphdb.model.*;
import com.syncleus.ferma.DelegatingFramedGraph;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.structure.Transaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class GraphStorageFactory {

    private static final Set<? extends Class<?>> MODEL_CLASSES = Sets.newHashSet(Arrays.asList(
        AetherMudMessageData.class,
        CoolDownData.class,
        EffectData.class,
        EquipmentData.class,
        ItemData.class,
        ItemInstance.class,
        LootData.class,
        NpcData.class,
        PlayerData.class,
        SpawnRuleData.class,
        StatData.class
    ));

    private static final String PROPS_PATH = "titan-cassandra-es.properties";

    private TitanGraph titanGraph;

    public GraphStorageFactory(String connectUrl, String username, String password) {
        try {
            Configuration conf = new PropertiesConfiguration(PROPS_PATH);
            titanGraph = TitanFactory.open(conf);
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public GraphStorageFactory(String connectUrl) {
        this(connectUrl, null, null);
    }

    public GraphStorageFactory() {
        this(false);
    }

    public GraphStorageFactory(boolean onDisk) {
        this(null, null, null);
    }

    public AetherMudTx beginTransaction() {
        return new AetherMudTx(this.titanGraph);
    }

    public void close() {
        if( this.titanGraph != null ) {
            this.titanGraph.close();
            this.titanGraph = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }

    public static class AetherMudTx implements AutoCloseable {
        private final GraphDbAetherMudStorage storage;
        private final Transaction tx;

        public AetherMudTx(final TitanGraph titanGraph) {
            this.tx = titanGraph.tx();
            this.storage = new GraphDbAetherMudStorage(new DelegatingFramedGraph<TitanGraph>(this.tx.createThreadedTx(), true, MODEL_CLASSES));
        }

        public void success() {
            tx.commit();
        }

        public void failure() {
            tx.rollback();
        }

        public void close() {
            tx.close();
        }

        public GraphDbAetherMudStorage getStorage() {
            return storage;
        }
    }
}
