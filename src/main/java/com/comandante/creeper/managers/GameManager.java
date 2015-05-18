package com.comandante.creeper.managers;


import com.codahale.metrics.Meter;
import com.comandante.creeper.CreeperConfiguration;
import com.comandante.creeper.IrcBotService;
import com.comandante.creeper.Items.ForageManager;
import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemDecayManager;
import com.comandante.creeper.Items.LootManager;
import com.comandante.creeper.Main;
import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.entity.EntityManager;
import com.comandante.creeper.fight.FightManager;
import com.comandante.creeper.fight.FightResults;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.*;
import com.comandante.creeper.server.ChannelUtils;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.server.CreeperSession;
import com.comandante.creeper.server.MultiLineInputManager;
import com.comandante.creeper.spawner.NpcSpawner;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.*;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.MessageEvent;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.Future;

import static com.comandante.creeper.server.Color.*;

public class GameManager {

    public static String LOGO = " ▄████▄   ██▀███  ▓█████ ▓█████  ██▓███  ▓█████  ██▀███  \r\n" +
            "▒██▀ ▀█  ▓██ ▒ ██▒▓█   ▀ ▓█   ▀ ▓██░  ██▒▓█   ▀ ▓██ ▒ ██▒\r\n" +
            "▒▓█    ▄ ▓██ ░▄█ ▒▒███   ▒███   ▓██░ ██▓▒▒███   ▓██ ░▄█ ▒\r\n" +
            "▒▓▓▄ ▄██▒▒██▀▀█▄  ▒▓█  ▄ ▒▓█  ▄ ▒██▄█▓▒ ▒▒▓█  ▄ ▒██▀▀█▄  \r\n" +
            "▒ ▓███▀ ░░██▓ ▒██▒░▒████▒░▒████▒▒██▒ ░  ░░▒████▒░██▓ ▒██▒\r\n" +
            "░ ░▒ ▒  ░░ ▒▓ ░▒▓░░░ ▒░ ░░░ ▒░ ░▒▓▒░ ░  ░░░ ▒░ ░░ ▒▓ ░▒▓░\r\n" +
            "  ░  ▒     ░▒ ░ ▒░ ░ ░  ░ ░ ░  ░░▒ ░      ░ ░  ░  ░▒ ░ ▒░\r\n" +
            "░          ░░   ░    ░      ░   ░░          ░     ░░   ░ \r\n" +
            "░ ░         ░        ░  ░   ░  ░            ░  ░   ░     \r\n" +
            "░                                                        ";

    public static String VERSION = "0.1-SNAPSHOT";

    private final RoomManager roomManager;
    private final PlayerManager playerManager;
    private final ChannelUtils channelUtils;
    private final NewUserRegistrationManager newUserRegistrationManager;
    private final EntityManager entityManager;
    private final ItemDecayManager itemDecayManager;
    private final FightManager fightManager;
    private final MultiLineInputManager multiLineInputManager;
    private final MapsManager mapsManager;
    private final FloorManager floorManager;
    private final LootManager lootManager;
    private final EquipmentManager equipmentManager;
    private final IrcBotService ircBotService;
    private final CreeperConfiguration creeperConfiguration;
    private final ForageManager forageManager;

    private static final Logger log = Logger.getLogger(GameManager.class);


    public GameManager(CreeperConfiguration creeperConfiguration, RoomManager roomManager, PlayerManager playerManager, EntityManager entityManager, MapsManager mapsManager, ChannelUtils channelUtils) {
        this.roomManager = roomManager;
        this.playerManager = playerManager;
        this.entityManager = entityManager;
        this.itemDecayManager = new ItemDecayManager(entityManager);
        this.entityManager.addEntity(itemDecayManager);
        this.newUserRegistrationManager = new NewUserRegistrationManager(playerManager);
        this.multiLineInputManager = new MultiLineInputManager();
        this.mapsManager = mapsManager;
        this.floorManager = new FloorManager();
        this.channelUtils = channelUtils;
        this.lootManager = new LootManager();
        this.equipmentManager = new EquipmentManager(entityManager, channelUtils, playerManager);
        this.fightManager = new FightManager(this);
        this.ircBotService = new IrcBotService(creeperConfiguration, this);
        this.creeperConfiguration = creeperConfiguration;
        this.forageManager = new ForageManager(this);
    }

    public ForageManager getForageManager() {
        return forageManager;
    }

    public IrcBotService getIrcBotService() {
        return ircBotService;
    }

    public CreeperConfiguration getCreeperConfiguration() {
        return creeperConfiguration;
    }

    public EquipmentManager getEquipmentManager() {
        return equipmentManager;
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

    public FightManager getFightManager() {
        return fightManager;
    }

    public NewUserRegistrationManager getNewUserRegistrationManager() {
        return newUserRegistrationManager;
    }

    public ChannelUtils getChannelUtils() {
        return channelUtils;
    }

    public ItemDecayManager getItemDecayManager() {
        return itemDecayManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }


    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static final Integer LOBBY_ID = 1;

    public Set<Player> getAllPlayers() {
        ImmutableSet.Builder<Player> builder = ImmutableSet.builder();
        Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRooms();
        while (rooms.hasNext()) {
            Map.Entry<Integer, Room> next = rooms.next();
            Room room = next.getValue();
            Set<Player> presentPlayers = roomManager.getPresentPlayers(room);
            for (Player player : presentPlayers) {
                builder.add(player);
            }
        }
        return builder.build();
    }

    public void movePlayer(PlayerMovement playerMovement) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(playerMovement.getPlayer().getPlayerId())) {
            Room sourceRoom = roomManager.getRoom(playerMovement.getSourceRoomId());
            Room destinationRoom = roomManager.getRoom(playerMovement.getDestinationRoomId());
            sourceRoom.removePresentPlayer(playerMovement.getPlayer().getPlayerId());
            for (Player next : roomManager.getPresentPlayers(sourceRoom)) {
                StringBuilder sb = new StringBuilder();
                sb.append(playerMovement.getPlayer().getPlayerName());
                sb.append(" ").append(playerMovement.getRoomExitMessage());
                channelUtils.write(next.getPlayerId(), sb.toString(), true);
            }
            destinationRoom.addPresentPlayer(playerMovement.getPlayer().getPlayerId());
            playerMovement.getPlayer().setCurrentRoom(destinationRoom);
            for (Player next : roomManager.getPresentPlayers(destinationRoom)) {
                if (next.getPlayerId().equals(playerMovement.getPlayer().getPlayerId())) {
                    continue;
                }
                channelUtils.write(next.getPlayerId(), playerMovement.getPlayer().getPlayerName() + " arrived.", true);
            }
        }
    }

    public void placePlayerInLobby(Player player) {
        Room room = roomManager.getRoom(LOBBY_ID);
        room.addPresentPlayer(player.getPlayerId());
        player.setCurrentRoom(room);
        for (Player next : roomManager.getPresentPlayers(room)) {
            if (next.getPlayerId().equals(player.getPlayerId())) {
                continue;
            }
            channelUtils.write(next.getPlayerId(), player.getPlayerName() + " arrived.", true);
        }
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

    public void currentRoomLogic(String playerId) {
        Player player = playerManager.getPlayer(playerId);
        final Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
        currentRoomLogic(playerId, playerCurrentRoom);
    }

    public void currentRoomLogic(String playerId, Room playerCurrentRoom) {
        Player player = playerManager.getPlayer(playerId);
        StringBuilder sb = new StringBuilder();
        sb.append(Color.BOLD_ON + Color.GREEN);
        sb.append(playerCurrentRoom.getRoomTitle()).append("\r\n\r\n");
        sb.append(RESET);
        //java.lang.String wrap(java.lang.String str, int wrapLength, java.lang.String newLineStr, boolean wrapLongWords)
        sb.append(WordUtils.wrap(playerCurrentRoom.getRoomDescription(), 80, "\r\n", true)).append("\r\n").append("\r\n");
        //  if (playerCurrentRoom.getMapData().isPresent()) {
        //      sb.append(playerCurrentRoom.getMapData().get()).append("\r\n");
        //  }
        sb.append(getExits(playerCurrentRoom, player)).append("\r\n");

        Set<Merchant> merchants = playerCurrentRoom.getMerchants();
        for (Merchant merchant : merchants) {
            sb.append(merchant.getColorName()).append(" is here.").append(RESET).append("\r\n");
        }
        for (Player searchPlayer : roomManager.getPresentPlayers(playerCurrentRoom)) {
            if (searchPlayer.getPlayerId().equals(player.getPlayerId())) {
                continue;
            }
                sb.append(searchPlayer.getPlayerName()).append(" is here.").append(RESET).append("\r\n");
        }

        for (String itemId : playerCurrentRoom.getItemIds()) {
            Item itemEntity = entityManager.getItemEntity(itemId);
            if (itemEntity == null) {
                playerCurrentRoom.removePresentItem(itemId);
                continue;
            }
            sb.append("   ").append(entityManager.getItemEntity(itemId).getRestingName()).append("\r\n");
        }

        for (String npcId : playerCurrentRoom.getNpcIds()) {
            Npc npcEntity = entityManager.getNpcEntity(npcId);
            sb.append("a ").append(npcEntity.getColorName()).append(" is here.\r\n");
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

    public void currentRoomLogic(CreeperSession creeperSession, MessageEvent e) {
        final String player = playerManager.getPlayerByUsername(creeperSession.getUsername().get()).getPlayerId();
        currentRoomLogic(player);
    }

    public void placeItemInRoom(Integer roomId, String itemId) {
            roomManager.getRoom(roomId).addPresentItem(entityManager.getItemEntity(itemId).getItemId());
    }

    public void acquireItem(Player player, String itemId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(itemId)) {
            playerManager.addInventoryId(player, itemId);
            Item itemEntity = entityManager.getItemEntity(itemId);
            itemEntity.setWithPlayer(true);
            entityManager.saveItem(itemEntity);
        }
    }


    public boolean acquireItemFromRoom(Player player, String itemId) {
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(itemId)) {
            Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
            if (playerCurrentRoom.getItemIds().contains(itemId)) {
                playerCurrentRoom.getItemIds().remove(itemId);
                playerManager.addInventoryId(player, itemId);
                Item itemEntity = entityManager.getItemEntity(itemId);
                itemEntity.setWithPlayer(true);
                entityManager.saveItem(itemEntity);
                return true;
            }
        }
        return false;
    }

    public void roomSay(Integer roomId, String message, String sourcePlayerId) {
        Set<Player> presentPlayers = roomManager.getPresentPlayers(roomManager.getRoom(roomId));
        for (Player player : presentPlayers) {
            if (player.getPlayerId().equals(sourcePlayerId)) {
                channelUtils.write(player.getPlayerId(), message, false);
                continue;
            }
            channelUtils.write(player.getPlayerId(), message, true);
        }
    }

    public String getLookString(Npc npc) {
        StringBuilder sb = new StringBuilder();
        // passing an empty createState because of the "difference calculation"
        sb.append(Color.MAGENTA + "-+=[ " + Color.RESET).append(npc.getColorName()).append(Color.MAGENTA + " ]=+- " + Color.RESET).append("\r\n");
        sb.append(Color.MAGENTA + "Stats--------------------------------" + Color.RESET).append("\r\n");
        sb.append(buildLookString(npc.getColorName(), npc.getStats(), new StatsBuilder().createStats())).append("\r\n");
        return sb.toString();
    }

    public String getLookString(Player player) {
        StringBuilder sb = new StringBuilder();
        Stats origStats = playerManager.getPlayerMetadata(player.getPlayerId()).getStats();
        Stats modifiedStats = equipmentManager.getPlayerStatsWithEquipment(player);
        Stats diffStats = equipmentManager.getDifference(modifiedStats, origStats);
        sb.append(Color.MAGENTA + "-+=[ " + Color.RESET).append(player.getPlayerName()).append(Color.MAGENTA + " ]=+- " + Color.RESET).append("\r\n");
        sb.append("Level ").append(Levels.getLevel(origStats.getExperience())).append("\r\n");
        sb.append(Color.MAGENTA + "Equip--------------------------------" + Color.RESET).append("\r\n");
        sb.append(buildEquipmentString(player)).append("\r\n");
        sb.append(Color.MAGENTA + "Stats--------------------------------" + Color.RESET).append("\r\n");
        sb.append(buildLookString(player.getPlayerName(), modifiedStats, diffStats)).append("\r\n");
        return sb.toString();
    }

    public String buildEquipmentString(Player player) {
        Table t = new Table(2, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.NONE);
        t.setColumnWidth(0, 16, 20);

        List<EquipmentSlotType> all = EquipmentSlotType.getAll();
        for (EquipmentSlotType slot : all) {
            t.addCell(capitalize(slot.getName()));
            Item slotItem = equipmentManager.getSlotItem(player, slot);
            if (slotItem != null) {
                t.addCell(slotItem.getItemName());
            } else {
                t.addCell("");
            }
        }
        return t.render();
    }

    public String buildLookString(String name, Stats stats, Stats diff) {
        StringBuilder returnString = new StringBuilder();
        Table t = new Table(3, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.NONE);

        t.setColumnWidth(0, 16, 20);
        t.setColumnWidth(1, 10, 13);


        t.addCell("Experience");
        t.addCell(NumberFormat.getNumberInstance(Locale.US).format(stats.getExperience()));
        t.addCell("");

        t.addCell("Health");
        t.addCell(getFormattedNumber(stats.getCurrentHealth()));
        t.addCell("");


        StringBuilder sb = new StringBuilder();
        t.addCell("Strength");
        t.addCell(getFormattedNumber(stats.getStrength()));
        if (diff.getStrength() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getStrength())).append(Color.RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Willpower");
        t.addCell(getFormattedNumber(stats.getWillpower()));
        if (diff.getWillpower() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getWillpower())).append(Color.RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Aim");
        t.addCell(getFormattedNumber(stats.getAim()));
        if (diff.getAim() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getAim())).append(Color.RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Agile");
        t.addCell(getFormattedNumber(stats.getAgile()));
        if (diff.getAgile() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getAgile())).append(Color.RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Armor");
        t.addCell(getFormattedNumber(stats.getArmorRating()));
        if (diff.getArmorRating() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getArmorRating())).append(Color.RESET).append(")");
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Mele");
        t.addCell(getFormattedNumber(stats.getMeleSkill()));
        if (diff.getMeleSkill() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getMeleSkill())).append(Color.RESET).append(")");
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
            sb.append(Integer.toString(diff.getWeaponRatingMin())).append(Color.RESET).append("-");
            if (diff.getWeaponRatingMax() > 0) {
                sb.append(Color.GREEN);
                sb.append("+");
            }
            sb.append(getFormattedNumber(diff.getWeaponRatingMax()));
            sb.append(Color.RESET).append(")");
        }
        t.addCell(sb.toString());

        sb = new StringBuilder();
        t.addCell("Forage");
        t.addCell(getFormattedNumber(stats.getForaging()));
        if (diff.getForaging() > 0)
            sb.append("(").append(Color.GREEN).append("+").append(getFormattedNumber(diff.getForaging())).append(Color.RESET).append(")");
        t.addCell(sb.toString());

        returnString.append(t.render());
        return returnString.toString();
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private String getFormattedNumber(Integer integer) {
        return NumberFormat.getNumberInstance(Locale.US).format(integer);
    }

    public void writeToPlayerCurrentRoom(String playerId, String message) {
        if (playerManager.getSessionManager().getSession(playerId).getGrabMultiLineInput().isPresent()) {
            return;
        }
        Player player = playerManager.getPlayer(playerId);
        Room playerCurrentRoom = roomManager.getPlayerCurrentRoom(player).get();
        Set<Player> presentPlayers = roomManager.getPresentPlayers(playerCurrentRoom);
        for (Player presentPlayer : presentPlayers) {
            channelUtils.write(presentPlayer.getPlayerId(), message, true);
        }
    }

    public void writeToRoom(Integer roomId, String message) {
        Room room = roomManager.getRoom(roomId);
        Set<Player> presentPlayers = roomManager.getPresentPlayers(room);
        for (Player presentPlayer : presentPlayers) {
            channelUtils.write(presentPlayer.getPlayerId(), message, true);
        }
    }

    public void announceLevelUp(String playerName, int previousLevel, int newLevel) {
        Iterator<Map.Entry<String, Player>> players = playerManager.getPlayers();
        while (players.hasNext()) {
            Map.Entry<String, Player> next = players.next();
            channelUtils.write(next.getValue().getPlayerId(), playerName + Color.BOLD_ON + Color.GREEN + " has reached LEVEL " + newLevel + Color.RESET + "\r\n");
        }
    }

    public void addExperience(Player player, int exp) {
        Interner<String> interner = Interners.newWeakInterner();
        final Meter requests = Main.metrics.meter("experience-" + player.getPlayerName());
        synchronized (interner.intern(player.getPlayerId())) {
            PlayerMetadata playerMetadata = playerManager.getPlayerMetadata(player.getPlayerId());
            int currentExperience = playerMetadata.getStats().getExperience();
            int currentLevel = Levels.getLevel(currentExperience);
            playerMetadata.getStats().setExperience(currentExperience + exp);
            requests.mark(exp);
            int newLevel = Levels.getLevel(playerMetadata.getStats().getExperience());
            if (newLevel > currentLevel) {
                announceLevelUp(player.getPlayerName(), currentLevel, newLevel);
            }
            playerManager.savePlayerMetadata(playerMetadata);
        }
    }


    public void updateNpcHealth(String npcId, int amt, String playerId) {
        Player player = playerManager.getPlayer(playerId);
        Interner<String> interner = Interners.newWeakInterner();
        synchronized (interner.intern(npcId)) {
            Npc npc = entityManager.getNpcEntity(npcId);
            if (npc != null) {
                npc.getStats().setCurrentHealth(npc.getStats().getCurrentHealth() + amt);
                if (npc.getStats().getCurrentHealth() <= 0) {
                    playerManager.getSessionManager().getSession(playerId).setActiveFight(Optional.<Future<FightResults>>absent());
                    addExperience(player, npc.getStats().getExperience());
                    writeToPlayerCurrentRoom(playerId, npc.getDieMessage());
                    channelUtils.write(playerId, "You killed a " + npc.getColorName() + " for " + Color.GREEN + "+" + npc.getStats().getExperience() + Color.RESET + " experience points." + "\r\n", true);
                    Item corpse = new Item(npc.getName() + " corpse", "a bloody corpse.", Arrays.asList("corpse", "c"), "a corpse lies on the ground.", UUID.randomUUID().toString(), Item.CORPSE_ID_RESERVED, 0, false, 120, npc.getLoot());
                    entityManager.saveItem(corpse);
                    Integer roomId = roomManager.getPlayerCurrentRoom(player).get().getRoomId();
                    Room room = roomManager.getRoom(roomId);
                    room.addPresentItem(corpse.getItemId());
                    itemDecayManager.addItem(corpse);
                    entityManager.deleteNpcEntity(npc.getEntityId());
                }
            }
        }
    }

    public synchronized void removeAllNpcs() {
        for (Npc npc : entityManager.getNpcs().values()) {
            Iterator<Map.Entry<Integer, Room>> rooms = roomManager.getRooms();
            while (rooms.hasNext()) {
                Map.Entry<Integer, Room> next = rooms.next();
                next.getValue().removePresentNpc(npc.getEntityId());
            }
            entityManager.getNpcs().remove(npc.getEntityId());
            entityManager.getEntities().remove(npc.getEntityId());
        }
        for (CreeperEntity creeperEntity : entityManager.getNpcs().values()) {
            if (creeperEntity instanceof NpcSpawner) {
                entityManager.getNpcs().remove(creeperEntity.getEntityId());
            }
        }


    }
}

