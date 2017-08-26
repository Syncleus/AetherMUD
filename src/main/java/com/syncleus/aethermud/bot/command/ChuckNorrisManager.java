/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.syncleus.aethermud.bot.command;

import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.Lists;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class ChuckNorrisManager {

private final AetherMudConfiguration aetherMudConfiguration;
private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
private final JsonFactory JSON_FACTORY = new JacksonFactory();
private final HttpRequestFactory requestFactory;

private static final Logger log = Logger.getLogger(ChuckNorrisManager.class);


        public ChuckNorrisManager(AetherMudConfiguration aetherMudConfiguration) {
            this.aetherMudConfiguration = aetherMudConfiguration;
            this.requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                        @Override
                        public void initialize(HttpRequest request) {
                            request.setParser(new JsonObjectParser(JSON_FACTORY));
                        }
                    });
        }

        public List<String> getRandomChuckNorrisJoke() throws IOException {
            List<String> resp = Lists.newArrayList();
            GenericUrl url = new GenericUrl("http://api.icndb.com/jokes/random");
            HttpRequest httpRequest = requestFactory.buildGetRequest(url);
            GenericJson content = httpRequest.execute().parseAs(GenericJson.class);
            resp.addAll(parseJoke(content));
            return resp;
        }


        private List<String> parseJoke(GenericJson content) {
            List<String> response = Lists.newArrayList();
            try {
                ArrayMap current_observation = (ArrayMap) content.get("value");
                Object joke = current_observation.get("joke");
                response.add(joke.toString());
            } catch (Exception e) {
                log.error("Error obtaining chuck norris joke!", e);
            }
            return response;
        }
}
