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

import com.syncleus.aethermud.items.Forage;
import com.syncleus.aethermud.player.PlayerRole;
import com.syncleus.aethermud.spawner.SpawnRule;
import com.syncleus.aethermud.world.model.Area;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import com.syncleus.ferma.ext.AbstractInterceptingVertexFrame;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@GraphElement
public abstract class ForageData extends AbstractInterceptingVertexFrame {
    public Set<Area> getForageAreas() {
        HashSet<Area> areas = new HashSet<>();
        Collection<String> areasText = this.getProperty("forageAreas", Collection.class);
        if( areasText == null )
            return null;
        for(final String areaText : areasText)
            areas.add(Area.valueOf(areaText));
        return Collections.unmodifiableSet(areas);
    }

    public void setForageAreas(Collection<Area> forageAreas) {
        if( forageAreas != null ) {
            ArrayList<String> newProperty = new ArrayList<String>();
            for (Area area : forageAreas) {
                newProperty.add(area.toString());
            }
            this.setProperty("forageAreas", newProperty);
        }
        else {
            this.setProperty("forageAreas", null);
        }
    }

    @Property("internalItemName")
    public abstract String getInternalItemName();

    @Property("minLevel")
    public abstract int getMinLevel();

    @Property("pctOfSuccess")
    public abstract double getPctOfSuccess();

    @Property("minAmt")
    public abstract int getMinAmt();

    @Property("maxAmt")
    public abstract int getMaxAmt();

    @Property("coolDownTicks")
    public abstract int getCoolDownTicks();

    @Property("forageExperience")
    public abstract int getForageExperience();

    @Property("coolDownTicksLeft")
    public abstract int getCoolDownTicksLeft();

    @Property("coolDownTicksLeft")
    public abstract void setCoolDownTicksLeft(int coolDownTicksLeft);

    @Property("internalItemName")
    public abstract void setInternalItemName(String internalItemName);

    @Property("minLevel")
    public abstract void setMinLevel(int minLevel);

    @Property("pctOfSuccess")
    public abstract void setPctOfSuccess(double pctOfSuccess);

    @Property("minAmt")
    public abstract void setMinAmt(int minAmt);

    @Property("maxAmt")
    public abstract void setMaxAmt(int maxAmt);

    @Property("forageExperience")
    public abstract void setForageExperience(int forageExperience);

    @Property("coolDownTicks")
    public abstract void setCoolDownTicks(int coolDownTicks);

    public static void copyForage(ForageData dest, Forage src) {
        try {
            PropertyUtils.copyProperties(dest, src);
            if( src.getForageAreas() != null || (!src.getForageAreas().isEmpty()) )
                dest.setForageAreas(src.getForageAreas());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static Forage copyForage(ForageData src) {
        return new Forage(src.getInternalItemName(), src.getMinLevel(), src.getPctOfSuccess(), src.getMinAmt(), src.getMaxAmt(), src.getForageExperience(), src.getCoolDownTicks(), src.getForageAreas());
    }
}
