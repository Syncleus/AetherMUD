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

import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.items.ItemMetadata;
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
    private final Set<Integer> roomIds;

    public Merchant(GameManager gameManager, String internalName, String name, String colorName, Set<String> validTriggers, List<MerchantItemForSale> merchantItemForSales, String welcomeMessage, Set<Integer> roomIds) {
        this(gameManager, internalName, name, colorName, validTriggers, merchantItemForSales, welcomeMessage, roomIds, MerchantType.BASIC);
    }

    public Merchant(GameManager gameManager, String internalName, String name, String colorName, Set<String> validTriggers, List<MerchantItemForSale> merchantItemForSales, String welcomeMessage, Set<Integer> roomIds, MerchantType merchantType) {
        this.gameManager = gameManager;
        this.name = name;
        this.colorName = colorName;
        this.validTriggers = validTriggers;
        this.merchantItemForSales = merchantItemForSales;
        this.welcomeMessage = welcomeMessage;
        this.merchantType = merchantType;
        this.roomIds = roomIds;
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

    public Set<Integer> getRoomIds() {
        return roomIds;
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
