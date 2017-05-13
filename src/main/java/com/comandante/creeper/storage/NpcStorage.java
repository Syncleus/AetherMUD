package com.comandante.creeper.storage;


import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.npc.Npc;
import com.comandante.creeper.npc.NpcBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NpcStorage {

    private final GameManager gameManager;
    private final static String LOCAL_NPC_DIRECTORY = "world/npcs/";
    private static final Logger log = Logger.getLogger(NpcStorage.class);
    private final FilebasedJsonStorage filebasedJsonStorage;

    public NpcStorage(GameManager gameManager, FilebasedJsonStorage filebasedJsonStorage) {
        this.gameManager = gameManager;
        this.filebasedJsonStorage = filebasedJsonStorage;
    }

    public List<Npc> getAllNpcs() {
        List<NpcMetadata> npcMetadata = readAlLNpcs();
        return npcMetadata.stream()
                .map(metadata -> new NpcBuilder(metadata).setGameManager(gameManager).createNpc())
                .collect(Collectors.toList());
    }

    public void saveNpcMetadata(NpcMetadata npcMetadata) throws IOException {
       filebasedJsonStorage.saveMetadata(npcMetadata.getName(), LOCAL_NPC_DIRECTORY, npcMetadata);
    }

    private List<NpcMetadata> readAlLNpcs() {
        return filebasedJsonStorage.readAllMetadatas(LOCAL_NPC_DIRECTORY, true, new NpcMetadata());
    }
}
