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
package com.syncleus.aethermud.common;


import com.syncleus.aethermud.server.communication.Color;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class ColorizedTextTemplate {

    private final static Map<String, Set<String>> colors = new ImmutableMap.Builder<String, Set<String>>()
            .put("c-yellow", Sets.newHashSet(Color.YELLOW))
            .put("c-default", Sets.newHashSet(Color.DEFAULT))
            .put("c-white", Sets.newHashSet(Color.WHITE))
            .put("c-cyan", Sets.newHashSet(Color.CYAN))
            .put("c-black", Sets.newHashSet(Color.BLACK))
            .put("c-blue", Sets.newHashSet(Color.BLUE))
            .put("c-magenta", Sets.newHashSet(Color.MAGENTA))
            .put("c-red", Sets.newHashSet(Color.RED))
            .put("c-green", Sets.newHashSet(Color.GREEN))
            //Seems that there are two valid RESET sequences that I have to account for.
            .put("c-reset", Sets.newHashSet(Color.RESET, "\u001b[0m"))
            .put("c-bold-on", Sets.newHashSet(Color.BOLD_ON))
            .put("c-bold-off", Sets.newHashSet(Color.BOLD_OFF))
            .build();

    private final static Map<String, Set<String>> defaults = new ImmutableMap.Builder<String, Set<String>>()
            .putAll(colors)
            .build();

    public static String renderFromTemplateLanguage(String rawTemplateText) {
        return renderFromTemplateLanguage(Maps.newHashMap(), rawTemplateText);
    }

    public static String renderFromTemplateLanguage(Map<String, String> variables, String rawTemplateText) {

        for (Map.Entry<String, Set<String>> def : defaults.entrySet()) {
            String key = def.getKey();
            Optional<String> first = def.getValue().stream().findFirst();
            if (first.isPresent()) {
                rawTemplateText = rawTemplateText.replaceAll(Pattern.quote("@" + key + "@"), first.get());
            }
        }

        for (String key : variables.keySet()) {
            String renderedValue = variables.get(key);
            rawTemplateText = rawTemplateText.replaceAll(Pattern.quote("@" + key + "@"), renderedValue);
        }

        return rawTemplateText;
    }

    public static String renderToTemplateLanguage(String raw) {
        return renderToTemplateLanguage(Maps.newHashMap(), raw);
    }

    public static String renderToTemplateLanguage(Map<String, String> variables, String raw) {

        for (Map.Entry<String, Set<String>> keyToValues : defaults.entrySet()) {
            String templateKey = keyToValues.getKey();
            for (String templateValue : keyToValues.getValue()) {
                raw = raw.replaceAll(Pattern.quote(templateValue), "@" + templateKey + "@");
            }
        }

        for (Map.Entry<String, String> next : variables.entrySet()) {
            String templateKey = next.getKey();
            raw = raw.replaceAll(Pattern.quote(next.getValue()), "@" + templateKey + "@");
        }

        return raw;
    }

}
