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
package com.syncleus.aethermud.merchant;

import java.util.List;
import java.util.Set;

public class MerchantMetadata {

    private String internalName;
    private Set<Integer> roomIds;
    private String name;
    private String colorName;
    private Set<String> validTriggers;
    private List<MerchantItemForSale> merchantItemForSales;
    private String welcomeMessage;
    private Merchant.MerchantType merchantType;

    public void setMerchantType(Merchant.MerchantType merchantType) {
        this.merchantType = merchantType;
    }

    public Set<Integer> getRoomIds() {

        return roomIds;
    }

    public Merchant.MerchantType getMerchantType() {
        return merchantType;
    }

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

    public Set<Integer> getRoomId() {
        return roomIds;
    }

    public void setRoomIds(Set<Integer> roomIds) {
        this.roomIds = roomIds;
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
