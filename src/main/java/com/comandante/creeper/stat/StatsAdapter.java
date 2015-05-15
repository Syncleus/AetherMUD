package com.comandante.creeper.stat;

import com.comandante.creeper.npc.Npc;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


public class StatsAdapter extends TypeAdapter<Stats>  {

    @Override
    public void write(JsonWriter jsonWriter, Stats stats) throws IOException {

    }

    @Override
    public Stats read(JsonReader jsonReader) throws IOException {

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

        return statsBuilder.createStats();
    }
}
