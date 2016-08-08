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
    private static final String LOGO = "                      __gggrgM**M#mggg__\n" +
            "                 __wgNN@\"B*P\"\"mp\"\"@d#\"@N#Nw__\n" +
            "               _g#@0F_a*F#  _*F9m_ ,F9*__9NG#g_\n" +
            "            _mN#F  aM\"    #p\"    !q@    9NL \"9#Qu_\n" +
            "           g#MF _pP\"L  _g@\"9L_  _g\"\"#__  g\"9w_ 0N#p\n" +
            "         _0F jL*\"   7_wF     #_gF     9gjF   \"bJ  9h_\n" +
            "        j#  gAF    _@NL     _g@#_      J@u_    2#_  #_\n" +
            "       ,FF_#\" 9_ _#\"  \"b_  g@   \"hg  _#\"  !q_ jF \"*_09_\n" +
            "       F N\"    #p\"      Ng@       `#g\"      \"w@    \"# t\n" +
            "      j p#    g\"9_     g@\"9_      gP\"#_     gF\"q    Pb L\n" +
            "      0J  k _@   9g_ j#\"   \"b_  j#\"   \"b_ _d\"   q_ g  ##\n" +
            "      #F  `NF     \"#g\"       \"Md\"       5N#      9W\"  j#\n" +
            "      #k  jFb_    g@\"q_     _*\"9m_     _*\"R_    _#Np  J#\n" +
            "      tApjF  9g  J\"   9M_ _m\"    9%_ _*\"   \"#  gF  9_jNF\n" +
            "       k`N    \"q#       9g@        #gF       ##\"    #\"j\n" +
            "       `_0q_   #\"q_    _&\"9p_    _g\"`L_    _*\"#   jAF,'\n" +
            "        9# \"b_j   \"b_ g\"    *g _gF    9_ g#\"  \"L_*\"qNF\n" +
            "         \"b_ \"#_    \"NL      _B#      _I@     j#\" _#\"\n" +
            "           NM_0\"*g_ j\"\"9u_  gP  q_  _w@ ]_ _g*\"F_g@\n" +
            "            \"NNh_ !w#_   9#g\"    \"m*\"   _#*\" _dN@\"\n" +
            "               9##g_0@q__ #\"4_  j*\"k __*NF_g#@P\"\n" +
            "                 \"9NN#gIPNL_ \"b@\" _2M\"Lg#N@F\"\n" +
            "                     \"\"P@*NN#gEZgNN@#@P\"\"\n";

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
            creeperSession.setUsername(java.util.Optional.of(message.replaceAll("[^a-zA-Z0-9]", "")));
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
