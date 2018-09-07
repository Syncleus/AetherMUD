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
package com.syncleus.aethermud.items.use;

import com.syncleus.aethermud.command.commands.UseCommand;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.*;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcStatsChangeBuilder;
import com.syncleus.aethermud.player.Player;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.stats.StatsBuilder;
import com.syncleus.aethermud.world.model.Room;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class DirtyBombUseAction implements ItemUseAction {

    private final Item item;

    public DirtyBombUseAction(Item item) {
        this.item = item;
    }

    @Override
    public String getInternalItemName() {
        return item.getInternalItemName();
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, ItemInstance item, UseCommand.UseItemOn useItemOn) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.getRoomId().equals(1)) {
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " tried to detonate a " + item.getItemName() + "!");
            return;
        }
        Set<String> npcIds = currentRoom.getNpcIds();
        for (String npcId : npcIds) {
            NpcSpawn npcSpawn = gameManager.getEntityManager().getNpcEntity(npcId);
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), npcSpawn.getColorName() + " is heavily damaged by a " + item.getItemName() + "!" + Color.YELLOW + " +" + NumberFormat.getNumberInstance(Locale.US).format(900000000) + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET);
            NpcStatsChangeBuilder npcStatsChangeBuilder = new NpcStatsChangeBuilder();
            final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + Color.YELLOW + " +" + NumberFormat.getNumberInstance(Locale.US).format(900000000) + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npcSpawn.getColorName();
            npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-900000000).createStats());
            npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            npcStatsChangeBuilder.setPlayer(player);
            npcStatsChangeBuilder.setIsItemDamage(true);
            npcSpawn.addNpcDamage(npcStatsChangeBuilder.createNpcStatsChange());
        }
        Set<Player> presentPlayers = currentRoom.getPresentPlayers();
        for (Player presentPlayer : presentPlayers) {
            if (allRadSuit(presentPlayer)) {
                gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), presentPlayer.getPlayerName() + " is immune to " + item.getItemName() + "!");
                continue;
            }
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), presentPlayer.getPlayerName() + " is heavily damaged by a " + item.getItemName() + "!");
            presentPlayer.updatePlayerHealth(-Integer.MAX_VALUE, null);
        }
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, ItemInstance item) {
        ItemUseHandler.incrementUses(item);
        if (item.isDisposable()) {
            if (item.getNumberOfUses() < item.getMaxUses()) {
                gameManager.getEntityManager().saveItem(item);
            } else {
                player.removeInventoryId(item.getItemId());
                gameManager.getEntityManager().removeItem(item);
            }
        }
    }

    @Override
    public Set<Effect> getEffects() {
        return null;
    }

    private boolean allRadSuit(Player player) {
 /*       if (player.getSlotItem(EquipmentSlotType.CHEST) == null || !Objects.equals(player.getSlotItem(EquipmentSlotType.CHEST).getItemTypeId(), ItemType.RADSUIT_CHESTPLATE.getItemTypeCode())) {
            return false;
        }
        if (player.getSlotItem(EquipmentSlotType.LEGS)  == null || !Objects.equals(player.getSlotItem(EquipmentSlotType.LEGS).getItemTypeId(), ItemType.RADSUIT_LEGGINGS.getItemTypeCode())) {
            return false;
        }
        if (player.getSlotItem(EquipmentSlotType.HEAD)  == null || !Objects.equals(player.getSlotItem(EquipmentSlotType.HEAD).getItemTypeId(), ItemType.RADSUIT_HELMET.getItemTypeCode())) {
            return false;
        }
        if (player.getSlotItem(EquipmentSlotType.WRISTS)  == null || !Objects.equals(player.getSlotItem(EquipmentSlotType.WRISTS).getItemTypeId(), ItemType.RADSUIT_BRACERS.getItemTypeCode())) {
            return false;
        }
        if (player.getSlotItem(EquipmentSlotType.FEET) == null || !Objects.equals(player.getSlotItem(EquipmentSlotType.FEET).getItemTypeId(), ItemType.RADSUIT_BOOTS.getItemTypeCode())) {
            return false;
        }*/
        return true;

    }
}
