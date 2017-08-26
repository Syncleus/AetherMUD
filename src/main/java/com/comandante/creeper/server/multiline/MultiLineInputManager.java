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
package com.comandante.creeper.server.multiline;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;
import java.util.UUID;

public class MultiLineInputManager {

    private final Map<UUID, StringBuilder> multiLineInputs = Maps.newConcurrentMap();

    public void addToMultiLine(UUID uuid, String input) {
        multiLineInputs.get(uuid).append(input);
    }

    public String retrieveMultiLineInput(UUID uuid) {
        return removeTrailingBlankLines(multiLineInputs.remove(uuid).toString());
    }

    public UUID createNewMultiLineInput() {
        UUID retrievalId = UUID.randomUUID();
        multiLineInputs.put(retrievalId, new StringBuilder());
        return retrievalId;
    }

    private String removeTrailingBlankLines(String s) {
        String[] split = s.split("\r\n");
        String s1 = split[split.length - 1].replaceAll("(\\r|\\n)", "");
        if (s1.isEmpty()) {
            String[] strings = ArrayUtils.removeElement(split, split.length - 1);
            return Joiner.on("\r\n").join(strings).replaceAll("(\\r|\\n)", "");
        } else {
            return s.replaceAll("(\\r|\\n)", "");
        }
    }
}
