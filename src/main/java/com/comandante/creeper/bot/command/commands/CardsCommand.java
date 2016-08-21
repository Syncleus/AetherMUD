package com.comandante.creeper.bot.command.commands;

import com.comandante.creeper.blackjack.BlackJack;
import com.comandante.creeper.blackjack.Deck;
import com.comandante.creeper.bot.command.BotCommandManager;
import com.comandante.creeper.common.CreeperUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
        StringBuilder sb = new StringBuilder();
        if (getMessageEvent() != null) {
            String nickName = getMessageEvent().getUser().getNick();
            sb.append(nickName).append(": ");
        }
        cards.forEach(card -> sb.append(card.type.textRepresentation).append(card.suit.textRepresentation).append(" / "));
        return Lists.newArrayList(CreeperUtils.replaceLast(sb.toString(), " / ", ""));
    }
}
