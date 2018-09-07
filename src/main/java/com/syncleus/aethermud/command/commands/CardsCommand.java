/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.command.commands;

import com.syncleus.aethermud.blackjack.BlackJack;
import com.syncleus.aethermud.blackjack.Deck;
import com.syncleus.aethermud.core.GameManager;
import com.google.common.collect.Lists;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class CardsCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("cards");
    final static String description = "Display Random Cards";
    final static String correctUsage = "cards";

    public CardsCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            Deck deck = new Deck();
            deck.shuffle();
            List<BlackJack.Card> cards = Lists.newArrayList(deck.next(), deck.next(), deck.next(), deck.next(), deck.deal());
            String asciiPlayingCardHand = BlackJack.getAsciiPlayingCardHand(cards);

            write(asciiPlayingCardHand + "\r\n");
        });
    }

}
