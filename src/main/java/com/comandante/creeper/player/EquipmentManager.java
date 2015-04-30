package com.comandante.creeper.player;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;

import java.util.Set;

public class EquipmentManager {

    private final EntityManager entityManager;
    private final ChannelUtils channelUtils;
    private final PlayerManager playerManager;

    public EquipmentManager(EntityManager entityManager, ChannelUtils channelUtils, PlayerManager playerManager) {
        this.entityManager = entityManager;
        this.channelUtils = channelUtils;
        this.playerManager = playerManager;
    }

    public void equip(PlayerMetadata playerMetaData, Item item) {
        if (item.getEquipment() == null){
            return;
        }
        Equipment equipment = item.getEquipment();
        EquipmentSlotType equipmentSlotType = equipment.getEquipmentSlotType();
        if (isSlotOccupied(playerMetaData, equipmentSlotType)) {
            unEquip(playerMetaData, item);
        }
        playerManager.addEquipmentId(playerMetaData.getPlayerId(), item.getItemId());
        playerManager.removeInventoryId(playerMetaData.getPlayerId(), item.getItemId());
    }

    public boolean isSlotOccupied(PlayerMetadata playerMetadata, EquipmentSlotType slot) {
        if (playerMetadata.getPlayerEquipment() == null) {
            return false;
        }
        for (String item: playerMetadata.getPlayerEquipment()) {
            Item itemEntity = entityManager.getItemEntity(item);
            EquipmentSlotType equipmentSlotType = itemEntity.getEquipment().getEquipmentSlotType();
            if (equipmentSlotType.equals(slot)) {
                return true;
            }
        }
        return false;
    }

    public void unEquip(PlayerMetadata playerMetadata, Item item) {
        channelUtils.write(playerMetadata.getPlayerId(), "Un-equipping " + item.getItemName());
        playerManager.removeEquipmentId(playerMetadata.getPlayerId(), item.getItemId());
        playerManager.addInventoryId(playerMetadata.getPlayerId(), item.getItemId());
    }

    public Stats getPlayerStatsWithEquipment(PlayerMetadata playerMetadata) {
        StatsBuilder statsBuilder = new StatsBuilder();
        Stats newStats = statsBuilder.createStats();
        Stats playerStats = playerMetadata.getStats();
        combineStats(newStats, playerStats);
        String[] playerEquipment = playerMetadata.getPlayerEquipment();
        if (playerEquipment == null) {
            return playerStats;
        }
        for (String equipId: playerEquipment) {
            Stats stats = entityManager.getItemEntity(equipId).getEquipment().getStats();
            combineStats(newStats, stats);
        }
        return newStats;
    }

    private void combineStats(Stats orig, Stats combine) {
        orig.setAgile(orig.getAgile() + combine.getAgile());
        orig.setAim(orig.getAim() + combine.getAim());
        orig.setArmorRating(orig.getArmorRating() + combine.getArmorRating());
        orig.setCurrentHealth(orig.getCurrentHealth() + combine.getCurrentHealth());
        orig.setMaxHealth(orig.getMaxHealth() + combine.getMaxHealth());
        orig.setExperience(orig.getExperience() + combine.getExperience());
        orig.setMeleSkill(orig.getMeleSkill() + combine.getMeleSkill());
        orig.setNumberOfWeaponRolls(orig.getNumberOfWeaponRolls() + combine.getNumberOfWeaponRolls());
        orig.setStrength(orig.getStrength() + combine.getStrength());
        orig.setWeaponRatingMax(orig.getWeaponRatingMax() + combine.getWeaponRatingMax());
        orig.setWeaponRatingMin(orig.getWeaponRatingMin() + combine.getWeaponRatingMin());
        orig.setWillpower(orig.getWillpower() + combine.getWillpower());
    }
}
