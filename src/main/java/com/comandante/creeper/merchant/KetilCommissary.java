package com.comandante.creeper.merchant;

import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.server.player_communication.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.comandante.creeper.server.player_communication.Color.BOLD_ON;

public class KetilCommissary extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "ketil";
    private final static String welcomeMessage = "Welcome to the Ketil Commissary.\r\n";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"ketil", "k", "commissary", "c", NAME}
    ));

    private final static String colorName = BOLD_ON + Color.CYAN + NAME  + Color.RESET ;

    public KetilCommissary(GameManager gameManager, Loot loot, Map<Integer, MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage);
    }
}
