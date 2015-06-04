package com.comandante.creeper.player;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.stat.StatsHelper;


public class EquipmentManager {

    private final EntityManager entityManager;
    private final ChannelUtils channelUtils;
    private final PlayerManager playerManager;

    public EquipmentManager(EntityManager entityManager, ChannelUtils channelUtils, PlayerManager playerManager) {
        this.entityManager = entityManager;
        this.channelUtils = channelUtils;
        this.playerManager = playerManager;
    }

    public void equip(Player player, Item item) {
        if (item.getEquipment() == null){
            return;
        }
        Equipment equipment = item.getEquipment();
        EquipmentSlotType equipmentSlotType = equipment.getEquipmentSlotType();
        Item slotItem = getSlotItem(player, equipmentSlotType);
        if (slotItem != null){
            unEquip(player, slotItem);
        }
        channelUtils.write(player.getPlayerId(), "Equipping " + item.getItemName() + "\r\n");
        playerManager.addEquipmentId(player, item.getItemId());
        playerManager.removeInventoryId(player, item.getItemId());
    }

    public Item getSlotItem(Player player, EquipmentSlotType slot) {
        PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
        if (playerMetadata.getPlayerEquipment() == null) {
            return null;
        }
        for (String item: playerMetadata.getPlayerEquipment()) {
            Item itemEntity = entityManager.getItemEntity(item);
            EquipmentSlotType equipmentSlotType = itemEntity.getEquipment().getEquipmentSlotType();
            if (equipmentSlotType.equals(slot)) {
                return itemEntity;
            }
        }
        return null;
    }

    public void unEquip(Player player, Item item) {
        channelUtils.write(player.getPlayerId(), "Un-equipping " + item.getItemName() + "\r\n");
        playerManager.removeEquipmentId(player, item.getItemId());
        playerManager.addInventoryId(player, item.getItemId());
    }

    public Stats getPlayerStatsWithEquipment(Player player) {
        PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
        StatsBuilder statsBuilder = new StatsBuilder();
        Stats newStats = statsBuilder.createStats();
        Stats playerStats = playerMetadata.getStats();
        StatsHelper.combineStats(newStats, playerStats);
        String[] playerEquipment = playerMetadata.getPlayerEquipment();
        if (playerEquipment == null) {
            return playerStats;
        }
        for (String equipId: playerEquipment) {
            Item itemEntity = entityManager.getItemEntity(equipId);
            Equipment equipment = itemEntity.getEquipment();
            Stats stats = equipment.getStats();
            StatsHelper.combineStats(newStats, stats);
        }
        return newStats;
    }

}
