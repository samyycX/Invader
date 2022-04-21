package com.samyyc.invader.util.logger;


import com.samyyc.invader.name.T;
import com.samyyc.invader.util.Pair;
import com.samyyc.invader.util.TextUtil;

public class Logger {

    public static void log(String msg) {
        System.out.println(msg);
    }

    @SafeVarargs
    public static void log(String msg, Pair<String, ?>... pairs) {
        msg = TextUtil.format(msg, pairs);
        log(msg);
    }

    public static void log(T t) {
        log(t.toString());
    }

    @SafeVarargs
    public static void log(T t, Pair<String, ?>... pairs) {
        log(t.toString(), pairs);
    }

}
