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
package com.syncleus.aethermud.common;

import com.syncleus.aethermud.items.Loot;
import com.syncleus.aethermud.server.communication.Color;
import com.syncleus.aethermud.storage.FilebasedJsonStorage;
import com.syncleus.aethermud.storage.NpcMetadata;
import com.syncleus.aethermud.storage.NpcStorage;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;


public class ColorizedTextTemplateTest {


    @Test
    public void testConversion() throws Exception {

        final String fightMsg =
                Color.BOLD_ON + Color.RED + "[attack] "
                        + Color.RESET + Color.YELLOW + "The " + "worm wobbler" + " was caught off guard by the attack! " + "+" +
                        NumberFormat.getNumberInstance(Locale.US).format(12) +
                        Color.RESET + Color.BOLD_ON + Color.RED + " DAMAGE" + Color.RESET + " done to " + "worm wobbler";

        String test = "@c-bold-on@" + "@c-red@" + "[attack] " + "@c-reset@" + "@c-yellow@" + "The " + "@npc-name@" + " was caught off guard by the attack! " + "+" + "@damage-done@" + "@c-reset@" + "@c-bold-on@" + "@c-red@" + " DAMAGE" + "@c-reset@" + " done to " + "@npc-name@";

        HashMap<String, String> variableMap = Maps.newHashMap();

        variableMap.put("npc-name", "worm wobbler");
        variableMap.put("damage-done", String.valueOf(12));

        String render = ColorizedTextTemplate.renderFromTemplateLanguage(variableMap, test);

        Assert.assertTrue(render.equals(fightMsg));

        String s = ColorizedTextTemplate.renderToTemplateLanguage(variableMap, render);

        Assert.assertTrue(s.equals(test));

    }

    @Test
    public void testConvertToTemplateLanguage() throws Exception {


        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        NpcStorage npcStorage = new NpcStorage(null, new FilebasedJsonStorage(gson));


        List<NpcMetadata> npcMetadata = npcStorage.getNpcMetadatas();

        npcMetadata.forEach(new Consumer<NpcMetadata>() {
            @Override
            public void accept(NpcMetadata npcMetadata) {
                System.out.println(npcMetadata.getColorName());
                try {
                    npcStorage.saveNpcMetadata(npcMetadata);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
//
//        MerchantStorage merchantStorage = new MerchantStorage(null, new FilebasedJsonStorage(gson));
//        List<MerchantMetadata> merchantMetadatas = merchantStorage.getMerchantMetadatas();
//
//        merchantMetadatas.forEach(new Consumer<MerchantMetadata>() {
//            @Override
//            public void accept(MerchantMetadata merchantMetadata) {
//
//                System.out.println(merchantMetadata.getColorName());
//                try {
//                    merchantStorage.saveMerchantMetadata(merchantMetadata);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        ItemStorage itemStorage = new ItemStorage(new FilebasedJsonStorage(gson));
//
//        itemStorage.getItemMetadatas().forEach(new Consumer<ItemMetadata>() {
//            @Override
//            public void accept(ItemMetadata itemMetadata) {
//                System.out.println(itemMetadata.getItemName());
//                try {
//                    itemStorage.saveItemMetadata(itemMetadata);
//                } catch (IOException e) {
//
//
//                }
//
//            }
//        });

        String green = Color.GREEN;
        String test = "\u001b[32m";


        System.out.printf("u");
    }

    @Test
    public void testSomething() throws Exception {

//        {
//            Set<AttackMessage> attackMessageSet = Sets.newHashSet();
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ throws a massive log at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ throws a large boulder at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ lunges at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ swings at @player-name@ with a large tree branch!"));
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String s = gson.toJson(attackMessageSet);
//            System.out.println(s);
//        }
//
//        {
//            Set<AttackMessage> attackMessageSet = Sets.newHashSet();
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ howls maniacally to call its pack!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ growls and charges slightly at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ jumps into the air toward @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ tries to bite @player-name@ in the leg!"));
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String s = gson.toJson(attackMessageSet);
//            System.out.println(s);
//        }


//        {
//            Set<AttackMessage> attackMessageSet = Sets.newHashSet();
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ growls and charges at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ opens its jaws and attempts to bite @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ stomps around and charges at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ swipes its paw toward @player-name@'s head!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ extends its claws and swings at @player-name@!"));
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String s = gson.toJson(attackMessageSet);
//            System.out.println(s);
//        }

//        {
//            Set<AttackMessage> attackMessageSet = Sets.newHashSet();
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ swings a berserker baton at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ foams at the mouth and lunges at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ charges at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ wildly swings its fists at @player-name@!"));
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String s = gson.toJson(attackMessageSet);
//            System.out.println(s);
//        }

//        {
//            Set<AttackMessage> attackMessageSet = Sets.newHashSet();
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ hisses and growls at @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ attempts to scratch @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ attempts to bite @player-name@!"));
//            attackMessageSet.add(new AttackMessage(AttackMessage.Type.NORMAL, "The @npc-color-name@ runs toward @player-name@ with exposed teeth!"));
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            String s = gson.toJson(attackMessageSet);
//            System.out.println(s);
//        }


        Loot loot = new Loot(16, 28, Sets.newHashSet("beserker baton", "bersker boots"));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String s = gson.toJson(loot);
        System.out.println(s);
    }

}
