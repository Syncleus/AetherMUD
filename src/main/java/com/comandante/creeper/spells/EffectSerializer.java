package com.comandante.creeper.spells;

import com.comandante.creeper.spells.Effect;
import com.google.gson.GsonBuilder;
import org.mapdb.Serializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public class EffectSerializer implements Serializer<Effect>, Serializable {
    @Override
    public void serialize(DataOutput out, Effect value) throws IOException {
        out.writeUTF(new GsonBuilder().create().toJson(value, Effect.class));
    }

    @Override
    public Effect deserialize(DataInput in, int available) throws IOException {
        return new GsonBuilder().create().fromJson(in.readUTF(), Effect.class);
    }

    @Override
    public int fixedSize() {
        return -1;
    }
}
