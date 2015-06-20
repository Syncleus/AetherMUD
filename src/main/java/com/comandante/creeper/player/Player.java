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

    public Player(String playerName, GameManager gameManager) {
        this.playerName = playerName;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        PlayerMetadata playerMetadata = gameManager.getPlayerManager().getPlayerMetadata(this.getPlayerId());
        Stats stats = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(this);
        if (playerMetadata.getStats().getCurrentHealth() < stats.getMaxHealth()) {
            gameManager.addHealth(this, (int) (stats.getMaxHealth() * .05));
        }
        if (playerMetadata.getStats().getCurrentMana() < stats.getMaxMana()) {
            gameManager.addMana(this, (int) (stats.getMaxMana() * .03));
        }
        for (String effectId : playerMetadata.getEffects()) {
            Effect effect = gameManager.getEntityManager().getEffect(effectId);
            if (effect.getTicks() >= effect.getLifeSpanTicks()) {
                gameManager.getChannelUtils().write(getPlayerId(), effect.getEffectName() + " has worn off.\r\n", true);
                gameManager.getEntityManager().removeEffect(effect);
                gameManager.getPlayerManager().removeEffect(this, effectId);
            } else {
                effect.setTicks(effect.getTicks() + 1);
                gameManager.getEffectsManager().applyEffectStatsOnTick(effect, playerMetadata);
                gameManager.getEntityManager().saveEffect(effect);
            }
        }
    }

    public void removeAllActiveFights() {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(getPlayerId())) {
            Iterator<Map.Entry<Long, ActiveFight>> iterator = activeFights.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, ActiveFight> next = iterator.next();
                iterator.remove();
            }
        }
    }

    public void removeActiveFight(Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(getPlayerId())) {
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

    public void addActiveFight(Npc npc) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(getPlayerId())) {
            ActiveFight activeFight = new ActiveFight(npc.getEntityId(), false);
            activeFights.put(System.currentTimeMillis(), activeFight);
            activateNextPrimaryActiveFight();
        }
    }

    public boolean doesActiveFightExist(Npc npc) {
        for(Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
            ActiveFight fight = entry.getValue();
            if (fight.getNpcId().equals(npc.getEntityId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isActiveFights() {
        return activeFights.size() > 0;
    }

    public boolean isValidPrimaryActiveFight(Npc npc) {
        for(Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
            ActiveFight fight = entry.getValue();
            if (fight.getNpcId().equals(npc.getEntityId()) && fight.isPrimary) {
                return true;
            }
        }
        return false;
    }

    public String getPrimaryActiveFight() {
        for(Map.Entry<Long, ActiveFight> entry : activeFights.entrySet()) {
            ActiveFight fight = entry.getValue();
            if (fight.isPrimary) {
                return fight.getNpcId();
            }
        }
        return null;
    }

    public void activateNextPrimaryActiveFight() {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(getPlayerId())) {
            if (getPrimaryActiveFight() == null) {
                if (activeFights.size() > 0) {
                    activeFights.get(activeFights.firstKey()).setIsPrimary(true);
                }
            }
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerId() {
        return new String(Base64.encodeBase64(playerName.getBytes()));
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
