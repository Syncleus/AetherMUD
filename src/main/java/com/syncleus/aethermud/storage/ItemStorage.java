/**
 * Copyright 2017 Syncleus, Inc.
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
package com.syncleus.aethermud.storage;

import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.items.ItemMetadata;
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
