package com.comandante.creeper.Items.use;

import com.comandante.creeper.Items.*;
import com.comandante.creeper.command.commands.UseCommand;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.stats.Stats;
import org.apache.log4j.Logger;

import java.util.Set;

public class DefaultApplyStatsAction implements ItemUseAction {

    private final Integer itemTypeId;
    private final Stats stats;
    private final Set<Effect> effectSet;
    private static final Logger log = Logger.getLogger(DefaultApplyStatsAction.class);

    public DefaultApplyStatsAction(ItemType itemType, Stats stats, Set<Effect> effects) {
        this.itemTypeId = itemType.getItemTypeCode();
        this.stats = stats;
        this.effectSet = effects;
    }

    @Override
    public Integer getItemTypeId() {
        return itemTypeId;
    }

    @Override
    public void executeAction(GameManager gameManager, Player player, Item item, UseCommand.UseItemOn useItemOn) {
        String playerName = player.getPlayerName();
        ItemType itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), playerName + " uses " + itemType.getItemName() + ".\r\n");
        if (stats.getCurrentMana() > 0) {
            gameManager.getChannelUtils().write(player.getPlayerId(), stats.getCurrentMana() + " mana is restored." + "\r\n");
        }
        if (stats.getCurrentHealth() > 0) {
            gameManager.getChannelUtils().write(player.getPlayerId(), stats.getCurrentHealth() + " health is restored." + "\r\n");
        }
        player.addMana(stats.getCurrentMana());
        player.updatePlayerHealth(stats.getCurrentHealth(), null);
        processEffects(gameManager, player, effectSet);
    }

    @Override
    public void postExecuteAction(GameManager gameManager, Player player, Item item) {
        ItemUseHandler.incrementUses(item);
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
        return effectSet;
    }

    public static void processEffects(GameManager gameManager, Player player, Set<Effect> effects) {
        if (effects == null) {
            return;
        }
        for (Effect effect : effects) {
            Effect nEffect = new Effect(effect);
            nEffect.setPlayerId(player.getPlayerId());
            gameManager.getEntityManager().saveEffect(nEffect);
            boolean effectResult = player.addEffect(nEffect.getEntityId());
            if (effect.getDurationStats() != null) {
                if (effect.getDurationStats().getCurrentHealth() < 0) {
                    log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                    continue;
                }
            }
            if (effectResult) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "You feel " + effect.getEffectName() + "\r\n");
            } else {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Unable to apply effect.\r\n");
            }
        }
    }
}
