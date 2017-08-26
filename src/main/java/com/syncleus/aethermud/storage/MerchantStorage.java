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
package com.syncleus.aethermud.storage;

import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.merchant.MerchantMetadata;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MerchantStorage {

    public final static String LOCAL_MERCHANT_DIRECTORY = "world/merchants/";

    private static final Logger log = Logger.getLogger(MerchantStorage.class);
    private final FilebasedJsonStorage filebasedJsonStorage;
    private final GameManager gameManager;
    private final List<MerchantMetadata> merchantMetadatas;

    public MerchantStorage(GameManager gameManager, FilebasedJsonStorage filebasedJsonStorage) {
        this.gameManager = gameManager;
        this.filebasedJsonStorage = filebasedJsonStorage;
        this.merchantMetadatas = getAllMerchantMetadata();
    }

    private List<MerchantMetadata> getAllMerchantMetadata() {
        return filebasedJsonStorage.readAllMetadatas(LOCAL_MERCHANT_DIRECTORY, true, new MerchantMetadata()).stream()
                .map(merchantMetadata -> {
                    merchantMetadata.setColorName(ColorizedTextTemplate.renderFromTemplateLanguage(merchantMetadata.getColorName()));
                    merchantMetadata.setWelcomeMessage(ColorizedTextTemplate.renderFromTemplateLanguage(merchantMetadata.getWelcomeMessage()));
                    return merchantMetadata;
                }).collect(Collectors.toList());
    }

    public List<MerchantMetadata> getMerchantMetadatas() {
        return merchantMetadatas;
    }

    public List<Merchant> getAllMerchants() {
        return merchantMetadatas.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }

    public Merchant create(MerchantMetadata merchantMetadata) {

        if (merchantMetadata.getMerchantType() != null) {
            return new Merchant(gameManager,
                    merchantMetadata.getInternalName(),
                    merchantMetadata.getName(),
                    merchantMetadata.getColorName(),
                    merchantMetadata.getValidTriggers(),
                    merchantMetadata.getMerchantItemForSales(),
                    merchantMetadata.getWelcomeMessage(),
                    merchantMetadata.getRoomId(),
                    merchantMetadata.getMerchantType());
        }

        return new Merchant(gameManager,
                merchantMetadata.getInternalName(),
                merchantMetadata.getName(),
                merchantMetadata.getColorName(),
                merchantMetadata.getValidTriggers(),
                merchantMetadata.getMerchantItemForSales(),
                merchantMetadata.getWelcomeMessage(),
                merchantMetadata.getRoomId());
    }

    public void saveMerchantMetadata(MerchantMetadata merchantMetadata) throws IOException {
        merchantMetadata.setColorName(ColorizedTextTemplate.renderToTemplateLanguage(merchantMetadata.getColorName()));
        merchantMetadata.setWelcomeMessage(ColorizedTextTemplate.renderToTemplateLanguage(merchantMetadata.getWelcomeMessage()));
        filebasedJsonStorage.saveMetadata(merchantMetadata.getInternalName(), LOCAL_MERCHANT_DIRECTORY, merchantMetadata);
    }

    public Optional<MerchantMetadata> get(String internalName) {
        return merchantMetadatas.stream()
                .filter(merchantMetadata -> merchantMetadata.getInternalName().equals(internalName))
                .findFirst();
    }

}
