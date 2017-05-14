package com.comandante.creeper.merchant;

import com.comandante.creeper.core_game.GameManager;
import com.comandante.creeper.items.ItemMetadata;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.*;

public class Merchant {

    private final GameManager gameManager;
    private final String internalName;
    private final String name;
    private final String colorName;
    private final Set<String> validTriggers;
    private final List<MerchantItemForSale> merchantItemForSales;
    private final String welcomeMessage;
    private final MerchantType merchantType;
    private final Integer roomId;

    public Merchant(GameManager gameManager, String internalName, String name, String colorName, Set<String> validTriggers, List<MerchantItemForSale> merchantItemForSales, String welcomeMessage, Integer roomId) {
        this(gameManager, internalName, name, colorName, validTriggers, merchantItemForSales, welcomeMessage, roomId, MerchantType.BASIC);
    }

    public Merchant(GameManager gameManager, String internalName, String name, String colorName, Set<String> validTriggers, List<MerchantItemForSale> merchantItemForSales, String welcomeMessage, Integer roomId, MerchantType merchantType) {
        this.gameManager = gameManager;
        this.name = name;
        this.colorName = colorName;
        this.validTriggers = validTriggers;
        this.merchantItemForSales = merchantItemForSales;
        this.welcomeMessage = welcomeMessage;
        this.merchantType = merchantType;
        this.roomId = roomId;
        this.internalName = internalName;

    }

    public String getInternalName() {
        return internalName;
    }

    public String getMenu() {
        Table t = new Table(3, BorderStyle.CLASSIC_COMPATIBLE,
                ShownBorders.HEADER_FIRST_AND_LAST_COLLUMN);
        t.setColumnWidth(0, 5, 5);
        t.setColumnWidth(1, 12, 16);
        t.setColumnWidth(2, 50, 69);
        t.addCell("#");
        t.addCell("price");
        t.addCell("description");
        int i = 0;
        Iterator<MerchantItemForSale> iterator = merchantItemForSales.iterator();
        while (iterator.hasNext()) {
            i++;
            MerchantItemForSale merchantItemForSale = iterator.next();
            Optional<ItemMetadata> itemMetadataOptional = gameManager.getItemStorage().get(merchantItemForSale.getInternalItemName());
            if (!itemMetadataOptional.isPresent()) {
                continue;
            }
            ItemMetadata itemMetadata = itemMetadataOptional.get();
            t.addCell(String.valueOf(i));
            t.addCell(NumberFormat.getNumberInstance(Locale.US).format(merchantItemForSale.getCost()));
            t.addCell(itemMetadata.getItemDescription());
        }
        return t.render();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getColorName() {
        return colorName;
    }

    public Set<String> getValidTriggers() {
        return validTriggers;
    }

    public List<MerchantItemForSale> merchantItemForSales() {
        return merchantItemForSales;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public MerchantType getMerchantType() {
        return merchantType;
    }

    public List<MerchantItemForSale> getMerchantItemForSales() {
        return merchantItemForSales;
    }

    public enum MerchantType {
        BANK,
        LOCKER,
        PLAYERCLASS_SELECTOR,
        BASIC
    }
}
