package com.comandante.creeper.storage;

import com.comandante.creeper.Main;
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

    public final static String LOCAL_ITEM_DIRECTORY = "world/items/";

    private final Gson gson;
    private static final Logger log = Logger.getLogger(NpcStorage.class);

    private final List<ItemMetadata> itemMetadatas;

    public ItemStorage(Gson gson) {
        this.gson = gson;
        this.itemMetadatas = readAllItemMetadatas();
    }

    public List<ItemMetadata> getAllItemMetadata()  {
        return readAllItemMetadatas();
    }

    public void saveItemMetaData(ItemMetadata itemMetadata) throws IOException {
        new File(LOCAL_ITEM_DIRECTORY).mkdirs();
        File npcFile = new File(LOCAL_ITEM_DIRECTORY + itemMetadata.getInternalItemName().replaceAll("\\s", "_") + ".json");
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
        Iterator<File> iterator = FileUtils.iterateFiles(new File(LOCAL_ITEM_DIRECTORY), new String[]{"json"}, false);
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

    public List<ItemMetadata> getItemMetadatas() {
        return itemMetadatas;
    }

    public Optional<ItemMetadata> get(String internalItemName) {
        return itemMetadatas.stream()
                .filter(itemMetadata -> itemMetadata.getInternalItemName().equals(internalItemName))
                .findFirst();
    }
}
