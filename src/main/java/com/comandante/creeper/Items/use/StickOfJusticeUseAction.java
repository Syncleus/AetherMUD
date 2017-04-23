package com.comandante.creeper.Items.use;

import com.comandante.creeper.Items.Effect;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.ItemUseAction;
import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.CoolDown;
import com.comandante.creeper.player.CoolDownType;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.player.PlayerMovement;
import com.comandante.creeper.server.player_communication.Color;

import java.util.Optional;
import java.util.Set;

public class StickOfJusticeUseAction implements ItemUseAction {

    private final ItemType itemType;

    public StickOfJusticeUseAction(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public Integer getItemTypeId() {
        return itemType.getItemTypeCode();
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item, UseCommand.UseItemOn useItemOn) {

        if (!useItemOn.getTarget().isPresent()) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You must use the Stick Of Justice on someone who deserves it.");
            return;
        }

        Optional<Player> playerByCommandTarget = gameManager.getPlayerManager().getPlayerByCommandTarget(player.getCurrentRoom(), useItemOn.getTarget().get());
        if (!playerByCommandTarget.isPresent()) {
            gameManager.getChannelUtils().write(player.getPlayerId(), "You must use the Stick Of Justice on someone who deserves it.");
            return;
        }

        Player targetedPlayer = playerByCommandTarget.get();

        // Source room Id is null, meaning it will disable things like the "Back" command.  Since its null you need to
        // remove the player from the current room manually.
        PlayerMovement playerMovement = new PlayerMovement(targetedPlayer,
                null,
                gameManager.getDetainmentRoom().getRoomId(),
                "has been placed under arrest.",
                null);

        targetedPlayer.removePlayerFromRoom(player.getCurrentRoom());
        targetedPlayer.movePlayer(playerMovement);
        gameManager.getPlayerManager().getAllPlayersMap().forEach((s, destinationPlayer) -> {
            gameManager.getChannelUtils().write(destinationPlayer.getPlayerId(), targetedPlayer.getPlayerName() + " has been " + Color.BOLD_ON + Color.RED + "DETAINED" + Color.RESET + "!" + "\r\n", true);
        });
        targetedPlayer.addCoolDown(new CoolDown(CoolDownType.DETAINMENT));
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
    }

    @Override
    public Set<Effect> getEffects() {
        return null;
    }
}
