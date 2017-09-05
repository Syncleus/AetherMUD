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

import com.google.common.util.concurrent.AbstractIdleService;
import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.ItemPojo;
import com.syncleus.aethermud.npc.NpcBuilder;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.ferma.WrappedFramedGraph;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

//TODO : multiple instances of this class could create conflicts in the DB
public class GraphDbAetherMudStorage extends AbstractIdleService implements AetherMudStorage {
    private static final int MAX_BACKUPS = 5;
    private static final Logger LOGGER = Logger.getLogger(GraphDbAetherMudStorage.class);
    private final WrappedFramedGraph<Graph> framedGraph;
    private final String graphDbFile;


    public GraphDbAetherMudStorage(WrappedFramedGraph<Graph> framedGraph, String graphDbFile) {
        this.framedGraph = framedGraph;
        this.graphDbFile = graphDbFile;
    }

    @Override
    public PlayerData newPlayerData(){
        return framedGraph.addFramedVertex(PlayerData.class);
    }

    @Override
    public Optional<PlayerData> getPlayerMetadata(String playerId) {
        final PlayerData data = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V().has("playerId", playerId), PlayerData.class)).nextOrDefault(PlayerData.class, null);
        return Optional.ofNullable(data);
    }

    @Override
    public Map<String, PlayerData> getAllPlayerMetadata() {
        final List<? extends PlayerData> datas = framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), PlayerData.class)).toList(PlayerData.class);
        final Map<String, PlayerData> retVal = new HashMap<>(datas.size());
        for( PlayerData data : datas ) {
            retVal.put(data.getPlayerId(), data);
        }
        return retVal;
    }

    @Override
    public Optional<ItemData> getItemEntity(String itemId) {
        return Optional.ofNullable(framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), ItemData.class).has("ItemId", itemId)).nextOrDefault(ItemData.class, null));
    }

    @Override
    public void removeItem(String itemId) {
        this.getItemEntity(itemId).ifPresent((i) -> i.remove());

    }

    public synchronized void persist() {
        try {
            Path defaultFilePath = Paths.get(this.graphDbFile);
            if( Files.exists(defaultFilePath) )
                Files.move(defaultFilePath, Paths.get(this.graphDbFile + new Date().getTime()), REPLACE_EXISTING);
        } catch (IOException e) {
            //do nothing, it probably doesnt exist
        }

        //remove all but the last 20 backedup files
        NavigableSet<File> orderedFiles = new TreeSet<>(new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                if( file.lastModified() > t1.lastModified() )
                    return 1;
                else if( file.lastModified() < t1.lastModified() )
                    return -1;
                else
                    return 0;
            }
        });
        File folder = new File(".");
        orderedFiles.addAll(Arrays.asList(folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().contains(graphDbFile);
            }
        })));
        while(orderedFiles.size() > MAX_BACKUPS) {
            File currentFile = orderedFiles.first();
            orderedFiles.remove(currentFile);
            currentFile.delete();
        }

        try {
            this.framedGraph.getBaseGraph().io(IoCore.graphson()).writeGraph(this.graphDbFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not write to graph file.", e);
        }
    }

    @Override
    public ItemData saveItem(ItemPojo item) {
        ItemData itemData = framedGraph.addFramedVertex(ItemData.class);
        try {
            PropertyUtils.copyProperties(itemData, item);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("Could not copy beans", e);
        }
        itemData.setItemTriggers(item.getItemTriggers());
        return itemData;
    }

    public List<? extends NpcSpawn> getAllNpcs(GameManager gameManager) {
        List<? extends NpcData> npcData = this.getNpcDatas();
        return npcData.stream()
            .map(metadata -> new NpcBuilder(metadata).setGameManager(gameManager).createNpc())
            .collect(Collectors.toList());
    }

    public List<? extends NpcData> getNpcDatas() {
        return framedGraph.traverse((g) -> framedGraph.getTypeResolver().hasType(g.V(), NpcData.class)).toList(NpcData.class);
    }

    public NpcData newNpcData() {
        return framedGraph.addFramedVertex(NpcData.class);
    }

    @Override
    protected void startUp() {
    }

    @Override
    protected void shutDown() {
    }
}
