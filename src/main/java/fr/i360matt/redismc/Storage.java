package fr.i360matt.redismc;

import fr.i360matt.redismc.storage.RedisHashMap;
import fr.i360matt.redismc.storage.RedisList;
import fr.i360matt.redismc.utils.Serialization;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Set;
import java.util.function.Consumer;

public class Storage {

    public static void ttl (String key, int ms) {
        RedisClient.getResource().pexpire(key, ms);
    }

    public static Set<String> getKeys (String path) {
        return RedisClient.getResource().keys(path);
    }

    public static void set (final String key, final String value) {
        RedisClient.getResource().set(key, value);
    }

    public static String get (final String key) {
        return RedisClient.getResource().get(key);
    }

    public static void del (final String key) {
        RedisClient.getResource().del(key);
    }

    public static void set (final String key, final Serializable value) {
        String toSave = Serialization.serialize(value);
        RedisClient.getResource().set(key, toSave);
    }

    public static void set (final String key, final Serializable value, final int ms) {
        String toSave = Serialization.serialize(value);
        RedisClient.getResource().psetex(key, ms, toSave);
    }

    public static <T extends Serializable> @Nullable T get (final String key, Class<T> clazz) {
        final String value = RedisClient.getResource().get(key);
        if (value == null)
            return null;
        return Serialization.deserialize(value);
    }

    @Contract(value = "_ -> new", pure = true)
    public static  <T extends Serializable> @NotNull RedisList<T> getList (final String key) {
        return new RedisList<>(key);
    }

    public static <V extends Serializable> void getList (final String key, final @NotNull Consumer<RedisList<V>> consumer) {
        consumer.accept(new RedisList<V>(key));
    }

    @Contract(value = "_ -> new", pure = true)
    public static <K extends Serializable, V extends Serializable> @NotNull RedisHashMap<K, V> getMap (final String key) {
        return new RedisHashMap<>(key);
    }

    public static <K extends Serializable, V extends Serializable> void getMap (final String key, final @NotNull Consumer<RedisHashMap<K, V>> consumer) {
        consumer.accept(new RedisHashMap<>(key));
    }

}
