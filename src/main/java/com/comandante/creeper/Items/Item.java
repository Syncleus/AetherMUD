package com.comandante.creeper.Items;


import java.io.Serializable;

public class Item implements Serializable {

    private String itemName;
    private String itemDescription;
    private String shortName;
    private String itemId;
    private Integer itemTypeId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    protected Item(String itemName, String itemDescription, String shortName, String itemId, Integer itemTypeId) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.shortName = shortName;
        this.itemId = itemId;
        this.itemTypeId = itemTypeId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getShortName() {
        return shortName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
