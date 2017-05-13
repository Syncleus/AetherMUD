package com.comandante.creeper.merchant;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.server.player_communication.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.comandante.creeper.server.player_communication.Color.BOLD_ON;

public class LloydBartender extends Merchant {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "lloyd the bartender";
    private final static String welcomeMessage = " _        _        _______           ______   _  _______ \r\n" +
            "( \\      ( \\      (  ___  )|\\     /|(  __  \\ ( )(  ____ \\\r\n" +
            "| (      | (      | (   ) |( \\   / )| (  \\  )|/ | (    \\/\r\n" +
            "| |      | |      | |   | | \\ (_) / | |   ) |   | (_____ \r\n" +
            "| |      | |      | |   | |  \\   /  | |   | |   (_____  )\r\n" +
            "| |      | |      | |   | |   ) (   | |   ) |         ) |\r\n" +
            "| (____/\\| (____/\\| (___) |   | |   | (__/  )   /\\____) |\r\n" +
            "(_______/(_______/(_______)   \\_/   (______/    \\_______)\r\n" +
            "                                                         ";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"lloyd", "bartender", "barkeep", "Lloyd", "LLOYD", NAME}
    ));

    private final static String colorName = BOLD_ON + Color.CYAN + NAME  + Color.RESET ;

    public LloydBartender(GameManager gameManager, List<MerchantItemForSale> merchantItemForSales) {
        super(gameManager, NAME, colorName, validTriggers, merchantItemForSales, welcomeMessage);
    }
}
