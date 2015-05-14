package com.comandante.creeper.npc;

import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.Area;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ScaledDeathcrawler extends Npc {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "scaled deathcrawler";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"d", "scaled deathcrawler", "scaled", "death crawler", NAME}
    ));

    private final static String colorName = "scaled" + Color.BOLD_ON + Color.MAGENTA + " deathcrawler"  + Color.RESET ;
    private final static String dieMessage = "a " + colorName + " breathes his last breath in a pool of " + Color.BOLD_ON + Color.RED + "blood" + Color.RESET + ".";

    public ScaledDeathcrawler(GameManager gameManager, Loot loot) {
        super(gameManager, NAME, colorName, 0, NpcStats.SCALED_DEATHCRAWLER.createStats(), dieMessage, Sets.<Area>newHashSet(), validTriggers, loot);
    }

    @Override
    public ScaledDeathcrawler create(GameManager gameManager, Loot loot) {
        return new ScaledDeathcrawler(gameManager, loot);
    }
}
