/**
 * Copyright 2017 Syncleus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncleus.aethermud.core;


import com.syncleus.aethermud.Main;
import com.syncleus.aethermud.bot.IrcBotService;
import com.syncleus.aethermud.bot.command.BotCommandFactory;
import com.syncleus.aethermud.bot.command.BotCommandManager;
import com.syncleus.aethermud.common.FriendlyTime;
import com.syncleus.aethermud.configuration.AetherMudConfiguration;
import com.syncleus.aethermud.core.service.MultiThreadedEventProcessor;
import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.entity.AetherMudEntity;
import com.syncleus.aethermud.entity.EntityManager;
import com.syncleus.aethermud.items.*;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.npc.NpcSpawn;
import com.syncleus.aethermud.npc.NpcMover;
import com.syncleus.aethermud.player.*;
import com.syncleus.aethermud.server.multiline.MultiLineInputManager;
import com.syncleus.aethermud.server.communication.ChannelCommunicationUtils;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.server.communication.GossipCache;
import com.syncleus.aethermud.spawner.NpcSpawner;
import com.syncleus.aethermud.spells.Spells;
import com.syncleus.aethermud.stats.Levels;
import com.syncleus.aethermud.stats.Stats;
import com.syncleus.aethermud.storage.graphdb.GraphDbNpcStorage;
import com.syncleus.aethermud.stats.StatsBuilder;
import com.syncleus.aethermud.stats.modifier.StatsModifierFactory;
import com.syncleus.aethermud.storage.*;
import com.syncleus.aethermud.world.FloorManager;
import com.syncleus.aethermud.world.MapsManager;
import com.syncleus.aethermud.world.RoomManager;
import com.syncleus.aethermud.world.model.BasicRoomBuilder;
import com.syncleus.aethermud.world.model.Coords;
import com.syncleus.aethermud.world.model.RemoteExit;
import com.syncleus.aethermud.world.model.Room;
import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syncleus.ferma.WrappedFramedGraph;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import static com.syncleus.aethermud.server.communication.Color.BOLD_OFF;
import static com.syncleus.aethermud.server.communication.Color.BOLD_ON;
import static com.syncleus.aethermud.server.communication.Color.RESET;

public class GameManager {

    public static final Integer LOBBY_ID = 1;
    private static final Logger log = Logger.getLogger(GameManager.class);
    public static String LOGO = "Creeper.";
    private final RoomManager roomManager;
    private final PlayerManager playerManager;
    private final ChannelCommunicationUtils channelUtils;
    private final NewUserRegistrationManager newUserRegistrationManager;
    private final EntityManager entityManager;
    private final ItemDecayManager itemDecayManager;
    private final MultiLineInputManager multiLineInputManager;
    private final MapsManager mapsManager;
    private final FloorManager floorManager;
    private final LootManager lootManager;
    private final IrcBotService ircBotService;
    private final AetherMudConfiguration aetherMudConfiguration;
    private final ForageManager forageManager;
    private final EffectsManager effectsManager;
    private final BotCommandFactory botCommandFactory;
    private final BotCommandManager botCommandManager;
    private final StatsModifierFactory statsModifierFactory;
    private final GossipCache gossipCache;
    private final Interner<String> interner = Interners.newWeakInterner();
    private final TimeTracker timeTracker;
    private final ItemUseHandler itemUseHandler;
    private final NpcMover npcMover;
    private final Spells spells;
    private final MultiThreadedEventProcessor eventProcessor = new MultiThreadedEventProcessor(new ArrayBlockingQueue<>(10000));
    private final Room detainmentRoom;
    private final NpcStorage npcStorage;
    private final ItemStorage itemStorage;
    private final HttpClient httpclient;
    private final Gson gson;
    private final FilebasedJsonStorage filebasedJsonStorage;
    private final AetherMudStorage graphStorage;

    public MerchantStorage getMerchantStorage() {
        return merchantStorage;
    }

    private final MerchantStorage merchantStorage;


    public GameManager(AetherMudStorage graphStorage, WrappedFramedGraph<Graph> framedGraph, AetherMudConfiguration aetherMudConfiguration, RoomManager roomManager, PlayerManager playerManager, EntityManager entityManager, MapsManager mapsManager, ChannelCommunicationUtils channelUtils, HttpClient httpClient) {
        this.graphStorage = graphStorage;
        this.roomManager = roomManager;
        this.playerManager = playerManager;
        this.entityManager = entityManager;
        this.newUserRegistrationManager = new NewUserRegistrationManager(this);
        this.multiLineInputManager = new MultiLineInputManager();
        this.mapsManager = mapsManager;
        this.floorManager = new FloorManager();
        this.channelUtils = channelUtils;
        this.lootManager = new LootManager(this);
        this.ircBotService = new IrcBotService(aetherMudConfiguration, this);
        this.aetherMudConfiguration = aetherMudConfiguration;
        this.forageManager = new ForageManager(this);
        this.effectsManager = new EffectsManager(this);
        this.botCommandManager = new BotCommandManager(this);
        this.botCommandFactory = new BotCommandFactory(botCommandManager);
        this.statsModifierFactory = new StatsModifierFactory(this);
        this.gossipCache = new GossipCache(this);
        this.timeTracker = new TimeTracker(this);
        this.entityManager.addEntity(timeTracker);
        this.itemDecayManager = new ItemDecayManager(entityManager, this);
        this.entityManager.addEntity(itemDecayManager);
        this.itemUseHandler = new ItemUseHandler(this);
        this.npcMover = new NpcMover(this);
        this.spells = new Spells(this);
        this.eventProcessor.startAsync();
        this.detainmentRoom = buildDetainmentRoom();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.filebasedJsonStorage = new FilebasedJsonStorage(gson);
        this.npcStorage = new GraphDbNpcStorage(this, framedGraph);;
        this.itemStorage = new ItemStorage(filebasedJsonStorage);
        this.merchantStorage = new MerchantStorage(this, filebasedJsonStorage);
        this.httpclient = httpClient;
    }

    public AetherMudStorage getGraphStorage() {
        return graphStorage;
    }

    public Gson getGson() {
        return gson;
    }

    public ItemStorage getItemStorage() {
        return itemStorage;
    }

    private Room buildDetainmentRoom() {
        BasicRoomBuilder basicRoomBuilder = new BasicRoomBuilder(this);

        basicRoomBuilder.setRoomDescription("The room is covered in a white soft padded material.");
        basicRoomBuilder.setRoomTitle("Detainment");
        Room detainmentRoom = basicRoomBuilder
                .setRoomId(-187)
                .setFloorId(-187)
                .createBasicRoom();
        roomManager.addRoom(detainmentRoom);
        return detainmentRoom;
    }

    public Spells getSpells() {
        return spells;
    }

    public NpcMover getNpcMover() {
        return npcMover;
    }

    public ItemUseHandler getItemUseHandler() {
        return itemUseHandler;
    }

    public GossipCache getGossipCache() {
        return gossipCache;
    }

    public StatsModifierFactory getStatsModifierFactory() {
        return statsModifierFactory;
    }

    public BotCommandFactory getBotCommandFactory() {
        return botCommandFactory;
    }

    public BotCommandManager getBotCommandManager() {
        return botCommandManager;
    }

    public EffectsManager getEffectsManager() {
        return effectsManager;
    }

    public ForageManager getForageManager() {
        return forageManager;
    }

    public IrcBotService getIrcBotService() {
        return ircBotService;
    }

    public AetherMudConfiguration getAetherMudConfiguration() {
        return aetherMudConfiguration;
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public FloorManager getFloorManager() {
        return floorManager;
    }

    public MapsManager getMapsManager() {
        return mapsManager;
    }

    public MultiLineInputManager getMultiLineInputManager() {
        return multiLineInputManager;
    }

    public NewUserRegistrationManager getNewUserRegistrationManager() {
        return newUserRegistrationManager;
    }

    public ItemDecayManager getItemDecayManager() {
        return itemDecayManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public TimeTracker getTimeTracker() {
        return timeTracker;
    }

    public MultiThreadedEventProcessor getEventProcessor() {
        return eventProcessor;
    }

    public NpcStorage getNpcStorage() {
        return npcStorage;
    }

    public void placePlayerInLobby(Player player) {
        Room room = roomManager.getRoom(LOBBY_ID);
        room.addPresentPlayer(player.getPlayerId());
        player.setCurrentRoomAndPersist(room);
        for (Player next : room.getPresentPlayers()) {
            if (next.getPlayerId().equals(player.getPlayerId())) {
                continue;
            }
            channelUtils.write(next.getPlayerId(), player.getPlayerName() + " arrived.", true);
        }
    }

    public void detainPlayer(Player player) {
        // Source room Id is null, meaning it will disable things like the "Back" command.  Since its null you need to
        // remove the player from the current room manually.
        PlayerMovement playerMovement = new PlayerMovement(player,
                null,
                detainmentRoom.getRoomId(),
                "has been placed under arrest.",
                null);

        player.removePlayerFromRoom(player.getCurrentRoom());
        player.movePlayer(playerMovement);
        playerManager.getAllPlayersMap().forEach((s, destinationPlayer) -> {
            channelUtils.write(destinationPlayer.getPlayerId(),
                    player.getPlayerName() + " has been " + BOLD_ON + Color.RED + "DETAINED" + RESET + "!" + "\r\n", true);
        });
        player.addCoolDown(CoolDownType.DETAINMENT);
    }

    public void announceConnect(String userName) {
        Set<Player> allPlayers = getAllPlayers();
        for (Player p : allPlayers) {
            getChannelUtils().write(p.getPlayerId(), Color.GREEN + userName + " has connected." + RESET + "\r\n", true);
        }
    }

    public Set<Player> getAllPlayers() {
        ImmutableSet.Builder<Player> builder = ImmutableSet.builder();
        Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRoomsIterator();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            Room room = next.getValue();
            Set<Player> presentPlayers = room.getPresentPlayers();
            for (Player player : presentPlayers) {
                builder.add(player);
            }
        }
        return builder.build();
    }

    public ChannelCommunicationUtils getChannelUtils() {
        return channelUtils;
    }

    public void currentRoomLogic(String playerId) {
        Player player = playerManager.getPlayer(playerId);
        final Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
        currentRoomLogic(playerId, playerCurrentRoom);
    }

    public void currentRoomLogic(String playerId, Room playerCurrentRoom) {
        Player player = playerManager.getPlayer(playerId);
        StringBuilder sb = new StringBuilder();
        sb.append(BOLD_ON + Color.GREEN);
        sb.append(playerCurrentRoom.getRoomTitle());
        sb.append(RESET);
        sb.append("\r\n\r\n");
        sb.append(WordUtils.wrap(playerCurrentRoom.getRoomDescription(), 80, "\r\n", true)).append("\r\n").append("\r\n");
        Optional<String> autoMapOptional = player.getPlayerSetting("auto_map");
        if (playerCurrentRoom.getMapData().isPresent() && autoMapOptional.isPresent()) {
            int i = Integer.parseInt(autoMapOptional.get());
            sb.append(mapsManager.drawMap(playerCurrentRoom.getRoomId(), new Coords(i, i))).append("\r\n");
        }
        sb.append(getExits(playerCurrentRoom, player)).append("\r\n");

        Set<Merchant> merchants = playerCurrentRoom.getMerchants();
        for (Merchant merchant : merchants) {
            sb.append(merchant.getColorName()).append(" is here.").append(RESET).append("\r\n");
        }
        for (Player searchPlayer : playerCurrentRoom.getPresentPlayers()) {
            if (searchPlayer.getPlayerId().equals(player.getPlayerId())) {
                continue;
            }
            sb.append(searchPlayer.getPlayerName()).append(" is here.").append(RESET).append("\r\n");
        }

        for (String itemId : playerCurrentRoom.getItemIds()) {
            Optional<ItemPojo> itemOptional = entityManager.getItemEntity(itemId);
            if (!itemOptional.isPresent()) {
                playerCurrentRoom.removePresentItem(itemId);
                continue;
            }
            ItemPojo item = itemOptional.get();
            sb.append("   ").append(item.getRestingName()).append("\r\n");
        }

        List<String> npcs = Lists.newArrayList();
        for (String npcId : playerCurrentRoom.getNpcIds()) {
            StringBuilder sbb = new StringBuilder();
            NpcSpawn npcSpawnEntity = entityManager.getNpcEntity(npcId);
            if (Main.vowels.contains(Character.toLowerCase(npcSpawnEntity.getName().charAt(0)))) {
                sbb.append("an ");
            } else {
                sbb.append("a ");
            }
            sbb.append(npcSpawnEntity.getColorName()).append(" is here.\r\n");
            npcs.add(sbb.toString());
        }
        Collections.sort(npcs, String.CASE_INSENSITIVE_ORDER);
        for (String s : npcs) {
            sb.append(s);
        }
        String msg = null;
        if (sb.toString().substring(sb.toString().length() - 2).equals("\r\n")) {
            CharSequence charSequence = sb.toString().subSequence(0, sb.toString().length() - 2);
            msg = charSequence.toString();
        } else {
            msg = sb.toString();
        }
        channelUtils.write(player.getPlayerId(), msg);
    }

    private String getExits(Room room, Player player) {
        int numExits = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(BOLD_ON);
        sb.append(Color.GREEN);
        if (!player.getReturnDirection().isPresent()) {
            player.setReturnDirection(Optional.of("-"));
        }
        if (room.getNorthId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("north")) {
                sb.append(BOLD_OFF);
                sb.append("North ");
                sb.append(BOLD_ON);
            } else {
                sb.append("North ");
            }
            numExits++;
        }
        if (room.getSouthId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("south")) {
                sb.append(BOLD_OFF);
                sb.append("South ");
                sb.append(BOLD_ON);
            } else {
                sb.append("South ");
            }
            numExits++;
        }
        if (room.getEastId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("east")) {
                sb.append(BOLD_OFF);
                sb.append("East ");
                sb.append(BOLD_ON);
            } else {
                sb.append("East ");
            }
            numExits++;
        }
        if (room.getWestId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("west")) {
                sb.append(BOLD_OFF);
                sb.append("West ");
                sb.append(BOLD_ON);
            } else {
                sb.append("West ");
            }
            numExits++;
        }
        if (room.getUpId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("up")) {
                sb.append(BOLD_OFF);
                sb.append("Up ");
                sb.append(BOLD_ON);
            } else {
                sb.append("Up ");
            }
            numExits++;
        }
        if (room.getDownId().isPresent()) {
            if (player.getReturnDirection().get().equalsIgnoreCase("down")) {
                sb.append(BOLD_OFF);
                sb.append("Down ");
                sb.append(BOLD_ON);
            } else {
                sb.append("Down ");
            }
            numExits++;
        }
        if (room.getEnterExits() != null && room.getEnterExits().size() > 0) {
            List<RemoteExit> enters = room.getEnterExits();
            for (RemoteExit enter : enters) {
                sb.append("e-" + enter.getExitDetail() + " ");
                numExits++;
            }
        }
        String fin = null;
        if (numExits == 1) {
            fin = sb.toString().replace(BOLD_OFF, BOLD_ON);
        } else {
            fin = sb.toString();
        }
        fin = fin + RESET + "]\r\n";
        return fin;
    }

    public void placeItemInRoom(Integer roomId, String itemId) {
        Optional<ItemPojo> itemOptional = entityManager.getItemEntity(itemId);
        if (!itemOptional.isPresent()) {
            return;
        }
        ItemPojo item = itemOptional.get();
        roomManager.getRoom(roomId).addPresentItem(item.getItemId());
    }

    public boolean acquireItemFromRoom(Player player, String itemId) {
        synchronized (interner.intern(itemId)) {
            Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
            if (playerCurrentRoom.getItemIds().contains(itemId)) {
                if (acquireItem(player, itemId)) {
                    playerCurrentRoom.getItemIds().remove(itemId);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean acquireItem(Player player, String itemId) {
        return acquireItem(player, itemId, false);
    }

    public boolean acquireItem(Player player, String itemId, boolean isFromLoot) {
        synchronized (interner.intern(itemId)) {
            Stats playerStatsWithEquipmentAndLevel = player.getPlayerStatsWithEquipmentAndLevel();
            if (player.getInventory().size() < playerStatsWithEquipmentAndLevel.getInventorySize()) {
                Optional<ItemPojo> itemOptional = entityManager.getItemEntity(itemId);
                if (!itemOptional.isPresent()) {
                    return false;
                }
                ItemPojo itemEntity = itemOptional.get();
                itemEntity.setWithPlayer(true);
                player.addInventoryId(itemId);
                entityManager.saveItem(itemEntity);
                return true;
            } else {
                Optional<ItemPojo> itemOptional = entityManager.getItemEntity(itemId);
                if (!itemOptional.isPresent()) {
                    return false;
                }
                ItemPojo itemEntity = itemOptional.get();
                channelUtils.write(player.getPlayerId(), "Your inventory is full, drop some items to free up room.\r\n");
                if (isFromLoot) {
                    player.getCurrentRoom().addPresentItem(itemId);
                    roomSay(player.getCurrentRoom().getRoomId(), player.getPlayerName() + " dropped " + itemEntity.getItemName(), player.getPlayerId() + "\r\n");
                }
                return false;
            }
        }
    }

    public void roomSay(Integer roomId, String message, String sourcePlayerId) {
        Set<Player> presentPlayers = roomManager.getRoom(roomId).getPresentPlayers();
        for (Player player : presentPlayers) {
            if (player.getPlayerId().equals(sourcePlayerId)) {
                channelUtils.write(player.getPlayerId(), message, false);
                continue;
            }
            channelUtils.write(player.getPlayerId(), message, true);
        }
    }

    public String getLookString(NpcSpawn npcSpawn, long playerLevel) {
        StringBuilder sb = new StringBuilder();
        // passing an empty createState because of the "difference calculation"
        sb.append(Color.MAGENTA + "-+=[ " + RESET).append(npcSpawn.getColorName()).append(Color.MAGENTA + " ]=+- " + RESET).append("\r\n");
        sb.append("Level ").append(Levels.getLevel(npcSpawn.getStats().getExperience())).append(" ")
                .append(npcSpawn.getLevelColor((int) playerLevel).getColor())
                .append(" [").append(npcSpawn.getTemperament().getFriendlyFormat()).append("]").append("\r\n");
        sb.append(Color.MAGENTA + "Stats--------------------------------" + RESET).append("\r\n");
        sb.append(buildLookString(npcSpawn.getColorName(), npcSpawn.getStats(), new StatsBuilder().createStats())).append("\r\n");
        if (npcSpawn.getEffects() != null && npcSpawn.getEffects().size() > 0) {
            sb.append(Color.MAGENTA + "Effects--------------------------------" + RESET).append("\r\n");
            sb.append(buldEffectsString(npcSpawn)).append("\r\n");
        }
        return sb.toString();
    }

    public String buildLookString(String name, Stats stats, Stats diff) {
        StringBuilder returnString = new StringBuilder();
        Table t = new Table(3, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.NONE);

        t.setColumnWidth(0, 16, 20);
        t.setColumnWidth(1, 10, 20);


        t.addCell("Experience");
        t.addCell(NumberFormat.getNumberInstance(Locale.US).format(stats.getExperience()));
        t.addCell("");

        t.addCell("Health");
        t.addCell(getFormattedNumber(stats.getCurrentHealth()));
        t.addCell("");

        t.addCell("Mana");
        t.addCell(getFormattedNumber(stats.getCurrentMana()));
        t.addCell("");

        StringBuilder sb = new StringBuilder();
        t.addCell("Strength");
        t.addCell(getFormattedNumber(stats.getStrength()));
        if (diff.getStrength() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getStrength())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Intelligence");
        t.addCell(getFormattedNumber(stats.getIntelligence()));
        if (diff.getStrength() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getIntelligence())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Willpower");
        t.addCell(getFormattedNumber(stats.getWillpower()));
        if (diff.getWillpower() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getWillpower())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Aim");
        t.addCell(getFormattedNumber(stats.getAim()));
        if (diff.getAim() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getAim())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Agile");
        t.addCell(getFormattedNumber(stats.getAgile()));
        if (diff.getAgile() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getAgile())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Armor");
        t.addCell(getFormattedNumber(stats.getArmorRating()));
        if (diff.getArmorRating() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getArmorRating())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Mele");
        t.addCell(getFormattedNumber(stats.getMeleSkill()));
        if (diff.getMeleSkill() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getMeleSkill())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Weapon Rating");
        t.addCell(getFormattedNumber(stats.getWeaponRatingMin()) + "-" + getFormattedNumber(stats.getWeaponRatingMax()));
        if (diff.getWeaponRatingMin() > 0 || diff.getWeaponRatingMax() > 0) {
            sb.append("(");
            if (diff.getWeaponRatingMin() > 0) {
                sb.append(Color.GREEN);
                sb.append("+");
            }
            sb.append(Long.toString(diff.getWeaponRatingMin())).append(RESET).append("-");
            if (diff.getWeaponRatingMax() > 0) {
                sb.append(Color.GREEN);
                sb.append("+");
            }
            sb.append(getFormattedNumber(diff.getWeaponRatingMax()));
            sb.append(RESET).append(")");
        }
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Forage");
        t.addCell(getFormattedNumber(stats.getForaging()));
        if (diff.getForaging() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getForaging())).append(RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Bag");
        t.addCell(getFormattedNumber(stats.getInventorySize()));
        if (diff.getInventorySize() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getInventorySize())).append(RESET).append(")");
        t.addCell(sb.toString());

        returnString.append(t.render());
        return returnString.toString();
    }

    public String buldEffectsString(NpcSpawn npcSpawn) {
        return renderEffectsString(npcSpawn.getEffects());

    }

    private String getFormattedNumber(Number longval) {
        return NumberFormat.getNumberInstance(Locale.US).format(longval);
    }

    public String renderEffectsString(List<Effect> effects) {
        Table t = new Table(2, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.NONE);

        t.setColumnWidth(0, 16, 20);
        // t.setColumnWidth(1, 10, 13);

        int i = 1;
        for (Effect effect : effects) {
            int percent = 100 - (int) ((effect.getEffectApplications() * 100.0f) / effect.getMaxEffectApplications());
            t.addCell(drawProgressBar(percent));
            t.addCell(effect.getEffectName());
            i++;
        }
        return t.render();
    }

    public String drawProgressBar(int pct) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int numberOfProgressBarNotches = getNumberOfProgressBarNotches(pct);
        for (int i = 0; i < numberOfProgressBarNotches; i++) {
            sb.append("+");
        }
        for (int i = numberOfProgressBarNotches; i < 10; i++) {
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public int getNumberOfProgressBarNotches(int y) {
        int x = (int) (Math.round(y / 10.0) * 10);
        String str = Integer.toString(x);
        if (str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        return Integer.parseInt(str);
    }

    public String renderCoolDownString(Set<? extends CoolDown> coolDowns) {
        Table t = new Table(2, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.NONE);

        t.setColumnWidth(0, 19, 25);
        // t.setColumnWidth(1, 10, 13);

        int i = 1;
        for (CoolDown coolDown : coolDowns) {
            int percent = 100 - (int) (((coolDown.getOriginalNumberOfTicks() - coolDown.getNumberOfTicks()) * 100.0f) / coolDown.getOriginalNumberOfTicks());
            // 1 tick == .5 seconds.
            int approxSecondsRemaining = coolDown.getNumberOfTicks() / 2;
            FriendlyTime friendlyTime = new FriendlyTime(approxSecondsRemaining);
            String friendlyFormattedShort = friendlyTime.getFriendlyFormattedShort();
            t.addCell(drawProgressBar(percent) + friendlyFormattedShort);
            t.addCell(coolDown.getName());
            i++;
        }
        return t.render();
    }

    public void writeToPlayerCurrentRoom(String playerId, String message) {
        if (playerManager.getSessionManager().getSession(playerId).getGrabMultiLineInput().isPresent()) {
            return;
        }
        Player player = playerManager.getPlayer(playerId);
        Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
        Set<Player> presentPlayers = playerCurrentRoom.getPresentPlayers();
        for (Player presentPlayer : presentPlayers) {
            channelUtils.write(presentPlayer.getPlayerId(), message, true);
        }
    }

    public void writeToRoom(Integer roomId, String message) {
        Room room = roomManager.getRoom(roomId);
        Set<Player> presentPlayers = room.getPresentPlayers();
        for (Player presentPlayer : presentPlayers) {
            channelUtils.write(presentPlayer.getPlayerId(), message, true);
        }
    }

    public void announceLevelUp(String playerName, long previousLevel, long newLevel) {
        Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
        while (players.hasNext()) {
            Map.Entry<String, Player> next = players.next();
            channelUtils.write(next.getValue().getPlayerId(), "\r\n" + playerName + BOLD_ON + Color.GREEN + " has reached LEVEL " + newLevel + RESET + "\r\n");
        }
    }

    public Map<String, Double> processExperience(NpcSpawn npcSpawn, Room npcCurrentRoom) {
        Iterator<Map.Entry<String, Long>> iterator = npcSpawn.getPlayerDamageMap().entrySet().iterator();
        int totalDamageDone = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, Long> damageEntry = iterator.next();
            totalDamageDone += damageEntry.getValue();
            String playerId = damageEntry.getKey();
            Optional<Room> playerCurrentRoom = getRoomManager().getPlayerCurrentRoom(playerId);
            if (!playerCurrentRoom.isPresent()) {
                iterator.remove();
            } else if (!Objects.equals(npcCurrentRoom.getRoomId(), playerCurrentRoom.get().getRoomId())) {
                iterator.remove();
            }
        }
        Map<String, Double> damagePcts = Maps.newHashMap();
        Set<Map.Entry<String, Long>> entries = npcSpawn.getPlayerDamageMap().entrySet();
        for (Map.Entry<String, Long> damageEntry : entries) {
            String playerId = damageEntry.getKey();
            long amount = damageEntry.getValue();
            double pct = (double) amount / totalDamageDone;
            if (pct >= .90) {
                damagePcts.put(playerId, (double) 1);
            } else if (pct >= 0.25) {
                damagePcts.put(playerId, .8);
            } else if (pct >= 0.10) {
                damagePcts.put(playerId, .5);
            } else {
                damagePcts.put(playerId, .25);
            }
        }
        return damagePcts;
    }

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public synchronized void removeAllNpcs() {
        for (NpcSpawn npcSpawn : entityManager.getNpcs().values()) {
            Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRoomsIterator();
            while (rooms.hasNext()) {
                Map.Entry<Integer, Room> next = rooms.next();
                next.getValue().removePresentNpc(npcSpawn.getEntityId());
            }
            entityManager.getNpcs().remove(npcSpawn.getEntityId());
            entityManager.getEntities().remove(npcSpawn.getEntityId());
        }
        for (AetherMudEntity aetherMudEntity : entityManager.getNpcs().values()) {
            if (aetherMudEntity instanceof NpcSpawner) {
                entityManager.getNpcs().remove(aetherMudEntity.getEntityId());
            }
        }
    }

    public String buildPrompt(String playerId) {
        Player player = playerManager.getPlayer(playerId);
        boolean isFight = player.isActiveFights();
        Stats stats = player.getPlayerStatsWithEquipmentAndLevel();
        long currentHealth = stats.getCurrentHealth();
        long maxHealth = stats.getMaxHealth();
        long currentMana = stats.getCurrentMana();
        long maxMana = stats.getMaxMana();
        StringBuilder sb = new StringBuilder()
                .append(BOLD_ON + Color.WHITE)
                .append("[")
                .append(RESET)
                .append(player.getPlayerName())
                .append(" ")
                .append(currentHealth)
                .append(BOLD_ON)
                .append(Color.WHITE)
                .append("/")
                .append(RESET)
                .append(maxHealth)
                .append("h")
                .append(" ")
                .append(currentMana)
                .append(BOLD_ON)
                .append(Color.WHITE)
                .append("/")
                .append(RESET)
                .append(maxMana).append("m");
        if (isFight) {
            sb.append(Color.RED + " ! " + RESET);
        }
        if (player.isActiveCoolDown()) {
            if (player.isActive(CoolDownType.DEATH)) {
                sb.append(" ");
                sb.append(Color.RED + "D" + RESET);
            }
            if (player.isActiveForageCoolDown()) {
                sb.append(" ");
                sb.append(Color.GREEN + "F" + RESET);
            }
            if (player.isActive(CoolDownType.DETAINMENT)) {
                sb.append(" ");
                sb.append(BOLD_ON + Color.RED + "DETAINED" + RESET);
            }
        }
        if (player.areAnyAlertedNpcsInCurrentRoom()) {
            sb.append(" ");
            sb.append(Color.RED + "ALERT" + RESET);
        }
        sb.append(BOLD_ON + Color.WHITE);
        sb.append("] ");
        sb.append(RESET);
        if (player.isChatModeOn()) {
            sb.append("<" + Color.GREEN + "=" + RESET + "> ");
        }

        return sb.toString();
    }

    public Room getDetainmentRoom() {
        return detainmentRoom;
    }
}

