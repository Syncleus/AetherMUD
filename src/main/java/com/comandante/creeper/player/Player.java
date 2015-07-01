package com.comandante.creeper.player;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcStatsChangeBuilder;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.apache.commons.codec.binary.Base64;
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

    public Player(String playerName, GameManager gameManager) {
        this.playerName = playerName;
        this.playerId = new String(Base64.encodeBase64(playerName.getBytes()));
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        Stats stats = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(this);
        if (tickBucket == 10) {
            if (!isActive(CoolDownType.DEATH)) {
                if (playerMetadata.getStats().getCurrentHealth() < stats.getMaxHealth()) {
                    updatePlayerHealth((int) (stats.getMaxHealth() * .05), null);
                }
                if (playerMetadata.getStats().getCurrentMana() < stats.getMaxMana()) {
                    gameManager.addMana(this, (int) (stats.getMaxMana() * .03));
                }
            }
            for (String effectId : playerMetadata.getEffects()) {
                Effect effect = gameManager.getEntityManager().getEffect(effectId);
                if (effect.getEffectApplications() >= effect.getMaxEffectApplications()) {
                    gameManager.getChannelUtils().write(playerId, effect.getEffectName() + " has worn off.\r\n", true);
                    gameManager.getEntityManager().removeEffect(effect);
                    removeEffect(effectId);
                } else {
                    effect.setEffectApplications(effect.getEffectApplications() + 1);
                    gameManager.getEffectsManager().application(effect, playerMetadata);
                    gameManager.getEntityManager().saveEffect(effect);
                }
            }
            tickBucket = 0;
            if (activeFights.size() > 0) {
                writePrompt();
            }
        } else {
            tickBucket = tickBucket + 1;
        }
        tickAllActiveCoolDowns();
        activateNextPrimaryActiveFight();
        if (fightTickBucket == 4) {
            for (ActiveFight activeFight : activeFights.values()) {
                Npc npcEntity = gameManager.getEntityManager().getNpcEntity(activeFight.getNpcId());
                if (npcEntity != null) {
                    System.out.println("do fight for " + npcEntity.getColorName());
                    doFightRound(this, npcEntity);
                }
            }
            fightTickBucket = 0;
        } else {
            fightTickBucket = fightTickBucket + 1;
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
                    gameManager.movePlayer(playerMovement);
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
        Stats statsModifier = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(this);
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

    public void removeEffect(String effectId) {
        synchronized (interner.intern(playerId)) {
            PlayerMetadata playerMetadata = getPlayerMetadata();
            playerMetadata.removeEffectId(effectId);
            savePlayerMetadata(playerMetadata);
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
                    System.out.println("Adding active fight for: " + npc.getColorName());
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

    private void doFightRound(Player player, Npc npc) {
        Stats npcStats = npc.getStats();
        Stats playerStats = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(player);
        NpcStatsChangeBuilder npcStatsChangeBuilder = new NpcStatsChangeBuilder().setPlayer(player);
        if (player.isValidPrimaryActiveFight(npc)) {
            int damageToVictim = 0;
            int chanceToHit = getChanceToHit(playerStats, npcStats);
            if (randInt(0, 100) < chanceToHit) {
                damageToVictim = getAttackAmt(playerStats, npcStats);
            }
            if (damageToVictim > 0) {
                final String fightMsg = Color.YELLOW + "+" + damageToVictim + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + npc.getColorName();
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            } else {
                final String fightMsg = "You MISS " + npc.getName() + "!";
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            }
        }
        if (player.doesActiveFightExist(npc)) {
            int chanceToHitBack = getChanceToHit(npcStats, playerStats);
            int damageBack = getAttackAmt(npcStats, playerStats);
            if (randInt(0, 100) < chanceToHitBack) {
                final String fightMsg = npc.getColorName() + Color.BOLD_ON + Color.RED + " DAMAGES" + Color.RESET + " you for " + Color.RED + "-" + damageBack + Color.RESET;
                npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(-damageBack).createStats());
                npcStatsChangeBuilder.setPlayerDamageStrings(Arrays.asList(fightMsg));

            } else {
                final String fightMsg = npc.getColorName() + Color.BOLD_ON + Color.CYAN + " MISSES" + Color.RESET + " you!";
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
