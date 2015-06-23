package com.comandante.creeper.player;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
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

    public Player(String playerName, GameManager gameManager) {
        this.playerName = playerName;
        this.playerId = new String(Base64.encodeBase64(playerName.getBytes()));
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(playerId);
        Stats stats = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(this);
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
            if (effect.getTicks() >= effect.getLifeSpanTicks()) {
                gameManager.getChannelUtils().write(playerId, effect.getEffectName() + " has worn off.\r\n", true);
                gameManager.getEntityManager().removeEffect(effect);
                gameManager.getPlayerManager().removeEffect(this, effectId);
            } else {
                effect.setTicks(effect.getTicks() + 1);
                gameManager.getEffectsManager().applyEffectStatsOnTick(effect, playerMetadata);
                gameManager.getEntityManager().saveEffect(effect);
            }
        }
        tickAllActiveCoolDowns();
    }

    public void removeAllActiveFights() {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                iterator.remove();
            }
        }
    }

    public void removeActiveFight(Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            boolean resetFights = false;
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                if (next.getValue().getNpcId().equals(npc.getEntityId())) {
                    if (next.getValue().isPrimary) {
                        resetFights = true;
                    }
                    iterator.remove();
                }
            }
            if (resetFights) {
                activateNextPrimaryActiveFight();
            }
        }
    }

    public boolean addActiveFight(Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            if (gameManager.getEntityManager().getNpcEntity(npc.getEntityId()) != null) {
                ActiveFight activeFight = new ActiveFight(npc.getEntityId(), false);
                activeFights.put(System.currentTimeMillis(), activeFight);
                activateNextPrimaryActiveFight();
                return true;
            }
        }
        return false;
    }

    public boolean doesActiveFightExist(Npc npc) {
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

    public boolean isActiveFights() {
        Interner<String> interner = Interners.newWeakInterner();
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
        for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
            ActiveFight fight = entry.getValue();
            if (fight.getNpcId().equals(npc.getEntityId()) && fight.isPrimary) {
                return true;
            }
        }
        return false;
    }

    public String getPrimaryActiveFight() {
        for (Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
            ActiveFight fight = entry.getValue();
            if (fight.isPrimary) {
                return fight.getNpcId();
            }
        }
        return null;
    }

    public void activateNextPrimaryActiveFight() {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            if (getPrimaryActiveFight() == null) {
                if (activeFights.size() > 0) {
                    activeFights.get(activeFights.firstKey()).setIsPrimary(true);
                }
            }
        }
    }

    public void killPlayer(Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
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
                    String prompt = gameManager.buildPrompt(getPlayerId());
                    gameManager.getChannelUtils().write(getPlayerId(), prompt, true);
                }
            }
        }
    }

    public boolean updatePlayerHealth(int amount, Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
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
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerId)) {
            return gameManager.getPlayerManager().getPlayerMetadata(playerId).getStats().getCurrentHealth();
        }
    }

    public void addCoolDown(CoolDown coolDown) {
        this.coolDowns.add(coolDown);
    }

    public boolean isActive(CoolDownType coolDownType) {
        for (CoolDown c: coolDowns) {
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
}
