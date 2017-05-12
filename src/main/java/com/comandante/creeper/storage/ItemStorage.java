package com.comandante.creeper.storage;

import com.comandante.creeper.Main;
import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.Item;
import com.comandante.creeper.items.ItemMetadata;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ItemStorage {

    private final static String LOCAL_ITEM_DIRECTORY = "world/items/";

    private final Gson gson;
    private final String storageDirectory;
    private static final Logger log = Logger.getLogger(NpcStorage.class);

    public ItemStorage(String storageDirectorty, ItemFactory itemFactory, Gson gson) {
        this.storageDirectory = storageDirectorty;
        this.gson = gson;
    }

    public List<ItemMetadata > getAllItemMetadata()  {
        return readAllItemMetadatas();
    }

    public void saveItemMetaData(ItemMetadata itemMetadata) throws IOException {
        File npcFile = new File(storageDirectory + itemMetadata.getBasicItemName().replaceAll("\\s", "_") + ".json");
        org.apache.commons.io.FileUtils.writeStringToFile(npcFile, gson.toJson(itemMetadata));
    }

    protected List<ItemMetadata> readAllItemMetadatas() {
        return readAllItemMetadatas(getAllJsonStrings());
    }

    protected List<ItemMetadata> readAllItemMetadatas(List<String> jsonStrings) {
        List<ItemMetadata> itemMetadata = jsonStrings.stream()
                .map(s -> {
                    try {
                        return gson.fromJson(s, ItemMetadata.class);
                    } catch (Exception e) {
                        log.error("Unable to read NpcMetaData from Json!", e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return itemMetadata;
    }

    protected List<String> getAllJsonStrings() {
        Iterator<File> iterator = FileUtils.iterateFiles(new File(storageDirectory), new String[]{"json"}, false);
        return toListOfJsonStrings(iterator);
    }

    protected List<String> toListOfJsonStrings(final Iterator<File> iterator) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .map(f -> {
                    try {
                        Main.startUpMessage("Reading item: " + f.getAbsolutePath());
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
