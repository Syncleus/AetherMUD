/**
 * Copyright 2017 Syncleus, Inc.
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

import com.google.common.collect.Lists;
import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.aethermud.common.ColorizedTextTemplate;
import com.syncleus.aethermud.merchant.Merchant;
import com.syncleus.aethermud.merchant.MerchantItemForSale;
import com.syncleus.aethermud.npc.Npc;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@GraphElement
public abstract class MerchantData extends AbstractInterceptingVertexFrame {

    @Property("merchantType")
    public abstract void setMerchantType(Merchant.MerchantType merchantType);

    @Property("merchantType")
    public abstract Merchant.MerchantType getMerchantType();

    @Property("internalName")
    public abstract String getInternalName();

    @Property("internalName")
    public abstract void setInternalName(String internalName);

    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

    public String getColorName() {
        return ColorizedTextTemplate.renderFromTemplateLanguage(this.getProperty("colorName"));
    }

    public void setColorName(String colorName) {
        this.setProperty("colorName", ColorizedTextTemplate.renderToTemplateLanguage(colorName));
    }

    @Property("validTriggers")
    public abstract Set<String> getValidTriggers();

    @Property("validTriggers")
    public abstract void setValidTriggers(Set<String> validTriggers);

    @Property("roomIds")
    public abstract Set<Integer> getRoomIds();

    @Property("roomIds")
    public abstract void setRoomIds(Set<Integer> roomIds);

    @Property("welcomeMessage")
    public abstract String getWelcomeMessage();

    @Property("welcomeMessage")
    public abstract void setWelcomeMessage(String welcomeMessage);

    @Adjacency(label = "merchantItemForSales", direction = Direction.OUT)
    public abstract <N extends MerchantItemForSaleData> Iterator<? extends N> getMerchantItemForSaleDataIterator(Class<? extends N> type);

    public List<MerchantItemForSaleData> getMerchantItemForSaleDatas() {
        return Lists.newArrayList(this.getMerchantItemForSaleDataIterator(MerchantItemForSaleData.class));
    }

    @Adjacency(label = "merchantItemForSales", direction = Direction.OUT)
    public abstract void addMerchantItemForSaleData(MerchantItemForSaleData item);

    @Adjacency(label = "merchantItemForSales", direction = Direction.OUT)
    public abstract void removeMerchantItemForSaleData(MerchantItemForSaleData item);

    public void setMerchantItemForSaleDatas(List<MerchantItemForSaleData> items) {
        DataUtils.setAllElements(items, () -> this.getMerchantItemForSaleDataIterator(MerchantItemForSaleData.class), item -> this.addMerchantItemForSaleData(item), () -> this.createMerchantItemForSaleData() );
    }

    public MerchantItemForSaleData createMerchantItemForSaleData() {
        MerchantItemForSaleData item = this.getGraph().addFramedVertex(MerchantItemForSaleData.class);
        this.addMerchantItemForSaleData(item);
        return item;
    }

    public static void copyMerchant(MerchantData dest, Merchant src) {
        try {
            PropertyUtils.copyProperties(dest, src);

            for(MerchantItemForSaleData data : dest.getMerchantItemForSaleDatas())
                data.remove();
            for(MerchantItemForSale item : src.getMerchantItemForSales())
                MerchantItemForSaleData.copyMerchantItemForSale(dest.createMerchantItemForSaleData(), item);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }
}
