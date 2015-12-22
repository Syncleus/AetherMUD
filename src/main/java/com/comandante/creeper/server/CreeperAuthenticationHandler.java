package com.comandante.creeper.server;

import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.managers.SentryManager;
import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

import static com.comandante.creeper.server.Color.RESET;

public class CreeperAuthenticationHandler extends SimpleChannelUpstreamHandler {

    private final GameManager gameManager;
    private final CreeperAuthenticator creeperAuthenticator;
    private static final Logger log = Logger.getLogger(CreeperAuthenticationHandler.class);




    private static final String LOGO = "                   .:::::::::::...                                       \n" +
            "                  .::::::::::::::::::::.                                  \n" +
            "                .::::::::::::::::::::::::.                                \n" +
            "               ::::::::::::::::::::::::::::.                              \n" +
            "              :::::::::::::::::::::::::::::::  .,uuu   ...                \n" +
            "             :::::::::::::::::::::::::::::::: dHHHHHLdHHHHb               \n" +
            "       ....:::::::'`    ::::::::::::::::::' uHHHHHHHHHHHHHF               \n" +
            "   .uHHHHHHHHH'         ::::::::::::::`.  uHHHHHHHHHHHHHP\"                \n" +
            "   HHHHHHHHHHH          `:::::::::::',dHHuHHHHHHHHP\".g@@g                 \n" +
            "  J\"HHHHHHHHHP        4H ::::::::'  u$$$.    \n" +
            "  \".HHHHHHHHP\"     .,uHP :::::' uHHHHHHHHHHP\"\",e$$$$$c                    \n" +
            "   HHHHHHHF'      dHHHHf `````.HHHHHHHHHHP\",d$$$$$$$P%C                   \n" +
            " .dHHHP\"\"         JHHHHbuuuu,JHHHHHHHHP\",d$$$$$$$$$e=,z$$$$$$$$ee..       \n" +
            " \"\"              .HHHHHHHHHHHHHHHHHP\",gdP\"  ..3$$$Jd$$$$$$$$$$$$$$e.      \n" +
            "                 dHHHHHHHHHHHHHHP\".edP    \" .zd$$$$$$$$$$$\"3$$$$$$$$c     \n" +
            "                 `???\"\"??HHHHP\",e$$F\" .d$,?$$$$$$$$$$$$$F d$$$$$$$$F\"     \n" +
            "                       ?be.eze$$$$$\".d$$$$ $$$E$$$$P\".,ede`?$$$$$$$$      \n" +
            "                      4.\"?$$$$$$$  z$$$$$$ $$$$r.,.e ?$$$$ $$$$$$$$$      \n" +
            "                      '$c  \"$$$$ .d$$$$$$$ 3$$$.$$$$ 4$$$ d$$$$P\"`,,      \n" +
            "                       \"\"\"- \"$$\".`$$\"    \" $$f,d$$P\".$$P zeee.zd$$$$$.    \n" +
            "                     ze.    .C$C\"=^\"    ..$$$$$$P\".$$$'e$$$$$P?$$$$$$     \n" +
            "                 .e$$$$$$$\"=\"$f\",c,3eee$$$$$$$$P $$$P'd$$$$\"..::..\"?$%    \n" +
            "                4d$$$P d$$$dF.d$$$$$$$$$$$$$$$$f $$$ d$$$\" :::::::::.     \n" +
            "               $$$$$$ d$$$$$ $$$$$$$$$$$$$$$$$$ J$$\",$$$'.::::::::::::    \n" +
            "              \"$$$$$$ ?$$$$ d$$$$$$$$$$$$$$$P\".dP'e$$$$':::::::::::::::   \n" +
            "              4$$$$$$c $$$$b`$$$$$$$$$$$P\"\",e$$\",$$$$$' ::::::::::::::::  \n" +
            "              ' ?\"?$$$b.\"$$$$.?$$$$$$P\".e$$$$F,d$$$$$F :::::::::::::::::: \n" +
            "                    \"?$$bc.\"$b.$$$$F z$$P?$$\",$$$$$$$ :::::::::::::::::::: \n" +
            "                        `\"$$c\"?$$$\".$$$)e$$F,$$$$$$$' :::::::::::::::::::: \n" +
            "                        ':. \"$b...d$$P4$$$\",$$$$$$$\" ::::::::::::::::::::: \n" +
            "                        ':::: \"$$$$$\".,\"\".d$$$$$$$F :::::::::::::::::::::: \n" +
            "                         :::: be.\"\".d$$$4$$$$$$$$F ::::::::::::::::::::::: \n" +
            "                          :::: \"??$$$$$$$$$$?$P\" ::::::::::::::::::::::::: \n" +
            "                           :::::: ?$$$$$$$$f .:::::::::::::::::::::::::::: \n" +
            "                            :::::::`\"????\"\".:::::::::::::::::::::::::::::: \n";

    public CreeperAuthenticationHandler(GameManager gameManager) {
        this.gameManager = gameManager;
        this.creeperAuthenticator = new GameAuth(gameManager);
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
        CreeperSession creeperSession = new CreeperSession();
        creeperSession.setState(CreeperSession.State.promptedForUsername);
        ctx.setAttachment(creeperSession);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        CreeperSession creeperSession = (CreeperSession) ctx.getAttachment();
        if (!creeperSession.isAuthed()) {
            if (creeperSession.state.equals(CreeperSession.State.newUserPromptedForUsername) || creeperSession.state.equals(CreeperSession.State.newUserPromptedForPassword)) {
                gameManager.getNewUserRegistrationManager().handle(creeperSession, e);
                if (!creeperSession.state.equals(CreeperSession.State.newUserRegCompleted)) {
                    return;
                }
            }
            doAuthentication(ctx, e);
            if (creeperSession.isAuthed()) {
                gameManager.getPlayerManager().getSessionManager().putSession(creeperSession);
                e.getChannel().getPipeline().remove(this);
                e.getChannel().getPipeline().addLast("server_handler", new CreeperCommandHandler(gameManager));
                e.getChannel().setAttachment(creeperSession);
                gameManager.announceConnect(creeperSession.getUsername().get());
                gameManager.currentRoomLogic(Main.createPlayerId(creeperSession.getUsername().get()));
                gameManager.getChannelUtils().write(Main.createPlayerId(creeperSession.getUsername().get()), "\r\n" + gameManager.buildPrompt(Main.createPlayerId(creeperSession.getUsername().get())));
            }
        } else {
            //gameManager.getPlayerManager().getSessionManager().putSession(creeperSession);
            e.getChannel().getPipeline().addLast("server_handler", new CreeperCommandHandler(gameManager));
            e.getChannel().getPipeline().remove(this);
            e.getChannel().setAttachment(creeperSession);
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
        CreeperSession creeperSession = (CreeperSession) ctx.getAttachment();
        if (creeperSession.getState().equals(CreeperSession.State.promptedForUsername)) {
            creeperSession.setUsername(Optional.of(message.replaceAll("[^a-zA-Z0-9]", "")));
            if (creeperSession.getUsername().isPresent() && creeperSession.getUsername().get().equals("tupac")) {
                gameManager.getNewUserRegistrationManager().newUserRegistrationFlow(creeperSession, e);
                return;
            }
            creeperSession.setState(CreeperSession.State.promptedForPassword);
            e.getChannel().write("password: ");
            return;
        }
        if (creeperSession.getState().equals(CreeperSession.State.promptedForPassword)) {
            creeperSession.setPassword(Optional.of(message));
        }
        boolean b = creeperAuthenticator.authenticateAndRegisterPlayer(creeperSession.getUsername().get(), creeperSession.getPassword().get(), e.getChannel());
        if (!b) {
            e.getChannel().write("authentication failed.\r\n");
            e.getChannel().write("username: ");
            creeperSession.setState(CreeperSession.State.promptedForUsername);
        } else {
            creeperSession.setAuthed(true);
            creeperSession.setState(CreeperSession.State.authed);
            e.getChannel().write("Welcome to creeper. (version: " + Main.getCreeperVersion() + ")\r\n");
        }
    }

}
