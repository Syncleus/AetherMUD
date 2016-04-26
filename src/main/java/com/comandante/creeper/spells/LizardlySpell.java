package com.comandante.creeper.spells;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.spells.Spell;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Room;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class LizardlySpell extends Spell {

    private final static String NAME = BOLD_ON + Color.YELLOW + "lizzards" + Color.RESET;
    private final static String DESCRIPTION = "Your target is LIZARDLY!!!!";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"lizardly", "liz", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder().createStats();
    private final static boolean isAreaSpell = false;

    private final static List<String> attackMessages = Lists.newArrayList("tainted lizzard blood is flung through the air... " + BOLD_ON + Color.YELLOW + "lizardly" + Color.RESET + "!!!!");
    private final static int manaCost = 300;
    private final static int coolDownTicks = 30;

    private static EffectBuilder aids = new EffectBuilder()
            .setEffectApplyMessages(Lists.newArrayList("You feel closer to death as " + Color.BOLD_ON + Color.YELLOW + "lizzardly" + Color.RESET + " destroys your will to live!"))
            .setEffectDescription("Target has aids.")
            .setEffectName(Color.BOLD_ON + Color.RED + "aids" + Color.RESET + Color.BOLD_ON + Color.BLUE + " FOR LIFE" + Color.RESET)
            .setDurationStats(new StatsBuilder().createStats())
            .setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-1000).createStats())
            .setFrozenMovement(false)
            .setLifeSpanTicks(5);

    public LizardlySpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME, Sets.<Effect>newHashSet(), isAreaSpell, null, coolDownTicks);
    }

    @Override
    public void attackSpell(Player destinationPlayer, Player sourcePlayer) {
        Stats stats = sourcePlayer.getPlayerStatsWithEquipmentAndLevel();
        this.setEffects(Sets.newHashSet(aids.setApplyStatsOnTick(new StatsBuilder().setCurrentHealth(-1000).createStats()).createEffect()));
        super.attackSpell(destinationPlayer, sourcePlayer);
    }
}
