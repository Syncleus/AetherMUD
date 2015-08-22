package com.comandante.creeper.Items.use;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseAction;
import com.comandante.creeper.Items.ItemUseRegistry;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcStatsChangeBuilder;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Room;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class DirtyBombUseAction implements ItemUseAction {

    private final ItemType itemType;

    public DirtyBombUseAction(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Integer getItemTypeId() {
        return itemType.getItemTypeCode();
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.getRoomId().equals(1)) {
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " tried to detonate a " + item.getItemName() + "!");
            return;
        }
        Set<String> npcIds = currentRoom.getNpcIds();
        Set<Player> presentPlayers = gameManager.getRoomManager().getPresentPlayers(currentRoom);
        for (Player ply: presentPlayers) {
            gameManager.getChannelUtils().write(ply.getPlayerId(), "A " + item.getItemName() + " has gone off somewhere in the world!");
        }
        for (String npcId : npcIds) {
            Npc npc = gameManager.getEntityManager().getNpcEntity(npcId);
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), npc.getColorName() + " is heavily damaged by a " + item.getItemName() + "!" + Color.YELLOW + " +" + NumberFormat.getNumberInstance(Locale.US).format(900000000) + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET);
            NpcStatsChangeBuilder npcStatsChangeBuilder = new NpcStatsChangeBuilder();
            final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + Color.YELLOW + " +" + NumberFormat.getNumberInstance(Locale.US).format(900000000) + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npc.getColorName();
            npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-900000000).createStats());
            npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            npcStatsChangeBuilder.setPlayer(player);
            npcStatsChangeBuilder.setIsItemDamage(true);
            npc.addNpcDamage(npcStatsChangeBuilder.createNpcStatsChange());
        }
        for (Player presentPlayer : presentPlayers) {
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " is heavily damaged by a " + item.getItemName() + "!");
            presentPlayer.updatePlayerHealth(-900000000, null);
        }
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
        ItemUseRegistry.incrementUses(item);
        if (ItemType.itemTypeFromCode(item.getItemTypeId()).isDisposable()) {
            if (item.getNumberOfUses() < ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses()) {
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
}
