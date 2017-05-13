package com.comandante.creeper.merchant;

import java.util.List;
import java.util.Set;

public class MerchantMetadata {

    private String name;
    private String colorName;
    private Set<String> validTriggers;
    private List<MerchantItemForSale> merchantItemForSales;
    private String welcomeMessage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Set<String> getValidTriggers() {
        return validTriggers;
    }

    public void setValidTriggers(Set<String> validTriggers) {
        this.validTriggers = validTriggers;
    }

    public List<MerchantItemForSale> getMerchantItemForSales() {
        return merchantItemForSales;
    }

    public void setMerchantItemForSales(List<MerchantItemForSale> merchantItemForSales) {
        this.merchantItemForSales = merchantItemForSales;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }
}
