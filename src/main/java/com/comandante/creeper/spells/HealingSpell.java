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

public class HealingSpell extends Spell {

    private final static String NAME = BOLD_ON + Color.MAGENTA + "healing" + Color.RESET;
    private final static String DESCRIPTION = "A pure aura of healing.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"healing", "h", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder().createStats();
    private final static boolean isAreaSpell = false;

    private final static List<String> attackMessages = Lists.newArrayList("an aura of " + BOLD_ON + Color.MAGENTA + "healing" + Color.RESET + " surrounds the area");
    private static int manaCost = 80000000;
    private final static int coolDownTicks = 20;

    private static EffectBuilder fullHealEffect = new EffectBuilder()
            .setEffectApplyMessages(Lists.newArrayList("An aura of " + Color.BOLD_ON + Color.MAGENTA + "healing" + Color.RESET + " surrounds you"))
            .setEffectDescription("Heals a target to full health.")
            .setEffectName(Color.BOLD_ON + Color.MAGENTA + "healing" + Color.RESET + Color.BOLD_ON + Color.YELLOW + " AURA" + Color.RESET)
            .setDurationStats(new StatsBuilder().createStats())
            .setFrozenMovement(false)
            .setLifeSpanTicks(1);

    public HealingSpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME, Sets.newHashSet(fullHealEffect.createEffect()), isAreaSpell, null, coolDownTicks);
    }

    @Override
    public void attackSpell(Player destinationPlayer, Player sourcePlayer) {
        Stats stats = destinationPlayer.getPlayerStatsWithEquipmentAndLevel();
        this.setEffects(Sets.newHashSet(fullHealEffect.setApplyStatsOnTick(new StatsBuilder().setCurrentHealth((stats.getMaxHealth())).createStats()).createEffect()));
        super.attackSpell(destinationPlayer, sourcePlayer);
    }
}
