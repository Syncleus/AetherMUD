package com.comandante.creeper.Items.use;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseAction;
import com.comandante.creeper.Items.ItemUseRegistry;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.world.Room;

import java.util.Set;

import static com.comandante.creeper.server.Color.*;
import static com.comandante.creeper.server.Color.RESET;

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
        Set<String> npcIds = currentRoom.getNpcIds();
        Set<Player> presentPlayers = gameManager.getRoomManager().getPresentPlayers(currentRoom);
        for (Player ply: presentPlayers) {
            gameManager.getChannelUtils().write(ply.getPlayerId(), "A " + item.getItemName() + " has gone off somewhere in the world!");
        }
        for (String npcId : npcIds) {
            Npc npc = gameManager.getEntityManager().getNpcEntity(npcId);
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), npc.getColorName() + " is heavily damaged by a " + item.getItemName() + "!");
            npc.addDamageToMap(player.getPlayerId(), -30000000);
        }
        for (Player presentPlayer : presentPlayers) {
            gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), player.getPlayerName() + " is heavily damaged by a " + item.getItemName() + "!");
            presentPlayer.updatePlayerHealth(-30000000, null);
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
