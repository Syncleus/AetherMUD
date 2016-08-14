package com.comandante.creeper.bot.commands;

import com.comandante.creeper.blackjack.BlackJack;
import com.comandante.creeper.blackjack.Deck;
import com.comandante.creeper.bot.BotCommandManager;
import com.google.api.client.util.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CardsCommand extends BotCommand {

    static Set<String> triggers = Sets.newHashSet("cards");
    static String helpUsage = "cards";
    static String helpDescription = "Some random cards.";

    public CardsCommand(BotCommandManager botCommandManager) {
        super(botCommandManager, triggers, helpUsage, helpDescription);
    }

    @Override
    public List<String> process() {
        Deck deck = new Deck();
        deck.shuffle();
        List<BlackJack.Card> cards = com.google.common.collect.Lists.newArrayList(deck.next(), deck.next(), deck.next(), deck.next(), deck.deal());
        String asciiPlayingCardHand = BlackJack.getAsciiPlayingCardHand(cards);
        ArrayList<String> resp = Lists.newArrayList();
        resp.add(asciiPlayingCardHand);
        return resp;
    }
}
