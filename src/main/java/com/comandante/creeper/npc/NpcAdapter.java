package com.comandante.creeper.npc;

import com.comandante.creeper.Items.Item;
import com.comandante.creeper.Items.ItemType;
import com.comandante.creeper.Items.Loot;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.spawner.SpawnRule;
import com.comandante.creeper.stat.StatsBuilder;
import com.comandante.creeper.world.Area;
import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class NpcAdapter extends TypeAdapter<Npc> {

    private final GameManager gameManager;

    public NpcAdapter(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void write(JsonWriter jsonWriter, Npc npc) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(npc.getName());
        jsonWriter.name("colorName").value(npc.getColorName());
        jsonWriter.name("dieMessage").value(npc.getDieMessage());

        Loot loot = npc.getLoot();
        jsonWriter.name("loot");
        jsonWriter.beginObject();
        jsonWriter.name("lootGoldMin").value(npc.getLoot().getLootGoldMin());
        jsonWriter.name("lootGoldMax").value(npc.getLoot().getLootGoldMax());
        jsonWriter.name("lootItems");
        jsonWriter.beginArray();
        for (Item item : loot.getItems()) {
            jsonWriter.value(item.getItemTypeId());
        }
        jsonWriter.endArray();
        jsonWriter.endObject();

        jsonWriter.name("roamAreas");
        jsonWriter.beginArray();
        for (Area area : npc.getRoamAreas()) {
            jsonWriter.value(area.getName());
        }
        jsonWriter.endArray();

        jsonWriter.name("stats");
        jsonWriter.beginObject();
        jsonWriter.name("agile").value(npc.getStats().getAgile());
        jsonWriter.name("aim").value(npc.getStats().getAim());
        jsonWriter.name("armorRating").value(npc.getStats().getArmorRating());
        jsonWriter.name("currentHealth").value(npc.getStats().getCurrentHealth());
        jsonWriter.name("currentMana").value(npc.getStats().getCurrentMana());
        jsonWriter.name("experience").value(npc.getStats().getExperience());
        jsonWriter.name("maxHealth").value(npc.getStats().getMaxHealth());
        jsonWriter.name("maxMana").value(npc.getStats().getMaxMana());
        jsonWriter.name("meleSkill").value(npc.getStats().getMeleSkill());
        jsonWriter.name("numberOfWeaponRolls").value(npc.getStats().getNumberOfWeaponRolls());
        jsonWriter.name("strength").value(npc.getStats().getStrength());
        jsonWriter.name("weaponRatingMax").value(npc.getStats().getWeaponRatingMax());
        jsonWriter.name("weaponRatingMin").value(npc.getStats().getWeaponRatingMin());
        jsonWriter.name("willPower").value(npc.getStats().getWillpower());
        jsonWriter.endObject();

        jsonWriter.name("spawnAreas");
        jsonWriter.beginObject();
        for (SpawnRule spawnRule : npc.getSpawnRules()) {
            jsonWriter.name(spawnRule.getArea().getName());
            jsonWriter.beginObject();
            jsonWriter.name("randomChance").value(spawnRule.getRandomChance());
            jsonWriter.name("maxPerRoom").value(spawnRule.getMaxPerRoom());
            jsonWriter.name("spawnIntervalTicks").value(spawnRule.getSpawnIntervalTicks());
            jsonWriter.name("maxInstances").value(spawnRule.getMaxInstances());
            jsonWriter.endObject();
        }
        jsonWriter.endObject();

        jsonWriter.name("validTriggers");
        jsonWriter.beginArray();
        for (String trigger : npc.getValidTriggers()) {
            jsonWriter.value(trigger);
        }
        jsonWriter.endArray();


        jsonWriter.endObject();

    }

    @Override
    public Npc read(JsonReader jsonReader) throws IOException {

        jsonReader.beginObject();
        jsonReader.nextName();
        final String npcName = jsonReader.nextString();
        jsonReader.nextName();
        final String npcColorName = jsonReader.nextString();
        jsonReader.nextName();
        final String npcDieMessage = jsonReader.nextString();
        jsonReader.nextName();
        jsonReader.beginObject();
        jsonReader.nextName();
        final int lootGoldMin = jsonReader.nextInt();
        jsonReader.nextName();
        final int lootGoldMax = jsonReader.nextInt();
        jsonReader.nextName();
        jsonReader.beginArray();
        Set<Item> items = Sets.newHashSet();
        while (jsonReader.hasNext()) {
            items.add(ItemType.itemTypeFromCode(jsonReader.nextInt()).create());
        }
        jsonReader.endArray();
        Loot loot = new Loot(lootGoldMin, lootGoldMax, items);
        jsonReader.endObject();

        jsonReader.nextName();
        jsonReader.beginArray();
        Set<Area> roamAreas = Sets.newHashSet();
        while (jsonReader.hasNext()) {
            roamAreas.add(Area.getByName(jsonReader.nextString()));
        }
        jsonReader.endArray();

        jsonReader.nextName();
        jsonReader.beginObject();
        StatsBuilder statsBuilder = new StatsBuilder();

        jsonReader.nextName();
        statsBuilder.setAgile(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setAim(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setArmorRating(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setCurrentHealth(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setCurrentMana(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setExperience(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setMaxHealth(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setMaxMana(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setMeleSkill(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setNumberOfWeaponRolls(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setStrength(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setWeaponRatingMax(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setWeaponRatingMin(jsonReader.nextInt());

        jsonReader.nextName();
        statsBuilder.setWillpower(jsonReader.nextInt());
        jsonReader.endObject();

        jsonReader.nextName();
        jsonReader.beginObject();
        Set<SpawnRule> spawnRules = Sets.newHashSet();
        while (jsonReader.hasNext()) {
            String spawnAreaName = jsonReader.nextName();
            jsonReader.beginObject();
            jsonReader.nextName();

            int randomChance = jsonReader.nextInt();
            jsonReader.nextName();

            int maxPerRoom = jsonReader.nextInt();
            jsonReader.nextName();

            int spawnIntervalTicks = jsonReader.nextInt();
            jsonReader.nextName();

            int maxInstances = jsonReader.nextInt();
            jsonReader.endObject();
            spawnRules.add(new SpawnRule(Area.getByName(spawnAreaName),spawnIntervalTicks, maxInstances, maxPerRoom, randomChance));
        }
        jsonReader.endObject();

        jsonReader.nextName();
        jsonReader.beginArray();
        Set<String> validTriggers = Sets.newHashSet();
        while (jsonReader.hasNext()) {
            validTriggers.add(jsonReader.nextString());
        }
        jsonReader.endArray();
        jsonReader.endObject();

        NpcBuilder npcBuilder = new NpcBuilder()
                .setColorName(npcColorName)
                .setDieMessage(npcDieMessage)
                .setLoot(loot)
                .setName(npcName)
                .setRoamAreas(roamAreas)
                .setSpawnRules(spawnRules)
                .setStats(statsBuilder.createStats())
                .setValidTriggers(validTriggers)
                .setGameManager(gameManager);

        return npcBuilder.createNpc();
    }
}
