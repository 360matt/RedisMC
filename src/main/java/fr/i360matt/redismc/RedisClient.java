package fr.i360matt.redismc;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

    private static RedisClient instance;

    private final RedisConnection connection;
    private final String name;
    private final String group;

    private final JedisPool pool;

    private RedisClient (final RedisConnection connection, final String name, final String groups) {
        this.connection = connection;
        this.name = name;
        this.group = groups;
        this.pool = new JedisPool(connection.getHost(), connection.getPort());
    }

    public static void create (final RedisConnection connection, final String name) {
        create(connection, name, "default");
    }

    public static void create (final RedisConnection connection, final String name, final String group) {
        if (instance != null) {
            if (!instance.pool.isClosed())
                return;
        }
        instance = new RedisClient(connection, name, group);
    }



    public static String getGroup () {
        checkInstance();
        return instance.group;
    }

    public static String getName() {
        checkInstance();
        return instance.name;
    }

    public static RedisConnection getConnection() {
        checkInstance();
        return instance.connection;
    }

    public static JedisPool getPool () {
        checkInstance();
        return instance.pool;
    }

    public static Jedis getResource () {
        checkInstance();
        return instance.pool.getResource();
    }

    private static void checkInstance () {
        if (instance == null)
            throw new IllegalStateException("RedisClient is not initialized");
    }

}
