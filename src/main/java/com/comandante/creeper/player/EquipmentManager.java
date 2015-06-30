package com.comandante.creeper.player;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.stat.StatsHelper;


public class EquipmentManager {

    private final EntityManager entityManager;
    private final ChannelUtils channelUtils;
    private final PlayerManager playerManager;
    private final GameManager gameManager;

    public EquipmentManager(EntityManager entityManager, ChannelUtils channelUtils, PlayerManager playerManager, GameManager gameManager) {
        this.entityManager = entityManager;
        this.channelUtils = channelUtils;
        this.playerManager = playerManager;
        this.gameManager = gameManager;
    }

    public void equip(Player player, Item item) {
        if (item.getEquipment() == null) {
            return;
        }
        Equipment equipment = item.getEquipment();
        EquipmentSlotType equipmentSlotType = equipment.getEquipmentSlotType();
        Item slotItem = getSlotItem(player, equipmentSlotType);
        if (slotItem != null) {
            if (!unEquip(player, slotItem)) {
                return;
            }
        }
        channelUtils.write(player.getPlayerId(), "Equipping " + item.getItemName() + "\r\n");
        player.addEquipmentId(item.getItemId());
        player.removeInventoryId(item.getItemId());
    }

    public Item getSlotItem(Player player, EquipmentSlotType slot) {
        PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
        if (playerMetadata.getPlayerEquipment() == null) {
            return null;
        }
        for (String item : playerMetadata.getPlayerEquipment()) {
            Item itemEntity = entityManager.getItemEntity(item);
            EquipmentSlotType equipmentSlotType = itemEntity.getEquipment().getEquipmentSlotType();
            if (equipmentSlotType.equals(slot)) {
                return itemEntity;
            }
        }
        return null;
    }

    public boolean unEquip(Player player, Item item) {
        channelUtils.write(player.getPlayerId(), "Un-equipping " + item.getItemName() + "\r\n");
        if(gameManager.acquireItem(player, item.getItemId())) {
            player.removeEquipmentId(item.getItemId());
            return true;
        }
        return false;
    }

    public Stats getPlayerStatsWithEquipmentAndLevel(Player player) {
        PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
        StatsBuilder statsBuilder = new StatsBuilder();
        Stats newStats = statsBuilder.createStats();
        Stats playerStats = gameManager.getStatsModifierFactory().getStatsModifier(player);
        StatsHelper.combineStats(newStats, playerStats);
        String[] playerEquipment = playerMetadata.getPlayerEquipment();
        if (playerEquipment == null) {
            return playerStats;
        }
        for (String equipId : playerEquipment) {
            Item itemEntity = entityManager.getItemEntity(equipId);
            Equipment equipment = itemEntity.getEquipment();
            Stats stats = equipment.getStats();
            StatsHelper.combineStats(newStats, stats);
        }
        if (playerMetadata.getEffects() != null) {
            for (String effectId : playerMetadata.getEffects()) {
                Effect effect = gameManager.getEntityManager().getEffect(effectId);
                StatsHelper.combineStats(newStats, effect.getDurationStats());
            }
        }
        return newStats;
    }

}
