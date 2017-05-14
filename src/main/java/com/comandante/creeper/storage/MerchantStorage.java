package com.comandante.creeper.storage;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.merchant.Merchant;
import com.comandante.creeper.merchant.MerchantMetadata;
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
        this.merchantMetadatas = filebasedJsonStorage.readAllMetadatas(LOCAL_MERCHANT_DIRECTORY, true, new MerchantMetadata());
    }

    public List<Merchant> getAllMerchants() {
        return merchantMetadatas.stream()
                .map(this::create)
                .collect(Collectors.toList());
    }

    public Merchant create(MerchantMetadata merchantMetadata) {
        return new Merchant(gameManager,
                merchantMetadata.getInternalName(),
                merchantMetadata.getName(),
                merchantMetadata.getColorName(),
                merchantMetadata.getValidTriggers(),
                merchantMetadata.getMerchantItemForSales(),
                merchantMetadata.getWelcomeMessage(),
                merchantMetadata.getRoomId());
    }


    public List<MerchantMetadata> getAllMerchantMetadatas() {
        return merchantMetadatas;
    }

    public void saveMerchantMetadata(MerchantMetadata merchantMetadata) throws IOException {
        filebasedJsonStorage.saveMetadata(merchantMetadata.getInternalName(), LOCAL_MERCHANT_DIRECTORY, merchantMetadata);
    }

    public Optional<MerchantMetadata> get(String internalName) {
        return merchantMetadatas.stream()
                .filter(merchantMetadata -> merchantMetadata.getInternalName().equals(internalName))
                .findFirst();
    }

}
