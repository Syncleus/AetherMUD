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
package com.syncleus.aethermud.storage.graphdb;

import com.syncleus.ferma.ElementFrame;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DataUtils {
    private DataUtils() {
        // can not instantiate
    }

    public static <E extends ElementFrame> void setAllElements(Collection<? extends E> newElements, Supplier<Iterator<? extends E>> allExistingFunc, Consumer<? super E> addFunc, Runnable addEmptyFunc) {
        Iterator<? extends ElementFrame> existingAll = allExistingFunc.get();
        if( existingAll != null ) {
            while( existingAll.hasNext() ) {
                ElementFrame existing = existingAll.next();
                existing.remove();
            }
        }

        if( newElements == null || newElements.isEmpty() ) {
            if( addEmptyFunc != null )
                addEmptyFunc.run();
            return;
        }

        for(E newElement : newElements)
            addFunc.accept(newElement);
    }
}
