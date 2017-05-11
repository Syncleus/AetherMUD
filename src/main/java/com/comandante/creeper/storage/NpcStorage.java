package com.comandante.creeper.storage;


import com.comandante.creeper.Main;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcBuilder;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NpcStorage {

    private final GameManager gameManager;
    private final Gson gson;
    private final static String LOCAL_NPC_DIRECTORY = "world/npcs/";
    private static final Logger log = Logger.getLogger(NpcStorage.class);

    public NpcStorage(GameManager gameManager, Gson gson) {
        this.gson = gson;
        this.gameManager = gameManager;
    }

    public List<Npc> getAllNpcs()  {
        List<NpcMetadata> npcMetadata = readAlLNpcs();
        return npcMetadata.stream()
                .map(metadata -> new NpcBuilder(metadata).setGameManager(gameManager).createNpc())
                .collect(Collectors.toList());
    }

    public void saveNpcMetadata(NpcMetadata npcMetadata) throws IOException {
        File npcFile = new File(LOCAL_NPC_DIRECTORY + npcMetadata.getName().replaceAll("\\s", "_") + ".json");
        org.apache.commons.io.FileUtils.writeStringToFile(npcFile, gson.toJson(npcMetadata));
    }

    protected List<NpcMetadata> readAlLNpcs() {
        return readAlLNpcs(getAllJsonStrings());
    }

    protected List<NpcMetadata> readAlLNpcs(List<String> jsonStrings) {
        List<NpcMetadata> npcMetadatas = jsonStrings.stream()
                .map(s -> {
                    try {
                        return gson.fromJson(s, NpcMetadata.class);
                    } catch (Exception e) {
                        log.error("Unable to read NpcMetaData from Json!", e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return npcMetadatas;
    }

    protected List<String> getAllJsonStrings() {
        Iterator<File> iterator = FileUtils.iterateFiles(new File(LOCAL_NPC_DIRECTORY), new String[]{"json"}, false);
        return toListOfJsonStrings(iterator);
    }

    protected List<String> toListOfJsonStrings(final Iterator<File> iterator) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .map(f -> {
                    try {
                        Main.startUpMessage("Reading npc: " + f.getAbsolutePath());
                        return new String(Files.readAllBytes(f.toPath()));
                    } catch (IOException e) {
                        log.error("Unable to read: " + f.getAbsolutePath(), e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
