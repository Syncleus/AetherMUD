package com.comandante.creeper.Items;


import com.codahale.metrics.MetricRegistry;
import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.CreeperSession;

public class ItemUseHandler {

    private final Item item;
    private final CreeperSession creeperSession;
    private final GameManager gameManager;
    private final String playerId;
    private final ItemType itemType;

    public ItemUseHandler(Item item, CreeperSession creeperSession, GameManager gameManager, String playerId) {
        this.item = item;
        this.creeperSession = creeperSession;
        this.gameManager = gameManager;
        this.playerId = playerId;
        this.itemType = ItemType.itemTypeFromCode(item.getItemTypeId());
    }

    private void processKey() {
        //If no doors
        writeToPlayer("There's no doors here to use this key on.");
    }

    private void processBeer() {
        String playerName = gameManager.getPlayerManager().getPlayer(playerId).getPlayerName();
        writeToRoom(playerName + " drinks an ice cold cruiser." + "\r\n");
        writeToPlayer("50 health is restored.");
        gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().incrementHealth(50);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-beer-drank")).inc();
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
                case UNKNOWN:
                    writeToPlayer("Item not found.");
                    return;
            }

            incrementUses(item);

            if (itemType.isDisposable()) {
                if (item.getNumberOfUses() < itemType.getMaxUses()) {
                    gameManager.getEntityManager().addItem(item);
                    return;
                }
                gameManager.getPlayerManager().removeInventoryId(playerId, item.getItemId());
                gameManager.getEntityManager().removeItem(item);
            }
        }
    }

    private void writeToPlayer(String message) {
        gameManager.getChannelUtils().write(playerId, message);
    }

    private void writeToRoom(String message) {
        gameManager.getChannelUtils().writeToRoom(playerId, message);
    }


    private void incrementUses(Item item) {
        item.setNumberOfUses(item.getNumberOfUses() + 1);
    }
}
