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
package com.syncleus.aethermud.command.commands.admin;

import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.NpcData;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class LoadNpcCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("loadnpc");
    final static String description = "Load a NPC using JSON over http";
    final static String correctUsage = "loadnpc <http url with json for npc>";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public LoadNpcCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {

            if (originalMessageParts.size() <= 1) {
                write("Please specify a http url." + "\r\n");
                return;
            }

            originalMessageParts.remove(0);


            String npcJsonHttpUrl = originalMessageParts.get(0);
            if (!isValidURL(npcJsonHttpUrl)) {
                write("Invalid HTTP address." + "\r\n");
                return;
            }

            HttpGet httpGet = new HttpGet(npcJsonHttpUrl);

            HttpClient httpclient = gameManager.getHttpclient();

            HttpResponse httpResponse = httpclient.execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();

            if (entity == null) {
                write("Error retrieving JSON url." + "\r\n");
                return;
            }

            String npcJson = EntityUtils.toString(entity);

            Npc npc = null;
            try {
                npc = gameManager.getGson().fromJson(npcJson, Npc.class);
            } catch (Exception ex) {
                write("Retrieved JSON file is malformed. " + ex.getLocalizedMessage() + "\r\n");
                return;
            }
            httpGet.reset();

            try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
                AetherMudStorage storage = tx.getStorage();
                NpcData npcData = storage.newNpcData();
                try {
                    PropertyUtils.copyProperties(npcData, npc);
                    PropertyUtils.copyProperties(npcData.createStats(), npc.getStats());
                    PropertyUtils.copyProperties(npcData.createLootData(), npc.getLoot());
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                    throw new IllegalStateException("Could not copy properties for stats", ex);
                }
                npc.getSpawnRules().forEach(new Consumer<SpawnRule>() {
                    @Override
                    public void accept(SpawnRule spawnRule) {
                        try {
                            PropertyUtils.copyProperties(npcData.createSpawnRuleData(), spawnRule);
                        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                            throw new IllegalStateException("Could not copy properties for stats", ex);
                        }
                    }
                });
                tx.success();
                write("NPC Saved. - " + npc.getName() + "\r\n");
            }

        });
    }

    public boolean isValidURL(String url) {
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

}



