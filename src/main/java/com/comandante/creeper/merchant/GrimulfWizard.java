package com.comandante.creeper.merchant;


import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.server.player_communication.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.comandante.creeper.server.player_communication.Color.BOLD_ON;

public class GrimulfWizard extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "grimulf the wizard";
    private final static String welcomeMessage = "Welcome to my den.\r\n";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"grimulf", "g", "wizard", "w", NAME}
    ));

    private final static String colorName = BOLD_ON + Color.CYAN + NAME  + Color.RESET ;

    public GrimulfWizard(GameManager gameManager, Loot loot, Map<Integer, MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage);
    }
}
