package com.comandante.creeper.player;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcStatsChangeBuilder;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.stat.StatsHelper;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import java.util.*;

public class Player extends CreeperEntity {

    private String playerName;
    private Channel channel;
    private Optional<String> returnDirection = Optional.absent();
    private final GameManager gameManager;
    private Room currentRoom;
    private SortedMap<Long, ActiveFight> activeFights = Collections.synchronizedSortedMap(new TreeMap<Long, ActiveFight>());
    private final String playerId;
    private Set<CoolDown> coolDowns = Collections.synchronizedSet(new HashSet<CoolDown>());
    private final Interner<String> interner = Interners.newWeakInterner();
    private final Random random = new Random();
    private int tickBucket = 0;
    private int fightTickBucket = 0;

    private static final Logger log = Logger.getLogger(Player.class);

    public Player(String playerName, GameManager gameManager) {
        this.playerName = playerName;
        this.playerId = new String(Base64.encodeBase64(playerName.getBytes()));
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            if (processTickBucket(10)) {
                processRegens();
                processEffects();
                if (activeFights.size() > 0) {
                    writePrompt();
                }
            }
            tickAllActiveCoolDowns();
            activateNextPrimaryActiveFight();
            if (processFightTickBucket(4)) {
                processFightRounds();
            }
        } catch (Exception e) {
            log.error("Player ticker failed! + " + playerName, e);
        }
    }


    private boolean processTickBucket(int numberOfTicksToFillBucket) {
        if (tickBucket == numberOfTicksToFillBucket) {
            tickBucket = 0;
            return true;
        } else {
            tickBucket = tickBucket + 1;
            return false;
        }
    }

    private boolean processFightTickBucket(int numberOfTicksToFillBucket) {
        if (fightTickBucket == numberOfTicksToFillBucket) {
            fightTickBucket = 0;
            return true;
        } else {
            fightTickBucket = fightTickBucket + 1;
            return false;
        }
    }

    private void processFightRounds() {
        for (ActiveFight activeFight : activeFights.values()) {
            doFightRound(activeFight);
        }
    }

    private void processRegens() {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            Stats stats = getPlayerStatsWithEquipmentAndLevel();
            if (!isActive(CoolDownType.DEATH)) {
                if (playerMetadata.getStats().getCurrentHealth() < stats.getMaxHealth()) {
                    updatePlayerHealth((int) (stats.getMaxHealth() * .05), null);
                }
                if (playerMetadata.getStats().getCurrentMana() < stats.getMaxMana()) {
                    addMana((int) (stats.getMaxMana() * .03));
                }
            }
        }
    }

    private void processEffects() {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            for (String effectId : playerMetadata.getEffects()) {
                Effect effect = gameManager.getEntityManager().getEffectEntity(effectId);
                if (effect.getEffectApplications() >= effect.getMaxEffectApplications()) {
                    gameManager.getChannelUtils().write(playerId, effect.getEffectName() + " has worn off.\r\n", true);
                    gameManager.getEntityManager().removeEffect(effect);
                    playerMetadata.removeEffectId(effectId);
                } else {
                    effect.setEffectApplications(effect.getEffectApplications() + 1);
                    gameManager.getEffectsManager().application(effect, playerMetadata);
                    gameManager.getEntityManager().saveEffect(effect);
                }
            }
            savePlayerMetadata(playerMetadata);
        }
    }

    public void killPlayer(Npc npc) {
        synchronized (interner.intern(playerId)) {
            if (doesActiveFightExist(npc)) {
                removeAllActiveFights();
                if (!isActive(CoolDownType.DEATH)) {
                    CoolDown death = new CoolDown(CoolDownType.DEATH);
                    addCoolDown(death);
                    gameManager.writeToPlayerCurrentRoom(getPlayerId(), getPlayerName() + " is now dead." + "\r\n");
                    PlayerMovement playerMovement = new PlayerMovement(this, gameManager.getRoomManager().getPlayerCurrentRoom(this).get().getRoomId(), GameManager.LOBBY_ID, null, "vanished into the ether.", "");
                    movePlayer(playerMovement);
                    gameManager.currentRoomLogic(getPlayerId());
                    String prompt = gameManager.buildPrompt(playerId);
                    gameManager.getChannelUtils().write(getPlayerId(), prompt, true);
                }
            }
        }
    }

    public boolean updatePlayerHealth(int amount, Npc npc) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
            if (amount > 0) {
                addHealth(amount, playerMetadata);
                gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
                return false;
            } else {
                Stats stats = playerMetadata.getStats();
                if ((stats.getCurrentHealth() + amount) < 0) {
                    stats.setCurrentHealth(0);
                } else {
                    stats.setCurrentHealth(stats.getCurrentHealth() + amount);
                }
                gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
                playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
                if (playerMetadata.getStats().getCurrentHealth() == 0 && npc != null) {
                    killPlayer(npc);
                    return true;
                }
            }
        }
        return false;
    }

    private void addHealth(int addAmt, PlayerMetadata playerMetadata) {
        int currentHealth = playerMetadata.getStats().getCurrentHealth();
        Stats statsModifier = getPlayerStatsWithEquipmentAndLevel();
        int maxHealth = statsModifier.getMaxHealth();
        int proposedNewAmt = currentHealth + addAmt;
        if (proposedNewAmt > maxHealth) {
            if (currentHealth < maxHealth) {
                int adjust = proposedNewAmt - maxHealth;
                proposedNewAmt = proposedNewAmt - adjust;
            } else {
                proposedNewAmt = proposedNewAmt - addAmt;
            }
        }
        playerMetadata.getStats().setCurrentHealth(proposedNewAmt);
    }

    public void addMana(int addAmt) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            int currentMana = playerMetadata.getStats().getCurrentMana();
            Stats statsModifier = getPlayerStatsWithEquipmentAndLevel();
            int maxMana = statsModifier.getMaxMana();
            int proposedNewAmt = currentMana + addAmt;
            if (proposedNewAmt > maxMana) {
                if (currentMana < maxMana) {
                    int adjust = proposedNewAmt - maxMana;
                    proposedNewAmt = proposedNewAmt - adjust;
                } else {
                    proposedNewAmt = proposedNewAmt - addAmt;
                }
            }
            playerMetadata.getStats().setCurrentMana(proposedNewAmt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public int getCurrentHealth() {
        synchronized (interner.intern(playerId)) {
            return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentHealth();
        }
    }

    public void transferGoldToBank(int amt) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.transferGoldToBank(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void transferBankGoldToPlayer(int amt) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.transferBankGoldToPlayer(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void incrementGold(int amt) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.incrementGold(amt);
            savePlayerMetadata(playerMetadata);
        }
    }

    public boolean addEffect(String effectId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            if (playerMetadata.getEffects() != null && (playerMetadata.getEffects().size() >= playerMetadata.getStats().getMaxEffects())) {
                return false;
            }
            playerMetadata.addEffectId(effectId);
            savePlayerMetadata(playerMetadata);
            return true;
        }
    }

    public void addLockerInventoryId(String entityId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.addLockerEntityId(entityId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void removeLockerInventoryId(String lockerInventoryId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.removeLockerEntityId(lockerInventoryId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void addEquipmentId(String equipmentId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.addEquipmentEntityId(equipmentId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void removeEquipmentId(String equipmentId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.removeEquipmentEntityId(equipmentId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void addInventoryId(String inventoryId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.addInventoryEntityId(inventoryId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void removeInventoryId(String inventoryId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.removeInventoryEntityId(inventoryId);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void transferItemToLocker(String inventoryId) {
        synchronized (interner.intern(playerId)) {
            removeInventoryId(inventoryId);
            addLockerInventoryId(inventoryId);
        }
    }

    public void transferItemFromLocker(String entityId) {
        synchronized (interner.intern(playerId)) {
            if (gameManager.acquireItem(this, entityId)) {
                removeLockerInventoryId(entityId);
            }
        }
    }

    public void updatePlayerMana(int amount) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            Stats stats = playerMetadata.getStats();
            stats.setCurrentMana(stats.getCurrentMana() + amount);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void updatePlayerForageExperience(int amount) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            Stats stats = playerMetadata.getStats();
            stats.setForaging(stats.getForaging() + amount);
            savePlayerMetadata(playerMetadata);
        }
    }

    public void addCoolDown(CoolDown coolDown) {
        this.coolDowns.add(coolDown);
    }

    public boolean isActive(CoolDownType coolDownType) {
        for (CoolDown c : coolDowns) {
            if (c.getCoolDownType().equals(coolDownType)) {
                if (c.isActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void tickAllActiveCoolDowns() {
        Iterator<CoolDown> iterator = coolDowns.iterator();
        while (iterator.hasNext()) {
            CoolDown coolDown = iterator.next();
            if (coolDown.isActive()) {
                coolDown.decrementTick();
            } else {
                iterator.remove();
            }
        }
    }

    public void writePrompt() {
        String prompt = gameManager.buildPrompt(playerId);
        gameManager.getChannelUtils().write(playerId, prompt, true);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Optional<String> getReturnDirection() {
        return returnDirection;
    }

    public void setReturnDirection(Optional<String> returnDirection) {
        this.returnDirection = returnDirection;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void movePlayer(PlayerMovement playerMovement) {
        synchronized (interner.intern(playerId)) {
            Room sourceRoom = gameManager.getRoomManager().getRoom(playerMovement.getSourceRoomId());
            Room destinationRoom = gameManager.getRoomManager().getRoom(playerMovement.getDestinationRoomId());
            sourceRoom.removePresentPlayer(playerMovement.getPlayer().getPlayerId());
            for (Player next : gameManager.getRoomManager().getPresentPlayers(sourceRoom)) {
                StringBuilder sb = new StringBuilder();
                sb.append(playerMovement.getPlayer().getPlayerName());
                sb.append(" ").append(playerMovement.getRoomExitMessage());
                gameManager.getChannelUtils().write(next.getPlayerId(), sb.toString(), true);
            }
            destinationRoom.addPresentPlayer(playerMovement.getPlayer().getPlayerId());
            playerMovement.getPlayer().setCurrentRoom(destinationRoom);
            for (Player next : gameManager.getRoomManager().getPresentPlayers(destinationRoom)) {
                if (next.getPlayerId().equals(playerMovement.getPlayer().getPlayerId())) {
                    continue;
                }
                gameManager.getChannelUtils().write(next.getPlayerId(), playerMovement.getPlayer().getPlayerName() + " arrived.", true);
            }
        }
    }

    public Item getInventoryItem(String itemKeyword) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            for (String itemId : playerMetadata.getInventory()) {
                Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
                if (itemEntity == null) {
                    log.info("Orphaned inventoryId:" + itemId + " player: " + getPlayerName());
                    continue;
                }
                if (itemEntity.getItemTriggers().contains(itemKeyword)) {
                    return itemEntity;
                }
            }
            return null;
        }
    }

    public List<Item> getInventory() {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            List<Item> inventoryItems = Lists.newArrayList();
            List<String> inventory = playerMetadata.getInventory();
            if (inventory != null) {
                for (String itemId : inventory) {
                    Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
                    if (itemEntity == null) {
                        log.info("Orphaned inventoryId:" + itemId + " player: " + getPlayerName());
                        continue;
                    }
                    inventoryItems.add(itemEntity);
                }
            }
            Collections.sort(inventoryItems, new Comparator<Item>() {
                @Override
                public int compare(final Item object1, final Item object2) {
                    return object1.getItemName().compareTo(object2.getItemName());
                }
            });
            return inventoryItems;
        }
    }

    public List<Item> getLockerInventory() {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            List<Item> inventoryItems = Lists.newArrayList();
            List<String> inventory = playerMetadata.getLockerInventory();
            if (inventory != null) {
                for (String itemId : inventory) {
                    Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
                    if (itemEntity == null) {
                        log.info("Orphaned inventoryId:" + itemId + " player: " + getPlayerName());
                        continue;
                    }
                    inventoryItems.add(itemEntity);
                }
            }
            Collections.sort(inventoryItems, new Comparator<Item>() {
                @Override
                public int compare(final Item object1, final Item object2) {
                    return object1.getItemName().compareTo(object2.getItemName());
                }
            });
            return inventoryItems;
        }
    }

    public List<String> getRolledUpLockerInventory() {
        synchronized (interner.intern(playerId)) {
            List<String> rolledUp = Lists.newArrayList();
            List<Item> inventory = getLockerInventory();
            Map<String, Integer> itemAndCounts = Maps.newHashMap();
            if (inventory != null) {
                for (Item item : inventory) {
                    StringBuilder invItem = new StringBuilder();
                    invItem.append(item.getItemName());
                    int maxUses = ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses();
                    if (maxUses > 0) {
                        int remainingUses = maxUses - item.getNumberOfUses();
                        invItem.append(" - ").append(remainingUses);
                        if (remainingUses == 1) {
                            invItem.append(" use left.");
                        } else {
                            invItem.append(" uses left.");
                        }
                    }
                    if (itemAndCounts.containsKey(invItem.toString())) {
                        Integer integer = itemAndCounts.get(invItem.toString());
                        integer = integer + 1;
                        itemAndCounts.put(invItem.toString(), integer);
                    } else {
                        itemAndCounts.put(invItem.toString(), 1);
                    }
                }
                StringBuilder inventoryLine = new StringBuilder();
                for (Map.Entry<String, Integer> next : itemAndCounts.entrySet()) {
                    if (next.getValue() > 1) {
                        inventoryLine.append(next.getKey()).append(" (").append(next.getValue()).append(")").append("\r\n");
                    } else {
                        inventoryLine.append(next.getKey()).append("\r\n");
                    }
                }
                rolledUp.add(inventoryLine.toString());
            }
            return rolledUp;
        }
    }

    public List<String> getRolledUpIntentory() {
        synchronized (interner.intern(playerId)) {
            List<String> rolledUp = Lists.newArrayList();
            List<Item> inventory = getInventory();
            Map<String, Integer> itemAndCounts = Maps.newHashMap();
            if (inventory != null) {
                for (Item item : inventory) {
                    StringBuilder invItem = new StringBuilder();
                    invItem.append(item.getItemName());
                    int maxUses = ItemType.itemTypeFromCode(item.getItemTypeId()).getMaxUses();
                    if (maxUses > 0) {
                        int remainingUses = maxUses - item.getNumberOfUses();
                        invItem.append(" - ").append(remainingUses);
                        if (remainingUses == 1) {
                            invItem.append(" use left.");
                        } else {
                            invItem.append(" uses left.");
                        }
                    }
                    if (itemAndCounts.containsKey(invItem.toString())) {
                        Integer integer = itemAndCounts.get(invItem.toString());
                        integer = integer + 1;
                        itemAndCounts.put(invItem.toString(), integer);
                    } else {
                        itemAndCounts.put(invItem.toString(), 1);
                    }
                }
                StringBuilder inventoryLine = new StringBuilder();
                for (Map.Entry<String, Integer> next : itemAndCounts.entrySet()) {
                    if (next.getValue() > 1) {
                        inventoryLine.append(next.getKey()).append(" (").append(next.getValue()).append(")").append("\r\n");
                    } else {
                        inventoryLine.append(next.getKey()).append("\r\n");
                    }
                }
                rolledUp.add(inventoryLine.toString());
            }
            return rolledUp;
        }
    }

    public Set<Item> getEquipment(){
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            Set<Item> equipmentItems = Sets.newHashSet();
            String[] equipment = playerMetadata.getPlayerEquipment();
            if (equipment != null) {
                for (String itemId : equipment) {
                    Item itemEntity = gameManager.getEntityManager().getItemEntity(itemId);
                    if (itemEntity == null) {
                        log.info("Orphaned equipmentId:" + itemId + " player: " + getPlayerName());
                        continue;
                    }
                    equipmentItems.add(itemEntity);
                }
            }
            return equipmentItems;
        }
    }

    public void equip(Item item) {
        synchronized (interner.intern(playerId)) {
            if (item.getEquipment() == null) {
                return;
            }
            Equipment equipment = item.getEquipment();
            EquipmentSlotType equipmentSlotType = equipment.getEquipmentSlotType();
            Item slotItem = getSlotItem(equipmentSlotType);
            if (slotItem != null) {
                if (!unEquip(slotItem)) {
                    return;
                }
            }
            gameManager.getChannelUtils().write(playerId, "Equipping " + item.getItemName() + "\r\n");
            addEquipmentId(item.getItemId());
            removeInventoryId(item.getItemId());
        }
    }

    public Item getSlotItem(EquipmentSlotType slot) {
        PlayerMetadata playerMetadata = getPlayerMetadata();
        if (playerMetadata.getPlayerEquipment() == null) {
            return null;
        }
        for (String item : playerMetadata.getPlayerEquipment()) {
            Item itemEntity = gameManager.getEntityManager().getItemEntity(item);
            EquipmentSlotType equipmentSlotType = itemEntity.getEquipment().getEquipmentSlotType();
            if (equipmentSlotType.equals(slot)) {
                return itemEntity;
            }
        }
        return null;
    }

    public boolean unEquip(Item item) {
        synchronized (interner.intern(playerId)) {
            gameManager.getChannelUtils().write(playerId, "Un-equipping " + item.getItemName() + "\r\n");
            if (gameManager.acquireItem(this, item.getItemId())) {
                removeEquipmentId(item.getItemId());
                return true;
            }
            return false;
        }
    }

    public Stats getPlayerStatsWithEquipmentAndLevel() {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            StatsBuilder statsBuilder = new StatsBuilder();
            Stats newStats = statsBuilder.createStats();
            Stats playerStats = gameManager.getStatsModifierFactory().getStatsModifier(this);
            StatsHelper.combineStats(newStats, playerStats);
            String[] playerEquipment = playerMetadata.getPlayerEquipment();
            if (playerEquipment == null) {
                return playerStats;
            }
            for (String equipId : playerEquipment) {
                Item itemEntity = gameManager.getEntityManager().getItemEntity(equipId);
                Equipment equipment = itemEntity.getEquipment();
                Stats stats = equipment.getStats();
                StatsHelper.combineStats(newStats, stats);
            }
            if (playerMetadata.getEffects() != null) {
                for (String effectId : playerMetadata.getEffects()) {
                    Effect effect = gameManager.getEntityManager().getEffectEntity(effectId);
                    StatsHelper.combineStats(newStats, effect.getDurationStats());
                }
            }
            return newStats;
        }
    }

    /* FIGHT FIGHT FIGHT FIGHT */

    public void removeAllActiveFights() {
        synchronized (interner.intern(playerId)) {
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                iterator.remove();
            }
        }
    }

    public void removeActiveFight(Npc npc) {
        synchronized (interner.intern(playerId)) {
            System.out.println("Removing active fight for: " + npc.getColorName());
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                if (next.getValue().getNpcId().equals(npc.getEntityId())) {
                    if (next.getValue().isPrimary) {
                    }
                    iterator.remove();
                }
            }
        }
    }

    public boolean addActiveFight(Npc npc) {
        synchronized (interner.intern(playerId)) {
            if (gameManager.getEntityManager().getNpcEntity(npc.getEntityId()) != null) {
                if (!doesActiveFightExist(npc)) {
                    ActiveFight activeFight = new ActiveFight(npc.getEntityId(), false);
                    activeFights.put(System.nanoTime(), activeFight);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean doesActiveFightExist(Npc npc) {
        synchronized (interner.intern(playerId)) {
            if (gameManager.getEntityManager().getNpcEntity(npc.getEntityId()) == null) {
                removeActiveFight(npc);
            }
            for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
                ActiveFight fight = entry.getValue();
                if (fight.getNpcId().equals(npc.getEntityId())) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isActiveFights() {
        synchronized (interner.intern(playerId)) {
            if (activeFights.size() > 0) {
                Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Long, ActiveFight> next = iterator.next();
                    if (gameManager.getEntityManager().getNpcEntity(next.getValue().getNpcId()) == null) {
                        iterator.remove();
                    }
                }
            }
        }
        return activeFights.size() > 0;
    }

    public boolean isValidPrimaryActiveFight(Npc npc) {
        synchronized (interner.intern(playerId)) {
            for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
                ActiveFight fight = entry.getValue();
                if (fight.getNpcId().equals(npc.getEntityId()) && fight.isPrimary) {
                    return true;
                }
            }
            return false;
        }
    }

    public String getPrimaryActiveFight() {
        synchronized (interner.intern(playerId)) {
            for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
                ActiveFight fight = entry.getValue();
                if (fight.isPrimary) {
                    return fight.getNpcId();
                }
            }
            return null;
        }
    }

    public void activateNextPrimaryActiveFight() {
        synchronized (interner.intern(playerId)) {
            if (getPrimaryActiveFight() == null) {
                if (activeFights.size() > 0) {
                    activeFights.get(activeFights.firstKey()).setIsPrimary(true);
                }
            }
        }
    }

    private void doFightRound(ActiveFight activeFight) {
        Npc npc = gameManager.getEntityManager().getNpcEntity(activeFight.getNpcId());
        if (npc == null) {
            return;
        }
        Stats npcStats = npc.getStats();
        Stats playerStats = getPlayerStatsWithEquipmentAndLevel();
        NpcStatsChangeBuilder npcStatsChangeBuilder = new NpcStatsChangeBuilder().setPlayer(this);
        if (this.isValidPrimaryActiveFight(npc)) {
            int damageToVictim = 0;
            int chanceToHit = getChanceToHit(playerStats, npcStats);
            if (randInt(0, 100) < chanceToHit) {
                damageToVictim = getAttackAmt(playerStats, npcStats);
            }
            if (damageToVictim > 0) {
                final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET +  Color.YELLOW + "+" + damageToVictim + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npc.getColorName();
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            } else {
                final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + "You MISS " + npc.getName() + "!";
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            }
        }
        if (this.doesActiveFightExist(npc)) {
            int chanceToHitBack = getChanceToHit(npcStats, playerStats);
            int damageBack = getAttackAmt(npcStats, playerStats);
            if (randInt(0, 100) < chanceToHitBack) {
                final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + npc.getColorName() + Color.BOLD_ON + Color.RED + " DAMAGES" + Color.RESET + " you for " + Color.RED + "-" + damageBack + Color.RESET;
                npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(-damageBack).createStats());
                npcStatsChangeBuilder.setPlayerDamageStrings(Arrays.asList(fightMsg));

            } else {
                final String fightMsg = Color.BOLD_ON + Color.RED + "[attack] " + Color.RESET + npc.getColorName() + Color.BOLD_ON + Color.CYAN + " MISSES" + Color.RESET + " you!";
                npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(0).createStats());
                npcStatsChangeBuilder.setPlayerDamageStrings(Arrays.asList(fightMsg));
            }
            npc.addNpcDamage(npcStatsChangeBuilder.createNpcStatsChange());
        }
    }

    private int getChanceToHit(Stats challenger, Stats victim) {
        return (challenger.getStrength() + challenger.getMeleSkill()) * 5 - victim.getAgile() * 5;
    }

    private int getAttackAmt(Stats challenger, Stats victim) {
        int rolls = 0;
        int totDamage = 0;
        while (rolls <= challenger.getNumberOfWeaponRolls()) {
            rolls++;
            totDamage = totDamage + randInt(challenger.getWeaponRatingMin(), challenger.getWeaponRatingMax());
        }
        int i = challenger.getStrength() + totDamage - victim.getArmorRating();
        if (i < 0) {
            return 0;
        } else {
            return i;
        }
    }

    public SortedMap<Long, ActiveFight> getActiveFights() {
        return activeFights;
    }

    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    class ActiveFight {
        private final String npcId;
        private boolean isPrimary;

        public ActiveFight(String npcId, boolean isPrimary) {
            this.npcId = npcId;
            this.isPrimary = isPrimary;
        }

        public String getNpcId() {
            return npcId;
        }

        public boolean isPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(boolean isPrimary) {
            this.isPrimary = isPrimary;
        }

    }

    private PlayerMetadata getPlayerMetadata() {
        return gameManager.getPlayerManager().getPlayerMetadata(playerId);
    }

    private void savePlayerMetadata(PlayerMetadata playerMetadata) {
        gameManager.getPlayerManager().savePlayerMetadata(playerMetadata);
    }

}
