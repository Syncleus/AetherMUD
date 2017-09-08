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
package com.syncleus.aethermud;

import com.syncleus.aethermud.blackjack.BlackJack;
import com.syncleus.aethermud.blackjack.Deck;
import com.syncleus.aethermud.blackjack.Hand;
import com.syncleus.aethermud.common.AetherMudUtils;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.stats.DefaultStats;
import com.syncleus.aethermud.stats.modifier.StatsModifierFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.syncleus.aethermud.storage.AetherMudStorage;
import com.syncleus.aethermud.storage.graphdb.GraphDbAetherMudStorage;
import com.syncleus.aethermud.storage.graphdb.GraphStorageFactory;
import com.syncleus.aethermud.storage.graphdb.model.PlayerData;
import com.syncleus.aethermud.storage.graphdb.model.StatsData;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.syncleus.aethermud.bot.command.commands.CardsCommand.convertCardFormats;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AetherMudUtilsTest {

    private static final String EXPECTED_COMBINED_STRING = "\u001B[35m-+=[ \u001B[musertest\u001B[35m ]=+- \u001B[m                   | \u001B[35m-+=[ \u001B[musertest\u001B[35m ]=+- \u001B[m                  \r\nLevel 0 \u001B[33m[\u001B[mBasic\u001B[33m]\u001B[m                       | Level 0 \u001B[33m[\u001B[mBasic\u001B[33m]\u001B[m                      \r\nForaging Level 0                      | Foraging Level 0                     \r\n\u001B[35mEquip--------------------------------\u001B[m | \u001B[35mEquip--------------------------------\u001B[m\r\nHand                                  | Hand                                 \r\nHead                                  | Head                                 \r\nFeet                                  | Feet                                 \r\nLegs                                  | Legs                                 \r\nWrists                                | Wrists                               \r\nChest                                 | Chest                                \r\nBag                                   | Bag                                  \r\n\u001B[35mStats--------------------------------\u001B[m | \u001B[35mStats--------------------------------\u001B[m\r\nnull                                  | null                                 \r\n";

    @Test
    public void testCombineStrings() throws Exception {
        List<String> strings = new ArrayList<>(2);
        strings.add("feet");
        strings.add("hand");

        PlayerData playerData = mock(PlayerData.class);
        playerData.setNpcKillLog(new HashMap<>());
        playerData.setCoolDowns(new HashMap<>());
        playerData.setEffects(new HashSet<>());
        playerData.setGold(0);
        playerData.setGoldInBank(0);
        playerData.setInventory(new ArrayList<>());
        playerData.setLearnedSpells(new ArrayList<>());
        playerData.setLockerInventory(new ArrayList<>());
        playerData.setIsMarkedForDelete(false);
        playerData.setPlayerName("usertest");
        playerData.setPassword("Testtest");
        playerData.setPlayerClass(PlayerClass.BASIC);
        playerData.setPlayerEquipment(strings);
        playerData.setPlayerId(Main.createPlayerId("usertest"));
        playerData.setPlayerRoles(Sets.newHashSet(PlayerRole.MORTAL));
        playerData.setPlayerSettings(new HashMap<>());
        StatsData statsData = mock(StatsData.class);
        try {
            PropertyUtils.copyProperties(statsData, DefaultStats.DEFAULT_PLAYER.createStats());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not create a stats object", e);
        }
        when(playerData.getStats()).thenReturn(statsData);

        GameManager gameManager = mock(GameManager.class);
        StatsModifierFactory statsModifierFactory = mock(StatsModifierFactory.class);
        when(statsModifierFactory.getStatsModifier(Matchers.any())).thenReturn(DefaultStats.DEFAULT_PLAYER.createStats());
        when(gameManager.getStatsModifierFactory()).thenReturn(statsModifierFactory);

        GraphStorageFactory storageFactory = mock(GraphStorageFactory.class);
        GraphStorageFactory.AetherMudTx tx = mock(GraphStorageFactory.AetherMudTx.class);
        GraphDbAetherMudStorage aetherStorage = mock(GraphDbAetherMudStorage.class);
        when(gameManager.getGraphStorageFactory()).thenReturn(storageFactory);
        when(storageFactory.beginTransaction()).thenReturn(tx);
        when( tx.getStorage() ).thenReturn(aetherStorage);
        when(aetherStorage.getPlayerMetadata(Matchers.any())).thenReturn(java.util.Optional.ofNullable(playerData));

        PlayerManager playerManager = mock(PlayerManager.class);
        when(gameManager.getPlayerManager()).thenReturn(playerManager);
        EntityManager entityManager = mock(EntityManager.class);
        when(gameManager.getEntityManager()).thenReturn(entityManager);
        Player usertest = new Player("usertest", gameManager);

//        when(entityManager.getItemEntity(Matchers.startsWith("feet"))).thenReturn(java.util.Optional.ofNullable(ItemType.BERSEKER_BOOTS.create()));
//        when(entityManager.getItemEntity(Matchers.startsWith("hand"))).thenReturn(java.util.Optional.ofNullable(ItemType.BERSERKER_BATON.create()));
        when(gameManager.getEntityManager().getItemEntity("feet")).thenReturn(Optional.empty());
        when(gameManager.getEntityManager().getItemEntity("hand")).thenReturn(Optional.empty());
        String s = AetherMudUtils.printStringsNextToEachOther(Lists.newArrayList(usertest.getLookString(), usertest.getLookString()), " | ");

        //System.out.println(org.apache.commons.lang.StringEscapeUtils.escapeJava(s));
        Assert.assertEquals(s, EXPECTED_COMBINED_STRING);
    }

    @Test
    public void simulatePokerHands() throws Exception {
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            Deck deck = new Deck();
            deck.shuffle();
            List<BlackJack.Card> handOfCards = Lists.newArrayList();
            for (int o = 0; o < 5; o++) {
                handOfCards.add(deck.next());
            }
            Set<Hand.Card> collect = handOfCards.stream()
                    .map(convertCardFormats())
                    .collect(Collectors.toSet());
            Hand hand = new Hand(collect);
            HashSet<Hand.Card> compareHand = Sets.newHashSet();
            compareHand.add(Hand.Card.FOUR_SPADES);
            compareHand.add(Hand.Card.FIVE_SPADES);
            compareHand.add(Hand.Card.SIX_SPADES);
            compareHand.add(Hand.Card.TWO_SPADES);
            compareHand.add(Hand.Card.THREE_SPADES);
            Hand benchmark = new Hand(compareHand);
            int i1 = benchmark.compareTo(hand);
            if (benchmark.compareTo(hand) == 0) {
                break;
            }
        }
    }
}
