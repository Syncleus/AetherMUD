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

import com.syncleus.aethermud.common.AetherMudMessage;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.annotations.GraphElement;
import com.syncleus.ferma.annotations.Property;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

@GraphElement
public abstract class AetherMudMessageData extends AbstractVertexFrame {
    @Property("messageType")
    public abstract AetherMudMessage.Type getType();

    @Property("messageType")
    public abstract void setType(AetherMudMessage.Type type);

    @Property("message")
    public abstract String getMessage();

    @Property("message")
    public abstract void setMessage(String message);

    public static void copyAetherMudMessage(AetherMudMessageData dest, AetherMudMessage src) {
        try {
            PropertyUtils.copyProperties(dest, src);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Could not copy properties", e);
        }
    }

    public static AetherMudMessage copyAetherMudMessage(AetherMudMessageData src) {
        return new AetherMudMessage(src.getType(), src.getMessage());
    }
}
