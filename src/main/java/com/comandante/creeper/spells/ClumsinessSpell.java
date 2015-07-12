package com.comandante.creeper.spells;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.player.Player;
import com.comandante.creeper.server.Color;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Room;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.comandante.creeper.server.Color.BOLD_ON;

public class ClumsinessSpell extends Spell {

    private final static String NAME = BOLD_ON + Color.MAGENTA + "clumsiness" + Color.RESET;
    private final static String DESCRIPTION = "A noticeable lapse in judgement occurs in your target.";
    private final static Set<String> validTriggers = new HashSet<String>(Arrays.asList(new String[]
                    {"clumsiness", "clumsy", NAME}
    ));
    private final static Stats attackStats = new StatsBuilder().createStats();
    private final static boolean isAreaSpell = false;

    private final static List<String> attackMessages = Lists.newArrayList("awkwardness permeates and as a result " + BOLD_ON + Color.MAGENTA + "clumsiness" + Color.RESET + " is had by all.");
    private final static int manaCost = 300;
    private final static SpellExecute spellExecute = new SpellExecute() {
        @Override
        public void executeNpc(GameManager gameManager, Npc npc, Player player) {
            Interner<String> interner = Interners.newWeakInterner();
            synchronized (interner.intern(npc.getEntityId())) {
                Loot loot = npc.getLoot();
                Room playerCurrentRoom = gameManager.getRoomManager().getPlayerCurrentRoom(player).get();
                Set<Item> items = gameManager.getLootManager().lootItemsReturn(loot);
                if (items.size() > 0) {
                    for (Item item : items) {
                        gameManager.placeItemInRoom(playerCurrentRoom.getRoomId(), item.getItemId());
                        gameManager.roomSay(playerCurrentRoom.getRoomId(), npc.getColorName() + Color.MAGENTA + " fumbles " + Color.RESET + item.getItemName(), player.getPlayerId());
                        gameManager.getItemDecayManager().addItem(item);
                    }
                    npc.setLoot(new Loot(loot.getLootGoldMin(), loot.getLootGoldMax(), Sets.<ItemType>newHashSet()));
                }
            }
        }
    };

    public ClumsinessSpell(GameManager gameManager) {
        super(gameManager, validTriggers, manaCost, attackStats, attackMessages, DESCRIPTION, NAME, Sets.<Effect>newHashSet(), isAreaSpell, spellExecute);
    }
}
