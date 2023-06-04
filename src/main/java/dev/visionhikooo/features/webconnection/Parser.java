package dev.visionhikooo.features.webconnection;

import java.util.HashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static HashMap<Integer, String> htmlChars;
    private static final String regex = "&#[0-9]{2,4};";

    static {
        htmlChars = new HashMap<>();
        htmlChars.put(8211, "-");
        htmlChars.put(8221, "\"");
    }

    public static String translateHtmlChars(String original) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(original);
        while (matcher.find()) {
            int i = Integer.parseInt(matcher.group().substring(2, matcher.group().length()-1));
            if (htmlChars.containsKey(i)) {
                original = original.replaceFirst(matcher.group(), htmlChars.get(i));
            }
        }
        return original;
    }
}
