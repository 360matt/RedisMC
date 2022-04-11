package fr.i360matt.redismc.storage;

import fr.i360matt.redismc.RedisClient;
import fr.i360matt.redismc.utils.Serialization;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RedisHashMap <K extends Serializable, V extends Serializable> {

    private final String key;
    public RedisHashMap (String key) {
        this.key = key;
    }

    public void set (HashMap<Serializable, Serializable> map) {
        final HashMap<String, String> toStore = new HashMap<>();
        for (final Map.Entry<Serializable, Serializable> entry : map.entrySet()) {
            String key = Serialization.serialize(entry.getValue());
            String value = Serialization.serialize(entry.getValue());
            toStore.put(key, value);
        }
        RedisClient.getResource().hmset(key, toStore);
    }

    public HashMap<K, V> get () {
        final HashMap<K, V> map = new HashMap<>();
        final Map<String, String> stored = RedisClient.getResource().hgetAll(key);
        for (final Map.Entry<String, String> entry : stored.entrySet()) {
            K key = Serialization.deserialize(entry.getKey());
            V value = Serialization.deserialize(entry.getValue());
            map.put(key, value);
        }
        return map;
    }

    public void clear () {
        RedisClient.getResource().del(key);
    }

    public void remove (Serializable field) {
        String deserialized = Serialization.serialize(field);
        RedisClient.getResource().hdel(key, deserialized);
    }

    public void put (Serializable field, Serializable value) {
        String deserialized = Serialization.serialize(field);
        String deserializedValue = Serialization.serialize(value);
        RedisClient.getResource().hset(key, deserialized, deserializedValue);
    }

    public V get (K field) {
        String deserialized = Serialization.serialize(field);
        String deserializedValue = RedisClient.getResource().hget(key, deserialized);
        return Serialization.deserialize(deserializedValue);
    }

    public boolean contains (K field) {
        String deserialized = Serialization.serialize(field);
        return RedisClient.getResource().hexists(key, deserialized);
    }

    public HashSet<K> keySet () {
        final HashSet<K> set = new HashSet<>();
        final Set<String> stored = RedisClient.getResource().hkeys(key);
        for (final String entry : stored) {
            K key = Serialization.deserialize(entry);
            set.add(key);
        }
        return set;
    }



}
