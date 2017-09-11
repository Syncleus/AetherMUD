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

import com.google.common.base.Function;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.ferma.ext.orientdb.OrientTransactionFactory;
import com.syncleus.ferma.ext.orientdb.impl.OrientTransactionFactoryImpl;
import com.syncleus.ferma.tx.Tx;
import com.syncleus.ferma.tx.TxFactory;
import org.apache.tinkerpop.gremlin.orientdb.OrientGraphFactory;

import java.util.Optional;
import java.util.function.Consumer;

public class GraphStorageFactory {
    OrientGraphFactory graphFactory;
    OrientTransactionFactory txFactory;

    public GraphStorageFactory(String connectUrl, String username, String password) {
        graphFactory = new OrientGraphFactory(connectUrl, username, password);
        txFactory = new OrientTransactionFactoryImpl(graphFactory, true, "com.syncleus.aethermud.storage.graphdb.model");
        txFactory.setupElementClasses();
        txFactory.addEdgeClass("ItemApplyStats");
    }

    public GraphStorageFactory(String connectUrl) {
        graphFactory = new OrientGraphFactory(connectUrl);
        txFactory = new OrientTransactionFactoryImpl(graphFactory, true, "com.syncleus.aethermud.storage.graphdb.model");
        txFactory.setupElementClasses();
        txFactory.addEdgeClass("ItemApplyStats");
    }

    public GraphStorageFactory() {
        this(false);
    }

    public GraphStorageFactory(boolean onDisk) {
        this(onDisk ? "plocal:./aethermud-graphdb-orientdb" : "memory:tinkerpop");
    }

    public AetherMudTx beginTransaction() {
        return new AetherMudTx(txFactory.tx());
    }

    public void close() {
        if( this.graphFactory != null ) {
            this.graphFactory.close();
            this.graphFactory = null;
            this.txFactory = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }

    public static class AetherMudTx implements AutoCloseable {
        private final Tx tx;
        private final GraphDbAetherMudStorage storage;

        public AetherMudTx(Tx tx) {
            this.tx = tx;
            this.storage = new GraphDbAetherMudStorage(tx.getGraph());
        }

        public void success() {
            tx.success();
        }

        public void failure() {
            tx.failure();
        }

        public void close() {
            tx.close();
        }

        public GraphDbAetherMudStorage getStorage() {
            return storage;
        }
    }
}
