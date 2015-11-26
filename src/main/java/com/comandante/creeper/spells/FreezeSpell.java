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

public class FreezeSpell extends Spell {

    private final static String NAME = BOLD_ON + Color.CYAN + "freeze" + Color.RESET;
    private final static String DESCRIPTION = "A vortex of ice.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"freeze", "f", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder()
            .setStrength(180)
            .setWeaponRatingMax(60)
            .setWeaponRatingMin(50)
            .setNumberOfWeaponRolls(4)
            .createStats();
    private final static boolean isAreaSpell = true;

    private final static List<String> attackMessages = Lists.newArrayList("a blizzard of " + BOLD_ON + Color.CYAN + "ice" + Color.RESET + " blasts through the area");
    private static int manaCost = 3000000;
    private final static int coolDownTicks = 6;

    private static EffectBuilder freezeEffect = new EffectBuilder()
            .setEffectApplyMessages(Lists.newArrayList("You are " + Color.BOLD_ON + Color.BLUE + "frozen" + Color.RESET + " by a violent blizzard!"))
            .setEffectDescription("a torrent of ice crystals.")
            .setEffectName(Color.BOLD_ON + Color.CYAN + "FROZEN" + Color.RESET)
            .setDurationStats(new StatsBuilder().createStats())
            .setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-666666).createStats())
            .setFrozenMovement(true)
            .setLifeSpanTicks(5);

    public FreezeSpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME, Sets.newHashSet(freezeEffect.createEffect()), isAreaSpell, null, coolDownTicks);
    }

    @Override
    public void attackSpell(Set<String> npcIds, Player player) {
        Stats playerStats = player.getPlayerStatsWithEquipmentAndLevel();
        long willpower = playerStats.getWillpower();
        long i = 5000000 + (willpower * 3);
        this.setEffects(Sets.newHashSet(freezeEffect.setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-i).createStats()).createEffect()));
        super.attackSpell(npcIds, player);
    }
}
