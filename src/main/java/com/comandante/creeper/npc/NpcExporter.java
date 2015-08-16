package com.comandante.creeper.npc;


import com.comandante.creeper.Main;
import com.comandante.creeper.managers.GameManager;
import com.comandante.creeper.world.WorldModel;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NpcExporter {

    public static List<Npc> getNpcsFromFile(GameManager gameManager) throws FileNotFoundException {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Npc.class, new NpcAdapter(gameManager));
        List<Npc> npcs = Lists.newArrayList();
        for (File f : Files.fileTreeTraverser().preOrderTraversal(new File("world/npcs/"))) {
            Main.startUpMessage("Loading json file: " + f);
            Path relativePath = new File("world/npcs/").toPath().getParent().relativize(f.toPath());
            if (f.getName().contains(".json")) {
                npcs.add(gsonBuilder.create().fromJson(Files.newReader(f, Charset.defaultCharset()), Npc.class));
            }
        }
        return npcs;
    }
}
