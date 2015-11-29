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

public class BlackHoleSpell extends Spell {

    private final static String NAME = BOLD_ON + Color.BLACK + "black" + Color.RESET + " hole";
    private final static String DESCRIPTION = "A black hole.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"black", "black hole", "bh", "b", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder()
            .setStrength(180)
            .setWeaponRatingMax(60)
            .setWeaponRatingMin(50)
            .setNumberOfWeaponRolls(4)
            .createStats();
    private final static boolean isAreaSpell = true;

    private final static List<String> attackMessages = Lists.newArrayList("a massive " + BOLD_ON + Color.BLACK + "black hole" + Color.RESET + " begins to form in front of you ");
    private static int manaCost = 20000000;
    private final static int coolDownTicks = 4;

    private static EffectBuilder holeEffect = new EffectBuilder()
            .setEffectApplyMessages(Lists.newArrayList("You are being " + Color.BOLD_ON + Color.BLUE + "nullified" + Color.RESET + " by a black hole!"))
            .setEffectDescription("a dark vortex of nothingness.")
            .setEffectName(Color.BOLD_ON + Color.BLACK + "Black vortex of" + Color.RESET + Color.BOLD_ON + Color.BLUE + " NOTHING" + Color.RESET)
            .setDurationStats(new StatsBuilder().createStats())
            .setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-15000000000).createStats())
            .setFrozenMovement(false)
            .setLifeSpanTicks(2);

    public BlackHoleSpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME, Sets.newHashSet(holeEffect.createEffect()), isAreaSpell, null, coolDownTicks);
    }

    @Override
    public void attackSpell(Set<String> npcIds, Player player) {
        Stats playerStats = player.getPlayerStatsWithEquipmentAndLevel();
        long willpower = playerStats.getWillpower();
        long i = 66666666666L + (willpower * 3);
        this.setEffects(Sets.newHashSet(holeEffect.setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-i).createStats()).createEffect()));
        super.attackSpell(npcIds, player);
    }
}
