package com.comandante.creeper.storage;

import com.comandante.creeper.items.ItemMetadata;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ItemStorage {

    public final static String LOCAL_ITEM_DIRECTORY = "world/items/";

    private static final Logger log = Logger.getLogger(NpcStorage.class);
    private final FilebasedJsonStorage filebasedJsonStorage;

    private final List<ItemMetadata> itemMetadatas;

    public ItemStorage(FilebasedJsonStorage filebasedJsonStorage) {
        this.filebasedJsonStorage = filebasedJsonStorage;
        this.itemMetadatas = filebasedJsonStorage.readAllMetadatas(LOCAL_ITEM_DIRECTORY, true, new ItemMetadata());
    }

    public List<ItemMetadata> getAllItemMetadata() {
        return itemMetadatas;
    }

    public void saveItemMetadata(ItemMetadata itemMetadata) throws IOException {
        filebasedJsonStorage.saveMetadata(itemMetadata.getInternalItemName(), LOCAL_ITEM_DIRECTORY, itemMetadata);
    }

    public Optional<ItemMetadata> get(String internalItemName) {
        return itemMetadatas.stream()
                .filter(itemMetadata -> itemMetadata.getInternalItemName().equals(internalItemName))
                .findFirst();
    }
}
