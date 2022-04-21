package com.samyyc.invader.util;

import com.samyyc.invader.name.T;

public class TextUtil {

    public static void color(String s) {
        s = s.replace("&", "§");
    }

    @SafeVarargs
    public static String format(String origin, Pair<String, ?>... variables) {
        for (Pair<String, ?> pair : variables) {
            origin = origin.replace("{{"+pair.K()+"}}", String.valueOf(pair.V()));
        }
        return origin;
    }

    @SafeVarargs
    public static String format(T t, Pair<String, ?>... variables) {
        String returned = t.toString();
        returned = format(returned, variables);
        return returned;
    }

}
