package com.comandante.creeper.Items;


import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.server.CreeperSession;

public class ItemUseHandler {

    private final Item item;
    private final CreeperSession creeperSession;
    private final GameManager gameManager;
    private final Player player;
    private final ItemType itemType;

    public ItemUseHandler(Item item, CreeperSession creeperSession, GameManager gameManager, Player player) {
        this.item = item;
        this.creeperSession = creeperSession;
        this.gameManager = gameManager;
        this.player = player;
        this.itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
    }

    private void processKey() {
        //If no doors
        writeToPlayer("There's no doors here to use this key on.");
    }

    private void processPurpleDrank() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " sips some " + Color.MAGENTA + "purple" + Color.RESET + " drank." + "\r\n");
        writeToPlayer("500 health is restored.");
        gameManager.getPlayerManager().incrementHealth(player, 500);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-purple-drank")).inc();
    }

    private void processBeer() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " drinks an ice cold cruiser." + "\r\n");
        writeToPlayer("100 health is restored.");
        gameManager.getPlayerManager().incrementHealth(player, 100);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-beer-drank")).inc();
    }

    private void processMarijuana() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " blazes " + Color.GREEN + "marijuana" + Color.RESET + ".\r\n");
        writeToPlayer("50 mana is restored." + "\r\n");
        writeToPlayer("20 health is restored.");
        gameManager.getPlayerManager().updatePlayerMana(player, 50);
        gameManager.getPlayerManager().incrementHealth(player, 20);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-weed-smoked")).inc();
    }

    private void processDogdicks() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.GREEN + "dog dick." + Color.RESET + ".\r\n");
        writeToPlayer("1000 mana is restored." + "\r\n");
        writeToPlayer("1500 health is restored.");
        gameManager.getPlayerManager().updatePlayerMana(player, 1000);
        gameManager.getPlayerManager().incrementHealth(player, 1500);
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

            incrementUses(item);

            if (itemType.isDisposable()) {
                if (item.getNumberOfUses() < itemType.getMaxUses()) {
                    gameManager.getEntityManager().saveItem(item);
                    return;
                }
                gameManager.getPlayerManager().removeInventoryId(player, item.getItemId());
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
