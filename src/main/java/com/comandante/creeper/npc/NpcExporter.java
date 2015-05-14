package com.comandante.creeper.npc;


import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.world.WorldModel;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;

public class NpcExporter {

    public static List<Npc> getNpcsFromFile(GameManager gameManager) throws FileNotFoundException {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Npc.class, new NpcAdapter(gameManager));
        Npc npc = gsonBuilder.create().fromJson(Files.newReader(new File(("world/npcs/streethustler.json")), Charset.defaultCharset()), Npc.class);
        return Lists.newArrayList(npc);
    }
}
