package com.comandante.creeper.merchant;

import com.comandante.creeper.items.Loot;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.server.player_communication.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.comandante.creeper.server.player_communication.Color.BOLD_ON;

public class NigelBartender extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "nigel the bartender";
    private final static String welcomeMessage = "\r\n N I G E L 'S   B A R \r\n";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"nigel", "bartender", "barkeep", "Nigel", "NIGEL", NAME}
    ));

    private final static String colorName = BOLD_ON + Color.CYAN + NAME  + Color.RESET ;

    public NigelBartender(GameManager gameManager, Loot loot, Map<Integer, MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage);
    }
}
