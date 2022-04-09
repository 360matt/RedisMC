package fr.i360matt.redismc;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {

    private final RedisConnection connection;
    private final String name;
    private final String group;

    private final JedisPool pool;
    private final Messaging messaging;
    private final Storage storage;

    public static RedisClient create (final RedisConnection connection, final String name) {
        return create(connection, name, "default");
    }

    public static RedisClient create (final RedisConnection connection, final String name, final String group) {
        return new RedisClient(connection, name, group);
    }



    private RedisClient (final RedisConnection connection, final String name, final String groups) {
        this.connection = connection;
        this.name = name;
        this.group = groups;
        this.pool = new JedisPool(connection.getHost(), connection.getPort());
        this.messaging = new Messaging(this);
        this.storage = new Storage(this);
    }


    public Messaging getMessagingManager () {
        return messaging;
    }

    public Storage getStorageManager () {
        return storage;
    }

    public String getGroup () {
        return group;
    }

    public String getName() {
        return name;
    }

    public RedisConnection getConnection() {
        return connection;
    }

    public JedisPool getPool () {
        return pool;
    }

    public Jedis getResource () {
        return pool.getResource();
    }

}
