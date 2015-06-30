package com.comandante.creeper.Items;


import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.spells.Effect;
import org.apache.log4j.Logger;

import java.util.Set;

public class ItemUseHandler {

    private final Item item;
    private final CreeperSession creeperSession;
    private final GameManager gameManager;
    private final Player player;
    private final ItemType itemType;

    private static final Logger log = Logger.getLogger(ItemUseHandler.class);


    public ItemUseHandler(Item item, CreeperSession creeperSession, GameManager gameManager, Player player) {
        this.item = item;
        this.creeperSession = creeperSession;
        this.gameManager = gameManager;
        this.player = player;
        this.itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
    }

    private void processEffects() {
        Set<Effect> effects = item.getEffects();
        if (effects == null) {
            return;
        }
        for (Effect effect: effects) {
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
                writeToPlayer("You feel " + effect.getEffectName() + "\r\n");
            } else {
                writeToPlayer("Unable to apply effect.\r\n");
            }
        }
    }

    private void processKey() {
        //If no doors
        writeToPlayer("There's no doors here to use this key on.");
    }

    private void processPurpleDrank() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " sips some " + Color.MAGENTA + "purple" + Color.RESET + " drank." + "\r\n");
        writeToPlayer("500 health is restored.");
        player.updatePlayerHealth(500, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-purple-drank")).inc();
    }

    private void processBeer() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " drinks an ice cold cruiser." + "\r\n");
        writeToPlayer("100 health is restored.");
        player.updatePlayerHealth(100, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-beer-drank")).inc();
    }

    private void processMarijuana() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " blazes " + Color.GREEN + "marijuana" + Color.RESET + ".\r\n");
        writeToPlayer("50 mana is restored." + "\r\n");
        writeToPlayer("20 health is restored.");
        gameManager.addMana(player, 50);
        player.updatePlayerHealth(20, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-weed-smoked")).inc();
    }

    private void processDogdicks() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.GREEN + "dog dick." + Color.RESET + ".\r\n");
        writeToPlayer("1000 mana is restored." + "\r\n");
        writeToPlayer("1500 health is restored.");
        gameManager.addMana(player, 1000);
        player.updatePlayerHealth(1500, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-dogdick-smoked")).inc();
    }

    private void processBook() {
        writeToPlayer("You crack open the book and immediately realize that you aren't familiar with it's written language.");
    }

    public void handle() {
        if (itemType != null) {
            switch (itemType) {
                case KEY:
                    processKey();
                    break;
                case BOOK:
                    processBook();
                    break;
                case BEER:
                    processBeer();
                    break;
                case MARIJUANA:
                    processMarijuana();
                    break;
                case DOGDICKS:
                    processDogdicks();
                    break;
                case PURPLE_DRANK:
                    processPurpleDrank();
                    break;
                case UNKNOWN:
                    writeToPlayer("Item not found.");
                    return;
            }

            processEffects();
            incrementUses(item);

            if (itemType.isDisposable()) {
                if (item.getNumberOfUses() < itemType.getMaxUses()) {
                    gameManager.getEntityManager().saveItem(item);
                    return;
                }
                player.removeInventoryId(item.getItemId());
                gameManager.getEntityManager().removeItem(item);
            }
        }
    }

    private void writeToPlayer(String message) {
        gameManager.getChannelUtils().write(player.getPlayerId(), message);
    }

    private void writeToRoom(String message) {
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), message);
    }

    private void incrementUses(Item item) {
        item.setNumberOfUses(item.getNumberOfUses() + 1);
    }
}
