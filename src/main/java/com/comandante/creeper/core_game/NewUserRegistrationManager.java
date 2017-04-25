package com.comandante.creeper.core_game;


import com.comandante.creeper.Main;
import com.comandante.creeper.player.*;
import com.comandante.creeper.server.model.CreeperSession;
import com.comandante.creeper.stats.DefaultStats;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.MessageEvent;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class NewUserRegistrationManager {

    private final PlayerManager playerManager;

    static final int MAX_USERNAME_LENGTH = 22;
    static final int MIN_USERNAME_LENGTH = 3;

    static final CharsetEncoder ASCII_ENCODER = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

    NewUserRegistrationManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void handle(CreeperSession session, MessageEvent e) {
        switch (session.getState()) {
            case newUserPromptedForUsername:
                if (setDesiredUsername(session, e)) {
                    promptForDesirePassword(session, e);
                }
                break;
            case newUserPromptedForPassword:
                createUser(session, e);
                break;
        }
    }

    public void newUserRegistrationFlow(CreeperSession session, MessageEvent e) {
        e.getChannel().write("desired username: ");
        session.setState(CreeperSession.State.newUserPromptedForUsername);
    }

    private boolean setDesiredUsername(CreeperSession session, MessageEvent e) {
        String name = (String) e.getMessage();
        String username = name.replaceAll("[^a-zA-Z0-9]", "");
        PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(Main.createPlayerId(username));
        if (!isValidUsername(username)) {
            e.getChannel().write("Username is in invalid.\r\n");
            return false;
        }
        if (playerMetadata != null) {
            e.getChannel().write("Username is in use.\r\n");
            newUserRegistrationFlow(session, e);
            return false;
        }
        session.setUsername(java.util.Optional.of(username));
        return true;
    }

    private void promptForDesirePassword(CreeperSession session, MessageEvent e) {
        e.getChannel().write("desired password: ");
        session.setState(CreeperSession.State.newUserPromptedForPassword);
    }

    private void createUser(CreeperSession session, MessageEvent e) {
        String password = (String) e.getMessage();
        if (password.length() < 8) {
            e.getChannel().write("Passwords must be at least 8 characters.\r\n");
            newUserRegistrationFlow(session, e);
            return;
        }
        session.setPassword(Optional.of(password));
        PlayerMetadata playerMetadata = new PlayerMetadata(
                session.getUsername().get(),
                session.getPassword().get(),
                Main.createPlayerId(session.getUsername().get()),
                DefaultStats.DEFAULT_PLAYER.createStats(),
                0, Sets.newHashSet(PlayerRole.MORTAL),
                new String[0],
                0,
                new String[0],
                Maps.newHashMap(),
                PlayerClass.BASIC,
                Sets.newConcurrentHashSet(Sets.newHashSet(new CoolDown(CoolDownType.NEWBIE))));
        playerManager.savePlayerMetadata(playerMetadata);
        e.getChannel().write("User created.\r\n");
        session.setState(CreeperSession.State.newUserRegCompleted);
    }

    public static boolean isValidUsername(String desired) {
        if (desired.length() > MAX_USERNAME_LENGTH) {
            return false;
        }
        if (desired.length() < MIN_USERNAME_LENGTH) {
            return false;
        }
        if (!ASCII_ENCODER.canEncode(desired)) {
            return false;
        }
        return true;
    }

}
