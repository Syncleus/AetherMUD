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
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.server.model.CreeperSession;
import com.syncleus.aethermud.stats.DefaultStats;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.graphdb.CoolDownData;
import com.syncleus.aethermud.storage.graphdb.PlayerData;
import com.syncleus.aethermud.storage.graphdb.StatsData;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.MessageEvent;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

public class NewUserRegistrationManager {

    private final GameManager gameManager;

    static final int MAX_USERNAME_LENGTH = 22;
    static final int MIN_USERNAME_LENGTH = 3;

    static final CharsetEncoder ASCII_ENCODER = Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

    private static final Logger log = Logger.getLogger(NewUserRegistrationManager.class);

    NewUserRegistrationManager(GameManager gameManager) {
        this.gameManager = gameManager;
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
        java.util.Optional<PlayerData> playerMetadataOptional = gameManager.getPlayerManager().getPlayerMetadata(Main.createPlayerId(username));
        if (!isValidUsername(username)) {
            e.getChannel().write("Username is in invalid.\r\n");
            return false;
        }
        if (playerMetadataOptional.isPresent()) {
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

    private void createUser(CreeperSession session, MessageEvent messageEvent) {
        String password = (String) messageEvent.getMessage();
        if (password.length() < 8) {
            messageEvent.getChannel().write("Passwords must be at least 8 characters.\r\n");
            newUserRegistrationFlow(session, messageEvent);
            return;
        }
        session.setPassword(Optional.of(password));

        PlayerData playerData = gameManager.getPlayerManager().newPlayerData();
        playerData.setNpcKillLog(new HashMap<>());
        playerData.setEffects(new ArrayList<>());
        playerData.setGold(0);
        playerData.setGoldInBank(0);
        playerData.setInventory(new ArrayList<>());
        playerData.setLearnedSpells(new ArrayList<>());
        playerData.setLockerInventory(new ArrayList<>());
        playerData.setIsMarkedForDelete(false);
        playerData.setPlayerName(session.getUsername().get());
        playerData.setPassword(session.getPassword().get());
        playerData.setPlayerClass(PlayerClass.BASIC);
        playerData.setPlayerEquipment(new ArrayList<>());
        playerData.setPlayerId(Main.createPlayerId(session.getUsername().get()));
        // TODO : remove this, not all players should be admins
        playerData.setPlayerRoleSet(Sets.newHashSet(PlayerRole.MORTAL, PlayerRole.ADMIN, PlayerRole.GOD, PlayerRole.TELEPORTER));
        playerData.setPlayerSettings(new HashMap<>());
        try {
            PropertyUtils.copyProperties(playerData.createStats(), DefaultStats.DEFAULT_PLAYER.createStats());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException("Could not copy properties for stats", e);
        }
        playerData.createCoolDown(CoolDownType.NEWBIE);
        gameManager.getPlayerManager().persist();

        messageEvent.getChannel().write("User created.\r\n");
        log.info("User " + playerData.getPlayerName() + " created.");
        session.setState(CreeperSession.State.newUserRegCompleted);
        try {
            PlayerManagementManager.registerPlayer(playerData.getPlayerName(), playerData.getPlayerId(), gameManager);
        } catch (Exception e) {
            log.error("Problem registering new player in the MBean server!");
        }
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
