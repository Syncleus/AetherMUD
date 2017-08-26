/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
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
package com.syncleus.aethermud.player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.io.Serializable;

public class PlayerMetadataSerializer implements Serializer<PlayerMetadata>, Serializable {

    private final static Gson GSON = new GsonBuilder().create();

    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull PlayerMetadata value) throws IOException {
        out.writeUTF(GSON.toJson(value, PlayerMetadata.class));

    }

    @Override
    public PlayerMetadata deserialize(@NotNull DataInput2 input, int available) throws IOException {
        return GSON.fromJson(input.readUTF(), PlayerMetadata.class);
    }

    @Override
    public int fixedSize() {
        return -1;
    }
}

