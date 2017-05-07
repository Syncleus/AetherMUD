package com.comandante.creeper.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mapdb.Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public class ItemSerializer implements Serializer<Item>, Serializable {

    private final static Gson GSON = new GsonBuilder().create();

    @Override
    public void serialize(DataOutput out, Item value) throws IOException {
        out.writeUTF(GSON.toJson(value, Item.class));
    }

    @Override
    public Item deserialize(DataInput in, int available) throws IOException {
        return GSON.fromJson(in.readUTF(), Item.class);
    }

    @Override
    public int fixedSize() {
        return -1;
    }
}
