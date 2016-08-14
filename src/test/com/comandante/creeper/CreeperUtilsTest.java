package com.comandante.creeper;

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
        PlayerMetadata playerMetadata = new PlayerMetadata("usertest", "Testtest", Main.createPlayerId("usertest"), PlayerStats.DEFAULT_PLAYER.createStats(), 0, Sets.newHashSet(PlayerRole.MORTAL), new String[0], 0, new String[0], Maps.newHashMap());
        GameManager gameManager = mock(GameManager.class);
        StatsModifierFactory statsModifierFactory = mock(StatsModifierFactory.class);
        when(statsModifierFactory.getStatsModifier(Matchers.any())).thenReturn(PlayerStats.DEFAULT_PLAYER.createStats());
        when(gameManager.getStatsModifierFactory()).thenReturn(statsModifierFactory);
        PlayerManager playerManager = mock(PlayerManager.class);
        when(playerManager.getPlayerMetadata(Matchers.any())).thenReturn(playerMetadata);
        when(gameManager.getPlayerManager()).thenReturn(playerManager);


        String usertest = new Player("usertest", gameManager).getLookString();

        String s = CreeperUtils.printStringsNextToEachOther(Lists.newArrayList(usertest, usertest), " | ");

        System.out.println(s);


    }

}