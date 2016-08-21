package com.comandante.creeper.merchant;

import com.comandante.creeper.items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.player_communication.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.comandante.creeper.server.player_communication.Color.BOLD_ON;

public class OldWiseMan extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "old wise man";
    private final static String colorName = BOLD_ON + Color.CYAN + NAME + Color.RESET;
    private final static String welcomeMessage = "The "+ colorName +" can assist you in choosing a character class.\r\n";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
            {"wise", "man", "old", "old wise man", "m", "w", NAME}
    ));


    public OldWiseMan(GameManager gameManager, Loot loot, Map<Integer, MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage, MerchantType.PLAYERCLASS_SELECTOR);
    }

    @Override
    public String getMenu() {
        return null;
    }
}

