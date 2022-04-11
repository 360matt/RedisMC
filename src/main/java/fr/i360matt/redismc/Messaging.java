package fr.i360matt.redismc;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.*;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Messaging {

    private static ExecutorService executor = Executors.newCachedThreadPool();

    public void sendMessage (final String client, final String channel, final String message) {
        RedisClient.getResource().publish("message:client:" + client + ":" + channel, message);
    }

    public void sendMessageGroup (final String group, final String channel, final String message) {
        RedisClient.getResource().publish("message:group:" + group + ":" + channel, message);
    }

    public void sendMessageAll (final String channel, final String message) {
        RedisClient.getResource().publish("message:all:" + channel, message);
    }


    /**
     * Send a serializable object to the channel
     * @param recipient The destination client
     * @param channel The channel to send to
     * @param object The object to send
     */
    private void sendInternal (final String recipient, final String channel, final Serializable object) {
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeUnshared(object);
            try (Jedis jedis = RedisClient.getResource()) {
                String toSend = Base64.getEncoder().encodeToString(bos.toByteArray());
                jedis.publish("obj:" + recipient + ":" + channel, toSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send (final String client, final String channel, final Serializable object) {
        this.sendInternal("client:" + client, channel, object);
    }

    public void sendGroup (final String group, final String channel, final Serializable object) {
        this.sendInternal("group:" + group, channel, object);
    }

    public void sendAll (final String channel, final Serializable object) {
        this.sendInternal("all", channel, object);
    }



    public void onMessage (final String channel, final Consumer<String> listener) {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                listener.accept(message);
            }
        };

        getExecutor().execute(() -> {
            try (Jedis jedis = RedisClient.getResource()) {
                jedis.subscribe(jedisPubSub, "msg:client:" + RedisClient.getName() + ":" + channel);
            }
        });
        getExecutor().execute(() -> {
            try (Jedis jedis = RedisClient.getResource()) {
                jedis.subscribe(jedisPubSub, "msg:group:" + RedisClient.getGroup() + ":" + channel);
            }
        });
        getExecutor().execute(() -> {
            try (Jedis jedis = RedisClient.getResource()) {
                jedis.subscribe(jedisPubSub, "msg:all:" + channel);
            }
        });

    }

    /**
     * Listen to a channel for a serializable object
     * @param channel The channel to listen to
     * @param listener The listener to call when a message is received
     */
    public <T extends Serializable> void on (final String channel, Class<T> clazz, final Consumer<T> listener) {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                final byte[] bytes = Base64.getDecoder().decode(message);
                try (final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                    listener.accept((T) in.readUnshared());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        getExecutor().execute(() -> {
            try (Jedis jedis = RedisClient.getResource()) {
                jedis.subscribe(jedisPubSub, "obj:client:" + RedisClient.getName() + ":" + channel);
            }
        });
        getExecutor().execute(() -> {
            try (Jedis jedis = RedisClient.getResource()) {
                jedis.subscribe(jedisPubSub, "obj:group:" + RedisClient.getGroup() + ":" + channel);
            }
        });
        getExecutor().execute(() -> {
            try (Jedis jedis = RedisClient.getResource()) {
                jedis.subscribe(jedisPubSub, "obj:all:?:" + channel);
            }
        });
    }

    public static void clearTasks () {
        if (executor != null) {
            executor.shutdownNow();
            executor = Executors.newCachedThreadPool();
        }
    }

    public ExecutorService getExecutor() {
        return executor;
    }

}
