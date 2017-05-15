package com.comandante.creeper.common;

import com.comandante.creeper.items.ItemMetadata;
import com.comandante.creeper.merchant.MerchantMetadata;
import com.comandante.creeper.server.player_communication.Color;
import com.comandante.creeper.storage.*;
import com.google.common.collect.Maps;
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

        MerchantStorage merchantStorage = new MerchantStorage(null, new FilebasedJsonStorage(gson));
        List<MerchantMetadata> merchantMetadatas = merchantStorage.getMerchantMetadatas();

        merchantMetadatas.forEach(new Consumer<MerchantMetadata>() {
            @Override
            public void accept(MerchantMetadata merchantMetadata) {

                System.out.println(merchantMetadata.getColorName());
                try {
                    merchantStorage.saveMerchantMetadata(merchantMetadata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ItemStorage itemStorage = new ItemStorage(new FilebasedJsonStorage(gson));

        itemStorage.getItemMetadatas().forEach(new Consumer<ItemMetadata>() {
            @Override
            public void accept(ItemMetadata itemMetadata) {
                System.out.println(itemMetadata.getItemName());
                try {
                    itemStorage.saveItemMetadata(itemMetadata);
                } catch (IOException e) {


                }

            }
        });

        String green = Color.GREEN;
        String test = "\u001b[32m";


        System.out.printf("u");
    }

}