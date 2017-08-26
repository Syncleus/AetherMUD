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
package com.comandante.creeper.server.telnet;


import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.server.auth.CreeperAuthenticationHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class CreeperServer {

    private final int port;

    public CreeperServer(int port) {
        this.port = port;
    }

    public static void exitServer(String exitMessage) {
        exitServer(exitMessage, 0);
    }

    public static void exitServer(String exitMessage, int code) {
        System.out.println("[SERVER IS SHUTTING DOWN] " + exitMessage);
        System.exit(code);
    }

    public void run(GameManager gameManager) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        CreeperAuthenticationHandler handler = new CreeperAuthenticationHandler(gameManager);
        bootstrap.setPipelineFactory(new CreeperServerPipelineFactory(handler));
        try {
            bootstrap.bind(new InetSocketAddress(port));
        } catch (ChannelException e) {
            exitServer(e.getMessage(), 127);
        }
    }
}
