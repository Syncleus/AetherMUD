/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comandante.creeper.blackjack;


import com.comandante.creeper.common.CreeperUtils;
import com.google.common.collect.Lists;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlackJack {

    public static class Card {

        public final Deck.Type type;
        public final Deck.Suit suit;

        public Card(Deck.Suit suit, Deck.Type type) {
            this.type = type;
            this.suit = suit;
        }
    }

    public static String templateCard =
            "┌─────────┐\n" +
                    "│{0}        │\n" +
                    "│         │\n" +
                    "│         │\n" +
                    "│   {1}     │\n" +
                    "│         │\n" +
                    "│         │\n" +
                    "│       {0} │\n" +
                    "└─────────┘\n";

    public static String getAsciiPlayingCardHand(List<Card> card) {
        List<String> asciiCards = card.stream().map(BlackJack::getAsciiPlayingCard).collect(Collectors.toList());
        return CreeperUtils.printStringsNextToEachOther(asciiCards, "");
    }


    public static String getAsciiPlayingCard(Card card) {
        String rawTemplate = templateCard;
        if (card.type.textRepresentation.length() == 2) {
            StringBuilder sb = new StringBuilder();
            ArrayList<String> strings = Lists.newArrayList(templateCard.split("[\\r\\n]+"));
            strings.stream().map(s -> {
                if (s.contains("{0}")) {
                    return s.replace("{0} ", "{0}");
                }
                return s;
            }).forEach((str) -> sb.append(str).append("\r\n"));
            rawTemplate = sb.toString();
        }

        MessageFormat messageFormat = new MessageFormat(rawTemplate);
        return messageFormat.format(Lists.newArrayList(card.type.textRepresentation, card.suit.textRepresentation).toArray());
    }

    public static void main(String[] args) {

        String test = "   │7        │   ";
        String s = CreeperUtils.trimTrailingBlanks(test);
        System.out.println(s);

        Deck deck = new Deck();
        deck.shuffle();
        List<Card> cards = Lists.newArrayList(deck.next(), deck.next(), deck.next(), deck.next());
        String asciiPlayingCardHand = getAsciiPlayingCardHand(cards);
        System.out.println(asciiPlayingCardHand);
    }

}
