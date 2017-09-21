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

import com.syncleus.aethermud.core.service.TimeTracker;
import com.syncleus.aethermud.items.ItemInstance;
import com.syncleus.aethermud.items.ItemInstanceImpl;
import com.syncleus.aethermud.items.Rarity;
import com.syncleus.aethermud.storage.graphdb.DataUtils;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Adjacency;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import org.apache.tinkerpop.gremlin.structure.Direction;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@GraphElement
public abstract class ItemInstanceData extends AbstractVertexFrame {
    @Property("withPlayer")
    public abstract boolean isWithPlayer();

    @Property("withPlayer")
    public abstract void setWithPlayer(boolean isWithPlayer);

    @Property("numberOfUses")
    public abstract int getNumberOfUses();

    @Property("numberOfUses")
    public abstract void setNumberOfUses(int numberOfUses);

    @Property("itemId")
    public abstract String getItemId();

    @Property("hasBeenWithPlayer")
    public abstract void setHasBeenWithPlayer(boolean hasBeenWithPlayer);

    @Property("itemId")
    public abstract void setItemId(String itemId);

    @Property("hasBeenWithPlayer")
    public abstract boolean isHasBeenWithPlayer();

    @Adjacency(label = "item", direction = Direction.OUT)
    public abstract <N extends ItemData> Iterator<? extends N> getItemDatasIterator(Class<? extends N> type);

    public ItemData getItemData() {
        Iterator<? extends ItemData> allItems = this.getItemDatasIterator(ItemData.class);
        if (allItems.hasNext())
            return allItems.next();
        else
            return null;
    }

    @Adjacency(label = "item", direction = Direction.OUT)
    public abstract ItemData addItemData(ItemData item);

    @Adjacency(label = "item", direction = Direction.OUT)
    public abstract void removeItemData(ItemData item);

    public void setItemData(ItemData item) {
        DataUtils.setAllElements(Collections.singletonList(item), () -> this.getItemDatasIterator(ItemData.class), itemData -> this.addItemData(itemData), () -> {
        });
    }

    public List<TimeTracker.TimeOfDay> getValidTimeOfDays() {
        return getItemData().getValidTimeOfDays();
    }

    public void setValidTimeOfDays(List<TimeTracker.TimeOfDay> validTimeOfDays) {
        getItemData().setValidTimeOfDays(validTimeOfDays);
    }

    public void setValidTimeOfDays(Set<TimeTracker.TimeOfDay> validTimeOfDays) {
        getItemData().setValidTimeOfDays(validTimeOfDays);
    }

    public boolean isDisposable() {
        return getItemData().isDisposable();
    }

    public int getMaxUses() {
        return getItemData().getMaxUses();
    }

    public String getInternalItemName() {
        return getItemData().getInternalItemName();
    }

    public String getItemName() {
        return getItemData().getItemName();
    }

    public String getItemDescription() {
        return getItemData().getItemDescription();
    }

    public List<String> getItemTriggers() {
        return getItemData().getItemTriggers();
    }

    public void setItemTriggers(List<String> itemTriggers) {
        getItemData().setItemTriggers(itemTriggers);
    }

    public String getRestingName() {
        return getItemData().getRestingName();
    }

    public int getItemHalfLifeTicks() {
        return getItemData().getItemHalfLifeTicks();
    }

    public Rarity getRarity() {
        return getItemData().getRarity();
    }

    public void setRarity(Rarity rarity) {
        getItemData().setRarity(rarity);
    }

    public int getValueInGold() {
        return getItemData().getValueInGold();
    }

    public void setItemName(String itemName) {
        getItemData().setItemName(itemName);
    }

    public void setItemDescription(String itemDescription) {
        getItemData().setItemDescription(itemDescription);
    }

    public void setInternalItemName(String internalItemName) {
        getItemData().setInternalItemName(internalItemName);
    }

    public void setRestingName(String restingName) {
        getItemData().setRestingName(restingName);
    }

    public void setItemHalfLifeTicks(int itemHalfLifeTicks) {
        getItemData().setItemHalfLifeTicks(itemHalfLifeTicks);
    }

    public void setValueInGold(int valueInGold) {
        getItemData().setValueInGold(valueInGold);
    }

    public void setMaxUses(int maxUses) {
        getItemData().setMaxUses(maxUses);
    }

    public void setDisposable(boolean disposable) {
        getItemData().setDisposable(disposable);
    }

    public EquipmentData getEquipmentData() {
        return getItemData().getEquipmentData();
    }

    public void setEquipmentData(EquipmentData equipment) {
        getItemData().setEquipmentData(equipment);
    }

    public EquipmentData createEquipmentData() {
        return getItemData().createEquipmentData();
    }

    public Set<EffectData> getEffectDatas() {
        return getItemData().getEffectDatas();
    }

    public void setEffectDatas(Set<EffectData> effects) {
        getItemData().setEffectDatas(effects);
    }

    public EffectData createEffectData() {
        return getItemData().createEffectData();
    }

    public StatData getItemApplyStatData() {
        return getItemData().getItemApplyStatData();
    }

    public void setItemApplyStatData(StatData stats) {
        getItemData().setItemApplyStatData(stats);
    }

    public StatData createItemApplyStatData() {
        return getItemData().createItemApplyStatData();
    }

    public LootData getLootData() {
        return getItemData().getLootData();
    }

    public void setLootData(LootData loot) {
        getItemData().setLootData(loot);
    }

    public LootData createLootData() {
        return getItemData().createLootData();
    }

    public static void copyItem(ItemInstanceData dest, ItemInstance src, ItemData itemData) {
        dest.setWithPlayer(src.isWithPlayer());
        dest.setNumberOfUses(src.getNumberOfUses());
        dest.setHasBeenWithPlayer(src.isHasBeenWithPlayer());
        dest.setItemId(src.getItemId());
        if (dest.getItemData() == null)
            dest.setItemData(itemData);
        else if (!dest.getItemData().getInternalItemName().equals(itemData.getInternalItemName())) {
            dest.removeItemData(dest.getItemData());
            dest.setItemData(itemData);
        }
    }

    public static ItemInstance copyItem(ItemInstanceData src) {
        ItemInstance itemInstance = new ItemInstanceImpl();
        itemInstance.setWithPlayer(src.isWithPlayer());
        itemInstance.setNumberOfUses(src.getNumberOfUses());
        itemInstance.setHasBeenWithPlayer(src.isHasBeenWithPlayer());
        itemInstance.setItemId(src.getItemId());
        itemInstance.setItem(ItemData.copyItem(src.getItemData()));
        return itemInstance;
    }
}
