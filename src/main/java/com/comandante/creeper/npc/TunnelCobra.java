package com.comandante.creeper.npc;


import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.world.Area;
import com.google.common.base.Optional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TunnelCobra extends Npc {
    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "tunnel cobra";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"t", "tunnel", "cobra", "c", NAME}
    ));

    private final static String colorName = "tunnel" + Color.BOLD_ON + Color.MAGENTA + " cobra"  + Color.RESET ;
    private final static String dieMessage = "a " + colorName + " breathes his last breath in a pool of " + Color.BOLD_ON + Color.RED + "blood" + Color.RESET + ".";

    public TunnelCobra(GameManager gameManager, Loot loot) {
        super(gameManager, NAME, colorName, 0, NpcStats.TUNNEL_COBRA.createStats(), dieMessage, Optional.<HashSet<Area>>absent(), validTriggers, loot);
    }

    @Override
    public TunnelCobra create(GameManager gameManager, Loot loot) {
        return new TunnelCobra(gameManager, loot);
    }
}
