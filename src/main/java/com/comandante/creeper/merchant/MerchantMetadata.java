package com.comandante.creeper.merchant;

import java.util.List;
import java.util.Set;

public class MerchantMetadata {

    private String internalName;
    private Integer roomId;
    private String name;
    private String colorName;
    private Set<String> validTriggers;
    private List<MerchantItemForSale> merchantItemForSales;
    private String welcomeMessage;


    public String getInternalName() {
        return internalName;
    }

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

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
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
