package fr.i360matt.redismc;

import fr.i360matt.redismc.storage.RedisHashMap;
import fr.i360matt.redismc.storage.RedisList;
import fr.i360matt.redismc.utils.Serialization;

import java.io.*;
import java.util.Set;

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

    public static <T extends Serializable> T get (final String key, Class<T> clazz) {
        final String value = RedisClient.getResource().get(key);
        if (value == null)
            return null;
        return Serialization.deserialize(value);
    }

    public static  <T extends Serializable> RedisList<T> getList (final String key) {
        return new RedisList<>(key);
    }

    public static <K extends Serializable, V extends Serializable> RedisHashMap<K, V> getMap (final String key) {
        return new RedisHashMap<>(key);
    }

}
