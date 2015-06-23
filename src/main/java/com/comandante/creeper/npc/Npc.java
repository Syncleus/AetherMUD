package com.comandante.creeper.npc;


import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.world.Area;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.comandante.creeper.server.Color.RED;
import static com.comandante.creeper.server.Color.RESET;


public class Npc extends CreeperEntity {

    private long lastPhraseTimestamp;
    private final GameManager gameManager;
    private final String name;
    private final String colorName;
    private final Stats stats;
    private final String dieMessage;
    private final Set<Area> roamAreas;
    private final Set<String> validTriggers;
    private Loot loot;
    private final Set<SpawnRule> spawnRules;
    private final AtomicBoolean isInFight = new AtomicBoolean(false);
    private final Random random = new Random();
    private List<Effect> effects = Lists.newCopyOnWriteArrayList();
    private int maxEffects = 4;
    private Map<String, Integer> playerDamageMap = Maps.newHashMap();
    private Room currentRoom;

    protected Npc(GameManager gameManager, String name, String colorName, long lastPhraseTimestamp, Stats stats, String dieMessage, Set<Area> roamAreas, Set<String> validTriggers, Loot loot, Set<SpawnRule> spawnRules) {
        this.gameManager = gameManager;
        this.name = name;
        this.colorName = colorName;
        this.lastPhraseTimestamp = lastPhraseTimestamp;
        this.stats = stats;
        this.dieMessage = dieMessage;
        this.roamAreas = roamAreas;
        this.validTriggers = validTriggers;
        this.loot = loot;
        this.spawnRules = spawnRules;
    }

    @Override
    public void run() {
        if (randInt(0, 100) < 1) {
            if (!isInFight.get() && roamAreas.size() > 0) {
                NpcMover npcMover = new NpcMover();
                npcMover.roam(getGameManager(), getEntityId());
            }
        }
        for (Effect effect: effects) {
            if (effect.getTicks() >= effect.getLifeSpanTicks()) {
                Optional<Room> npcCurrentRoom = gameManager.getRoomManager().getNpcCurrentRoom(this);
                if (npcCurrentRoom.isPresent()) {
                    Room room = npcCurrentRoom.get();
                    gameManager.writeToRoom(room.getRoomId(), effect.getEffectName() + " has worn off of " + getName() + "\r\n");
                }
                gameManager.getEffectsManager().removeDurationStats(effect, this);
                gameManager.getEntityManager().removeEffect(effect);
                effects.remove(effect);
            } else {
                effect.setTicks(effect.getTicks() + 1);
                gameManager.getEffectsManager().applyEffectStatsOnTick(effect, this);
                gameManager.getEntityManager().saveEffect(effect);
            }
        }
    }

    public void setLastPhraseTimestamp(long lastPhraseTimestamp) {
        this.lastPhraseTimestamp = lastPhraseTimestamp;
    }

    public String getColorName() {
        return colorName;
    }

    public Set<String> getValidTriggers() {
        return validTriggers;
    }

    public Set<Area> getRoamAreas() {
        return roamAreas;
    }

    public boolean getIsInFight() {
        return this.isInFight.get();
    }

    public void setIsInFight(boolean isInFight) {
        this.isInFight.set(isInFight);
    }

    public Stats getStats() {
        return stats;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public long getLastPhraseTimestamp() {
        return lastPhraseTimestamp;
    }

    public String getName() {
        return name;
    }

    public String getDieMessage() {
        return dieMessage;
    }

    public void npcSay(Integer roomId, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(RED);
        sb.append(name).append(": ").append(message);
        sb.append(RESET);
    }

    public Loot getLoot() {
        return loot;
    }

    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public Set<SpawnRule> getSpawnRules() {
        return spawnRules;
    }

    public void addEffect(Effect effect) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(getEntityId())) {
            if (effects.size() >= maxEffects) {
                return;
            } else {
                effects.add(effect);
            }
        }
    }

    public void remoteEffect(Effect effect) {
        effects.remove(effect);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setMaxEffects(int maxEffects) {
        this.maxEffects = maxEffects;
    }

    public int getExperience(int playerLevel) {
        return getStats().getExperience();
    }

    public double getPctOFExperience(double pct, int playerLevel) {
        return getExperience(playerLevel) * pct;
    }

    public void addDamageToMap(String playerId, int amt) {
        playerDamageMap.put(playerId, amt);
    }

    public Map<String, Integer> getPlayerDamageMap() {
        return playerDamageMap;
    }

    public void setLoot(Loot loot) {
        this.loot = loot;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}
