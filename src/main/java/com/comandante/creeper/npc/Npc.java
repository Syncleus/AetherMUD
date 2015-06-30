package com.comandante.creeper.npc;


import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.Items.Rarity;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.spells.Effect;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.stat.StatsHelper;
import com.comandante.creeper.world.Area;
import com.comandante.creeper.world.Room;
import com.google.common.base.Optional;
import com.google.common.collect.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
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
    private final ArrayBlockingQueue<NpcStatsChange> npcStatsChanges = new ArrayBlockingQueue<NpcStatsChange>(3000);
    private final Set<Player> activeFights = Sets.newConcurrentHashSet();


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
        for (Effect effect : effects) {
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
        for (Player player : activeFights) {
            doFight(player);
        }
        List<NpcStatsChange> npcStatsChangeList = Lists.newArrayList();
        npcStatsChanges.drainTo(npcStatsChangeList);
        for (NpcStatsChange npcStatsChange : npcStatsChangeList) {
            processNpcStatChange(npcStatsChange);
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

    public void addNpcDamage(NpcStatsChange npcStatsChange) {
        this.npcStatsChanges.add(npcStatsChange);
    }

    public void doHealthDamage(Player player, List<String> damageStrings, int amt) {
        NpcStatsChange npcStatsChange =
                new NpcStatsChangeBuilder().setStats(new StatsBuilder().setCurrentHealth(amt).createStats()).setDamageStrings(damageStrings).setPlayer(player).createNpcStatsChange();
        addNpcDamage(npcStatsChange);
    }

    private void processNpcStatChange(NpcStatsChange npcStatsChange) {
        if (!activeFights.contains(npcStatsChange.getPlayer())) {
            activeFights.add(npcStatsChange.getPlayer());
            npcStatsChange.getPlayer().addActiveFight(this);
        }
        for (String message : npcStatsChange.getDamageStrings()) {
            gameManager.getChannelUtils().write(npcStatsChange.getPlayer().getPlayerId(), message + "\r\n");
        }
        StatsHelper.combineStats(getStats(), npcStatsChange.getStats());
        int amt = npcStatsChange.getStats().getCurrentHealth();
        int damageReportAmt = -npcStatsChange.getStats().getCurrentHealth();
        if (getStats().getCurrentHealth() < 0) {
            damageReportAmt = -amt + getStats().getCurrentHealth();
            getStats().setCurrentHealth(0);
        }
        int damage = 0;
        if (getPlayerDamageMap().containsKey(npcStatsChange.getPlayer().getPlayerId())) {
            damage = getPlayerDamageMap().get(npcStatsChange.getPlayer().getPlayerId());
        }
        addDamageToMap(npcStatsChange.getPlayer().getPlayerId(), damage + damageReportAmt);
        if (getStats().getCurrentHealth() == 0) {
            killNpc(npcStatsChange.getPlayer());
            return;
        }
        for (String message : npcStatsChange.getPlayerDamageStrings()) {
            gameManager.getChannelUtils().write(npcStatsChange.getPlayer().getPlayerId(), message + "\r\n");
            if (npcStatsChange.getPlayer().updatePlayerHealth(npcStatsChange.getPlayerStatsChange().getCurrentHealth(), this)) {
                activeFights.remove(npcStatsChange.getPlayer());
            }
        }
    }

    private void doFight(Player player) {
        Stats npcStats = getStats();
        Stats playerStats = gameManager.getEquipmentManager().getPlayerStatsWithEquipmentAndLevel(player);
        NpcStatsChangeBuilder npcStatsChangeBuilder = new NpcStatsChangeBuilder().setPlayer(player);
        if (player.isValidPrimaryActiveFight(this)) {
            int damageToVictim = 0;
            int chanceToHit = getChanceToHit(playerStats, npcStats);
            if (randInt(0, 100) < chanceToHit) {
                damageToVictim = getAttackAmt(playerStats, npcStats);
            }
            if (damageToVictim > 0) {
                final String fightMsg = Color.YELLOW + "+" + damageToVictim + Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + getColorName();
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));
            } else {
                final String fightMsg = "You MISS " + getName() + "!";
                npcStatsChangeBuilder.setStats(new StatsBuilder().setCurrentHealth(-damageToVictim).createStats());
                npcStatsChangeBuilder.setDamageStrings(Arrays.asList(fightMsg));;
            }
        }
        if (player.doesActiveFightExist(this)) {
            int chanceToHitBack = getChanceToHit(npcStats, playerStats);
            int damageBack = getAttackAmt(npcStats, playerStats);
            if (randInt(0, 100) < chanceToHitBack) {
                final String fightMsg = getColorName() + Color.BOLD_ON + Color.RED + " DAMAGES" + Color.RESET + " you for " + Color.RED + "-" + damageBack + Color.RESET;
                npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(-damageBack).createStats());
                npcStatsChangeBuilder.setPlayerDamageStrings(Arrays.asList(fightMsg));

            } else {
                final String fightMsg = getColorName() + Color.BOLD_ON + Color.CYAN + " MISSES" + Color.RESET + " you!";
                npcStatsChangeBuilder.setPlayerStatsChange(new StatsBuilder().setCurrentHealth(0).createStats());
                npcStatsChangeBuilder.setPlayerDamageStrings(Arrays.asList(fightMsg));
            }
            addNpcDamage(npcStatsChangeBuilder.createNpcStatsChange());
        } else {
            activeFights.remove(player);
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

    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private void killNpc(Player player) {
        Map<String, Double> xpProcessed = null;
        Item corpse = new Item(getName() + " corpse", "a bloody corpse.", Arrays.asList("corpse", "c"), "a corpse lies on the ground.", UUID.randomUUID().toString(), Item.CORPSE_ID_RESERVED, 0, false, 120, Rarity.BASIC, 0, getLoot());
        gameManager.writeToPlayerCurrentRoom(player.getPlayerId(), getDieMessage());
        xpProcessed = gameManager.processExperience(this, getCurrentRoom());
        gameManager.getEntityManager().saveItem(corpse);
        Integer roomId = gameManager.getRoomManager().getNpcCurrentRoom(this).get().getRoomId();
        Room room = gameManager.getRoomManager().getRoom(roomId);
        room.addPresentItem(corpse.getItemId());
        gameManager.getItemDecayManager().addItem(corpse);
        getCurrentRoom().removePresentNpc(getEntityId());
        gameManager.getEntityManager().deleteNpcEntity(getEntityId());
        player.removeActiveFight(this);
        for (Map.Entry<String, Double> playerDamageExperience : xpProcessed.entrySet()) {
            Player p = gameManager.getPlayerManager().getPlayer(playerDamageExperience.getKey());
            if (p == null) {
                continue;
            }
            int xpEarned = (int) Math.round(playerDamageExperience.getValue());
            gameManager.addExperience(p, xpEarned);
            gameManager.getChannelUtils().write(p.getPlayerId(), "You killed a " + getColorName() + " for " + Color.GREEN + "+" + xpEarned + Color.RESET + " experience points." + "\r\n", true);
        }
    }

    public void addFight(Player player) {
        this.activeFights.add(player);
    }
}
