package fr.i360matt.redismc;


import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Consumer;

public class RedisClient {

    private static RedisClient instance;

    private final RedisAuth auth;

    private final JedisPool pool;

    private RedisClient (final @NotNull RedisAuth auth) {
        this.pool = new JedisPool(auth.getHost(), auth.getPort());
        this.auth = auth;
    }

    public static void create (final RedisAuth connection) {
        if (instance != null) {
            if (!instance.pool.isClosed())
                return;
        }
        instance = new RedisClient(connection);
    }

    public static void create (final @NotNull Consumer<RedisAuth> auth) {
        auth.accept(new RedisAuth());
    }

    public static boolean isClosed () {
        return instance == null || instance.pool.isClosed();
    }

    public static void close () {
        if (instance == null)
            return;
        instance.pool.close();
        instance = null;
    }

    public static String getGroup () {
        checkInstance();
        return instance.auth.getGroup();
    }

    public static String getName() {
        checkInstance();
        return instance.auth.getName();
    }

    public static RedisAuth getConnection() {
        checkInstance();
        return instance.auth;
    }

    public static JedisPool getPool () {
        checkInstance();
        return instance.pool;
    }

    public static Jedis getResource () {
        checkInstance();
        Jedis jedis = instance.pool.getResource();
        if (instance.auth.getPassword() != null)
            jedis.auth(instance.auth.getPassword());
        return instance.pool.getResource();
    }

    private static void checkInstance () {
        if (instance == null)
            throw new IllegalStateException("RedisClient is not initialized");
    }

}
