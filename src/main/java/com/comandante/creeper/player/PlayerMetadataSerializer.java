package com.comandante.creeper.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mapdb.Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public class PlayerMetadataSerializer implements Serializer<PlayerMetadata>, Serializable {

    private final static Gson GSON = new GsonBuilder().create();

    @Override
    public void serialize(DataOutput out, PlayerMetadata value) throws IOException {
        out.writeUTF(GSON.toJson(value, PlayerMetadata.class));
    }

    @Override
    public PlayerMetadata deserialize(DataInput in, int available) throws IOException {
        return GSON.fromJson(in.readUTF(), PlayerMetadata.class);
    }

    @Override
    public int fixedSize() {
        return -1;
    }
}

