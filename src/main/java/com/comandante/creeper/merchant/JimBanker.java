package com.comandante.creeper.merchant;


import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.*;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class JimBanker extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "jim the banker";
    private final static String welcomeMessage = "Welcome to the First National Bank of Creeper.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"bank", "banker", "jim the banker", "jim", "j", NAME}
    ));

    private final static String colorName = BOLD_ON + Color.CYAN + NAME + Color.RESET;

    public JimBanker(GameManager gameManager, Loot loot, Map<Integer, MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage, MerchantType.BANK);
    }

    @Override
    public String getMenu() {
        return null;
    }
}

