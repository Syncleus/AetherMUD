package com.comandante.creeper.merchant;

import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class LockerRoomGuy extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "a bank of lockers";
    private final static String welcomeMessage = "Your personal locker.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"lockers", "locker", "l", NAME}
    ));

    private final static String colorName = BOLD_ON + Color.RED + NAME + Color.RESET;

    public LockerRoomGuy(GameManager gameManager, Loot loot, Map<Integer, MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage, MerchantType.LOCKER);
    }

    @Override
    public String getMenu() {
        return null;
    }
}

