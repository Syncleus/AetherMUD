package com.comandante.creeper.Items;

import com.comandante.creeper.Items.use.DefaultApplyStatsAction;
import com.comandante.creeper.Items.use.LightningSpellBookUseAction;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.google.api.client.util.Maps;
import com.google.api.client.util.Sets;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class ItemUseRegistry {

    private static final Map<Integer, ItemUseAction> itemUseActionMap = Maps.newHashMap();
    private static final Logger log = Logger.getLogger(ItemUseRegistry.class);


    public static void addItemUseAction(ItemUseAction itemUseAction) {
        itemUseActionMap.put(itemUseAction.getItemTypeId(), itemUseAction);
    }

    public static ItemUseAction getItemUseAction(Integer id) {
        return itemUseActionMap.get(id);
    }

    public static void configure() {
        //Beer
        addItemUseAction(new DefaultApplyStatsAction(ItemType.SMALL_HEALTH_POTION, buildStats(100, 0), Sets.<Effect>newHashSet()));

        //Purple Drank
        addItemUseAction(new DefaultApplyStatsAction(ItemType.PURPLE_DRANK, buildStats(500, 0), Sets.<Effect>newHashSet()));

        //Marijuana
        addItemUseAction(new DefaultApplyStatsAction(ItemType.MARIJUANA, buildStats(500,500), Sets.<Effect>newHashSet()));

        //Lightning Spellbook
        addItemUseAction(new LightningSpellBookUseAction(ItemType.LIGHTNING_SPELLBOOKNG));
    }

    private static Stats buildStats(int health, int mana) {
        StatsBuilder statsBuilder = new StatsBuilder();
        statsBuilder.setCurrentHealth(health);
        statsBuilder.setCurrentMana(mana);
        return statsBuilder.createStats();
    }

    public static void processEffects(GameManager gameManager, Player player, Set<Effect> effects) {
        if (effects == null) {
            return;
        }
        for (Effect effect : effects) {
            Effect nEffect = new Effect(effect);
            nEffect.setPlayerId(player.getPlayerId());
            gameManager.getEntityManager().saveEffect(nEffect);
            boolean effectResult = player.addEffect(nEffect.getEntityId());
            if (effect.getDurationStats() != null) {
                if (effect.getDurationStats().getCurrentHealth() < 0) {
                    log.error("ERROR! Someone added an effect with a health modifier which won't work for various reasons.");
                    continue;
                }
            }
            if (effectResult) {
                gameManager.getChannelUtils().write(player.getPlayerId(), "You feel " + effect.getEffectName() + "\r\n");
            } else {
                gameManager.getChannelUtils().write(player.getPlayerId(), "Unable to apply effect.\r\n");
            }
        }
    }

    public static void incrementUses(Item item) {
        item.setNumberOfUses(item.getNumberOfUses() + 1);
    }
}

