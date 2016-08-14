package com.comandante.creeper;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CreeperUtils {
    /*  Prints things "next" to each other, like this:
   ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐
   │4        │   │2        │   │5        │   │5        │
   │         │   │         │   │         │   │         │
   │         │   │         │   │         │   │         │
   │   ♦     │   │   ♠     │   │   ♣     │   │   ♦     │
   │         │   │         │   │         │   │         │
   │         │   │         │   │         │   │         │
   │       4 │   │       2 │   │       5 │   │       5 │
   └─────────┘   └─────────┘   └─────────┘   └─────────┘
   */
    public static String printStringsNextToEachOther(List<String> strings, String seperator) {
        // Find the "longest", meaning most newlines string.
        final String[] maxHeightLine = {strings.get(0)};
        strings.stream().forEach(s -> {
            int height = s.split("[\\r\\n]+").length;
            if (maxHeightLine[0] != null && height > maxHeightLine[0].split("[\\r\\n]+").length) {
                maxHeightLine[0] = s;
            }
        });

        final int[] maxLineLength = {0};
        // Split all of the strings, to create a List of a List Of Strings
        // Make them all even length.  This is terrible streams api usage.
        List<List<String>> textToJoin = strings.stream()
                .map((Function<String, List<String>>) s -> Lists.newArrayList(s.split("[\\r\\n]+"))).map(strings1 -> {
                    strings1.forEach(s -> {
                        if (s.length() > maxLineLength[0]) {
                            maxLineLength[0] = s.replaceAll("\u001B\\[[;\\d]*m", "").length();
                        }
                    });
                    List<String> newStrings = Lists.newArrayList();
                    strings1.forEach(s -> {
                        int diff = maxLineLength[0] - s.replaceAll("\u001B\\[[;\\d]*m", "").length();
                        for (int i = 0; i < diff; i++) {
                            s += " ";
                        }
                        newStrings.add(s);
                    });
                    return newStrings;
                })
                .collect(Collectors.toList());


        // Go through and piece together the lines, by removing the top of each list and concatenating it
        final StringBuilder[] sb = {new StringBuilder()};
        List<String> splitLines = Lists.newArrayList(maxHeightLine[0].split("[\\r\\n]+"));
        splitLines
                .forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        textToJoin.forEach(ss -> {
                            if (ss.size() > 0) {
                                sb[0].append(ss.remove(0)).append(seperator);
                            }
                        });
                        sb[0].append("\r\n");
                        String finalformatted = sb[0].toString();
                        sb[0] = new StringBuilder(replaceLast(finalformatted, seperator + "\r\n", "\r\n"));
                    }
                });
        return sb[0].toString();
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

}
