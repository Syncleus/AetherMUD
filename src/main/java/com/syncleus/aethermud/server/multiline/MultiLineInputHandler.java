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
package com.syncleus.aethermud.server.multiline;

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.server.model.CreeperSession;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.UUID;

public class MultiLineInputHandler extends SimpleChannelUpstreamHandler {
    private final GameManager gameManager;

    public MultiLineInputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        try {
            CreeperSession creeperSession = (CreeperSession) e.getChannel().getAttachment();
            String message = (String) e.getMessage();
            if (message.equalsIgnoreCase("done")) {
                e.getChannel().getPipeline().addLast(UUID.randomUUID().toString(), creeperSession.getGrabMultiLineInput().get().getValue());
                return;
            }
            gameManager.getMultiLineInputManager().addToMultiLine(creeperSession.getGrabMultiLineInput().get().getKey(), message + "\r\n");
        } finally {
            e.getChannel().getPipeline().remove(ctx.getHandler());
            super.messageReceived(ctx, e);
        }
    }
}
