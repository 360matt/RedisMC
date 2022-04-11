package fr.i360matt.redismc.storage;

import fr.i360matt.redismc.RedisClient;
import fr.i360matt.redismc.utils.Serialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedisList <T extends Serializable> {

    private final String key;

    public RedisList (final String key) {
        this.key = key;
    }

    public T get (final int index) {
        String value = RedisClient.getResource().lindex(key, index);
        return Serialization.deserialize(value);
    }

    public List<T> getInRange (int start, int end) {
        List<String> values = RedisClient.getResource().lrange(key, start, end);
        List<T> list = new ArrayList<>();
        for (String value : values)
            list.add(Serialization.deserialize(value));
        return list;
    }

    public List<T> getAll () {
        return getInRange(0, -1);
    }

    public void set (final int index, final T value) {
        String serialized = Serialization.serialize(value);
        RedisClient.getResource().lset(key, index, serialized);
    }

    public void add (final T... value) {
        String[] values = new String[value.length];
        for (final T v : value) {
            String serialized = Serialization.serialize(v);
            values[values.length - 1] = serialized;
        }
        RedisClient.getResource().rpush(key, values);
    }

    public void add (final T value) {
        String serialized = Serialization.serialize(value);
        RedisClient.getResource().rpush(key, serialized);
    }

    public T pop () {
        String serialized = RedisClient.getResource().rpop(key);
        return Serialization.deserialize(serialized);
    }

    public T peek () {
        String serialized = RedisClient.getResource().lindex(key, 0);
        return Serialization.deserialize(serialized);
    }

    public Long size () {
        return RedisClient.getResource().llen(key);
    }

    public void remove (final T value) {
        String serialized = Serialization.serialize(value);
        RedisClient.getResource().lrem(key, 0, serialized);
    }

    public void remove (final int index) {
        RedisClient.getResource().ltrim(key, index, -1);
    }

    public void remove (final int start, final int end) {
        RedisClient.getResource().ltrim(key, start, end);
    }

    public void clear () {
        RedisClient.getResource().del(key);
    }


}
