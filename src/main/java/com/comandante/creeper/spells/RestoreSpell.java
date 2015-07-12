package com.comandante.creeper.spells;

import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class RestoreSpell extends Spell {

    private final static String NAME = BOLD_ON + Color.MAGENTA + "restore" + Color.RESET;
    private final static String DESCRIPTION = "A potent wave of healing.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"restore", "r", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder().createStats();
    private final static boolean isAreaSpell = false;

    private final static List<String> attackMessages = Lists.newArrayList("a wave of " + BOLD_ON + Color.MAGENTA + "healing" + Color.RESET + " flows to those in need");
    private static int manaCost = 80;

    private static EffectBuilder burnEffect = new EffectBuilder()
            .setEffectApplyMessages(Lists.newArrayList("You feel a rush of dopamine as a " + Color.BOLD_ON + Color.MAGENTA + "healing" + Color.RESET + " sensation flows through your body!"))
            .setEffectDescription("Heals a target over time.")
            .setEffectName(Color.BOLD_ON + Color.MAGENTA + "healing" + Color.RESET + Color.BOLD_ON + Color.BLUE + " WAVE" + Color.RESET)
            .setDurationStats(new StatsBuilder().createStats())
            .setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(400).createStats())
            .setFrozenMovement(false)
            .setLifeSpanTicks(2);

    public RestoreSpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME, Sets.newHashSet(burnEffect.createEffect()), isAreaSpell, null);
    }

    @Override
    public void attackSpell(Player destinationPlayer, Player sourcePlayer) {
        this.setEffects(Sets.newHashSet(burnEffect.setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(800).createStats()).createEffect()));
        super.attackSpell(destinationPlayer, sourcePlayer);
    }
}