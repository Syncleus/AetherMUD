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
package com.syncleus.aethermud.server.auth;

import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.command.CommandHandler;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.core.SentryManager;
import com.syncleus.aethermud.server.model.AetherMudSession;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

import static com.syncleus.aethermud.server.communication.Color.RESET;

public class AetherMudAuthenticationHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final AetherMudAuthenticator aetherMudAuthenticator;
    private static final Logger log = Logger.getLogger(AetherMudAuthenticationHandler.class);
    private static final String LOGO =
            "          .,;;##########::,.\n" +
            "       .;##''       ,/|  ``##;.\n" +
            "     .;#'         ,/##|__    `#;.\n" +
            "   .;#'          /######/'     `#;.\n" +
            "  ;#'             ,/##/__        `#;\n" +
            " ;#'            ,/######/'        `#;\n" +
            ";#'            /######/'           `#;\n" +
            ";#'             ,/##/___           `#;\n" +
            ";#            ,/#######/'           #;\n" +
            ";#           /#######/'             #;\n" +
            ";#             ,/##/__              #;\n" +
            "`#;          ,/######/'            ;#'\n" +
            "`#;.        /######/'             ,;#'\n" +
            " `#;.        ,/##/__             ,;#'\n" +
            "  `#;.      /######/'           ,;#'\n" +
            "    ##;_      |##/'           _;##\n" +
            "    :#`-;#;...|/'       ...;#;-'#:\n" +
            "    :`__ `-#### __  __ ####-' __':\n" +
            "    :  ``------.. `' ..------''  :\n" +
            "    `.. `--------`..'--------' ..'\n" +
            "      :                        :\n" +
            "      `:..      /:  :\\      ..:'\n" +
            "         `.     ::  ::     .'\n" +
            "          #.              .#\n" +
            "          `'##;##;##;##;##`'\n" +
            "            `' `' `' `' `'";

    public AetherMudAuthenticationHandler(GameManager gameManager) {
        this.gameManager = gameManager;
        this.aetherMudAuthenticator = new GameAuth(gameManager);
    }


    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            System.out.println("Upstream Handling: " + e);
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(LOGO)
                .append(RESET + "\r\n")
                .append("First time here? Type \"tupac\".\r\n")
                .append("username: ");
        e.getChannel().write(stringBuilder.toString());
        AetherMudSession aetherMudSession = new AetherMudSession();
        aetherMudSession.setState(AetherMudSession.State.promptedForUsername);
        ctx.setAttachment(aetherMudSession);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        AetherMudSession aetherMudSession = (AetherMudSession) ctx.getAttachment();
        if (!aetherMudSession.isAuthed()) {
            if (aetherMudSession.state.equals(AetherMudSession.State.newUserPromptedForUsername) || aetherMudSession.state.equals(AetherMudSession.State.newUserPromptedForPassword)) {
                gameManager.getNewUserRegistrationManager().handle(aetherMudSession, e);
                if (!aetherMudSession.state.equals(AetherMudSession.State.newUserRegCompleted)) {
                    return;
                }
            }
            doAuthentication(ctx, e);
            if (aetherMudSession.isAuthed()) {
                gameManager.getPlayerManager().getSessionManager().putSession(aetherMudSession);
                e.getChannel().getPipeline().remove(this);
                e.getChannel().getPipeline().addLast("server_handler", new CommandHandler(gameManager));
                e.getChannel().setAttachment(aetherMudSession);
                gameManager.announceConnect(aetherMudSession.getUsername().get());
                gameManager.currentRoomLogic(Main.createPlayerId(aetherMudSession.getUsername().get()));
                gameManager.getChannelUtils().write(Main.createPlayerId(aetherMudSession.getUsername().get()), "\r\n" + gameManager.buildPrompt(Main.createPlayerId(aetherMudSession.getUsername().get())));
            }
        } else {
            //gameManager.getPlayerManager().getSessionManager().putSession(creeperSession);
            e.getChannel().getPipeline().addLast("server_handler", new CommandHandler(gameManager));
            e.getChannel().getPipeline().remove(this);
            e.getChannel().setAttachment(aetherMudSession);
        }
        super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        log.error("Authentication failure.", e.getCause());
        SentryManager.logSentry(this.getClass(), e.getCause(), "Authentication failure.");
    }


    private void doAuthentication(ChannelHandlerContext ctx, MessageEvent e) {
        String message = (String) e.getMessage();
        AetherMudSession aetherMudSession = (AetherMudSession) ctx.getAttachment();
        if (aetherMudSession.getState().equals(AetherMudSession.State.promptedForUsername)) {
            aetherMudSession.setUsername(java.util.Optional.of(message.replaceAll("[^a-zA-Z0-9]", "")));
            if (aetherMudSession.getUsername().isPresent() && aetherMudSession.getUsername().get().equals("tupac")) {
                gameManager.getNewUserRegistrationManager().newUserRegistrationFlow(aetherMudSession, e);
                return;
            }
            aetherMudSession.setState(AetherMudSession.State.promptedForPassword);
            e.getChannel().write("password: ");
            return;
        }
        if (aetherMudSession.getState().equals(AetherMudSession.State.promptedForPassword)) {
            aetherMudSession.setPassword(Optional.of(message));
        }
        boolean b = aetherMudAuthenticator.authenticateAndRegisterPlayer(aetherMudSession.getUsername().get(), aetherMudSession.getPassword().get(), e.getChannel());
        if (!b) {
            e.getChannel().write("authentication failed.\r\n");
            e.getChannel().write("username: ");
            aetherMudSession.setState(AetherMudSession.State.promptedForUsername);
        } else {
            aetherMudSession.setAuthed(true);
            aetherMudSession.setState(AetherMudSession.State.authed);
            e.getChannel().write("Welcome to the Aether. (version: " + Main.getAetherMudVersion() + ")\r\n");
        }
    }

}
