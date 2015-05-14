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

import static com.comandante.creeper.server.Color.BOLD_ON;
import static com.comandante.creeper.server.Color.RESET;

public class DemonSuccubus extends Npc {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "demon succubus";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"d", "demon", "succubus","s", NAME}
    ));

    private final static String colorName = "demon" + BOLD_ON + Color.MAGENTA + " succubus"  + Color.RESET ;
    private final static String dieMessage = "a " + colorName + " breathes his last breath in a pool of " + BOLD_ON + Color.RED + "blood" + RESET + ".";

    public DemonSuccubus(GameManager gameManager, Loot loot) {
        super(gameManager, NAME, colorName, 0, NpcStats.DEMON_SUCCUBUS.createStats(), dieMessage, Sets.<Area>newHashSet(), validTriggers, loot);
    }

    @Override
    public DemonSuccubus create(GameManager gameManager, Loot loot) {
        return new DemonSuccubus(gameManager, loot);
    }
}