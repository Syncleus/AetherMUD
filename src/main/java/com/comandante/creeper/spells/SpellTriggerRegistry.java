package com.comandante.creeper.spells;


import com.google.common.collect.Maps;

import java.util.Map;

public class SpellTriggerRegistry {

    public static final Map<String, Spell> spellMap = Maps.newHashMap();
    public static final Map<String, Spell> spellNameMap = Maps.newHashMap();


    public static void addSpell(Spell spell) {
        for (String trigger : spell.getValidTriggers()) {
            spellMap.put(trigger, spell);
        }
    }

    public static Spell getSpell(String trigger) {
        return spellMap.get(trigger);
    }

}
