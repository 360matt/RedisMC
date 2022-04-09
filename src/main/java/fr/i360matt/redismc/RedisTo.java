package fr.i360matt.redismc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RedisTo {

    @NotNull
    @Contract(pure = true)
    public static String client (final String name) {
        return "client:" + name;
    }

    @NotNull
    @Contract(pure = true)
    public static String group (final String name) {
        return "group:" + name;
    }

    @NotNull
    @Contract(pure = true)
    public static String all () {
        return "all:?";
    }

}
