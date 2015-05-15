package com.comandante.creeper.npc;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.stat.Stats;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Area;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class NpcAdapterTest {

    Npc npcOne;
    Gson gson;

    @Before
    public void setUp() throws Exception {

        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Npc.class, new NpcAdapter(null));

        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

        Random randomGenerator = new Random();

        Stats npcOneStats = new StatsBuilder()
                .setAgile(randomGenerator.nextInt(100))
                .setAim(randomGenerator.nextInt(100))
                .setArmorRating(randomGenerator.nextInt(100))
                .setCurrentHealth(randomGenerator.nextInt(100))
                .setCurrentMana(randomGenerator.nextInt(100))
                .setExperience(randomGenerator.nextInt(100))
                .setMaxHealth(randomGenerator.nextInt(100))
                .setMaxMana(randomGenerator.nextInt(100))
                .setMeleSkill(randomGenerator.nextInt(100))
                .setNumberOfWeaponRolls(randomGenerator.nextInt(100))
                .setStrength(randomGenerator.nextInt(100))
                .setWeaponRatingMax(randomGenerator.nextInt(100))
                .setWeaponRatingMin(randomGenerator.nextInt(100))
                .setWillpower(randomGenerator.nextInt(100))
                .createStats();

        Loot npcOneLoot = new Loot(randomGenerator.nextInt(100), randomGenerator.nextInt(100), Sets.newHashSet(ItemType.BEER.create()));
        SpawnRule npcOneSpawnRule1 = new SpawnRule(Area.BLOODRIDGE10_ZONE, randomGenerator.nextInt(100), randomGenerator.nextInt(100), randomGenerator.nextInt(100), randomGenerator.nextInt(100));
        SpawnRule npcOneSpawnRule2 = new SpawnRule(Area.BLOODRIDGE10_ZONE, randomGenerator.nextInt(100), randomGenerator.nextInt(100), randomGenerator.nextInt(100), randomGenerator.nextInt(100));
        npcOne = new NpcBuilder()
                .setColorName(UUID.randomUUID().toString())
                .setDieMessage(UUID.randomUUID().toString())
                .setName(UUID.randomUUID().toString())
                .setStats(npcOneStats)
                .setLoot(npcOneLoot)
                .setRoamAreas(Sets.newHashSet(Area.BLOODRIDGE10_ZONE, Area.BLOODRIDGE11_ZONE))
                .setSpawnRules(Sets.newHashSet(npcOneSpawnRule1, npcOneSpawnRule2))
                .setValidTriggers(Sets.newHashSet(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .createNpc();
    }

    @Test
    public void testSerializeDeSerialize() throws Exception {


        String npcOneJson = gson.toJson(npcOne);
        System.out.println(npcOneJson);

        Npc npc = gson.fromJson(npcOneJson, Npc.class);

        assertEquals(npcOne.getDieMessage(), npc.getDieMessage());
        assertEquals(npcOne.getColorName(), npc.getColorName());
        assertEquals(npcOne.getName(), npc.getName());

        assertEquals(npcOne.getLoot().getLootGoldMax(), npc.getLoot().getLootGoldMax());
        assertEquals(npcOne.getLoot().getLootGoldMin(), npc.getLoot().getLootGoldMin());
        Set<Item> items = npcOne.getLoot().getItems();
        Item originalItem = null;
        Item newItem = null;
        for (Item item: items) {
            originalItem = item;
        }
        items = npc.getLoot().getItems();
        for (Item item: items) {
            newItem = item;
        }
        assertEquals(originalItem.getItemDescription(), newItem.getItemDescription());
        assertEquals(npcOne.getRoamAreas(), npc.getRoamAreas());

        assertEquals(npcOne.getStats().getWeaponRatingMax(), npc.getStats().getWeaponRatingMax());
        assertEquals(npcOne.getStats().getWeaponRatingMin(), npc.getStats().getWeaponRatingMin());
        assertEquals(npcOne.getStats().getStrength(), npc.getStats().getStrength());
        assertEquals(npcOne.getStats().getAgile(), npc.getStats().getAgile());
        assertEquals(npcOne.getStats().getCurrentHealth(), npc.getStats().getCurrentHealth());
        assertEquals(npcOne.getStats().getCurrentMana(), npc.getStats().getCurrentMana());
        assertEquals(npcOne.getStats().getExperience(), npc.getStats().getExperience());
        assertEquals(npcOne.getStats().getAim(), npc.getStats().getAim());
        assertEquals(npcOne.getStats().getArmorRating(), npc.getStats().getArmorRating());
        assertEquals(npcOne.getStats().getMaxMana(), npc.getStats().getMaxMana());
        assertEquals(npcOne.getStats().getMeleSkill(), npc.getStats().getMeleSkill());
        assertEquals(npcOne.getStats().getNumberOfWeaponRolls(), npc.getStats().getNumberOfWeaponRolls());
        assertEquals(npcOne.getStats().getWillpower(), npc.getStats().getWillpower());

        assertEquals(npcOne.getValidTriggers(), npc.getValidTriggers());

    }

    @Test
    public void testRawJson() throws Exception {
        String testJson = Files.toString(new File("/Users/kearney/Desktop/npcs/tunnelcobra.json"), Charset.defaultCharset());
        System.out.println(testJson);

        Npc npc = gson.fromJson(testJson, Npc.class);


    }
}