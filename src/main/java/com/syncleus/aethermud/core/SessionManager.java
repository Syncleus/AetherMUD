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
package com.syncleus.aethermud.core;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.server.model.CreeperSession;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private final ConcurrentHashMap<String, CreeperSession> sessionMap = new ConcurrentHashMap<>();

    public void putSession(CreeperSession creeperSession) {
        sessionMap.put(Main.createPlayerId(creeperSession.getUsername().get()), creeperSession);
    }

    public CreeperSession getSession(String playerId) {
        return sessionMap.get(playerId);
    }
}
