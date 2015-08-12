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
        player.addMana(50);
        player.updatePlayerHealth(20, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-weed-smoked")).inc();
    }

    private void processDogdicks() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.GREEN + "dog dick" + Color.RESET + ".\r\n");
        writeToPlayer("1000 mana is restored." + "\r\n");
        writeToPlayer("1500 health is restored.");
        player.addMana(1000);
        player.updatePlayerHealth(1500, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-dogdick-smoked")).inc();
    }

    private void processHaze() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " smokes " + Color.GREEN + "haze" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-haze-smoked")).inc();
    }

    private void processAexirianRoot() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " chews on an " + Color.YELLOW + "aexirian " + Color.RESET + Color.GREEN + "root" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-aexirian-chewed")).inc();
    }
    
    private void processMithaemLeaf() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.YELLOW + "mithaem " + Color.RESET + Color.GREEN + "leaf" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-mithaem-ate")).inc();
    }
    
    private void processDuriccaRoot() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " chews a " + Color.YELLOW + "duricca " + Color.RESET + Color.GREEN + "root" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-duricca-chewed")).inc();
    }

    private void processPondeselBerry() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.YELLOW + "pondesel " + Color.RESET + Color.GREEN + "berry" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-pondesel-ate")).inc();
    }

    private void processVikalionusCap() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.YELLOW + "vikalionus " + Color.RESET + Color.GREEN + "cap" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-vikalionus-ate")).inc();
    }
    
    private void processLoornsLace() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " smokes " + Color.YELLOW + "Loorn's " + Color.RESET + Color.GREEN + "lace" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-loorns-smoked")).inc();
    }

    private void processTournearesLeaf() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " smokes " + Color.YELLOW + "Tourneares " + Color.RESET + Color.GREEN + "leaf" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-tourneares-smoked")).inc();
    }
    
    private void processHaussianBerry() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.YELLOW + "Haussian " + Color.RESET + Color.GREEN + "berry" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-haussian-ate")).inc();
    }
    
    private void processPertilliumRoot() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " grinds some " + Color.YELLOW + "Pertillium " + Color.RESET + Color.GREEN + "root " + Color.RESET + "into a tincture.\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-pertillium-ground")).inc();
    }
    
    private void processHycianthisBark() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " brews a tea from some " + Color.YELLOW + "Hycianthis " + Color.RESET + Color.GREEN + "bark" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-hycianthis-drank")).inc();
    }
    
    private void processPunilareFern() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " chews a " + Color.YELLOW + "Punilare " + Color.RESET + Color.GREEN + "fern " + Color.RESET + "leaf.\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-punilare-chewed")).inc();
    }
    
    private void processKeakiarCap() {
        String playerName = player.getPlayerName();
        writeToRoom(playerName + " eats a " + Color.YELLOW + "Keakiar " + Color.RESET + Color.GREEN + "cap" + Color.RESET + ".\r\n");
        writeToPlayer("5000 mana is restored." + "\r\n");
        writeToPlayer("3000 health is restored.");
        player.addMana(5000);
        player.updatePlayerHealth(3000, null);
        Main.metrics.counter(MetricRegistry.name(ItemUseHandler.class, playerName + "-keakiar-ate")).inc();
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
                case HAZE:
                    processHaze();
                    break;
                case AEXIRIAN_ROOT:
                    processAexirianRoot();
                    break;
                case MITHAEM_LEAF:
                    processMithaemLeaf();
                    break;
                case DURICCA_ROOT:
                    processDuriccaRoot();
                    break;
                case PONDESEL_BERRY:
                    processPondeselBerry();
                    break;
                case VIKALIONUS_CAP:
                    processVikalionusCap();
                    break;
                case LOORNS_LACE:
                    processLoornsLace();
                    break;
                case TOURNEARES_LEAF:
                    processTournearesLeaf();
                    break;
                case HAUSSIAN_BERRY:
                    processHaussianBerry();
                    break;
                case PERTILLIUM_ROOT:
                    processPertilliumRoot();
                    break;
                case HYCIANTHIS_BARK:
                    processHycianthisBark();
                    break;
                case PUNILARE_FERN:
                    processPunilareFern();
                    break;
                case KEAKIAR_CAP:
                    processKeakiarCap();
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
