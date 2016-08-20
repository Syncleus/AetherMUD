package com.comandante.creeper.merchant;

import com.comandante.creeper.entity.CreeperEntity;
import com.comandante.creeper.managers.GameManager;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class Merchant extends CreeperEntity {

    private long lastPhraseTimestamp;
    private final GameManager gameManager;
    private final String name;
    private final String colorName;
    private final Set<String> validTriggers;
    private final Map<Integer, MerchantItemForSale> merchantItemForSales;
    private final String welcomeMessage;
    private final MerchantType merchantType;

    public Merchant(GameManager gameManager, String name, String colorName, Set<String> validTriggers, Map<Integer, MerchantItemForSale> merchantItemForSales, String welcomeMessage) {
        this(gameManager, name, colorName, validTriggers, merchantItemForSales, welcomeMessage, MerchantType.BASIC);
    }

    public Merchant(GameManager gameManager, String name, String colorName, Set<String> validTriggers, Map<Integer, MerchantItemForSale> merchantItemForSales, String welcomeMessage, MerchantType merchantType) {
        this.gameManager = gameManager;
        this.name = name;
        this.colorName = colorName;
        this.validTriggers = validTriggers;
        this.merchantItemForSales = merchantItemForSales;
        this.welcomeMessage = welcomeMessage;
        this.merchantType = merchantType;
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
        int i = 1;
        Iterator<Map.Entry<Integer, MerchantItemForSale>> entries = merchantItemForSales.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, MerchantItemForSale> next = entries.next();
            t.addCell(String.valueOf(next.getKey()));
            t.addCell(NumberFormat.getNumberInstance(Locale.US).format(next.getValue().getCost()));
            t.addCell(next.getValue().getItem().getItemDescription());
            i++;
        }
        return t.render();
    }

    @Override
    public void run() {

    }

    public long getLastPhraseTimestamp() {
        return lastPhraseTimestamp;
    }

    public GameManager getGameManager() {
        return gameManager;
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

    public Map<Integer, MerchantItemForSale> getMerchantItemForSales() {
        return merchantItemForSales;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public MerchantType getMerchantType() {
        return merchantType;
    }

    public enum MerchantType {
        BANK,
        LOCKER,
        PLAYERCLASS_SELECTOR,
        BASIC
    }
}
