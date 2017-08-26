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
import com.syncleus.aethermud.merchant.MerchantMetadata;
import com.syncleus.aethermud.player.PlayerRole;
import com.google.common.collect.Sets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LoadMerchantCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("loadmerchant");
    final static String description = "Load a Merchant using JSON over http";
    final static String correctUsage = "loadmerchant <http url with json for npc>";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.ADMIN);

    public LoadMerchantCommand(GameManager gameManager) {
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

            MerchantMetadata merchantMetadata = null;
            try {
                merchantMetadata = gameManager.getGson().fromJson(npcJson, MerchantMetadata.class);
            } catch (Exception ex) {
                write("Retrieved JSON file is malformed. " + ex.getLocalizedMessage() + "\r\n");
                return;
            }
            httpGet.reset();

            gameManager.getMerchantStorage().saveMerchantMetadata(merchantMetadata);
            write("Merchant Saved. - " + merchantMetadata.getInternalName() + "\r\n");

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

