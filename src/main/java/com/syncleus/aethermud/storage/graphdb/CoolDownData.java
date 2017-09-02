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
package com.syncleus.aethermud.storage.graphdb;

import com.syncleus.aethermud.player.CoolDown;
import com.syncleus.aethermud.player.CoolDownType;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.Property;

public abstract class CoolDownData extends AbstractVertexFrame implements CoolDown {
    @Property("type")
    public abstract CoolDownType getCoolDownType();

    @Property("type")
    public abstract void setCoolDownType(CoolDownType coolDownType);

    @Property("name")
    public abstract String getName();

    @Property("name")
    public abstract void setName(String name);

    @Property("ticksLeft")
    public abstract int getNumberOfTicks();

    @Property("ticksLeft")
    public abstract void setNumberOfTicks(int numberOfTicks);

    @Property("ticksTotal")
    public abstract int getOriginalNumberOfTicks();

    @Property("ticksTotal")
    public abstract void setOriginalNumberOfTicks(int ticks);

    @Override
    public int hashCode() {
        return this.getCoolDownType().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if( o instanceof CoolDown )
            return this.getCoolDownType().equals(((CoolDown)o).getCoolDownType());
        else if( o instanceof CoolDownType )
            return this.getCoolDownType().equals((CoolDownType)o);
        else
            return false;
    }
}
