package com.comandante.creeper;

import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreeperUtilsTest {

    @Test
    public void testCombineStrings() throws Exception {
        String[] strings = new String[2];
        strings[0] = "feet";
        strings[1] = "hand";
        PlayerMetadata playerMetadata = new PlayerMetadata("usertest", "Testtest", Main.createPlayerId("usertest"), PlayerStats.DEFAULT_PLAYER.createStats(), 0, Sets.newHashSet(PlayerRole.MORTAL), strings, 0, new String[0], Maps.newHashMap());
        GameManager gameManager = mock(GameManager.class);
        StatsModifierFactory statsModifierFactory = mock(StatsModifierFactory.class);
        when(statsModifierFactory.getStatsModifier(Matchers.any())).thenReturn(PlayerStats.DEFAULT_PLAYER.createStats());
        when(gameManager.getStatsModifierFactory()).thenReturn(statsModifierFactory);
        PlayerManager playerManager = mock(PlayerManager.class);
        when(playerManager.getPlayerMetadata(Matchers.any())).thenReturn(playerMetadata);
        when(gameManager.getPlayerManager()).thenReturn(playerManager);
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getItemEntity(Matchers.startsWith("feet"))).thenReturn(ItemType.BERSEKER_BOOTS.create());
        when(entityManager.getItemEntity(Matchers.startsWith("hand"))).thenReturn(ItemType.BERSERKER_BATON.create());
        when(gameManager.getEntityManager()).thenReturn(entityManager);
        Player usertest = new Player("usertest", gameManager);

        String s = CreeperUtils.printStringsNextToEachOther(Lists.newArrayList(usertest.getLookString(), usertest.getLookString()), "");

        System.out.println(s);


        System.out.println(usertest.getLookString().replaceAll("\u001B\\[[;\\d]*m", ""));


    }

}