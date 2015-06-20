package com.comandante.creeper.player;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import org.apache.commons.codec.binary.Base64;
import org.jboss.netty.channel.Channel;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends CreeperEntity {

    private String playerName;
    private Channel channel;
    private Optional<String> returnDirection = Optional.absent();
    private final GameManager gameManager;
    private Room currentRoom;
    private Set<ActiveFight> activeFights = Collections.newSetFromMap(new ConcurrentHashMap<ActiveFight, Boolean>());;

    public Player(String playerName, GameManager gameManager) {
        this.playerName = playerName;
        this.gameManager = gameManager;
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
        for (String effectId: playerMetadata.getEffects()) {
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

    public void addActiveFight(Npc npc, boolean isPrimary) {
        activeFights.add(new ActiveFight(npc.getEntityId(), isPrimary));
        if (isPrimary) {
            for (ActiveFight fight : activeFights) {
                if (fight.isPrimary) {
                    fight.setIsPrimary(false);
                }
            }
        }
    }

    public boolean isValidFight(Npc npc) {
        for (ActiveFight fight: activeFights){
            if (fight.getNpcId().equals(npc.getEntityId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidPrimaryFight(Npc npc) {
        for (ActiveFight fight: activeFights){
            if (fight.getNpcId().equals(npc.getEntityId()) && fight.isPrimary) {
                return true;
            }
        }
        return false;
    }

    public String getPrimaryFight(){
        for (ActiveFight fight: activeFights) {
            if (fight.isPrimary) {
                return fight.getNpcId();
            }
        }
        return null;
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
