package com.comandante.creeper.bot.commands;

import com.comandante.creeper.blackjack.BlackJack;
import com.comandante.creeper.blackjack.Deck;
import com.comandante.creeper.bot.BotCommandManager;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return cards.stream()
                .map(card -> card.type.textRepresentation + card.suit.textRepresentation + " / ")
                .collect(Collectors.toList());
    }
}
