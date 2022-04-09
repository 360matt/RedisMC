package fr.i360matt.redismc;

import java.io.*;
import java.util.Base64;

public class Storage {

    private final RedisClient client;
    public Storage (RedisClient redisClient) {
        this.client = redisClient;
    }

    public void set (final String key, final String value) {
        client.getResource().set(key, value);
    }

    public String get (final String key) {
        return client.getResource().get(key);
    }

    public void del (final String key) {
        client.getResource().del(key);
    }

    public void set (final String key, final Serializable value) {
        // convert object to byte array
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeUnshared(value);
            String toSave = Base64.getEncoder().encodeToString(bos.toByteArray());
            client.getResource().set(key, toSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends Serializable> T get (final String key, Class<T> clazz) {
        final String value = client.getResource().get(key);
        if (value == null)
            return null;
        final byte[] bytes = Base64.getDecoder().decode(value);

        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) in.readUnshared();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
