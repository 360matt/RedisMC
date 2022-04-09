package fr.i360matt.redismc;


public class RedisConnection {

    private final String host;
    private final int port;

    public RedisConnection (String host, int port) {
        this.host = host;
        this.port = port;
    }
    public String getHost () {
        return host;
    }

    public int getPort () {
        return port;
    }

}
