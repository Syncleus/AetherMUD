package com.comandante.creeper.player;


import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import org.apache.commons.codec.binary.Base64;
import org.jboss.netty.channel.Channel;

public class Player extends CreeperEntity {

    private String playerName;
    private Channel channel;
    private Optional<String> returnDirection = Optional.absent();
    private final GameManager gameManager;
    private Room currentRoom;

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

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
        Stats stats = gameManager.getStatsModifierFactory().getStatsModifier(this);
        if (playerMetadata.getStats().getCurrentHealth() < stats.getMaxHealth()) {
            gameManager.addHealth(this, (int) (stats.getMaxHealth() * .05));
        }
        if (playerMetadata.getStats().getCurrentMana() < stats.getMaxMana()) {
            gameManager.addMana(this, (int) (stats.getMaxMana() * .03));
        }
        for (String effectId: playerMetadata.getEffects()) {
            Effect effect = gameManager.getEntityManager().getEffect(effectId);
            gameManager.getEffectsManager().applyEffectStatsOnTick(effect, playerMetadata);
            effect.setTicks(effect.getTicks() + 1);
            if (effect.getTicks() >= effect.getLifeSpanTicks()) {
                gameManager.getEffectsManager().removeDurationStats(effect, playerMetadata);
                gameManager.getEntityManager().removeEffect(effect);
            } else {
                gameManager.getEntityManager().saveEffect(effect);
            }
        }
    }

    public static void main(String[] args) {
        int test = 2297;

       int result = (int) (2297 * .05);
        System.out.println(result);
    }
}
