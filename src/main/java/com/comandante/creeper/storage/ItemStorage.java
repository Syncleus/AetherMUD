package com.comandante.creeper.storage;

import com.comandante.creeper.common.ColorizedTextTemplate;
import com.comandante.creeper.items.ItemMetadata;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemStorage {

    public final static String LOCAL_ITEM_DIRECTORY = "world/items/";

    private static final Logger log = Logger.getLogger(NpcStorage.class);
    private final FilebasedJsonStorage filebasedJsonStorage;

    private final List<ItemMetadata> itemMetadatas;

    public ItemStorage(FilebasedJsonStorage filebasedJsonStorage) {
        this.filebasedJsonStorage = filebasedJsonStorage;
        this.itemMetadatas = getAllItemMetadata();
    }

    private List<ItemMetadata> getAllItemMetadata() {
        return filebasedJsonStorage.readAllMetadatas(LOCAL_ITEM_DIRECTORY, true, new ItemMetadata()).stream()
                .map(itemMetadata -> {
                    itemMetadata.setItemDescription(ColorizedTextTemplate.renderFromTemplateLanguage(itemMetadata.getItemDescription()));
                    itemMetadata.setItemName(ColorizedTextTemplate.renderFromTemplateLanguage(itemMetadata.getItemName()));
                    itemMetadata.setRestingName(ColorizedTextTemplate.renderFromTemplateLanguage(itemMetadata.getRestingName()));
                    return itemMetadata;
                }).collect(Collectors.toList());
    }

    public List<ItemMetadata> getItemMetadatas() {
        return itemMetadatas;
    }

    public void saveItemMetadata(ItemMetadata itemMetadata) throws IOException {
        itemMetadata.setItemName(ColorizedTextTemplate.renderToTemplateLanguage(itemMetadata.getItemName()));
        itemMetadata.setItemDescription(ColorizedTextTemplate.renderToTemplateLanguage(itemMetadata.getItemDescription()));
        itemMetadata.setRestingName(ColorizedTextTemplate.renderToTemplateLanguage(itemMetadata.getRestingName()));
        filebasedJsonStorage.saveMetadata(itemMetadata.getInternalItemName(), LOCAL_ITEM_DIRECTORY, itemMetadata);
    }

    public Optional<ItemMetadata> get(String internalItemName) {
        return itemMetadatas.stream()
                .filter(itemMetadata -> itemMetadata.getInternalItemName().equals(internalItemName))
                .findFirst();
    }
}
