/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.storage.graphdb.model;

import com.syncleus.aethermud.merchant.MerchantItemForSale;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

@GraphElement
public abstract class MerchantItemForSaleData extends AbstractInterceptingVertexFrame {
    @Property("internalName")
    public abstract String getInternalItemName();

    @Property("internalName")
    public abstract void setInternalItemName(String internalName);

    @Property("cost")
    public abstract int getCost();

    @Property("cost")
    public abstract void setCost(int cost);

    public static void copyMerchantItemForSale(MerchantItemForSaleData dest, MerchantItemForSale src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static MerchantItemForSale copyMerchantItemForSale(MerchantItemForSaleData src) {
        return new MerchantItemForSale(src.getInternalItemName(), src.getCost());
    }
}
