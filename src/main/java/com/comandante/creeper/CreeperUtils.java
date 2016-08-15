package com.comandante.creeper;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class CreeperUtils {

    public static String asciiColorPattern = "\u001B\\[[;\\d]*m";


    /*  Prints things "next" to each other, like this:
-+=[ fibs ]=+-                        | -+=[ fibs ]=+-
Level 1                               | Level 1
Foraging Level 0                      | Foraging Level 0
Equip-------------------------------- | Equip--------------------------------
Hand            a berserker baton     | Hand            a berserker baton
Head                                  | Head
Feet            berserker boots       | Feet            berserker boots
Legs            berserker shorts      | Legs            berserker shorts
Wrists                                | Wrists
Chest                                 | Chest
Bag             a leather satchel     | Bag             a leather satchel
Stats-------------------------------- | Stats--------------------------------
Experience      7,260                 | Experience      7,260
Health          101                   | Health          101
Mana            101                   | Mana            101
Strength        11                    | Strength        11
Willpower       2                     | Willpower       2
Aim             2                     | Aim             2
Agile           2                     | Agile           2
Armor           7         (+4)        | Armor           7         (+4)
Mele            11                    | Mele            11
Weapon Rating   5-8       (+2-+3)     | Weapon Rating   5-8       (+2-+3)
Forage          0                     | Forage          0
Bag             25        (+15)       | Bag             25        (+15)
   */
    public static String printStringsNextToEachOther(List<String> inputStrings, String seperator) {
        // Find the "longest", meaning most newlines string.
        final String[] maxHeightLine = {inputStrings.get(0)};
        inputStrings.forEach(s -> {
            int height = splitToArrayLines(s).size();
            if (maxHeightLine[0] != null && height > splitToArrayLines(maxHeightLine[0]).size()) {
                maxHeightLine[0] = s;
            }
        });

        final int[] maxLineLength = {0};
        // Split all of the strings, to create a List of a List Of Strings
        // Make them all even length.  This is terrible streams api usage.
        // remove any hidden ascii color characters as they mess with the line length math
        List<List<String>> textToJoin =
                inputStrings.stream().map(CreeperUtils::splitToArrayLines).map(strings -> {
                    maxLineLength[0] = getLongestStringLength(strings);
                    List<String> newStrings = Lists.newArrayList();
                    strings.forEach(s -> {
                        int diff = maxLineLength[0] - removeAllAsciiColorCodes(s).length();
                        String whiteSpacePadded = addTrailingWhiteSpace(s, diff);
                        newStrings.add(whiteSpacePadded);
                    });
                    return newStrings;
                }).collect(Collectors.toList());


        // Go through and piece together the lines, by removing the top of each list and concatenating it
        final StringBuilder[] outputBuilder = {new StringBuilder()};
        List<String> splitLines = splitToArrayLines(maxHeightLine[0]);
        splitLines.forEach(s -> {
            textToJoin.forEach(listOfLines -> {
                if (listOfLines.size() > 0) {
                    String nextLine = listOfLines.remove(0);
                    outputBuilder[0].append(nextLine);
                    outputBuilder[0].append(seperator);
                } else {
                    outputBuilder[0].append(seperator);
                }
            });
            outputBuilder[0].append("\r\n");
            String formatted = outputBuilder[0].toString();
            outputBuilder[0] = new StringBuilder(replaceLast(formatted, seperator + "\r\n", "\r\n"));
        });
        return outputBuilder[0].toString();
    }

    public static int getLongestStringLength(List<String> strings) {
        final int[] maxLineLength = {0};
        strings.forEach(s -> {
            if (s.replaceAll(asciiColorPattern, "").length() > maxLineLength[0]) {
                maxLineLength[0] = s.replaceAll(asciiColorPattern, "").length();
            }
        });
        return maxLineLength[0];
    }

    public static String addTrailingWhiteSpace(String s, int numberOfWhiteSpace) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < numberOfWhiteSpace; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String removeAllAsciiColorCodes(String s) {
        return s.replaceAll(asciiColorPattern, "");
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos) + replacement + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    public static List<String> splitToArrayLines(String s) {
        return Lists.newArrayList(s.split("[\\r\\n]+"));
    }

    public static String trimTrailingBlanks(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        for (; len > 0; len--) {
            if (!Character.isWhitespace(str.charAt(len - 1))) {
                break;
            }
        }
        return str.substring(0, len);
    }
}
