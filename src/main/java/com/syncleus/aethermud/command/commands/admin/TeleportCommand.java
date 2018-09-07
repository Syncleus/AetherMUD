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
package com.syncleus.aethermud.command.commands.admin;


import com.syncleus.aethermud.command.commands.Command;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.EffectData;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.world.model.Room;
import com.google.common.collect.Sets;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.*;

public class TeleportCommand extends Command {
    final static List<String> validTriggers = Arrays.asList("teleport", "t");
    final static String description = "Teleport to a roomId or playerId.";
    final static String correctUsage = "teleport <roomId|playerName>";
    final static Set<PlayerRole> roles = Sets.newHashSet(PlayerRole.GOD);


    final static String teleportMessage = "A " + Color.YELLOW + "lightning" + Color.RESET + " bolt descends from the sky and annihilates the earth below." + "\r\n";

    public TeleportCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage, roles);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            if (originalMessageParts.size() <= 1) {
                return;
            }
            if (player.isActiveFights()) {
                write("You can't teleport while in a fight!");
                return;
            }
            if (player.isActive(CoolDownType.DEATH)) {
                write("You are dead and can not move.");
                return;
            }
            try( GraphStorageFactory.AetherMudTx tx = this.gameManager.getGraphStorageFactory().beginTransaction() ) {
                Optional<PlayerData> playerMetadataOptional = tx.getStorage().getPlayerMetadata(playerId);
                if (!playerMetadataOptional.isPresent()) {
                    return;
                }
                PlayerData playerData = playerMetadataOptional.get();
                for (EffectData effect : playerData.getEffects()) {
                    if (effect.isFrozenMovement()) {
                        write("You are frozen and can not move.");
                        return;
                    }
                }
            }
            String desiredId = originalMessageParts.get(1);
            Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
            while (players.hasNext()) {
                Map.Entry<String, Player> next = players.next();
                if (next.getValue().getPlayerName().equals(desiredId)) {
                    Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(next.getValue()).get();
                    Integer destinationRoomId = playerCurrentRoom.getRoomId();
                    PlayerMovement playerMovement = new PlayerMovement(player, gameManager.getRoomManager().getPlayerCurrentRoom(player).get().getRoomId(), playerCurrentRoom.getRoomId(), "vanished into the heavens.", "");
                    gameManager.writeToRoom(destinationRoomId, teleportMessage);
                    channelUtils.write(playerId, teleportMessage);
                    player.movePlayer(playerMovement);
                    gameManager.currentRoomLogic(player.getPlayerId());
                    return;
                }
            }
            Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRoomsIterator();
            while (rooms.hasNext()) {
                Map.Entry<Integer, Room> next = rooms.next();
                if (Integer.toString(next.getKey()).equals(desiredId)) {
                    PlayerMovement playerMovement = new PlayerMovement(player, gameManager.getRoomManager().getPlayerCurrentRoom(player).get().getRoomId(), Integer.parseInt(desiredId), "vanished into the heavens.", "");
                    gameManager.writeToRoom(Integer.parseInt(desiredId), teleportMessage);
                    channelUtils.write(playerId, teleportMessage);
                    player.movePlayer(playerMovement);
                    gameManager.currentRoomLogic(player.getPlayerId());
                    return;
                }
            }
        });
    }
}
