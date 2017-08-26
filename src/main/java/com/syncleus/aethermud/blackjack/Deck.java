/**
 * Copyright 2017 Syncleus, Inc.
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
package com.syncleus.aethermud.blackjack;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Deck implements Iterator<BlackJack.Card> {

    public enum Type {
        ACE(1, "A"),
        KING(10, "K"),
        QUEEN(10, "Q"),
        JACK(10, "J"),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10);


        public final int value;
        public final String textRepresentation;

        Type(int value, String textRepresentation) {
            this.value = value;
            this.textRepresentation = textRepresentation;
        }


        Type(int value) {
            this.value = value;
            this.textRepresentation = String.valueOf(value);
        }
    }

    public enum Suit {
        SPADES("♠"),
        HEARTS("♥"),
        DIAMONDS("♦"),
        CLUBS("♣");

        public final String textRepresentation;

        Suit(String textRepresentation) {
            this.textRepresentation = textRepresentation;
        }

        public String getName() {
            return StringUtils.capitalize(this.name().toLowerCase());
        }
    }


    private List<BlackJack.Card> deckCards = Lists.newArrayList();
    private Iterator<BlackJack.Card> delegate = deckCards.iterator();

    public Deck() {
        Arrays.stream(Deck.Suit.values())
                .forEach(suit ->
                        Arrays.stream(Deck.Type.values())
                                .forEach(type ->
                                        deckCards.add(new BlackJack.Card(suit, type))));
    }

    public void shuffle() {
        Collections.shuffle(deckCards);
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public BlackJack.Card next() {
        BlackJack.Card next = deckCards.iterator().next();
        ArrayList<BlackJack.Card> copy = Lists.newArrayList(deckCards);
        copy.remove(next);
        deckCards = copy;
        delegate = copy.iterator();
        return next;
    }

    public BlackJack.Card deal() {
        return next();
    }


    public void addCard(BlackJack.Card card) {
        ArrayList<BlackJack.Card> copy = Lists.newArrayList(deckCards);
        copy.add(card);
        deckCards = copy;
        delegate = copy.iterator();
    }

    public int size() {
        return deckCards.size();
    }
}
